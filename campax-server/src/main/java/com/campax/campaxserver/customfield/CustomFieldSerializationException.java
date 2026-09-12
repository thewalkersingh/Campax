package com.campax.campaxserver.customfield;

/** Wraps low-level JSON (de)serialization failures for the customfield package. */
public class CustomFieldSerializationException extends RuntimeException {
	
	public CustomFieldSerializationException(String message, Throwable cause) {
		super(message, cause);
	}
	
}