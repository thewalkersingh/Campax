/**
 * Support for school-defined custom fields on staff categories
 * ({@link com.campax.campaxserver.model.StaffCategory#getCustomFieldsSchema()})
 * and the values entered for them
 * ({@link com.campax.campaxserver.model.StaffProfile#getCustomAttributes()}).
 * Both are stored as raw JSON at the persistence layer; this package is
 * the single place that (de)serializes and validates that JSON, so the
 * rest of the app can work with typed Java objects.
 */
package com.campax.campaxserver.customfield;
