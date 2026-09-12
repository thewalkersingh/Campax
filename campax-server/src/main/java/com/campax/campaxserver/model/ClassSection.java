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

/** E.g. "Grade 5 - B". Students enroll into a section; a teacher may homeroom it. */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "class_sections")
public class ClassSection extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @NotBlank
    @Column(nullable = false)
    private String grade;

    @NotBlank
    @Column(nullable = false)
    private String sectionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homeroom_teacher_id")
    private TeacherProfile homeroomTeacher;

    private String academicYear;
}
