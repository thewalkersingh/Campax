package com.campax.campaxserver.customfield;

import lombok.Getter;

import java.util.List;

/** Thrown when a StaffProfile.customAttributes payload doesn't satisfy its category's schema. */
@Getter
public class CustomFieldValidationException extends RuntimeException {
	
	private final List<String> errors;
	
	public CustomFieldValidationException(List<String> errors) {
		super("Custom field validation failed: " + String.join("; ", errors));
		this.errors = errors;
	}
	
}