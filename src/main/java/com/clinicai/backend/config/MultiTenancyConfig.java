package com.clinicai.backend.config;

import com.clinicai.backend.tenant.TenantConnectionProvider;
import com.clinicai.backend.tenant.TenantIdentifierResolver;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MultiTenancyConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            TenantConnectionProvider tenantConnectionProvider,
            TenantIdentifierResolver tenantIdentifierResolver) {

        return properties -> {
            properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER,
                    tenantConnectionProvider);
            properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER,
                    tenantIdentifierResolver);
        };
    }
}