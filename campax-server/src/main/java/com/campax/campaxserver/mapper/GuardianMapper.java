package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.guardian.GuardianRequest;
import com.campax.campaxserver.dto.guardian.GuardianResponseDto;
import com.campax.campaxserver.model.Guardian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
	componentModel = "spring",
	uses = StudentGuardianMapper.class,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GuardianMapper {
	
	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "studentGuardians", target = "students")
	GuardianResponseDto toResponseDto(Guardian guardian);
	
	List<GuardianResponseDto> toResponseDtoList(List<Guardian> guardians);
	
	/** school/user/studentGuardians need repository lookups, so the service sets those. */
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "school", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "studentGuardians", ignore = true)
	Guardian toEntity(GuardianRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "school", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "studentGuardians", ignore = true)
	void updateEntityFromDto(GuardianRequest request, @MappingTarget Guardian entity);
	
}