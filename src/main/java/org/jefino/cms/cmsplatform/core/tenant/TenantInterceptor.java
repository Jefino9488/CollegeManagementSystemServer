package org.jefino.cms.cmsplatform.core.tenant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts tenant code from URL path and sets it in TenantContext.
 * URL pattern: /api/v1/{tenantCode}/...
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Pattern TENANT_PATTERN = Pattern.compile("^/api/v\\d+/([^/]+)/.*$");
    private static final String PUBLIC_PATH_PREFIX = "/api/v1/public/";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        // Skip public endpoints (login, refresh, etc.)
        if (path.startsWith(PUBLIC_PATH_PREFIX)) {
            return true;
        }

        String tenantCode = extractTenant(path);
        if (tenantCode != null) {
            TenantContext.setTenantCode(tenantCode);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        TenantContext.clear();
    }

    private String extractTenant(String path) {
        Matcher matcher = TENANT_PATTERN.matcher(path);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
