package com.hzcu.order.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.hzcu.order.dto.LoginResponse;
import com.hzcu.order.entity.User;
import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;

class AuthServiceWhiteBoxTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private MerchantAccountService merchantAccountService;
    @Mock
    private AdminService adminService;
    @Mock
    private WechatMiniProgramService wechatService;
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("AUTH-TC-01: 微信登录失败 - OpenID 获取不到")
    void loginWithWechatCode_GetOpenidFail_ThrowsException() {
        when(wechatService.getOpenid("invalid_code")).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> {
            authService.loginWithWechatCode("invalid_code", "nick", "avatar", null);
        });
    }

    @Test
    @DisplayName("AUTH-TC-02: 微信正常登录 - 无手机号")
    void loginWithWechatCode_NormalLogin_NoPhone() {
        Map<String, String> wechatInfo = new HashMap<>();
        wechatInfo.put("openid", "test_openid");
        when(wechatService.getOpenid("valid_code")).thenReturn(wechatInfo);

        User user = new User();
        user.setUserId(1L);
        user.setOpenid("test_openid");
        when(userService.createOrUpdateUser(anyString(), anyString(), anyString())).thenReturn(user);
        when(tokenProvider.generateToken(any())).thenReturn("fake_token");

        LoginResponse response = authService.loginWithWechatCode("valid_code", "nick", "avatar", null);

        assertNotNull(response.getToken());
        assertEquals("fake_token", response.getToken());
        verify(wechatService, never()).getPhoneNumber(anyString());
    }

    @Test
    @DisplayName("AUTH-TC-03: 微信登录并绑定手机号成功")
    void loginWithWechatCode_WithPhoneBinding_Success() {
        Map<String, String> wechatInfo = new HashMap<>();
        wechatInfo.put("openid", "test_openid");
        when(wechatService.getOpenid("code")).thenReturn(wechatInfo);
        when(wechatService.getPhoneNumber("pcode")).thenReturn("13800000000");

        User user = new User();
        user.setUserId(1L);
        when(userService.createOrUpdateUser(anyString(), anyString(), anyString())).thenReturn(user);

        authService.loginWithWechatCode("code", "nick", "avatar", "pcode");

        assertEquals("13800000000", user.getMobile());
        verify(userService).save(user); // 确保调用了保存
    }

    @Test
    @DisplayName("AUTH-TC-04: 商户登录 - 角色不匹配")
    void loginMerchant_RoleMismatch_ThrowsException() {
        // 模拟认证通过，但返回的角色是 ROLE_USER 而不是 ROLE_MERCHANT
        UserPrincipal principal = new UserPrincipal(1L, null, null, "user", "pass", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "pass", principal.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);

        assertThrows(BadCredentialsException.class, () -> {
            authService.loginMerchant("user", "pass");
        });
    }

    @Test
    @DisplayName("AUTH-TC-05: 商户登录 - 成功路径")
    void loginMerchant_Success() {
        UserPrincipal principal = new UserPrincipal(1L, 1L, "Canteen", "merchant", "pass", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MERCHANT")));
        
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "pass", principal.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenProvider.generateToken(any())).thenReturn("merchant_token");

        LoginResponse response = authService.loginMerchant("merchant", "pass");

        assertEquals("merchant_token", response.getToken());
        verify(merchantAccountService).updateLastLogin(1L);
    }
}
