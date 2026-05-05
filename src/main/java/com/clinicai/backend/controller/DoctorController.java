package com.clinicai.backend.controller;

import com.clinicai.backend.dto.DoctorRequest;
import com.clinicai.backend.dto.DoctorResponse;
import com.clinicai.backend.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> listAll() {
        return ResponseEntity.ok(doctorService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.findById(id));
    }

    @GetMapping("/specialty/{specialityId}")
    public ResponseEntity<List<DoctorResponse>> listBySpeciality(@PathVariable UUID specialityId) {
        return ResponseEntity.ok(doctorService.listBySpeciality(specialityId));
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(@PathVariable UUID id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        doctorService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}