package com.techverse.satya.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // You can define custom query methods here if needed
	   // Query method to find today's appointments for a specific user
   // List<Appointment> findByUserIdAndDate(Long userId, String date);
    List<Appointment> findByUserIdAndDateAndStatus(Long userId, String date,String status);

    List<Appointment> findByUserId(Long userId);
    
    List<Appointment> findByAdminIdAndDateAndStatus(Long userId, String date,String status);
    List<Appointment> findByAdminIdAndAppointmentTypeAndDateAndStatus(Long adminId, String availabilityType, String date,String status);
    List<Appointment> findByAdminId(Long userId);
    

    // Query method to find upcoming appointments for a specific user
    List<Appointment> findByUserIdAndDateAfterAndStatus(Long userId, String date,String status);
    List<Appointment> findByAdminIdAndDateAfterAndStatus(Long userId, String date,String status);
    List<Appointment> findByAdminIdAndAppointmentTypeAndDateAfter(Long adminId, String availabilityType, String date);
    List<Appointment> findByAdminIdAndAppointmentTypeAndDateAfterAndStatus(Long adminId, String availabilityType, String date,String status);
    

    // Query method to find past appointments for a specific user
    List<Appointment> findByUserIdAndDateBefore(Long userId, String date);
    List<Appointment> findByAdminIdAndDateBefore(Long userId, String date);
    List<Appointment> findByAdminIdAndAppointmentTypeAndDateBefore(Long adminId, String availabilityType, String date);
    
    List<Appointment> findAll();
    
    List<Appointment> findByDate(String date);

    List<Appointment> findByDateGreaterThan(String date);

    List<Appointment> findByDateLessThan(String date);
    List<Appointment> findByAppointmentType(String appointmentType);
    
    List<Appointment> findByUserIdAndStatus(Long userId, String status);
    List<Appointment> findByAdminIdAndStatus(Long userId, String status);
    List<Appointment> findByAdminIdAndAppointmentTypeAndStatus(Long adminId, String availabilityType, String status);
    
    List<Appointment> findByStatus(String status);

}

