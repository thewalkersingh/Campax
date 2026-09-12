package com.campax.campaxserver.dto.student;

import com.campax.campaxserver.dto.guardian.GuardianSummaryDto;
import com.campax.campaxserver.model.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StudentResponseDto(
	UUID id,
	UUID userId,
	String fullName,
	String email,
	String phone,
	UUID schoolId,
	UUID classSectionId,
	String sectionLabel,
	String admissionNo,
	LocalDate dateOfBirth,
	Gender gender,
	LocalDate admissionDate,
	boolean active,
	List<GuardianSummaryDto> guardians) {
}