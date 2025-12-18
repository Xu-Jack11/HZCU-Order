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
}