package com.hzcu.order.service;

import com.hzcu.order.dto.CreateMerchantRequest;
import com.hzcu.order.entity.Canteen;
import com.hzcu.order.entity.MerchantAccount;
import com.hzcu.order.repository.AdminUserRepository;
import com.hzcu.order.repository.CanteenRepository;
import com.hzcu.order.repository.MerchantAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private CanteenRepository canteenRepository;

    @Autowired
    private MerchantAccountRepository merchantAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void updateLastLogin(Long id) {
        adminUserRepository.findById(id).ifPresent(admin -> {
            admin.setLastLoginAt(LocalDateTime.now());
            adminUserRepository.save(admin);
        });
    }

    @Transactional
    public Canteen createMerchant(CreateMerchantRequest request) {
        if (merchantAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Canteen canteen = Canteen.builder()
                .name(request.getName())
                .campus(request.getCampus())
                .location(request.getLocation())
                .contactPhone(request.getContactPhone())
                .businessHours(request.getBusinessHours())
                .serviceFeeRate(request.getServiceFeeRate())
                .remark(request.getRemark())
                .imageUrl(request.getImageUrl())
                .status(1)
                .sortOrder(0)
                .build();

        Canteen savedCanteen = canteenRepository.save(canteen);

        MerchantAccount account = MerchantAccount.builder()
                .canteen(savedCanteen)
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .realName(request.getRealName())
                .mobile(request.getMobile())
                .role("ADMIN")
                .status(1)
                .build();

        merchantAccountRepository.save(account);

        return savedCanteen;
    }

    public List<MerchantAccount> getMerchantAccounts(Long canteenId) {
        return merchantAccountRepository.findByCanteen_CanteenId(canteenId);
    }

    @Transactional
    public void resetMerchantPassword(Long accountId, String newPassword) {
        MerchantAccount account = merchantAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setPasswordHash(passwordEncoder.encode(newPassword));
        merchantAccountRepository.save(account);
    }
}
