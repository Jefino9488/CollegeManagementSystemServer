package org.jefino.cms.cmsplatform.identity.service;

import org.jefino.cms.cmsplatform.core.security.JwtService;
import org.jefino.cms.cmsplatform.identity.dto.AuthResponse;
import org.jefino.cms.cmsplatform.identity.dto.LoginRequest;
import org.jefino.cms.cmsplatform.identity.entity.Role;
import org.jefino.cms.cmsplatform.identity.entity.User;
import org.jefino.cms.cmsplatform.identity.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Authentication service handling login and token refresh.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Authenticates user and returns tokens.
     */
    public AuthResponse login(LoginRequest request, String tenantCode) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!user.isActive()) {
            throw new BadCredentialsException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(user, tenantCode);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    /**
     * Refreshes access token using refresh token.
     */
    public AuthResponse refresh(String refreshToken, String tenantCode) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        UUID userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!user.isActive()) {
            throw new BadCredentialsException("Account is disabled");
        }

        String newAccessToken = jwtService.generateAccessToken(user, tenantCode);
        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    /**
     * Gets user details for current user endpoint.
     */
    public AuthResponse.UserDto getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse.UserDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()),
                user.getAllPermissions());
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        var userDto = new AuthResponse.UserDto(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()),
                user.getAllPermissions());
        return new AuthResponse(accessToken, refreshToken, userDto);
    }
}
