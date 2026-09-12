package com.campax.campaxserver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Common fields shared by every entity: a UUID primary key, an optimistic
 * locking version, and auto-managed audit timestamps.
 *
 * <p>@SuperBuilder here (not just on subclasses) is required - every
 * subclass builder extends this one's, and Lombok can't generate that
 * chain if the parent has no builder of its own. @NoArgsConstructor is
 * required alongside it: without it, Lombok's builder-only constructor
 * becomes the sole constructor, and JPA/Hibernate needs a no-arg
 * constructor to instantiate entities via reflection.
 *
 * <p><b>equals()/hashCode()</b> are id-based and Hibernate-proxy-safe -
 * the standard, well-documented pattern for JPA entities (see Vlad
 * Mihalcea's "The best way to map an equals/hashCode method with JPA
 * and Hibernate"). Two transient (unsaved, id == null) instances are
 * never equal to each other, even to themselves across calls, which is
 * intentional: a not-yet-persisted entity has no stable identity yet.
 *
 * <p>Deliberately no {@code @ToString} / Lombok {@code @Data} here or on
 * subclasses: entities have bidirectional and/or lazy associations, and
 * a generated toString() that walks them can trigger
 * LazyInitializationException outside a session, or a StackOverflowError
 * on a bidirectional cycle. Add explicit, hand-written toString() methods
 * on subclasses only where genuinely needed, and only over scalar fields.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue
	@UuidGenerator
	private UUID id;
	
	@Version
	private Long version;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private Instant updatedAt;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		Class<?> thisEffectiveClass = effectiveClass(this);
		Class<?> otherEffectiveClass = effectiveClass(o);
		if (!thisEffectiveClass.equals(otherEffectiveClass)) {
			return false;
		}
		BaseEntity other = (BaseEntity) o;
		return getId() != null && Objects.equals(getId(), other.getId());
	}
	
	@Override
	public int hashCode() {
		return effectiveClass(this).hashCode();
	}
	
	private static Class<?> effectiveClass(Object entity) {
		return entity instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer()
		                                                     .getPersistentClass() : entity.getClass();
	}
	
}