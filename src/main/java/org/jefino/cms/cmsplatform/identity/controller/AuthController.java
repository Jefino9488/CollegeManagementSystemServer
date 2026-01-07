package org.jefino.cms.cmsplatform.identity.controller;

import org.jefino.cms.cmsplatform.core.security.RbacService;
import org.jefino.cms.cmsplatform.identity.dto.AuthResponse;
import org.jefino.cms.cmsplatform.identity.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authenticated user endpoints (auth required).
 */
@RestController
@RequestMapping("/api/v1/{tenant}/auth")
public class AuthController {

    private final AuthService authService;
    private final RbacService rbacService;

    public AuthController(AuthService authService, RbacService rbacService) {
        this.authService = authService;
        this.rbacService = rbacService;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> getCurrentUser(@PathVariable String tenant) {
        var userId = rbacService.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        AuthResponse.UserDto user = authService.getCurrentUser(userId);
        return ResponseEntity.ok(user);
    }
}
