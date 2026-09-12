/**
 * Concrete {@code *ServiceImpl} classes implementing the interfaces in
 * {@link com.campax.campaxserver.service}. Kept in a separate package by
 * convention so callers depend on the interface (import from
 * {@code service}, never {@code service.impl}), and Spring wires the
 * implementation in automatically as the interface's single bean.
 */
package com.campax.campaxserver.service.impl;
