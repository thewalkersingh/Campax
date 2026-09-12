package com.campax.campaxserver.dto.school;

import java.time.Instant;
import java.util.UUID;

public record SchoolResponseDto(
	UUID id,
	String name,
	String subdomain,
	String planTier,
	boolean active,
	Instant createdAt) {
}