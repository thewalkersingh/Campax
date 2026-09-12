package com.campax.campaxserver.model;

import com.campax.campaxserver.model.enums.AttendanceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/** One row per student per day. Unique constraint prevents duplicate entries. */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "attendance_records",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "attendance_date"}))
public class AttendanceRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marked_by_user_id", nullable = false)
    private UserAccount markedBy;

    private String remarks;
}
