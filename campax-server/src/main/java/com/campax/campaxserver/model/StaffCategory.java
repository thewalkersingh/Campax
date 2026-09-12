package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A staff sub-type such as Cleaning, Clerk, Driver, Librarian, Security,
 * Lab, Peon. school = null means it's a platform-provided default
 * category available to every school; schools can also define their own.
 *
 * customFieldsSchema optionally describes category-specific fields
 * (e.g. Driver -> licenseNo, licenseExpiry) so the frontend can render
 * a dynamic form. The actual values are stored per staff member in
 * StaffProfile.customAttributes.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "staff_categories")
public class StaffCategory extends BaseEntity {

    /** Null = platform default category, visible to all schools. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String customFieldsSchema;
}
