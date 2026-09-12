package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The single login/auth identity for every person in the system -
 * students, teachers, staff, parents, and admins alike. Role-specific
 * data lives in the matching profile entity (StudentProfile,
 * TeacherProfile, StaffProfile). A UserAccount with no matching
 * profile and role = parent-type is referenced instead via Guardian.
 *
 * passwordHash is nullable for now since auth isn't wired up yet.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "user_accounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"school_id", "email"}))
public class UserAccount extends BaseEntity {

    /** Null only for a platform_super_admin account. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    /** Nullable until authentication is implemented. */
    private String passwordHash;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
