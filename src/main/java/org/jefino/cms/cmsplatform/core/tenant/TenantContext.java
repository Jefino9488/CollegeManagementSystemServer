package org.jefino.cms.cmsplatform.core.tenant;

/**
 * Thread-local holder for tenant context.
 * Set by TenantInterceptor, cleared after request.
 */
public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Utility class
    }

    public static String getTenantCode() {
        return CURRENT_TENANT.get();
    }

    public static void setTenantCode(String tenantCode) {
        CURRENT_TENANT.set(tenantCode);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static boolean isSet() {
        return CURRENT_TENANT.get() != null;
    }
}
