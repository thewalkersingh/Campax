package com.campax.campaxserver.repository;

import com.campax.campaxserver.model.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<School, UUID>, JpaSpecificationExecutor<School> {
	
	boolean existsBySubdomainIgnoreCase(String subdomain);
	
	Page<School> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
}