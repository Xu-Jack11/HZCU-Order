package com.hzcu.order.mapper;

import com.hzcu.order.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpenid(String openid);

    @Select("SELECT * FROM user WHERE mobile = #{mobile}")
    User findByMobile(String mobile);

    @Insert("INSERT INTO user (openid, unionid, nickname, avatar_url, mobile, status, last_login_at, created_at) " +
            "VALUES (#{openid}, #{unionid}, #{nickname}, #{avatarUrl}, #{mobile}, #{status}, #{lastLoginAt}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    @Update("UPDATE user SET nickname = #{nickname}, avatar_url = #{avatarUrl}, unionid = #{unionid}, " +
            "last_login_at = #{lastLoginAt} WHERE user_id = #{userId}")
    int updateByOpenid(User user);

    @Update("UPDATE user SET nickname = #{nickname}, avatar_url = #{avatarUrl}, " +
            "last_login_at = #{lastLoginAt} WHERE user_id = #{userId}")
    int updateByMobile(User user);

    @Select("SELECT * FROM user WHERE user_id = #{userId}")
    User findById(Long userId);
}