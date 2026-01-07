package org.jefino.cms.cmsplatform.policy.controller;

import org.jefino.cms.cmsplatform.policy.dto.FieldDefinitionDto;
import org.jefino.cms.cmsplatform.policy.dto.NavigationItem;
import org.jefino.cms.cmsplatform.policy.service.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Configuration endpoints for schema and navigation.
 */
@RestController
@RequestMapping("/api/v1/{tenant}/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * Gets field definitions for dynamic form rendering.
     */
    @GetMapping("/fields")
    public ResponseEntity<List<FieldDefinitionDto>> getFields(
            @PathVariable String tenant,
            @RequestParam String entityType) {
        List<FieldDefinitionDto> fields = configService.getFieldsByEntityType(entityType);
        return ResponseEntity.ok(fields);
    }

    /**
     * Gets navigation structure for sidebar.
     */
    @GetMapping("/navigation")
    public ResponseEntity<List<NavigationItem>> getNavigation(@PathVariable String tenant) {
        List<NavigationItem> navigation = configService.getNavigation();
        return ResponseEntity.ok(navigation);
    }
}
