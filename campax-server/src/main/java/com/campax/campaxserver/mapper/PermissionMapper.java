package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.permission.PermissionRequest;
import com.campax.campaxserver.dto.permission.PermissionResponseDto;
import com.campax.campaxserver.model.Permission;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionResponseDto toResponseDto(Permission permission);

    List<PermissionResponseDto> toResponseDtoList(List<Permission> permissions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Permission toEntity(PermissionRequest request);
}
