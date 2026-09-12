package com.campax.campaxserver.customfield;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * One field in a StaffCategory's custom_fields_schema, e.g. a Driver
 * category might define: {"key":"licenseNo","label":"License No.",
 * "type":"TEXT","required":true}. options is only meaningful for
 * type = SELECT and is validated further (must be non-empty) by
 * CustomFieldJsonService at the point a schema is saved, since that
 * cross-field rule isn't expressible with bean validation alone.
 */
public record CustomFieldDefinition(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-Z][a-zA-Z0-9_]*$",
                message = "key must be a valid identifier (letters, digits, underscore, starting with a letter)")
        String key,
        @NotBlank String label,
        @NotNull FieldType type,
        boolean required,
        List<String> options) {}
