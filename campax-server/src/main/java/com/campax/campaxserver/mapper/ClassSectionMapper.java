package com.campax.campaxserver.mapper;

import com.campax.campaxserver.model.ClassSection;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

/**
 * Single source of truth for turning a ClassSection into its display
 * label (e.g. "Grade 5 - B"). Pulled out of StudentMapper/
 * StudentGuardianMapper because both needed it - duplicating a
 *
 * @Named default method with the same name in two mappers that one
 * `uses` the other is exactly what causes MapStruct's "ambiguous
 * mapping methods" error.
 */
@Mapper(componentModel = "spring")
public interface ClassSectionMapper {
	
	@Named("sectionLabel")
	default String sectionLabel(ClassSection section) {
		if (section == null) {
			return null;
		}
		return section.getGrade() + " - " + section.getSectionName();
	}
	
}