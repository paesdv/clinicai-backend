package com.clinicai.backend.controller;

import com.clinicai.backend.model.Speciality;
import com.clinicai.backend.service.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController (SpecialtyService specialtyService){
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public ResponseEntity<List<Speciality>> listAll(){
        return ResponseEntity.status(200).body(specialtyService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Speciality> findById(@PathVariable UUID id){
        return ResponseEntity.status(200).body(specialtyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Speciality> create(@RequestBody Speciality speciality){
        return ResponseEntity.status(201).body(specialtyService.create(speciality));
    }

    @PutMapping("{id}")
    public ResponseEntity<Speciality> update(@PathVariable UUID id, @RequestBody Speciality data){
        return ResponseEntity.status(200).body(specialtyService.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id){
        specialtyService.deactivate(id);
        return ResponseEntity.noContent().build();

    }
}
