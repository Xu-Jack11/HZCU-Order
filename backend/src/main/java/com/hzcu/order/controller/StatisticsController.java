package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.security.UserPrincipal;
import com.hzcu.order.service.CanteenService;
import com.hzcu.order.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/statistics")
@Tag(name = "Statistics", description = "Data analysis and reporting APIs")
@SecurityRequirement(name = "bearerAuth")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private CanteenService canteenService;

    @GetMapping("/merchant")
    @Operation(summary = "Get statistics for the logged-in merchant's canteen")
    @PreAuthorize("hasRole('MERCHANT')")
    public ApiResponse<Map<String, Object>> getMerchantStats(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(defaultValue = "7") Integer days) {

        if (currentUser.getCanteenId() == null) {
            return ApiResponse.error(403, "No canteen associated");
        }

        return canteenService.getCanteenById(currentUser.getCanteenId())
                .map(canteen -> ApiResponse.success(statisticsService.getCanteenStats(canteen, days)))
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }

    @GetMapping("/admin")
    @Operation(summary = "Get statistics for a specific canteen (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> getAdminStats(
            @RequestParam Long canteenId,
            @RequestParam(defaultValue = "7") Integer days) {

        return canteenService.getCanteenById(canteenId)
                .map(canteen -> ApiResponse.success(statisticsService.getCanteenStats(canteen, days)))
                .orElse(ApiResponse.error(404, "Canteen not found"));
    }

    @GetMapping("/platform")
    @Operation(summary = "Get platform-wide summary statistics (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, Object>> getPlatformSummary() {
        return ApiResponse.success(statisticsService.getPlatformSummary());
    }
}
