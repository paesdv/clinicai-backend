package com.clinicai.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoctorResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String crm,
        boolean active,
        UUID specialityId,
        String specialityName,
        LocalDateTime createdAt
) {}
