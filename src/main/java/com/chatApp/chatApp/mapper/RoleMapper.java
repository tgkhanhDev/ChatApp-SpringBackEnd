package com.chatApp.chatApp.mapper;

import com.chatApp.chatApp.dto.Response.RoleResponse;
import com.chatApp.chatApp.dto.request.RoleRequest;
import com.chatApp.chatApp.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);

}
