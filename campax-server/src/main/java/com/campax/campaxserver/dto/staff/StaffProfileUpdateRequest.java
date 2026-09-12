package com.campax.campaxserver.dto.staff;

import java.util.Map;
import java.util.UUID;

/**
 * Partial update. A non-null customAttributes replaces the whole map
 * (not merged key-by-key) and is re-validated against the category's
 * current schema by the service, same as on create.
 */
public record StaffProfileUpdateRequest(
        String phone,
        String department,
        String jobTitle,
        UUID reportsToUserId,
        Map<String, Object> customAttributes,
        Boolean active) {}
