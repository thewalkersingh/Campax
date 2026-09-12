package com.campax.campaxserver.dto.permission;

import java.util.UUID;

public record PermissionResponseDto(UUID id, String code, String description) {}