package com.clinicai.backend.repository;

import com.clinicai.backend.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    List<Doctor> findBySpeciality_Id(UUID speciality);
    List<Doctor> findByActiveTrue();
    Optional<Doctor> findByIdAndActiveTrue(UUID id);
}
