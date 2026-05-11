package com.clinicai.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record GenerateSlotsRequest(

        @NotNull(message = "Data de início é obrigatória")
        LocalDate startDate,

        @NotNull(message = "Data de fim é obrigatória")
        LocalDate endDate,

        @NotNull(message = "Hora de início é obrigatória")
        LocalTime startTime,

        @NotNull(message = "Hora de fim é obrigatória")
        LocalTime endTime,

        @Min(value = 5, message = "Intervalo mínimo é de 5 minutos")
        int intervalMinutes

) {
    public GenerateSlotsRequest {
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("endDate deve ser igual ou após startDate");
        }
        if (endTime != null && startTime != null && !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime deve ser após startTime");
        }
    }
}