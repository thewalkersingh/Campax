package com.campax.campaxserver.dto.guardian;

import com.campax.campaxserver.model.enums.GuardianRelationship;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Used when attaching a guardian to a student, either by linking an
 * existing Guardian (guardianId set) or creating a new one inline
 * (newGuardian set). Exactly one of the two must be provided.
 */
public record GuardianLinkRequest(
        UUID guardianId,
        GuardianRequest newGuardian,
        @NotNull GuardianRelationship relationship,
        boolean isPrimaryContact,
        boolean canPickup,
        boolean isEmergencyContact) {

    public GuardianLinkRequest {
        boolean hasExisting = guardianId != null;
        boolean hasNew = newGuardian != null;
        if (hasExisting == hasNew) {
            throw new IllegalArgumentException(
                    "Exactly one of guardianId or newGuardian must be provided, not both or neither");
        }
    }
}