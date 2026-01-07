package org.jefino.cms.cmsplatform.policy.repository;

import org.jefino.cms.cmsplatform.policy.entity.FieldDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldDefinitionRepository extends JpaRepository<FieldDefinition, UUID> {

    List<FieldDefinition> findByEntityTypeAndActiveOrderByDisplayOrder(String entityType, boolean active);

    default List<FieldDefinition> findActiveByEntityType(String entityType) {
        return findByEntityTypeAndActiveOrderByDisplayOrder(entityType, true);
    }
}
