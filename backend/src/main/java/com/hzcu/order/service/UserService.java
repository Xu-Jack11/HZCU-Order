package com.hzcu.order.service;

import com.hzcu.order.entity.User;
import com.hzcu.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByOpenid(String openid) {
        return userRepository.findByOpenid(openid);
    }

    @Transactional
    public User createOrUpdateUser(String openid, String nickname, String avatarUrl) {
        User user = userRepository.findByOpenid(openid)
                .orElse(User.builder()
                        .openid(openid)
                        .status(1)
                        .build());

        user.setNickname(nickname);
        user.setAvatarUrl(avatarUrl);
        user.setLastLoginAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setStatus(status);
            userRepository.save(user);
        });
    }
}
