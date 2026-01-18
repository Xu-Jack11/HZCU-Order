package com.hzcu.order.repository;

import com.hzcu.order.entity.DishSpec;
import com.hzcu.order.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishSpecRepository extends JpaRepository<DishSpec, Long> {
    List<DishSpec> findByDish(Dish dish);
}
