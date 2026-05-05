package com.clinicai.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SpecialtyResponse(
        UUID id,
        String name,
        String description,
        int durationMinutes,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
