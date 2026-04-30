package com.clinicai.backend.service;

import com.clinicai.backend.dto.AuthResponse;
import com.clinicai.backend.dto.RegisterRequest;
import com.clinicai.backend.enums.Plan;
import com.clinicai.backend.enums.Role;
import com.clinicai.backend.enums.Status;
import com.clinicai.backend.model.Clinic;
import com.clinicai.backend.model.User;
import com.clinicai.backend.repository.ClinicRepository;
import com.clinicai.backend.repository.UserRepository;
import com.clinicai.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JdbcTemplate jdbcTemplate;



    public AuthResponse register(RegisterRequest request){
        if(clinicRepository.existsByEmail(request.adminEmail())){
            throw new RuntimeException("Admin already exists");
        }

        String baseName = request.clinicName().toLowerCase().replaceAll("\\s+", "_");
        String tenantId = baseName + "_" + String.format("%04d", new Random().nextInt(10000));

        Clinic clinic = Clinic.builder()
                .name(request.clinicName())
                .email(request.adminEmail())
                .phone(request.phone())
                .plan(Plan.STARTER)
                .status(Status.TRIAL)
                .trialEndsAt(LocalDateTime.now().plusDays(14))
                .build();

        clinicRepository.save(clinic);


        String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + tenantId;
        jdbcTemplate.execute(createSchemaSql);


        User admin = User.builder()
                .name(request.adminName())
                .email(request.adminEmail())
                .password(passwordEncoder.encode(request.adminPassword()))
                .tenantId(tenantId)
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);


        String jwtToken = jwtService.generateToken(admin);


        return new AuthResponse(
                clinic.getName(),
                clinic.getEmail(),
                clinic.getPhone(),
                clinic.getStatus().name(),
                jwtToken
        );
    }

}
