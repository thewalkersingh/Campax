package com.campax.campaxserver.dto.staff;

import com.campax.campaxserver.customfield.CustomFieldDefinition;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Partial update. A non-null customFieldsSchema replaces the schema
 * entirely (field definitions aren't merged field-by-field) - the
 * service should warn or block this if existing StaffProfile records
 * under the category already hold attributes for fields being removed.
 */
public record StaffCategoryUpdateRequest(
        String name,
        @Valid List<CustomFieldDefinition> customFieldsSchema) {}
