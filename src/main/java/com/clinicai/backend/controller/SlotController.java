package com.clinicai.backend.controller;


import com.clinicai.backend.dto.SlotRequest;
import com.clinicai.backend.dto.SlotResponse;
import com.clinicai.backend.model.Slot;
import com.clinicai.backend.service.SlotService;
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
@RequestMapping("/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<SlotResponse>> listByDoctor(@PathVariable UUID doctorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(slotService.listByDoctor(doctorId, date));
    }

    @GetMapping("/doctor/{doctorId}/available")
    public ResponseEntity<List<SlotResponse>> listAvailable(@PathVariable UUID doctorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ResponseEntity.ok(slotService.listAvailable(doctorId, date));
    }

    @PostMapping
    public ResponseEntity<SlotResponse> create(@Valid @RequestBody SlotRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(slotService.createSlot(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        slotService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }

}
