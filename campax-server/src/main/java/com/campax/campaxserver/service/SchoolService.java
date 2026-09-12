package com.campax.campaxserver.service;

import com.campax.campaxserver.dto.common.PageResponse;
import com.campax.campaxserver.dto.school.SchoolCreateRequest;
import com.campax.campaxserver.dto.school.SchoolResponseDto;
import com.campax.campaxserver.dto.school.SchoolUpdateRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Business operations for the School (tenant) aggregate. Implementation
 * lives in {@code service.impl.SchoolServiceImpl} - controllers and
 * other services depend on this interface, never the impl directly, so
 * the implementation can be swapped or mocked without touching callers.
 */
public interface SchoolService {

    SchoolResponseDto create(SchoolCreateRequest request);

    SchoolResponseDto getById(UUID id);

    PageResponse<SchoolResponseDto> list(String search, Pageable pageable);

    SchoolResponseDto update(UUID id, SchoolUpdateRequest request);

    /** Soft-delete: deactivates the school rather than removing it. See impl Javadoc for why. */
    void deactivate(UUID id);

    void reactivate(UUID id);
}