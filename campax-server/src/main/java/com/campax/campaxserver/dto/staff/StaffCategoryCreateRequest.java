package com.campax.campaxserver.dto.staff;

import com.campax.campaxserver.customfield.CustomFieldDefinition;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

/**
 * schoolId is set by the service from the authenticated caller's tenant
 * context once auth exists (a school admin cannot create a category for
 * another school) - it is accepted here only for the platform-admin path
 * of seeding default categories across schools.
 */
public record StaffCategoryCreateRequest(
        UUID schoolId,
        @NotBlank String name,
        @Valid List<CustomFieldDefinition> customFieldsSchema) {}
