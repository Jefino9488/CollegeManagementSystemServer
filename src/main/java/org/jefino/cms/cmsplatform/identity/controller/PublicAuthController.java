package org.jefino.cms.cmsplatform.identity.controller;

import jakarta.validation.Valid;
import org.jefino.cms.cmsplatform.core.security.RbacService;
import org.jefino.cms.cmsplatform.core.tenant.TenantContext;
import org.jefino.cms.cmsplatform.identity.dto.AuthResponse;
import org.jefino.cms.cmsplatform.identity.dto.LoginRequest;
import org.jefino.cms.cmsplatform.identity.dto.RefreshTokenRequest;
import org.jefino.cms.cmsplatform.identity.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public authentication endpoints (no auth required).
 */
@RestController
@RequestMapping("/api/v1/public/auth")
public class PublicAuthController {

    private final AuthService authService;

    public PublicAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            @RequestHeader(value = "X-Tenant-Code", required = false) String tenantCode) {
        // Use header or default to "demo" for MVP
        String tenant = tenantCode != null ? tenantCode : "demo";
        AuthResponse response = authService.login(request, tenant);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            @RequestHeader(value = "X-Tenant-Code", required = false) String tenantCode) {
        String tenant = tenantCode != null ? tenantCode : "demo";
        AuthResponse response = authService.refresh(request.getRefreshToken(), tenant);
        return ResponseEntity.ok(response);
    }
}
