package com.campax.campaxserver.dto.role;

import com.campax.campaxserver.dto.permission.PermissionResponseDto;
import com.campax.campaxserver.model.enums.RoleScope;

import java.util.List;
import java.util.UUID;

public record RoleResponseDto(
	UUID id,
	UUID schoolId,
	String code,
	String name,
	RoleScope scope,
	List<PermissionResponseDto> permissions) {
}