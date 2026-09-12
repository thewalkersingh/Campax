package com.campax.campaxserver.exception;

/** Thrown when a create/update would violate a uniqueness rule the caller controls (e.g. subdomain). Maps to HTTP 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
