package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.guardian.GuardianSummaryDto;
import com.campax.campaxserver.dto.student.StudentSummaryDto;
import com.campax.campaxserver.model.StudentGuardian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ClassSectionMapper.class)
public interface StudentGuardianMapper {
	
	@Mapping(source = "guardian.id", target = "guardianId")
	@Mapping(source = "guardian.fullName", target = "fullName")
	@Mapping(source = "guardian.phone", target = "phone")
	@Mapping(source = "guardian.email", target = "email")
	GuardianSummaryDto toGuardianSummary(StudentGuardian studentGuardian);
	
	@Mapping(source = "student.id", target = "studentId")
	@Mapping(source = "student.user.id", target = "userId")
	@Mapping(source = "student.user.fullName", target = "fullName")
	@Mapping(source = "student.admissionNo", target = "admissionNo")
	@Mapping(source = "student.classSection", target = "sectionLabel", qualifiedByName = "sectionLabel")
	StudentSummaryDto toStudentSummary(StudentGuardian studentGuardian);
	
}