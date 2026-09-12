package com.campax.campaxserver.model;

import com.campax.campaxserver.model.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Student-specific fields. Shares its primary key with the owning
 * UserAccount (1:1), so loading a StudentProfile always resolves to
 * exactly one login identity and vice versa.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "student_profiles")
public class StudentProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_section_id")
    private ClassSection classSection;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String admissionNo;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate admissionDate;

    /** Read-only convenience side of the Student<->Guardian relationship. */
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    private List<StudentGuardian> studentGuardians = new ArrayList<>();
}
