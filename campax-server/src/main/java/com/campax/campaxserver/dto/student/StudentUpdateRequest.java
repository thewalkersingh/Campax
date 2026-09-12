package com.campax.campaxserver.dto.student;

import com.campax.campaxserver.model.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Partial update - any null field is left untouched. Guardian links are
 * managed via dedicated endpoints, not through this DTO, since add/remove
 * is a different operation shape than a field patch.
 */
public record StudentUpdateRequest(
	String fullName,
	String phone,
	UUID classSectionId,
	LocalDate dateOfBirth,
	Gender gender,
	Boolean active) {
}