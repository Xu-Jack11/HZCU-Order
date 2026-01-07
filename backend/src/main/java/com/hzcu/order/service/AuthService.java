package com.hzcu.order.service;

import com.hzcu.order.entity.User;
import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.dto.LoginResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private WechatMiniProgramService wechatService;

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
        userInfo.put("mobile", user.getMobile());
        userInfo.put("balance", user.getBalance());

        return LoginResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }

    public LoginResponse loginWithWechatCode(String code, String nickname, String avatarUrl, String phoneCode) {
        Map<String, String> wechatInfo = wechatService.getOpenid(code);
        if (wechatInfo == null || wechatInfo.get("openid") == null) {
            throw new BadCredentialsException("Failed to get openid from WeChat");
        }

        String openid = wechatInfo.get("openid");
        User user = userService.createOrUpdateUser(openid, nickname, avatarUrl);

        // If phoneCode is provided during login, bind it immediately
        if (phoneCode != null && !phoneCode.isEmpty()) {
            System.out.println("Processing login with phoneCode: " + phoneCode);
            String phoneNumber = wechatService.getPhoneNumber(phoneCode);
            System.out.println("Retrieved phone number: " + phoneNumber);
            if (phoneNumber != null) {
                user.setMobile(phoneNumber);
                userService.save(user);
            }
        } else {
            System.out.println("Login without phoneCode");
        }

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
        userInfo.put("mobile", user.getMobile());
        userInfo.put("balance", user.getBalance());

        return LoginResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }

    public void bindPhone(Long userId, String phoneCode) {
        String phoneNumber = wechatService.getPhoneNumber(phoneCode);
        if (phoneNumber == null) {
            throw new RuntimeException("Failed to get phone number from WeChat");
        }
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setMobile(phoneNumber);
        userService.save(user);
    }

    public LoginResponse loginMerchant(String username, String password) {
        return authenticateAndGenerateToken("MERCHANT:" + username, password, "ROLE_MERCHANT");
    }

    public LoginResponse loginAdmin(String username, String password) {
        return authenticateAndGenerateToken("ADMIN:" + username, password, "ROLE_ADMIN");
    }

    private LoginResponse authenticateAndGenerateToken(String username, String password, String expectedRole) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Strict role check
        boolean hasRequiredRole = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(expectedRole));

        if (!hasRequiredRole) {
            throw new BadCredentialsException("Account not authorized for this login type");
        }

        if (expectedRole.equals("ROLE_MERCHANT")) {
            merchantAccountService.updateLastLogin(principal.getId());
        } else if (expectedRole.equals("ROLE_ADMIN")) {
            adminService.updateLastLogin(principal.getId());
        }

        String token = tokenProvider.generateToken(authentication);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", principal.getId());
        String usernameForFrontend = principal.getUsername();
        if (usernameForFrontend.contains(":")) {
            usernameForFrontend = usernameForFrontend.split(":", 2)[1];
        }
        userInfo.put("username", usernameForFrontend);

        if (expectedRole.equals("ROLE_MERCHANT")) {
            userInfo.put("canteenId", principal.getCanteenId());
            userInfo.put("canteenName", principal.getCanteenName());
        } else {
            userInfo.put("realName", "Site Administrator");
        }

        return LoginResponse.builder()
                .token(token)
                .user(userInfo)
                .build();
    }
}
