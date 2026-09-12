package com.campax.campaxserver.customfield;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Converts the raw JSON strings stored in StaffCategory.customFieldsSchema
 * and StaffProfile.customAttributes into typed Java objects at the
 * DTO/service boundary, and validates a set of attribute values against
 * a category's schema.
 * <p>
 * Uses Jackson 3's JsonMapper - Spring Boot 4's default JSON library.
 * The classic Jackson 2 com.fasterxml.jackson.databind.ObjectMapper is
 * NOT auto-configured in Spring Boot 4 unless you opt into the
 * deprecated Jackson 2 compatibility mode, so this class depends on the
 * Spring-managed JsonMapper bean instead. JacksonException (Jackson 3)
 * is unchecked, unlike Jackson 2's JsonProcessingException, but we still
 * wrap it into CustomFieldSerializationException so callers only ever
 * deal with one exception type from this package.
 */
@Service
public class CustomFieldJsonService {
	
	private static final TypeReference<List<CustomFieldDefinition>> SCHEMA_TYPE =
		new TypeReference<>() {
		};
	private static final TypeReference<Map<String, Object>> ATTRIBUTES_TYPE =
		new TypeReference<>() {
		};
	
	private final JsonMapper jsonMapper;
	
	public CustomFieldJsonService(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}
	
	/** Parses StaffCategory.customFieldsSchema. Returns an empty list for null/blank input. */
	public List<CustomFieldDefinition> readSchema(String json) {
		if (!StringUtils.hasText(json)) {
			return Collections.emptyList();
		}
		try {
			return jsonMapper.readValue(json, SCHEMA_TYPE);
		} catch (JacksonException e) {
			throw new CustomFieldSerializationException("Failed to parse custom fields schema", e);
		}
	}
	
	/** Serializes a schema definition back to JSON for persistence. */
	public String writeSchema(List<CustomFieldDefinition> schema) {
		try {
			return jsonMapper.writeValueAsString(schema == null ? Collections.emptyList() : schema);
		} catch (JacksonException e) {
			throw new CustomFieldSerializationException("Failed to serialize custom fields schema", e);
		}
	}
	
	/** Parses StaffProfile.customAttributes. Returns an empty map for null/blank input. */
	public Map<String, Object> readAttributes(String json) {
		if (!StringUtils.hasText(json)) {
			return Collections.emptyMap();
		}
		try {
			return jsonMapper.readValue(json, ATTRIBUTES_TYPE);
		} catch (JacksonException e) {
			throw new CustomFieldSerializationException("Failed to parse custom attributes", e);
		}
	}
	
	/** Serializes attribute values back to JSON for persistence. */
	public String writeAttributes(Map<String, Object> attributes) {
		try {
			return jsonMapper.writeValueAsString(attributes == null ? Collections.emptyMap() : attributes);
		} catch (JacksonException e) {
			throw new CustomFieldSerializationException("Failed to serialize custom attributes", e);
		}
	}
	
	/**
	 * Validates a schema definition itself before it's saved on a
	 * StaffCategory: field keys must be unique within the schema, and
	 * every SELECT field must declare at least one option. Bean
	 * validation (via {@code @Valid} on the request DTOs) already
	 * covers per-field constraints like a blank key or label; this
	 * method covers the cross-field rules that annotations can't express.
	 *
	 * @throws CustomFieldValidationException if any rule is violated
	 */
	public void validateSchema(List<CustomFieldDefinition> schema) {
		List<String> errors = new ArrayList<>();
		Map<String, Long> keyCounts = new HashMap<>();
		
		for (CustomFieldDefinition field : schema) {
			keyCounts.merge(field.key(), 1L, Long::sum);
			if (field.type() == FieldType.SELECT
					 && (field.options() == null || field.options().isEmpty())) {
				errors.add("Field '" + field.key() + "' is type SELECT but declares no options");
			}
		}
		
		keyCounts.forEach((key, count) -> {
			if (count > 1) {
				errors.add("Field key '" + key + "' is declared more than once");
			}
		});
		
		if (!errors.isEmpty()) {
			throw new CustomFieldValidationException(errors);
		}
	}
	
	/**
	 * Validates a set of attribute values against a category's field
	 * definitions: required fields must be present and non-null, and
	 * each present value must match its declared type. Collects every
	 * problem found rather than failing on the first one, so a caller
	 * can show the user a complete list of issues at once.
	 *
	 * @throws CustomFieldValidationException if any field fails validation
	 */
	public void validate(List<CustomFieldDefinition> schema, Map<String, Object> attributes) {
		List<String> errors = new ArrayList<>();
		Map<String, Object> values = attributes == null ? Collections.emptyMap() : attributes;
		
		for (CustomFieldDefinition field : schema) {
			Object value = values.get(field.key());
			boolean present = value != null;
			
			if (field.required() && !present) {
				errors.add("Field '" + field.key() + "' is required");
				continue;
			}
			if (!present) {
				continue;
			}
			if (!matchesType(field, value)) {
				errors.add("Field '" + field.key() + "' must be of type " + field.type());
			}
		}
		
		if (!errors.isEmpty()) {
			throw new CustomFieldValidationException(errors);
		}
	}
	
	private boolean matchesType(CustomFieldDefinition field, Object value) {
		return switch (field.type()) {
			case TEXT -> value instanceof String;
			case NUMBER -> value instanceof Number;
			case BOOLEAN -> value instanceof Boolean;
			case DATE -> value instanceof String s && isIsoDate(s);
			case SELECT -> value instanceof String s
									&& field.options() != null
									&& field.options().contains(s);
		};
	}
	
	private boolean isIsoDate(String value) {
		try {
			LocalDate.parse(value);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
	
}