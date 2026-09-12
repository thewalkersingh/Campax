package com.campax.campaxserver.dto.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

/**
 * Creates the UserAccount + StaffProfile together. customAttributes is
 * validated against the target category's customFieldsSchema by the
 * service (via CustomFieldJsonService.validate) before persisting -
 * not something this DTO or its mapper can do on their own, since that
 * requires loading the StaffCategory first.
 */
public record StaffProfileCreateRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        String phone,
        @NotNull UUID schoolId,
        @NotNull UUID categoryId,
        String department,
        String jobTitle,
        LocalDate joiningDate,
        UUID reportsToUserId,
        Map<String, Object> customAttributes) {}
