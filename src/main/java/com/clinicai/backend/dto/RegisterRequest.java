package com.clinicai.backend.dto;

public record RegisterRequest(String clinicName, String adminEmail, String adminPassword, String adminName, String phone) {
}
