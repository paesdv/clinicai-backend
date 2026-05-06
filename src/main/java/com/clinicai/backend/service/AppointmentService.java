package com.clinicai.backend.service;

import com.clinicai.backend.dto.AppointmentRequest;
import com.clinicai.backend.dto.AppointmentResponse;
import com.clinicai.backend.dto.SlotResponse;
import com.clinicai.backend.enums.AppointmentStatus;
import com.clinicai.backend.model.Appointment;
import com.clinicai.backend.model.Slot;
import com.clinicai.backend.repository.AppointmentRepository;
import com.clinicai.backend.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public AppointmentResponse create(AppointmentRequest request){

        Slot slot = slotRepository.findById(request.slotId())
                .orElseThrow(() -> new RuntimeException("Slot não encontrado."));

        if (!slot.isAvailable()){
            throw new RuntimeException("Slot já está ocupado");
        }

        Appointment appointment = new Appointment();
        appointment.setSlot(slot);
        appointment.setPatientName(request.patientName());
        appointment.setPatientEmail(request.patientEmail());
        appointment.setPatientPhone(request.patientPhone());
        appointment.setNotes(request.notes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        slot.setAvailable(false);
        appointmentRepository.save(appointment);

        return toResponse(appointment);

    }


    public AppointmentResponse findById(UUID id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment não encontrado"));
        return toResponse(appointment);
    }

    public List<AppointmentResponse> listByDate(LocalDate date){
        return appointmentRepository.findBySlot_Date(date)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void cancel(UUID id){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment não encontrado"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.getSlot().setAvailable(true);

        appointmentRepository.save(appointment);
    }

    public AppointmentResponse reschedule(UUID id, UUID newSlotId){
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment não encontrado"));

        Slot slot = slotRepository.findById(newSlotId)
                .orElseThrow(() -> new RuntimeException("Novo Slot não encontrado"));

        if (!slot.isAvailable()){
            throw new RuntimeException("Novo Slot não disponível");
        }

        appointment.getSlot().setAvailable(true);
        slot.setAvailable(false);
        appointment.setSlot(slot);

        appointmentRepository.save(appointment);
        return toResponse(appointment);

    }


    private AppointmentResponse toResponse(Appointment s) {
        return new AppointmentResponse(
                s.getId(),
                s.getPatientName(),
                s.getPatientEmail(),
                s.getPatientPhone(),
                s.getNotes(),
                s.getStatus().name(),
                s.getSlot().getId(),
                s.getSlot().getDate(),
                s.getSlot().getStartTime(),
                s.getSlot().getEndTime(),
                s.getSlot().getDoctor().getName(),
                s.getSlot().getDoctor().getSpeciality().getName(),
                s.getCreatedAt()
        );
    }

}
