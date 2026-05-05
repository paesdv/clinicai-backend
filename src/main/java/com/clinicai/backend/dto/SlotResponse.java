package com.clinicai.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record SlotResponse(
        UUID id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean available,
        UUID doctorId,
        String doctorName,
        LocalDateTime createdAt
) {}
