package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Staff-specific fields, 1:1 with the owning UserAccount. Covers every
 * non-teaching role (Cleaning, Clerk, Driver, Librarian, Security, Lab,
 * Peon, and any category a school adds later). Common fields are real
 * columns; category-specific fields live in customAttributes (jsonb),
 * validated against the linked StaffCategory's customFieldsSchema.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "staff_profiles")
public class StaffProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private StaffCategory category;

    private String department;

    private String jobTitle;

    private LocalDate joiningDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reports_to_user_id")
    private UserAccount reportsTo;

    /** Category-specific fields, e.g. { "licenseNo": "...", "licenseExpiry": "..." } */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String customAttributes;

    @Column(nullable = false)
    @lombok.Builder.Default
    private boolean active = true;
}
