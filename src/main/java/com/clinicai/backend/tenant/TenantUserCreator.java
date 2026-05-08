package com.clinicai.backend.tenant;

import com.clinicai.backend.model.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantUserCreator {

    private final EntityManager entityManager;

    public TenantUserCreator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveUser(User admin) {
        entityManager.persist(admin);
        entityManager.flush();
    }
}