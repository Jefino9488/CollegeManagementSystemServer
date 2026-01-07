package org.jefino.cms.cmsplatform.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * RBAC service for permission checking.
 * Used by controllers and service layer.
 */
@Service
public class RbacService {

    /**
     * Gets current authenticated user ID.
     */
    public UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UUID) {
            return (UUID) principal;
        }
        return null;
    }

    /**
     * Checks if current user has specific permission.
     */
    public boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }

    /**
     * Checks if current user has all specified permissions.
     */
    public boolean hasAllPermissions(Set<String> permissions) {
        return permissions.stream().allMatch(this::hasPermission);
    }

    /**
     * Checks if current user has any of the specified permissions.
     */
    public boolean hasAnyPermission(Set<String> permissions) {
        return permissions.stream().anyMatch(this::hasPermission);
    }
}
