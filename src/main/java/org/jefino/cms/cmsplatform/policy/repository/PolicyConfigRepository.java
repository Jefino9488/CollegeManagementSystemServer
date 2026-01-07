package org.jefino.cms.cmsplatform.policy.repository;

import org.jefino.cms.cmsplatform.policy.entity.PolicyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyConfigRepository extends JpaRepository<PolicyConfig, UUID> {

    Optional<PolicyConfig> findByPolicyCode(String policyCode);

    List<PolicyConfig> findByPolicyTypeAndActive(String policyType, boolean active);

    @Query("SELECT p FROM PolicyConfig p WHERE p.policyType = :policyType AND p.active = true " +
            "AND (p.effectiveFrom IS NULL OR p.effectiveFrom <= :now) " +
            "AND (p.effectiveTo IS NULL OR p.effectiveTo > :now)")
    List<PolicyConfig> findEffectivePolicies(String policyType, Instant now);

    default List<PolicyConfig> findActiveByPolicyType(String policyType) {
        return findByPolicyTypeAndActive(policyType, true);
    }
}
