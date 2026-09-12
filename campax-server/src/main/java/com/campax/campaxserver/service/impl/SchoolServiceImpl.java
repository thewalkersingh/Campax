package com.campax.campaxserver.service.impl;

import com.campax.campaxserver.dto.common.PageResponse;
import com.campax.campaxserver.dto.school.SchoolCreateRequest;
import com.campax.campaxserver.dto.school.SchoolResponseDto;
import com.campax.campaxserver.dto.school.SchoolUpdateRequest;
import com.campax.campaxserver.exception.DuplicateResourceException;
import com.campax.campaxserver.exception.ResourceNotFoundException;
import com.campax.campaxserver.mapper.SchoolMapper;
import com.campax.campaxserver.model.School;
import com.campax.campaxserver.repository.SchoolRepository;
import com.campax.campaxserver.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Class-level {@code @Transactional(readOnly = true)} covers the read
 * methods; write methods override it with their own {@code @Transactional}.
 * readOnly = true lets Hibernate skip dirty-checking overhead on plain
 * reads - a small but free win.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SchoolServiceImpl implements SchoolService {
	
	private final SchoolRepository schoolRepository;
	private final SchoolMapper schoolMapper;
	
	public SchoolServiceImpl(SchoolRepository schoolRepository, SchoolMapper schoolMapper) {
		this.schoolRepository = schoolRepository;
		this.schoolMapper = schoolMapper;
	}
	
	@Override
	@Transactional
	public SchoolResponseDto create(SchoolCreateRequest request) {
		if (schoolRepository.existsBySubdomainIgnoreCase(request.subdomain())) {
			throw new DuplicateResourceException(
				"A school with subdomain '" + request.subdomain() + "' already exists");
		}
		School school = schoolMapper.toEntity(request);
		School saved = schoolRepository.save(school);
		log.info("Created school id={} subdomain={}", saved.getId(), saved.getSubdomain());
		return schoolMapper.toResponseDto(saved);
	}
	
	@Override
	public SchoolResponseDto getById(UUID id) {
		return schoolMapper.toResponseDto(findByIdOrThrow(id));
	}
	
	// package: com.campax.campaxserver.service.impl (only the list() method changes - rest of the class is unchanged)
	@Override
	public PageResponse<SchoolResponseDto> list(String search, Pageable pageable) {
		Specification<School> spec = StringUtils.hasText(search)
												  ? (root, query, cb) ->
														 cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%")
												  : null;
		Page<School> page = schoolRepository.findAll(spec, pageable);
		return PageResponse.from(page.map(schoolMapper::toResponseDto));
	}
	
	/**
	 * Partial update. The fetched entity stays managed for the duration
	 * of this transaction, so mutating it via the mapper is enough -
	 * Hibernate's dirty checking flushes the change at commit without an
	 * explicit save() call.
	 */
	@Override
	@Transactional
	public SchoolResponseDto update(UUID id, SchoolUpdateRequest request) {
		School school = findByIdOrThrow(id);
		schoolMapper.updateEntityFromDto(request, school);
		log.info("Updated school id={}", id);
		return schoolMapper.toResponseDto(school);
	}
	
	/**
	 * Soft-delete: schools are never hard-deleted, since historical
	 * records (past students, past enrollments, billing history) must
	 * survive even after a school stops being an active tenant.
	 */
	@Override
	@Transactional
	public void deactivate(UUID id) {
		School school = findByIdOrThrow(id);
		school.setActive(false);
		log.info("Deactivated school id={}", id);
	}
	
	@Override
	@Transactional
	public void reactivate(UUID id) {
		School school = findByIdOrThrow(id);
		school.setActive(true);
		log.info("Reactivated school id={}", id);
	}
	
	private School findByIdOrThrow(UUID id) {
		return schoolRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.of(School.class, id));
	}
	
}