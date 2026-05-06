package com.clinicai.backend.controller;


import com.clinicai.backend.dto.AppointmentRequest;
import com.clinicai.backend.dto.AppointmentResponse;
import com.clinicai.backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(@PathVariable UUID id){
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> listByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(appointmentService.listByDate(date));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id){
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> reschedule(@PathVariable UUID id, @RequestParam UUID newSlotId){
        return ResponseEntity.ok(appointmentService.reschedule(id, newSlotId));
    }




}
