package com.hzcu.order.security;

import com.hzcu.order.repository.UserRepository;
import com.hzcu.order.repository.MerchantAccountRepository;
import com.hzcu.order.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MerchantAccountRepository merchantAccountRepository;

        @Autowired
        private AdminUserRepository adminUserRepository;

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
                System.out.println("Auth request for identifier: " + identifier);

                if (identifier.startsWith("MERCHANT:")) {
                        String username = identifier.substring(9);
                        return merchantAccountRepository.findByUsername(username)
                                        .map(merchant -> {
                                                System.out.println("Found Merchant: " + merchant.getUsername()
                                                                + ", Canteen: "
                                                                + (merchant.getCanteen() != null
                                                                                ? merchant.getCanteen().getName()
                                                                                : "NULL"));
                                                return UserPrincipal.create(merchant);
                                        })
                                        .orElseThrow(() -> new UsernameNotFoundException(
                                                        "Merchant not found: " + username));
                }

                if (identifier.startsWith("ADMIN:")) {
                        String username = identifier.substring(6);
                        return adminUserRepository.findByUsername(username)
                                        .map(admin -> {
                                                System.out.println("Found Admin: " + admin.getUsername());
                                                return UserPrincipal.create(admin);
                                        })
                                        .orElseThrow(() -> new UsernameNotFoundException(
                                                        "Admin not found: " + username));
                }

                // Default fallback for user/wechat
                return userRepository.findByOpenid(identifier)
                                .map(user -> {
                                        System.out.println("Found User: " + user.getNickname());
                                        return UserPrincipal.create(user);
                                })
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with identifier: " + identifier));
        }

        @Transactional
        public UserDetails loadUserById(Long id) {
                return userRepository.findById(id)
                                .map(UserPrincipal::create)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
        }
}
