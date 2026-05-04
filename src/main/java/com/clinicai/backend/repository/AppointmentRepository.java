package com.clinicai.backend.repository;

import com.clinicai.backend.model.Appointment;
import com.clinicai.backend.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findBySlot_Date(LocalDate date);
    Optional<Appointment> findBySlot(Slot slot);
    Optional<Appointment> findByCancellationToken(String token);
}
