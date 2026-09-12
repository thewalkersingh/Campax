package com.campax.campaxserver.dto.school;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SchoolCreateRequest(
	@NotBlank String name,
	@NotBlank
	@Pattern(regexp = "^[a-z0-9-]+$", message = "subdomain must be lowercase alphanumeric with hyphens only")
	String subdomain,
	@NotBlank String planTier) {
}