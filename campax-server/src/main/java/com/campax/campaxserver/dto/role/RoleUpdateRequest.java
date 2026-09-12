package com.campax.campaxserver.dto.role;

import java.util.List;
import java.util.UUID;

public record RoleUpdateRequest(
	String name,
	List<UUID> permissionIds) {
}