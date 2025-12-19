package com.hzcu.order.security;

import com.hzcu.order.entity.User;
import com.hzcu.order.entity.MerchantAccount;
import com.hzcu.order.entity.AdminUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {
    private Long id;
    private Long canteenId;
    private String canteenName;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, Long canteenId, String canteenName, String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.canteenId = canteenId;
        this.canteenName = canteenName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UserPrincipal(
                user.getUserId(),
                null,
                null,
                user.getOpenid(),
                "",
                authorities);
    }

    public static UserPrincipal create(MerchantAccount merchant) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_MERCHANT"),
                new SimpleGrantedAuthority("ROLE_" + merchant.getRole()));
        return new UserPrincipal(
                merchant.getMerchantAccountId(),
                merchant.getCanteen() != null ? merchant.getCanteen().getCanteenId() : null,
                merchant.getCanteen() != null ? merchant.getCanteen().getName() : null,
                "MERCHANT:" + merchant.getUsername(),
                merchant.getPasswordHash(),
                authorities);
    }

    public static UserPrincipal create(AdminUser admin) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        return new UserPrincipal(
                admin.getAdminId(),
                null,
                null,
                "ADMIN:" + admin.getUsername(),
                admin.getPasswordHash(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
