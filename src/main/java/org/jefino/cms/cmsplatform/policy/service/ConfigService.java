package org.jefino.cms.cmsplatform.policy.service;

import org.jefino.cms.cmsplatform.policy.dto.FieldDefinitionDto;
import org.jefino.cms.cmsplatform.policy.dto.NavigationItem;
import org.jefino.cms.cmsplatform.policy.entity.FieldDefinition;
import org.jefino.cms.cmsplatform.policy.repository.FieldDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Configuration service providing schema and navigation data.
 */
@Service
public class ConfigService {

    private final FieldDefinitionRepository fieldDefinitionRepository;

    public ConfigService(FieldDefinitionRepository fieldDefinitionRepository) {
        this.fieldDefinitionRepository = fieldDefinitionRepository;
    }

    /**
     * Gets field definitions for an entity type.
     */
    public List<FieldDefinitionDto> getFieldsByEntityType(String entityType) {
        return fieldDefinitionRepository.findActiveByEntityType(entityType)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Gets navigation items for sidebar.
     * MVP: Returns static structure, filtered by permissions on frontend.
     */
    public List<NavigationItem> getNavigation() {
        // MVP: Static navigation structure
        // In production, this would be configurable per tenant
        return List.of(
                new NavigationItem("dashboard", "Dashboard", "home", "/dashboard",
                        List.of(), List.of()),
                new NavigationItem("students", "Students", "users", "/students",
                        List.of("STUDENT_VIEW"), List.of()),
                new NavigationItem("faculty", "Faculty", "briefcase", "/faculty",
                        List.of("FACULTY_VIEW"), List.of()),
                new NavigationItem("academics", "Academics", "book-open", "/academics",
                        List.of("ACADEMIC_VIEW"), List.of(
                                new NavigationItem("programs", "Programs", "graduation-cap", "/academics/programs",
                                        List.of("PROGRAM_VIEW"), List.of()),
                                new NavigationItem("courses", "Courses", "layers", "/academics/courses",
                                        List.of("COURSE_VIEW"), List.of()))),
                new NavigationItem("settings", "Settings", "settings", "/settings",
                        List.of("ADMIN"), List.of()));
    }

    private FieldDefinitionDto toDto(FieldDefinition entity) {
        return new FieldDefinitionDto(
                entity.getId(),
                entity.getFieldCode(),
                entity.getFieldType(),
                entity.getLabel(),
                entity.getDisplayOrder(),
                entity.isRequired(),
                entity.getConfig());
    }
}
