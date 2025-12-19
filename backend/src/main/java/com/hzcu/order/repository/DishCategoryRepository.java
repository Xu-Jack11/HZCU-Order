package com.hzcu.order.repository;

import com.hzcu.order.entity.DishCategory;
import com.hzcu.order.entity.Canteen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishCategoryRepository extends JpaRepository<DishCategory, Long> {
    List<DishCategory> findByCanteenOrderBySortOrderAsc(Canteen canteen);

    java.util.Optional<DishCategory> findByCanteenAndName(Canteen canteen, String name);
}
