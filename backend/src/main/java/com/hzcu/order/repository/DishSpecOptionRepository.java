package com.hzcu.order.repository;

import com.hzcu.order.entity.DishSpecOption;
import com.hzcu.order.entity.DishSpec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishSpecOptionRepository extends JpaRepository<DishSpecOption, Long> {
    List<DishSpecOption> findBySpec(DishSpec spec);
}
