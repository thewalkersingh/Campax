package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.staff.StaffCategoryCreateRequest;
import com.campax.campaxserver.dto.staff.StaffCategoryResponseDto;
import com.campax.campaxserver.dto.staff.StaffCategoryUpdateRequest;
import com.campax.campaxserver.model.StaffCategory;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * customFieldsSchema is intentionally ignored on every method here: the
 * entity stores it as a raw JSON string while the DTOs use the typed
 * {@code List<CustomFieldDefinition>}. The service layer is responsible
 * for converting between the two via CustomFieldJsonService
 * (readSchema/writeSchema) and for calling validateSchema() before
 * persisting - keeping that conversion and validation out of the mapper
 * is what lets the JSON shape evolve independently of this class.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StaffCategoryMapper {

    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(target = "customFieldsSchema", ignore = true)
    StaffCategoryResponseDto toResponseDto(StaffCategory entity);

    List<StaffCategoryResponseDto> toResponseDtoList(List<StaffCategory> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "customFieldsSchema", ignore = true)
    StaffCategory toEntity(StaffCategoryCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "customFieldsSchema", ignore = true)
    void updateEntityFromDto(StaffCategoryUpdateRequest request, @MappingTarget StaffCategory entity);
}
