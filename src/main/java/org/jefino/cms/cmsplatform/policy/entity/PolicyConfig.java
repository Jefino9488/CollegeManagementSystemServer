package org.jefino.cms.cmsplatform.policy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Configurable business policies that replace hardcoded rules.
 * Examples: attendance thresholds, grading scales, fee structures.
 */
@Entity
@Table(name = "policy_configs")
@Getter
@Setter
@NoArgsConstructor
public class PolicyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Policy category: ATTENDANCE_THRESHOLD, GRADING_SCALE, FEE_STRUCTURE, etc.
     */
    @Column(nullable = false)
    private String policyType;

    /**
     * Unique code for this policy within type.
     */
    @Column(nullable = false, unique = true)
    private String policyCode;

    /**
     * Human-readable name
     */
    @Column(nullable = false)
    private String displayName;

    private String description;

    /**
     * JSON rules defining the policy logic.
     * Structure depends on policyType.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> rules = new HashMap<>();

    /**
     * When this policy becomes active
     */
    private Instant effectiveFrom;

    /**
     * When this policy expires (null = no expiry)
     */
    private Instant effectiveTo;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    /**
     * Checks if policy is currently effective.
     */
    public boolean isEffective() {
        Instant now = Instant.now();
        boolean afterStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || now.isBefore(effectiveTo);
        return active && afterStart && beforeEnd;
    }
}
