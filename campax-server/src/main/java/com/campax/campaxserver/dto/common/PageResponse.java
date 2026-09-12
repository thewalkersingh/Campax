package com.campax.campaxserver.dto.common;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * A stable, API-owned shape for paginated responses. Deliberately not
 * {@code org.springframework.data.domain.Page} itself - returning that
 * type directly from a controller couples the public API contract to
 * Spring Data's internal representation (including a verbose, somewhat
 * leaky "pageable"/"sort" structure), which then can't change
 * independently of a Spring Data upgrade.
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}
