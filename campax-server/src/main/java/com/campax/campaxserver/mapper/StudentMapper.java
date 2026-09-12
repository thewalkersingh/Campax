package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.student.StudentCreateRequest;
import com.campax.campaxserver.dto.student.StudentResponseDto;
import com.campax.campaxserver.dto.student.StudentUpdateRequest;
import com.campax.campaxserver.model.StudentProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {StudentGuardianMapper.class, ClassSectionMapper.class},
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudentMapper {
	
	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "user.fullName", target = "fullName")
	@Mapping(source = "user.email", target = "email")
	@Mapping(source = "user.phone", target = "phone")
	@Mapping(source = "user.active", target = "active")
	@Mapping(source = "school.id", target = "schoolId")
	@Mapping(source = "classSection.id", target = "classSectionId")
	@Mapping(source = "classSection", target = "sectionLabel", qualifiedByName = "sectionLabel")
	@Mapping(source = "studentGuardians", target = "guardians")
	StudentResponseDto toResponseDto(StudentProfile student);
	
	List<StudentResponseDto> toResponseDtoList(List<StudentProfile> students);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "school", ignore = true)
	@Mapping(target = "classSection", ignore = true)
	@Mapping(target = "studentGuardians", ignore = true)
	StudentProfile toEntity(StudentCreateRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "school", ignore = true)
	@Mapping(target = "admissionNo", ignore = true)
	@Mapping(target = "classSection", ignore = true)
	@Mapping(target = "studentGuardians", ignore = true)
	void updateEntityFromDto(StudentUpdateRequest request, @MappingTarget StudentProfile entity);
	
}