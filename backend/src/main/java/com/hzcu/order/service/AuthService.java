package com.hzcu.order.service;

import com.hzcu.order.entity.User;
import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.dto.LoginResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantAccountService merchantAccountService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public LoginResponse loginWithWechat(String openid, String nickname, String avatarUrl) {
        User user = userService.createOrUpdateUser(openid, nickname, avatarUrl);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getUserId());
        userInfo.put("username", user.getOpenid());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatarUrl", user.getAvatarUrl());

        return LoginResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }

    public LoginResponse loginMerchant(String username, String password) {
        return authenticateAndGenerateToken(username, password, "ROLE_MERCHANT");
    }

    public LoginResponse loginAdmin(String username, String password) {
        return authenticateAndGenerateToken(username, password, "ROLE_ADMIN");
    }

    private LoginResponse authenticateAndGenerateToken(String username, String password, String expectedRole) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        if (expectedRole.equals("ROLE_MERCHANT")) {
            merchantAccountService.updateLastLogin(principal.getId());
        } else if (expectedRole.equals("ROLE_ADMIN")) {
            adminService.updateLastLogin(principal.getId());
        }

        String token = tokenProvider.generateToken(authentication);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", principal.getId());
        userInfo.put("username", principal.getUsername());
        userInfo.put("canteenId", principal.getCanteenId());
        userInfo.put("canteenName", principal.getCanteenName());

        return LoginResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }
}
