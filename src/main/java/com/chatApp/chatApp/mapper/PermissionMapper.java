package com.chatApp.chatApp.mapper;

import com.chatApp.chatApp.dto.Response.PermissionResponse;
import com.chatApp.chatApp.dto.request.PermissionRequest;
import com.chatApp.chatApp.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {

    Permission toPermission(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toPermissionResponseList(List<Permission> permissions);

}
