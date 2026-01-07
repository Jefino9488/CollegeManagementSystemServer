package org.jefino.cms.cmsplatform.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDto user;

    @Data
    @AllArgsConstructor
    public static class UserDto {
        private UUID id;
        private String email;
        private String displayName;
        private Set<String> roles;
        private Set<String> permissions;
    }
}
