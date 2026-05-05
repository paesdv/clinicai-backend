package com.clinicai.backend.service;

import com.clinicai.backend.dto.DoctorRequest;
import com.clinicai.backend.dto.DoctorResponse;
import com.clinicai.backend.model.Doctor;
import com.clinicai.backend.model.Speciality;
import com.clinicai.backend.repository.DoctorRepository;
import com.clinicai.backend.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;

    public List<DoctorResponse> listAll() {
        return doctorRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DoctorResponse findById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        return toResponse(doctor);
    }

    public List<DoctorResponse> listBySpeciality(UUID specialityId) {
        specialtyRepository.findById(specialityId)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        return doctorRepository.findBySpeciality_Id(specialityId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DoctorResponse create(DoctorRequest request) {
        Speciality speciality = specialtyRepository.findById(request.specialityId())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        Doctor doctor = new Doctor();
        doctor.setName(request.name());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setCrm(request.crm());
        doctor.setSpeciality(speciality);
        doctor.setActive(true);
        doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    public DoctorResponse update(UUID id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        Speciality speciality = specialtyRepository.findById(request.specialityId())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

        doctor.setName(request.name());
        doctor.setEmail(request.email());
        doctor.setPhone(request.phone());
        doctor.setCrm(request.crm());
        doctor.setSpeciality(speciality);
        doctorRepository.save(doctor);
        return toResponse(doctor);
    }

    public void deactivate(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    private DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(
                d.getId(), d.getName(), d.getEmail(), d.getPhone(), d.getCrm(),
                d.isActive(),
                d.getSpeciality().getId(), d.getSpeciality().getName(),
                d.getCreatedAt()
        );
    }
}
