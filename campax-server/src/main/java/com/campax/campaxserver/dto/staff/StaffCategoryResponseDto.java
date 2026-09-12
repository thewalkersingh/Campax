package com.campax.campaxserver.dto.staff;

import com.campax.campaxserver.customfield.CustomFieldDefinition;
import java.util.List;
import java.util.UUID;

/**
 * schoolId is null for a platform-provided default category (e.g. a
 * baseline "Driver" or "Librarian" category every school gets out of the
 * box); non-null for a category a specific school defined itself.
 */
public record StaffCategoryResponseDto(
        UUID id,
        UUID schoolId,
        String name,
        List<CustomFieldDefinition> customFieldsSchema) {}
