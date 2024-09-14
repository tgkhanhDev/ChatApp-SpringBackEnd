package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.RoleResponse;
import com.chatApp.chatApp.dto.request.RoleRequest;
import com.chatApp.chatApp.services.RoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
//@PreAuthorize("hasAuthority('MANAGE_ROLES')")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@PreAuthorize("hasAuthority('CRUD_ROLE')")
public class RoleController {

    final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("")
    public ApiResponse<List<RoleResponse>> getAllRoles() {

        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .data(roleService.getAllRoles())
                .message("Get All Roles Success")
                .build();
    }

    @GetMapping("/{roleName}")
    public ApiResponse<RoleResponse> getRoleByName(@PathVariable String roleName) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .data(roleService.getRoleByName(roleName))
                .message("Get Role Success")
                .build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> addRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Add Role Success")
                .data(roleService.addRole(roleRequest))
                .build();
    }

    @PutMapping("/{name}")
    public ApiResponse<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest, @PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Update Role Success")
                .data(roleService.updateRole(roleRequest, name))
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<String> deleteRoleByName(@PathVariable String name) {
        roleService.deleteRoleByName(name);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Delete Role Success")
                .build();
    }

}
