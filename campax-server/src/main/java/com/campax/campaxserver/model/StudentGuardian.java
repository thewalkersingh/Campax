package com.campax.campaxserver.model;

import com.campax.campaxserver.model.enums.GuardianRelationship;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Join entity linking a Student to a Guardian. A student can have
 * multiple guardians; a guardian can have multiple children.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "student_guardians", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "guardian_id"}))
public class StudentGuardian extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "student_id", nullable = false)
	private StudentProfile student;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "guardian_id", nullable = false)
	private Guardian guardian;
	
	@Enumerated(EnumType.STRING)
	private GuardianRelationship relationship;
	
	@lombok.Builder.Default
	private boolean isPrimaryContact = false;
	
	@lombok.Builder.Default
	private boolean canPickup = true;
	
	@lombok.Builder.Default
	private boolean isEmergencyContact = true;
	
}