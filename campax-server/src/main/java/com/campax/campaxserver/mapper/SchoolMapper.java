package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.school.SchoolCreateRequest;
import com.campax.campaxserver.dto.school.SchoolResponseDto;
import com.campax.campaxserver.dto.school.SchoolUpdateRequest;
import com.campax.campaxserver.model.School;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SchoolMapper {

    SchoolResponseDto toResponseDto(School school);

    List<SchoolResponseDto> toResponseDtoList(List<School> schools);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "active", ignore = true)
    School toEntity(SchoolCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "subdomain", ignore = true)
    void updateEntityFromDto(SchoolUpdateRequest request, @MappingTarget School entity);
}
