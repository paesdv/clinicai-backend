package com.clinicai.backend.service;

import com.clinicai.backend.dto.GenerateSlotsRequest;
import com.clinicai.backend.dto.SlotRequest;
import com.clinicai.backend.dto.SlotResponse;
import com.clinicai.backend.model.Doctor;
import com.clinicai.backend.model.Slot;
import com.clinicai.backend.repository.DoctorRepository;
import com.clinicai.backend.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;

    public List<SlotResponse> listByDoctor(UUID doctorId, LocalDate date) {
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        return slotRepository.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SlotResponse> listAvailable(UUID doctorId, LocalDate date) {
        return slotRepository.findByDoctorIdAndDateAndAvailableTrue(doctorId, date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SlotResponse create(SlotRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        validateConflict(request.doctorId(), request.date(), request.startTime(), request.endTime());

        Slot slot = new Slot();
        slot.setDoctor(doctor);
        slot.setDate(request.date());
        slot.setStartTime(request.startTime());
        slot.setEndTime(request.endTime());
        slot.setAvailable(true);
        slotRepository.save(slot);
        return toResponse(slot);
    }

    public List<SlotResponse> generateSlots(UUID doctorId, GenerateSlotsRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        // Busca todos os slots existentes do período de uma única vez
        Set<String> existingKeys = slotRepository
                .findByDoctorIdAndDateBetween(doctorId, request.startDate(), request.endDate())
                .stream()
                .map(s -> s.getDate() + "_" + s.getStartTime())
                .collect(Collectors.toSet());

        List<Slot> slots = new ArrayList<>();
        LocalDate currentDate = request.startDate();

        while (!currentDate.isAfter(request.endDate())) {
            LocalTime currentTime = request.startTime();

            while (currentTime.isBefore(request.endTime())) {
                LocalTime slotEnd = currentTime.plusMinutes(request.intervalMinutes());

                if (!slotEnd.isAfter(request.endTime())) {
                    String key = currentDate + "_" + currentTime;
                    if (!existingKeys.contains(key)) {
                        Slot slot = new Slot();
                        slot.setDoctor(doctor);
                        slot.setDate(currentDate);
                        slot.setStartTime(currentTime);
                        slot.setEndTime(slotEnd);
                        slot.setAvailable(true);
                        slots.add(slot);
                    }
                }
                currentTime = slotEnd;
            }
            currentDate = currentDate.plusDays(1);
        }

        slotRepository.saveAll(slots);
        return slots.stream().map(this::toResponse).toList();
    }

    public void delete(UUID id) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot não encontrado"));
        slotRepository.delete(slot);
    }

    private void validateConflict(UUID doctorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Slot> conflicts = slotRepository.findConflicting(doctorId, date, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Já existe um slot neste horário para este médico");
        }
    }

    private SlotResponse toResponse(Slot s) {
        return new SlotResponse(
                s.getId(), s.getDate(), s.getStartTime(), s.getEndTime(),
                s.isAvailable(), s.getDoctor().getId(), s.getDoctor().getName(),
                s.getCreatedAt()
        );
    }
}