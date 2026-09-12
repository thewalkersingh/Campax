package com.campax.campaxserver.dto.permission;

import jakarta.validation.constraints.NotBlank;

public record PermissionRequest(
        @NotBlank String code,
        String description) {}