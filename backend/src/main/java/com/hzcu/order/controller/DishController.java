package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.DishDTO;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.service.CanteenService;
import com.hzcu.order.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/dishes")
@Tag(name = "Dish", description = "Dish and category APIs")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CanteenService canteenService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping("/canteen/{canteenId}")
    @Operation(summary = "Get dishes by canteen")
    public ApiResponse<List<DishDTO>> getDishesByCanteen(@PathVariable Long canteenId) {
        return canteenService.getCanteenById(canteenId)
                .map(canteen -> {
                    List<DishDTO> dishes = dishService.getDishesByCanteen(canteen)
                            .stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(dishes);
                })
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dish by ID")
    public ApiResponse<DishDTO> getDish(@PathVariable Long id) {
        return dishService.getDishById(id)
                .map(entityMapper::toDto)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Dish not found"));
    }
}
