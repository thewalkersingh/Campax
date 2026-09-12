package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.role.RoleCreateRequest;
import com.campax.campaxserver.dto.role.RoleResponseDto;
import com.campax.campaxserver.model.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * uses = PermissionMapper.class lets MapStruct auto-resolve
 * Set<Permission> -> List<PermissionResponseDto> via the method it
 * finds there.
 */
@Mapper(componentModel = "spring", uses = PermissionMapper.class)
public interface RoleMapper {

    @Mapping(source = "school.id", target = "schoolId")
    RoleResponseDto toResponseDto(Role role);

    List<RoleResponseDto> toResponseDtoList(List<Role> roles);

    /** school and permissions need repository lookups, so the service sets those. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleCreateRequest request);
}
