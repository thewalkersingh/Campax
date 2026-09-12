package com.campax.campaxserver.dto.guardian;

import com.campax.campaxserver.model.enums.GuardianRelationship;

import java.util.UUID;

/**
 * A guardian as seen from a student's perspective - guardian identity
 * plus the relationship metadata that lives on the StudentGuardian join row.
 */
public record GuardianSummaryDto(
	UUID guardianId,
	String fullName,
	String phone,
	String email,
	GuardianRelationship relationship,
	boolean isPrimaryContact,
	boolean canPickup,
	boolean isEmergencyContact) {
}