package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.DishCategoryDTO;
import com.hzcu.order.dto.DishDTO;
import com.hzcu.order.dto.EntityMapper;
import com.hzcu.order.entity.Dish;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.CanteenService;
import com.hzcu.order.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/merchant/dishes")
@Tag(name = "Merchant Dish", description = "Merchant-side dish management APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('MERCHANT')")
public class MerchantDishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CanteenService canteenService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping
    @Operation(summary = "Get all dishes for the logged-in merchant's canteen")
    public ApiResponse<List<DishDTO>> getMyDishes(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser.getCanteenId() == null) {
            return ApiResponse.<List<DishDTO>>error(403, "No canteen associated with this account");
        }
        return canteenService.getCanteenById(currentUser.getCanteenId())
                .map(canteen -> {
                    List<DishDTO> dishes = dishService.getDishesByCanteen(canteen)
                            .stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(dishes);
                })
                .orElse(ApiResponse.<List<DishDTO>>error(404, "Canteen not found"));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all dish categories for the logged-in merchant's canteen")
    public ApiResponse<List<DishCategoryDTO>> getMyCategories(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser.getCanteenId() == null) {
            return ApiResponse.<List<DishCategoryDTO>>error(403, "No canteen associated with this account");
        }
        return canteenService.getCanteenById(currentUser.getCanteenId())
                .map(canteen -> {
                    List<DishCategoryDTO> categories = dishService.getCategoriesByCanteen(canteen)
                            .stream()
                            .map(entityMapper::toDto)
                            .collect(Collectors.toList());
                    return ApiResponse.success(categories);
                })
                .orElse(ApiResponse.<List<DishCategoryDTO>>error(404, "Canteen not found"));
    }

    @PostMapping
    @Operation(summary = "Add a new dish")
    public ApiResponse<DishDTO> addDish(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody DishDTO dishDTO) {

        return canteenService.getCanteenById(currentUser.getCanteenId())
                .map(canteen -> {
                    Dish dish = entityMapper.toEntity(dishDTO);
                    Dish savedDish = dishService.addDish(canteen, dishDTO.getCategoryName(), dish);
                    return ApiResponse.success(entityMapper.toDto(savedDish));
                })
                .orElse(ApiResponse.<DishDTO>error(404, "Canteen not found"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing dish")
    public ApiResponse<DishDTO> updateDish(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody DishDTO dishDTO) {

        return dishService.getDishById(id)
                .map(existingDish -> {
                    // Security check: Ensure the merchant owns this dish
                    if (!existingDish.getCanteen().getCanteenId().equals(currentUser.getCanteenId())) {
                        return ApiResponse.<DishDTO>error(403, "Access denied: You don't own this dish");
                    }
                    Dish saved = dishService.updateDish(existingDish, dishDTO.getCategoryName(), dishDTO);
                    return ApiResponse.success(entityMapper.toDto(saved));
                })
                .orElse(ApiResponse.<DishDTO>error(404, "Dish not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a dish")
    public ApiResponse<String> deleteDish(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        return dishService.getDishById(id)
                .map(dish -> {
                    if (!dish.getCanteen().getCanteenId().equals(currentUser.getCanteenId())) {
                        return ApiResponse.<String>error(403, "Access denied");
                    }
                    dishService.deleteDish(id);
                    return ApiResponse.success("Dish deleted successfully", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Dish not found"));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update dish status (on/off shelf)")
    public ApiResponse<String> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        return dishService.getDishById(id)
                .map(dish -> {
                    if (!dish.getCanteen().getCanteenId().equals(currentUser.getCanteenId())) {
                        return ApiResponse.<String>error(403, "Access denied");
                    }
                    dishService.updateDishStatus(id, status);
                    return ApiResponse.success("Status updated successfully", "Success");
                })
                .orElse(ApiResponse.<String>error(404, "Dish not found"));
    }
}
