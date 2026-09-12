/**
 * JPA entities. Every tenant-owned entity carries a direct school
 * reference (see {@link com.campax.campaxserver.model.School}) so
 * queries can filter on it without a join, and extends
 * {@link com.campax.campaxserver.model.BaseEntity} for a UUID id,
 * optimistic locking version, and audit timestamps.
 */
package com.campax.campaxserver.model;
