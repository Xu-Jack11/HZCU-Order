package com.hzcu.order.service;

import com.hzcu.order.entity.MerchantAccount;
import com.hzcu.order.repository.MerchantAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MerchantAccountService {

    @Autowired
    private MerchantAccountRepository merchantAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<MerchantAccount> findByUsername(String username) {
        return merchantAccountRepository.findByUsername(username);
    }

    @Transactional
    public MerchantAccount createAccount(MerchantAccount account) {
        account.setPasswordHash(passwordEncoder.encode(account.getPasswordHash()));
        account.setCreatedAt(LocalDateTime.now());
        account.setStatus(1);
        return merchantAccountRepository.save(account);
    }

    @Transactional
    public void updateLastLogin(Long id) {
        merchantAccountRepository.findById(id).ifPresent(account -> {
            account.setLastLoginAt(LocalDateTime.now());
            merchantAccountRepository.save(account);
        });
    }
}
