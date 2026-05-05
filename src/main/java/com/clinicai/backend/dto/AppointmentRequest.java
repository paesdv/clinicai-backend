package com.clinicai.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AppointmentRequest(
        @NotNull UUID slotId,
        @NotBlank String patientName,
        @Email String patientEmail,
        String patientPhone,
        String notes
) {}
