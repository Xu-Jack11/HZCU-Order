package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.CanteenDTO;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.service.CanteenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/canteens")
@Tag(name = "Canteen", description = "Canteen management APIs")
public class CanteenController {

    @Autowired
    private CanteenService canteenService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all active canteens")
    public ApiResponse<List<CanteenDTO>> getCanteens() {
        List<CanteenDTO> canteens = canteenService.getActiveCanteens()
                .stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(canteens);
    }

    @Autowired
    private com.hzcu.order.service.DishService dishService;

    @GetMapping("/{id}")
    @Operation(summary = "Get canteen by ID")
    public ApiResponse<CanteenDTO> getCanteen(@PathVariable Long id) {
        return canteenService.getCanteenById(id)
                .map(entityMapper::toDto)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }

    @GetMapping("/{id}/categories")
    @Operation(summary = "Get canteen categories with dishes")
    public ApiResponse<List<com.hzcu.order.dto.DishCategoryDTO>> getCanteenCategories(@PathVariable Long id) {
        return canteenService.getCanteenById(id)
                .map(canteen -> {
                    List<com.hzcu.order.entity.DishCategory> categories = dishService.getCategoriesByCanteen(canteen);
                    List<com.hzcu.order.entity.Dish> dishes = dishService.getDishesByCanteen(canteen);

                    List<com.hzcu.order.dto.DishCategoryDTO> categoryDTOs = categories.stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());

                    java.util.Map<Long, List<com.hzcu.order.dto.DishDTO>> dishesByCategoryId = dishes.stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.groupingBy(com.hzcu.order.dto.DishDTO::getCategoryId));

                    for (com.hzcu.order.dto.DishCategoryDTO catDto : categoryDTOs) {
                        catDto.setDishes(dishesByCategoryId.getOrDefault(catDto.getId(), new java.util.ArrayList<>()));
                    }

                    return ApiResponse.success(categoryDTOs);
                })
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }
}
