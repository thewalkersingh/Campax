package com.campax.campaxserver.dto.student;

import com.campax.campaxserver.model.enums.GuardianRelationship;

import java.util.UUID;

/**
 * A student as seen from a guardian's perspective - student identity
 * plus the relationship metadata that lives on the StudentGuardian join row.
 */
public record StudentSummaryDto(
	UUID studentId,
	UUID userId,
	String fullName,
	String admissionNo,
	String sectionLabel,
	GuardianRelationship relationship,
	boolean isPrimaryContact,
	boolean canPickup,
	boolean isEmergencyContact) {
}