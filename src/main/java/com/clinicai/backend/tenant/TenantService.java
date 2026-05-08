package com.clinicai.backend.tenant;

import com.clinicai.backend.dto.AuthResponse;
import com.clinicai.backend.dto.RegisterRequest;
import com.clinicai.backend.enums.Plan;
import com.clinicai.backend.enums.Role;
import com.clinicai.backend.enums.Status;
import com.clinicai.backend.model.Clinic;
import com.clinicai.backend.model.User;
import com.clinicai.backend.repository.ClinicRepository;
import com.clinicai.backend.security.JwtService;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TenantService {

    private final ClinicRepository clinicRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TenantUserCreator tenantUserCreator;

    public TenantService(JwtService jwtService,
                         PasswordEncoder passwordEncoder,
                         JdbcTemplate jdbcTemplate,
                         ClinicRepository clinicRepository,
                         TenantUserCreator tenantUserCreator) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.clinicRepository = clinicRepository;
        this.tenantUserCreator = tenantUserCreator;
    }

    public String generateTenantId(String nome) {
        String slug = java.text.Normalizer.normalize(nome, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .replace(" ", "_")
                .toLowerCase();

        String alfabeto = "abcdefghijklmnopqrstuvwxyz0123456789";
        java.security.SecureRandom random = new java.security.SecureRandom();

        String tenantId;
        int tentativas = 0;

        do {
            StringBuilder sufixo = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                int indice = random.nextInt(alfabeto.length());
                sufixo.append(alfabeto.charAt(indice));
            }
            tenantId = slug + "_" + sufixo.toString();
            tentativas++;
        } while (clinicRepository.findByTenantId(tenantId).isPresent() && tentativas < 10);

        return tenantId;
    }

    public void createTenantSchema(String tenantId) {
        if (!tenantId.matches("^[a-z0-9_]+$")) {
            throw new IllegalArgumentException("ID de tenant inválido");
        }
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + tenantId);
    }

    public void runFlywayMigrations(String tenantId) {
        Flyway flyway = Flyway.configure()
                .dataSource(jdbcTemplate.getDataSource())
                .schemas(tenantId)
                .locations("db/tenant_migration")
                .load();
        flyway.migrate();
    }

    public AuthResponse register(RegisterRequest request) {
        if (clinicRepository.existsByEmail(request.adminEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        String tenantId = generateTenantId(request.clinicName());

        Clinic clinic = new Clinic();
        clinic.setTenantId(tenantId);
        clinic.setName(request.clinicName());
        clinic.setEmail(request.adminEmail());
        clinic.setPhone(request.phone());
        clinic.setPlan(Plan.STARTER);
        clinic.setStatus(Status.TRIAL);
        clinic.setTrialEndsAt(LocalDateTime.now().plusDays(14));
        clinic.setCreatedAt(LocalDateTime.now());
        clinic.setUpdatedAt(LocalDateTime.now());
        clinicRepository.save(clinic);

        createTenantSchema(tenantId);
        runFlywayMigrations(tenantId);

        User admin = new User();
        admin.setName(request.adminName());
        admin.setEmail(request.adminEmail());
        admin.setPassword(passwordEncoder.encode(request.adminPassword()));
        admin.setTenantId(tenantId);
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        // Seta ANTES de entrar na transação — o ConnectionProvider lê isso ao abrir conexão
        TenantContext.setCurrentTenant(tenantId);
        try {
            tenantUserCreator.saveUser(admin);
        } finally {
            TenantContext.clear();
        }

        String token = jwtService.generateToken(admin);

        return new AuthResponse(
                clinic.getName(),
                clinic.getEmail(),
                clinic.getPhone(),
                clinic.getStatus().name(),
                token
        );
    }
}