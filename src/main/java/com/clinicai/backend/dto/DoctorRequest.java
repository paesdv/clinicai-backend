package com.clinicai.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DoctorRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        String phone,
        String crm,
        @NotNull UUID specialityId
) {}
