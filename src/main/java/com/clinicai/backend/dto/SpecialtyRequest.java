package com.clinicai.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record SpecialtyRequest(
        @NotBlank String name,
        String description,
        int durationMinutes
) {}
