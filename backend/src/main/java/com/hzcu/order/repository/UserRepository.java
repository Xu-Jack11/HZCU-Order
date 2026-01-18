package com.hzcu.order.repository;

import com.hzcu.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOpenid(String openid);

    Optional<User> findByNickname(String nickname);
}
