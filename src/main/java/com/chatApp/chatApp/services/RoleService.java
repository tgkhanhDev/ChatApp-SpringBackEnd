package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.RoleResponse;
import com.chatApp.chatApp.dto.request.RoleRequest;
import com.chatApp.chatApp.model.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    Set<String> generateDefaultRole(); //For default role if there nothing in DB

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleByName(String name);

    RoleResponse addRole(RoleRequest roleRequest);

    RoleResponse updateRole(RoleRequest roleRequest, String name);

    void deleteRoleByName(String name);
}
