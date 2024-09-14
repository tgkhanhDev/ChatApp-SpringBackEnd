package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.PermissionResponse;
import com.chatApp.chatApp.dto.request.PermissionRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.exception.ErrorCode;
import com.chatApp.chatApp.mapper.PermissionMapper;
import com.chatApp.chatApp.model.Permission;
import com.chatApp.chatApp.repositories.PermissionRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PermissionServiceImpl implements PermissionService {

    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }


    @Override
    @Transactional
    public Set<Permission> generateDefaultPermissionSet() {
        if (permissionRepository.existsByName("VIEW_CHAT")) {
            return Set.of(permissionRepository.findByName("VIEW_CHAT").orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND)));
        }
            Permission permission = Permission.builder()
                    .name("VIEW_CHAT")
                    .description("Basic permission for guest")
                    .build();
            permissionRepository.save(permission);

        return Set.of(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public PermissionResponse getPermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    @Transactional
    public PermissionResponse addPermission(PermissionRequest permissionRequest) {
        permissionRepository.findByName(permissionRequest.getName()).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    @Transactional
    public void deletePermissionByName(String name) {
        if ( permissionRepository.findByName(name).isEmpty() ) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        permissionRepository.deleteByName(name);
    }

}
