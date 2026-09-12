package com.campax.campaxserver.dto.staff;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record StaffProfileResponseDto(
        UUID id,
        UUID userId,
        String fullName,
        String email,
        String phone,
        UUID schoolId,
        UUID categoryId,
        String categoryName,
        String department,
        String jobTitle,
        LocalDate joiningDate,
        UUID reportsToUserId,
        String reportsToName,
        Map<String, Object> customAttributes,
        boolean active) {}
