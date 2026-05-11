package com.clinicai.backend.service;

import com.clinicai.backend.dto.SpecialtyRequest;
import com.clinicai.backend.dto.SpecialtyResponse;
import com.clinicai.backend.model.Speciality;
import com.clinicai.backend.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public List<SpecialtyResponse> listAll() {
        return specialtyRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SpecialtyResponse findById(UUID id) {
        Speciality specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade não encontrada"
                ));

        return toResponse(specialty);
    }

    public SpecialtyResponse create(SpecialtyRequest request) {

        Speciality specialty = new Speciality();

        specialty.setName(request.name());
        specialty.setDescription(request.description());
        specialty.setDurationMinutes(request.durationMinutes());
        specialty.setActive(true);

        specialtyRepository.save(specialty);

        return toResponse(specialty);
    }

    public SpecialtyResponse update(UUID id, SpecialtyRequest request) {

        Speciality specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade não encontrada"
                ));

        specialty.setName(request.name());
        specialty.setDescription(request.description());
        specialty.setDurationMinutes(request.durationMinutes());

        specialtyRepository.save(specialty);

        return toResponse(specialty);
    }

    public void deactivate(UUID id) {

        Speciality specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Especialidade não encontrada"
                ));

        specialty.setActive(false);

        specialtyRepository.save(specialty);
    }

    private SpecialtyResponse toResponse(Speciality s) {

        return new SpecialtyResponse(
                s.getId(),
                s.getName(),
                s.getDescription(),
                s.getDurationMinutes(),
                s.isActive(),
                s.getCreatedAt(),
                s.getUpdatedAt()
        );
    }
}