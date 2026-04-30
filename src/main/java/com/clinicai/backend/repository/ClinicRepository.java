package com.clinicai.backend.repository;

import com.clinicai.backend.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    Optional<Clinic> findByEmail(String email);
    Optional<Clinic> findByTenantId(String tenantId);
    boolean existsByEmail(String email);

    String email(String email);
}