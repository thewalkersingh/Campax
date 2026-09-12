package com.campax.campaxserver.mapper;

import com.campax.campaxserver.dto.staff.StaffProfileCreateRequest;
import com.campax.campaxserver.dto.staff.StaffProfileResponseDto;
import com.campax.campaxserver.dto.staff.StaffProfileUpdateRequest;
import com.campax.campaxserver.model.StaffProfile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * customAttributes is intentionally ignored on every method here, for
 * the same reason as StaffCategoryMapper.customFieldsSchema: the entity
 * holds raw JSON, the DTO holds a typed {@code Map<String, Object>}, and
 * the service performs that conversion via CustomFieldJsonService -
 * validating against the linked StaffCategory's schema in the process,
 * which requires a lookup this mapper doesn't have access to.
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StaffProfileMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.active", target = "active")
    @Mapping(source = "school.id", target = "schoolId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "reportsTo.id", target = "reportsToUserId")
    @Mapping(source = "reportsTo.fullName", target = "reportsToName")
    @Mapping(target = "customAttributes", ignore = true)
    StaffProfileResponseDto toResponseDto(StaffProfile entity);

    List<StaffProfileResponseDto> toResponseDtoList(List<StaffProfile> entities);

    /**
     * Maps only the fields StaffProfile itself owns. fullName/email/phone
     * (UserAccount), schoolId/categoryId/reportsToUserId (need repository
     * lookups), and customAttributes (needs JSON conversion + schema
     * validation) are all assembled by the service - see class Javadoc.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "reportsTo", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    StaffProfile toEntity(StaffProfileCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "school", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "reportsTo", ignore = true)
    @Mapping(target = "customAttributes", ignore = true)
    void updateEntityFromDto(StaffProfileUpdateRequest request, @MappingTarget StaffProfile entity);
}
