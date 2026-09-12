package com.campax.campaxserver.context;

import java.util.UUID;

public class TenantContext {
	private static final ThreadLocal<UUID> CURRENT_SCHOOL = new ThreadLocal<>();
	
	public static void setSchoolId(UUID id) {CURRENT_SCHOOL.set(id);}
	
	public static UUID getSchoolId() {return CURRENT_SCHOOL.get();}
	
	public static void clear() {CURRENT_SCHOOL.remove();}
	
}