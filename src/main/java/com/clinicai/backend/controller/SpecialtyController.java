package com.clinicai.backend.controller;

import com.clinicai.backend.dto.SpecialtyRequest;
import com.clinicai.backend.dto.SpecialtyResponse;
import com.clinicai.backend.model.Speciality;
import com.clinicai.backend.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> listAll() {
        return ResponseEntity.ok(specialtyService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(specialtyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SpecialtyResponse> create(@Valid @RequestBody SpecialtyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> update(@PathVariable UUID id, @Valid @RequestBody SpecialtyRequest request) {
        return ResponseEntity.ok(specialtyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        specialtyService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
