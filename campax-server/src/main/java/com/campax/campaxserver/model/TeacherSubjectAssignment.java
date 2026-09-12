package com.campax.campaxserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** Which teacher teaches which subject to which section. */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "teacher_subject_assignments",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"teacher_id", "subject_id", "class_section_id"}))
public class TeacherSubjectAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "teacher_id", nullable = false)
    private TeacherProfile teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;
}
