package com.clinicai.backend.dto;

public record AuthResponse(String clinicName, String email, String phone, String status, String token) {
}