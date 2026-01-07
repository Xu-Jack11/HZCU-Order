package com.hzcu.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzcu.order.entity.User;
import com.hzcu.order.security.CustomUserDetailsService;
import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private String validToken;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        // 创建一个模拟的认证信息并生成 Token
        principal = new UserPrincipal(1L, null, null, "test_user", "pass",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        validToken = "Bearer " + tokenProvider.generateToken(auth);

        // 模拟 UserDetailsService，防止过滤器找人失败
        when(customUserDetailsService.loadUserByUsername("test_user")).thenReturn(principal);
    }

    @Test
    @DisplayName("集成测试：获取当前用户信息链路")
    void getCurrentUserProfile_Integration() throws Exception {
        User user = new User();
        user.setUserId(1L);
        user.setNickname("小张");
        user.setBalance(new BigDecimal("99.50"));

        when(userService.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/v1/users/me")
                .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("小张"))
                .andExpect(jsonPath("$.data.balance").value(99.50));
    }

    @Test
    @DisplayName("系统集成测试：模拟充值完整链路")
    void rechargeBalance_Integration() throws Exception {
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setBalance(new BigDecimal("150.00"));

        when(userService.recharge(eq(1L), any(BigDecimal.class))).thenReturn(updatedUser);

        Map<String, Object> rechargeData = new HashMap<>();
        rechargeData.put("amount", 50.00);

        mockMvc.perform(post("/api/v1/users/recharge")
                .header("Authorization", validToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(rechargeData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance").value(150.00));
    }
}
