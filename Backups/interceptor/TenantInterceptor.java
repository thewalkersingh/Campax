package com.campax.campaxserver.interceptor;

import com.campax.campaxserver.context.TenantContext;
import com.campax.campaxserver.model.School;
import com.campax.campaxserver.repository.SchoolRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {
	
	private final SchoolRepository schoolRepository;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String host = request.getServerName();
		log.debug("Incoming request host: {}", host);
		
		// Handling localhost/127.0.0.1 for development purpose
		if (isLocalhost(host)) {
			log.info("Localhost detected. Falling back to first active school for development.");
			School defaultSchool = schoolRepository
											  .findFirstByIsActiveTrue()
											  .orElseThrow(
												  () -> new RuntimeException(
													  "No active school found for localhost fallback. Please seed data" + ".")
											  );
			TenantContext.setSchoolId(defaultSchool.getId());
			log.debug("Set tenant to school: {} (ID: {})", defaultSchool.getName(), defaultSchool.getId());
			return true;
		}
		
		// Production: Extract subdomain from the header request
		String subdomain = host.split("\\.")[0];
		School school = schoolRepository.findBySubdomain(subdomain).orElseThrow(
			() -> new RuntimeException("School not found for subdomain: " + subdomain));
		
		TenantContext.setSchoolId(school.getId());
		log.debug("Set tenant to school: {} (Subdomain: {})", school.getName(), subdomain);
		return true;
	}
	
	private boolean isLocalhost(String host) {
		return "localhost".equalsIgnoreCase(host)
					 || "127.0.0.1".equals(host)
					 || "0:0:0:0:0:0:0:1".equals(host) || // IPv6 localhost
					 host.startsWith("192.168.") ||     // Local network fallback
					 host.startsWith("10.");            // Local network fallback
	}
	
	@Override
	public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull Object handler, Exception ex) {
		TenantContext.clear();
		log.debug("Cleared tenant context");
	}
	
}