package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.RoleResponse;
import com.chatApp.chatApp.dto.request.RoleRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.exception.ErrorCode;
import com.chatApp.chatApp.mapper.RoleMapper;
import com.chatApp.chatApp.model.Permission;
import com.chatApp.chatApp.model.Role;
import com.chatApp.chatApp.repositories.PermissionRepository;
import com.chatApp.chatApp.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleServiceImpl implements RoleService {

    //const
//    final DEFAULT_ROLE = "GUEST";
//    final DEFAULT_PERMISSION = "VIEW_CHAT";

    final RoleRepository roleRepository;
    final PermissionRepository permissionRepository;
    final RoleMapper roleMapper;
    final PermissionService permissionService;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper, PermissionService permissionService) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
        this.permissionService = permissionService;
    }


    @Override
    @Transactional
    public Set<String> generateDefaultRole() {
        Set<Permission> permission = permissionService.generateDefaultPermissionSet();
        if (roleRepository.existsByName("GUEST")) {
            return Set.of(roleRepository.findByName("GUEST").getName());
        }
        Role role = Role.builder()
                .name("GUEST")
                .description("Basic role for guest")
                .permissions(permission)
                .build();
        roleRepository.save(role);
        return Set.of(role.getName());
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        if (!roleRepository.existsByName(name)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return roleMapper.toRoleResponse(roleRepository.findByName(name));
    }

    @Override
    @Transactional
    public RoleResponse addRole(RoleRequest roleRequest) {
        if(roleRepository.existsByName(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS);
        }

        Set<Permission> permissions = roleRequest.getPermissions().stream()
                .map(permissionRepository::findByName)
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());
        if (permissions.size() != roleRequest.getPermissions().size()) {
            throw new RuntimeException("Permission not found");
        }

        Role role = roleMapper.toRole(roleRequest);
        role.setPermissions(permissions);
        roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @Override
    @Transactional
    public void deleteRoleByName(String name) {
        if(!roleRepository.existsByName(name)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        roleRepository.deleteByName(name);
    }
}
