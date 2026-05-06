package com.clinicai.backend.service;


import com.clinicai.backend.dto.SlotRequest;
import com.clinicai.backend.dto.SlotResponse;
import com.clinicai.backend.model.Doctor;
import com.clinicai.backend.model.Slot;
import com.clinicai.backend.repository.DoctorRepository;
import com.clinicai.backend.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;

    public List<SlotResponse> listByDoctor(UUID doctorId, LocalDate date){
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        return slotRepository.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SlotResponse> listAvailable(UUID doctorId, LocalDate date){
        return slotRepository.findByDoctorIdAndDateAndAvailableTrue(doctorId, date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SlotResponse createSlot(SlotRequest request){

        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        Slot slot = new Slot();
        slot.setDoctor(doctor);
        slot.setDate(request.date());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());

        slotRepository.save(slot);

        return toResponse(slot);
    }


    public void deleteSlot(UUID id){
        Slot slot = slotRepository.findById(id).orElseThrow(() -> new RuntimeException("Slot não encontrado"));
        slotRepository.delete(slot);
    }




    private SlotResponse toResponse(Slot s) {
        return new SlotResponse(
                s.getId(),
                s.getDate(),
                s.getStartTime(),
                s.getEndTime(),
                s.isAvailable(),
                s.getDoctor().getId(),
                s.getDoctor().getName(),
                s.getCreatedAt()
        );
    }

}
