package com.hzcu.order.service;

import com.hzcu.order.entity.User;
import com.hzcu.order.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public User createOrUpdateByOpenid(String openid, String nickname, String avatarUrl, String unionid) {
        User existingUser = userMapper.findByOpenid(openid);

        if (existingUser != null) {
            // 更新现有用户
            existingUser.setNickname(nickname);
            existingUser.setAvatarUrl(avatarUrl);
            if (unionid != null) {
                existingUser.setUnionid(unionid);
            }
            existingUser.setLastLoginAt(LocalDateTime.now());
            userMapper.updateByOpenid(existingUser);
            return existingUser;
        } else {
            // 创建新用户
            User newUser = new User(openid, nickname, avatarUrl);
            newUser.setUnionid(unionid);
            userMapper.insert(newUser);
            return newUser;
        }
    }

    @Transactional
    public User createOrUpdateByMobile(String mobile, String nickname, String avatarUrl) {
        User existingUser = userMapper.findByMobile(mobile);

        if (existingUser != null) {
            // 更新现有用户
            existingUser.setNickname(nickname);
            existingUser.setAvatarUrl(avatarUrl);
            existingUser.setLastLoginAt(LocalDateTime.now());
            userMapper.updateByMobile(existingUser);
            return existingUser;
        } else {
            // 创建新用户
            User newUser = new User();
            newUser.setMobile(mobile);
            newUser.setNickname(nickname != null ? nickname : mobile);
            newUser.setAvatarUrl(avatarUrl);
            newUser.setStatus("active");
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLoginAt(LocalDateTime.now());
            userMapper.insert(newUser);
            return newUser;
        }
    }

    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    public User findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }

    @Transactional
    public User createOrUpdateByOpenidAndPhone(String openid, String nickname, String avatarUrl, String phoneNumber, String unionid) {
        User existingUser = userMapper.findByOpenid(openid);

        if (existingUser != null) {
            // 更新现有用户
            existingUser.setNickname(nickname);
            existingUser.setAvatarUrl(avatarUrl);
            if (unionid != null) {
                existingUser.setUnionid(unionid);
            }
            // 如果提供了手机号，更新手机号
            if (phoneNumber != null && !phoneNumber.isBlank()) {
                existingUser.setMobile(phoneNumber);
            }
            existingUser.setLastLoginAt(LocalDateTime.now());
            userMapper.updateByOpenid(existingUser);
            return existingUser;
        } else {
            // 创建新用户
            User newUser = new User(openid, nickname, avatarUrl);
            newUser.setUnionid(unionid);
            newUser.setMobile(phoneNumber);
            newUser.setStatus("active");
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLoginAt(LocalDateTime.now());
            userMapper.insert(newUser);
            return newUser;
        }
    }

    public User findByMobile(String mobile) {
        return userMapper.findByMobile(mobile);
    }

    public User findByToken(String token) {
        // 简单实现：使用用户ID作为token
        // 由于微信登录时token格式是 "token_" + UUID，这里我们需要一种不同的方式
        // 暂时使用一个简单的映射：如果token以数字开头，则解析为用户ID
        if (token != null && token.startsWith("token_")) {
            // 对于新用户，我们需要一个临时的实现
            // 这里先返回null，实际项目中应该使用JWT或其他token验证方式
            return null;
        } else if (token != null && token.matches("\\d+")) {
            // 如果token是纯数字，则作为用户ID
            try {
                Long userId = Long.parseLong(token);
                return userMapper.findById(userId);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // 临时方法：从用户ID创建简单token
    public String createTokenFromUserId(Long userId) {
        return userId.toString();
    }
}