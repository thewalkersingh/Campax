package com.campax.campaxserver.dto.guardian;

import jakarta.validation.constraints.NotBlank;

/** Fields needed to create or update a Guardian contact record. */
public record GuardianRequest(@NotBlank String fullName, String phone, String email, String occupation) {

}