package com.campax.campaxserver.exception;

/** Thrown when a lookup by id (or another unique key) finds nothing. Maps to HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(Class<?> resourceType, Object id) {
        return new ResourceNotFoundException(resourceType.getSimpleName() + " not found with id " + id);
    }
}
