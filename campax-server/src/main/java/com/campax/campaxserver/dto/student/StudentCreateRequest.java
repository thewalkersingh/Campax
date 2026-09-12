package com.campax.campaxserver.dto.student;

import com.campax.campaxserver.dto.guardian.GuardianLinkRequest;
import com.campax.campaxserver.model.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Creates the UserAccount + StudentProfile together, optionally linking
 * or creating guardians in the same call. The service layer (not the
 * mapper) is responsible for the multi-entity write and any guardian
 * lookups/creation, since that requires repository access this DTO/mapper
 * layer doesn't have.
 */
public record StudentCreateRequest(
	@NotBlank String fullName,
	@NotBlank @Email String email,
	String phone,
	@NotNull UUID schoolId,
	UUID classSectionId,
	@NotBlank String admissionNo,
	LocalDate dateOfBirth,
	Gender gender,
	LocalDate admissionDate,
	List<GuardianLinkRequest> guardians) {
}