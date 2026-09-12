package com.campax.campaxserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * A real-world guardian contact. Not every guardian needs a login -
 * user is only set once/if they're given parent-portal access via
 * UserAccount (role = parent).
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "guardians")
public class Guardian extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    /** Nullable - set only if this guardian has portal login access. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    private String phone;

    private String email;

    private String occupation;

    /** Read-only convenience side of the Guardian<->Student relationship. */
    @OneToMany(mappedBy = "guardian", fetch = FetchType.LAZY)
    @Builder.Default
    private List<StudentGuardian> studentGuardians = new ArrayList<>();
}
