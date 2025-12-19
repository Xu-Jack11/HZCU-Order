package com.hzcu.order.repository;

import com.hzcu.order.entity.SystemParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemParamRepository extends JpaRepository<SystemParam, Long> {
    Optional<SystemParam> findByParamKey(String paramKey);
}
