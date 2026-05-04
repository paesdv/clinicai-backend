package com.clinicai.backend.repository;

import com.clinicai.backend.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<Slot, UUID> {
    List<Slot> findByDoctorIdAndDate(UUID doctorId, LocalDate date);
    List<Slot> findByDoctorIdAndDateAndAvailableTrue(UUID doctorId, LocalDate date);
    Optional<Slot> findByIdAndAvailableTrue(UUID id);
}
