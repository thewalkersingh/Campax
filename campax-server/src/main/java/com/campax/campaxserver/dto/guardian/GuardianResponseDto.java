package com.campax.campaxserver.dto.guardian;

import com.campax.campaxserver.dto.student.StudentSummaryDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GuardianResponseDto(
	UUID id,
	UUID userId,
	String fullName,
	String phone,
	String email,
	String occupation,
	List<StudentSummaryDto> students,
	Instant createdAt) {
}