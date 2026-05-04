package com.clinicai.backend.service;

import com.clinicai.backend.model.Speciality;
import com.clinicai.backend.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public List<Speciality> listAll(){
        return specialtyRepository.findByActiveTrue();
    }

    public Speciality findById(UUID id){
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada."));
    }

    public Speciality create(Speciality speciality){
        speciality.setActive(true);
        return specialtyRepository.save(speciality);
    }

    public Speciality update(UUID id, Speciality data){
        Speciality especialidade = findById(id);
        especialidade.setName(data.getName());
        especialidade.setDescription(data.getDescription());
        especialidade.setDurationMinutes(data.getDurationMinutes());

        return specialtyRepository.save(especialidade);
    }

    public void deactivate(UUID id){
        Speciality especialidade = findById(id);
        especialidade.setActive(false);
        specialtyRepository.save(especialidade);
    }

}
