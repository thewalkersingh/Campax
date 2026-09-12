package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The tenant root. Every other business entity (directly or indirectly)
 * belongs to exactly one School.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "schools", uniqueConstraints = @UniqueConstraint(columnNames = "subdomain"))
public class School extends BaseEntity {
	
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	/** Used for tenant resolution, e.g. acme.yourapp.com */
	@NotBlank
	@Column(nullable = false, unique = true)
	private String subdomain;
	
	@Column(nullable = false)
	private String planTier;
	
	@Column(nullable = false)
	@lombok.Builder.Default
	private boolean active = true;
	
}