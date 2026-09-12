package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * A single grantable capability, e.g. "students.read", "staff.manage".
 * Global across the platform - not tenant-scoped.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    private String description;
}
