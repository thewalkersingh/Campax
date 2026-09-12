package com.campax.campaxserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** A student's score for one ExamSubject. */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "student_marks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "exam_subject_id"}))
public class StudentMark extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_subject_id", nullable = false)
    private ExamSubject examSubject;

    private BigDecimal marksObtained;

    private String gradeLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entered_by_user_id")
    private UserAccount enteredBy;

    private String remarks;
}
