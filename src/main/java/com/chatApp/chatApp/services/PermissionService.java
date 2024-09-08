package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.PermissionResponse;
import com.chatApp.chatApp.dto.request.PermissionRequest;
import com.chatApp.chatApp.model.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    Set<Permission> generateDefaultPermissionSet(); //For default role if there nothing in DB

    List<PermissionResponse> getAllPermissions();

    PermissionResponse getPermissionByName(String name);

    PermissionResponse addPermission(PermissionRequest permissionRequest);

    void deletePermissionByName(String name);
}
