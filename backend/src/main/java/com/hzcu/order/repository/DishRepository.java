package com.hzcu.order.repository;

import com.hzcu.order.entity.Dish;
import com.hzcu.order.entity.Canteen;
import com.hzcu.order.entity.DishCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCanteenAndIsDeleted(Canteen canteen, Integer isDeleted);

    List<Dish> findByCategoryAndStatusAndIsDeleted(DishCategory category, Integer status, Integer isDeleted);
}
