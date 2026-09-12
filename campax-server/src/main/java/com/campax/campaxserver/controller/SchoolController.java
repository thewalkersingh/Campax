package com.campax.campaxserver.controller;

import com.campax.campaxserver.dto.common.PageResponse;
import com.campax.campaxserver.dto.school.SchoolCreateRequest;
import com.campax.campaxserver.dto.school.SchoolResponseDto;
import com.campax.campaxserver.dto.school.SchoolUpdateRequest;
import com.campax.campaxserver.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Platform-level: creating, listing, and editing schools is something
 * only we (the vendor) do, not a school's own admins - hence the
 * {@code /api/v1/platform/*} base path agreed on earlier, rather than
 * the generic school-scoped {@code /api/v1/*} used by every other
 * resource in this app.
 * <p>
 * No authentication/authorization yet (explicitly deferred). Once auth
 * exists, every mutating endpoint here gets restricted to
 * platform_super_admin, e.g. via {@code @PreAuthorize("hasAuthority('platform.schools.manage')")}.
 */
@RestController
@RequestMapping("/api/v1/platform/schools")
public class SchoolController {
	
	private final SchoolService schoolService;
	
	public SchoolController(SchoolService schoolService) {
		this.schoolService = schoolService;
	}
	
	@PostMapping
	public ResponseEntity<SchoolResponseDto> create(@Valid @RequestBody SchoolCreateRequest request) {
		SchoolResponseDto created = schoolService.create(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
																.path("/{id}")
																.buildAndExpand(created.id())
																.toUri();
		return ResponseEntity.created(location).body(created);
	}
	
	@GetMapping("/{id}")
	public SchoolResponseDto getById(@PathVariable UUID id) {
		return schoolService.getById(id);
	}
	
	@GetMapping
	public PageResponse<SchoolResponseDto> list(@RequestParam(required = false) String search,
		@PageableDefault(size = 20, sort = "name") Pageable pageable) {
		return schoolService.list(search, pageable);
	}
	
	@PatchMapping("/{id}")
	public SchoolResponseDto update(@PathVariable UUID id, @Valid @RequestBody SchoolUpdateRequest request) {
		return schoolService.update(id, request);
	}
	
	/** Soft-delete (deactivate), not a hard DELETE - see SchoolService.deactivate() Javadoc. */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deactivate(@PathVariable UUID id) {
		schoolService.deactivate(id);
	}
	
	@PostMapping("/{id}/reactivate")
	public SchoolResponseDto reactivate(@PathVariable UUID id) {
		schoolService.reactivate(id);
		return schoolService.getById(id);
	}
	
}