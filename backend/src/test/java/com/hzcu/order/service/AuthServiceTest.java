package com.hzcu.order.service;

import com.hzcu.order.security.JwtTokenProvider;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private AdminService adminService;

    @Mock
    private MerchantAccountService merchantAccountService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateToken() {
        Authentication authentication = mock(Authentication.class);
        UserPrincipal mockPrincipal = mock(UserPrincipal.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockPrincipal);
        when(mockPrincipal.getId()).thenReturn(1L);
        when(tokenProvider.generateToken(authentication)).thenReturn("test-token");

        LoginResponse response = authService.loginMerchant("user", "pass");

        assertNotNull(response);
        assertEquals("test-token", response.getToken());
    }
}
