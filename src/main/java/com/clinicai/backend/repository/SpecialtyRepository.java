package com.clinicai.backend.repository;

import com.clinicai.backend.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpecialtyRepository extends JpaRepository<Speciality, UUID> {
    List<Speciality> findByActiveTrue();
}
