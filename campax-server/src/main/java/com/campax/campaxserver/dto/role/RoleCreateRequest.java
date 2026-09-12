package com.campax.campaxserver.dto.role;

import com.campax.campaxserver.model.enums.RoleScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * schoolId is null only for platform-scoped roles (scope = PLATFORM,
 * e.g. platform_super_admin) - that combination should be restricted
 * to platform_super_admins in the service/controller layer once auth
 * is wired up. permissionIds are resolved to Permission entities by
 * the service, not this mapper layer.
 */
public record RoleCreateRequest(
        UUID schoolId,
        @NotBlank String code,
        @NotBlank String name,
        @NotNull RoleScope scope,
        List<UUID> permissionIds) {}