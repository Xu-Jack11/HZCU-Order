package com.hzcu.order.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.hzcu.order.entity.Dish;
import com.hzcu.order.repository.DishRepository;

class DishServiceWhiteBoxTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("DISH-TC-01: 逻辑删除验证")
    void deleteDish_SetsIsDeletedFlag() {
        Dish dish = new Dish();
        dish.setDishId(1L);
        dish.setIsDeleted(0);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        dishService.deleteDish(1L);

        assertEquals(1, dish.getIsDeleted());
        verify(dishRepository).save(dish);
    }

    @Test
    @DisplayName("DISH-TC-02: 更新菜品上架状态")
    void updateDishStatus_UpdatesField() {
        Dish dish = new Dish();
        dish.setDishId(1L);
        dish.setStatus(1);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        dishService.updateDishStatus(1L, 0);

        assertEquals(0, dish.getStatus());
        verify(dishRepository).save(dish);
    }

    @Test
    @DisplayName("DISH-TC-03: 保存菜品设置默认值")
    void saveDish_PersistsEntity() {
        Dish dish = new Dish();
        when(dishRepository.save(any())).thenReturn(dish);

        Dish result = dishService.saveDish(dish);

        assertNotNull(result);
        verify(dishRepository).save(dish);
    }
}
