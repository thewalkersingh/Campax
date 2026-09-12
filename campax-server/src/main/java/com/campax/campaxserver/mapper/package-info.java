/**
 * MapStruct mappers between entities and DTOs. By convention, any field
 * that requires a repository lookup (resolving an id to an entity) or a
 * non-trivial transformation (e.g. JSON <-> typed object, see
 * {@link com.campax.campaxserver.customfield}) is explicitly marked
 * {@code ignore = true} here and assembled instead in the service layer -
 * mappers only do structural, in-memory field mapping.
 */
package com.campax.campaxserver.mapper;
