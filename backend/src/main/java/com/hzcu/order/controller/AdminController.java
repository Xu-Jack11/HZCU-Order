package com.hzcu.order.controller;

import com.hzcu.order.common.ApiResponse;
import com.hzcu.order.dto.*;
import com.hzcu.order.entity.Canteen;
import com.hzcu.order.service.AdminService;
import com.hzcu.order.service.CanteenService;
import com.hzcu.order.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "System Administration APIs")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private CanteenService canteenService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityMapper entityMapper;

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll().stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(users);
    }

    @PatchMapping("/users/{id}/status")
    @Operation(summary = "Update user status")
    public ApiResponse<String> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return ApiResponse.success("User status updated", "Success");
    }

    @GetMapping("/canteens")
    @Operation(summary = "Get all canteens")
    public ApiResponse<List<CanteenDTO>> getAllCanteens() {
        List<CanteenDTO> canteens = canteenService.getAllCanteens().stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(canteens);
    }

    @PostMapping("/canteens")
    @Operation(summary = "Create a new canteen")
    public ApiResponse<CanteenDTO> createCanteen(@RequestBody CanteenDTO canteenDTO) {
        Canteen canteen = entityMapper.toEntity(canteenDTO);
        Canteen saved = canteenService.saveCanteen(canteen);
        return ApiResponse.success(entityMapper.toDto(saved));
    }

    @PutMapping("/canteens/{id}")
    @Operation(summary = "Update canteen info")
    public ApiResponse<CanteenDTO> updateCanteen(@PathVariable Long id, @RequestBody CanteenDTO canteenDTO) {
        Canteen canteen = entityMapper.toEntity(canteenDTO);
        canteen.setCanteenId(id);
        Canteen saved = canteenService.saveCanteen(canteen);
        return ApiResponse.success(entityMapper.toDto(saved));
    }

    @PostMapping("/merchants")
    @Operation(summary = "Create merchant (canteen + admin account)")
    public ApiResponse<CanteenDTO> createMerchant(@RequestBody CreateMerchantRequest request) {
        Canteen saved = adminService.createMerchant(request);
        return ApiResponse.success(entityMapper.toDto(saved));
    }

    @GetMapping("/merchants/{canteenId}/accounts")
    @Operation(summary = "Get accounts for a merchant")
    public ApiResponse<List<MerchantAccountDTO>> getMerchantAccounts(@PathVariable Long canteenId) {
        List<MerchantAccountDTO> accounts = adminService.getMerchantAccounts(canteenId).stream()
                .map(entityMapper::toDto)
                .collect(Collectors.toList());
        return ApiResponse.success(accounts);
    }

    @PatchMapping("/merchants/accounts/{accountId}/password")
    @Operation(summary = "Reset merchant account password")
    public ApiResponse<String> resetPassword(@PathVariable Long accountId, @RequestParam String newPassword) {
        adminService.resetMerchantPassword(accountId, newPassword);
        return ApiResponse.success("Password reset successfully", "Success");
    }
}
