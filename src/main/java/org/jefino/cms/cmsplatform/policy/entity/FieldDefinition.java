package org.jefino.cms.cmsplatform.policy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Defines dynamic form fields for entities.
 * Drives schema-driven UI rendering.
 */
@Entity
@Table(name = "field_definitions")
@Getter
@Setter
@NoArgsConstructor
public class FieldDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Entity type this field belongs to: STUDENT, FACULTY, COURSE, etc.
     */
    @Column(nullable = false)
    private String entityType;

    /**
     * Unique code for this field within entity type.
     */
    @Column(nullable = false)
    private String fieldCode;

    /**
     * Field data type: TEXT, SELECT, DATE, NUMBER, BOOLEAN, FILE
     */
    @Column(nullable = false)
    private String fieldType;

    /**
     * Display label for UI
     */
    @Column(nullable = false)
    private String label;

    /**
     * Order in which field appears in forms
     */
    @Column(nullable = false)
    private int displayOrder;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private boolean active = true;

    /**
     * JSON configuration for field-specific settings:
     * - options (for SELECT)
     * - validation rules
     * - visibility rules
     * - placeholder text
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> config = new HashMap<>();

    public FieldDefinition(String entityType, String fieldCode, String fieldType, String label) {
        this.entityType = entityType;
        this.fieldCode = fieldCode;
        this.fieldType = fieldType;
        this.label = label;
    }
}
