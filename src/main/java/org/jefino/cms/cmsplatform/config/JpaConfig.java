package org.jefino.cms.cmsplatform.config;

import org.hibernate.cfg.AvailableSettings;
import org.jefino.cms.cmsplatform.core.tenant.SchemaBasedMultiTenantConnectionProvider;
import org.jefino.cms.cmsplatform.core.tenant.TenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * JPA/Hibernate configuration for multi-tenancy.
 */
@Configuration
public class JpaConfig {

    private final SchemaBasedMultiTenantConnectionProvider connectionProvider;
    private final TenantIdentifierResolver tenantIdentifierResolver;

    public JpaConfig(SchemaBasedMultiTenantConnectionProvider connectionProvider,
            TenantIdentifierResolver tenantIdentifierResolver) {
        this.connectionProvider = connectionProvider;
        this.tenantIdentifierResolver = tenantIdentifierResolver;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        };
    }
}
