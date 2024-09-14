package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.PermissionResponse;
import com.chatApp.chatApp.dto.request.PermissionRequest;
import com.chatApp.chatApp.mapper.PermissionMapper;
import com.chatApp.chatApp.services.PermissionService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@PreAuthorize("hasAuthority('SCOPE_CRUD_PERMISSION')")
public class PermissionController {
   final PermissionService permissionService;

   @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("")
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermissions())
                .code(200)
                .message("Success")
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<PermissionResponse> getPermissionsByName(@PathVariable String name){
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.getPermissionByName(name))
                .message("Get Permission Success")
                .code(200)
                .build();
    }

    @PostMapping("")
    public ApiResponse<PermissionResponse> addPermission(@RequestBody PermissionRequest permissionRequest) {
       return ApiResponse.<PermissionResponse>builder()
               .data(permissionService.addPermission(permissionRequest))
               .code(200)
               .message("Add Permission Success")
               .build();
    }

    @DeleteMapping("/delete/{name}")
    public ApiResponse<String> deletePermissionByName(@PathVariable String name) {
        permissionService.deletePermissionByName(name);
        return ApiResponse.<String>builder()
                .message("Delete Permission Success")
                .code(200)
                .build();
    }

}
