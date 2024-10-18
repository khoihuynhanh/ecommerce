package com.ecommerce.mappers;

import com.ecommerce.entities.Role;
import com.ecommerce.responses.RoleResponse;

public class RoleMapper {
    public static Role toRole(Role role) {
        return Role.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .name(role.getName())
                .build();
    }
}
