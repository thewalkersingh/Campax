/**
 * Domain exceptions and their translation to HTTP responses.
 * {@link com.campax.campaxserver.exception.GlobalExceptionHandler} is the
 * single place that maps any exception to an RFC 7807 ProblemDetail -
 * controllers and services just throw plain exceptions.
 */
package com.campax.campaxserver.exception;
