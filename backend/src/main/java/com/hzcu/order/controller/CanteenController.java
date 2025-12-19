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

    @GetMapping("/{id}")
    @Operation(summary = "Get canteen by ID")
    public ApiResponse<CanteenDTO> getCanteen(@PathVariable Long id) {
        return canteenService.getCanteenById(id)
                .map(entityMapper::toDto)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }
}
