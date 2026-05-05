package com.clinicai.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        String patientName,
        String patientEmail,
        String patientPhone,
        String notes,
        String status,
        UUID slotId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String doctorName,
        String specialityName,
        LocalDateTime createdAt
) {}
