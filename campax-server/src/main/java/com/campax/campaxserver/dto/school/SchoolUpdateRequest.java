package com.campax.campaxserver.dto.school;

public record SchoolUpdateRequest(
	String name,
	String planTier,
	Boolean active) {
}