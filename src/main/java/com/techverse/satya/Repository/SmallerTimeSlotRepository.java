package com.techverse.satya.Repository;

import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Model.TimeSlot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.List;

public interface SmallerTimeSlotRepository extends JpaRepository<SmallerTimeSlot, Long> {
    List<SmallerTimeSlot> findByTimeSlotDateAndTimeSlotAvailability(String date, String availability);
    List<SmallerTimeSlot> findByTimeSlotDateAndStartTimeAndEndTime(String date, String startTime, String endTime);
 //   List<SmallerTimeSlot> findByTimeSlotDateAndStartTime(TimeSlot timeSlot, String startTime);
    @Modifying
    @Query("DELETE FROM SmallerTimeSlot s WHERE s.id = :smallerTimeSlotId")
    void deleteById(Long smallerTimeSlotId);

    
    
    List<SmallerTimeSlot> findByAdminId(Long adminId);
    
    @Query("SELECT s FROM SmallerTimeSlot s WHERE s.timeSlot.date = :date AND s.startTime = :startTime")
    Optional<SmallerTimeSlot> findByTimeSlotDateAndStartTime(@Param("date") String date, @Param("startTime") String startTime);

   /* 
    @Query("SELECT s FROM SmallerTimeSlot s WHERE s.timeSlot.date = :date AND s.startTime = :startTime AND s.timeSlot.availability = :availability AND s.slotBook = false")
    Optional<SmallerTimeSlot> findByDateAndStartTimeAndAvailabilityAndSlotBook(
            @Param("date") String date,
            @Param("startTime") String startTime,
            @Param("availability") String availability
    );*/
    @Query("SELECT s FROM SmallerTimeSlot s WHERE s.timeSlot.date = :date AND s.startTime = :startTime AND s.timeSlot.availability = :availability AND s.slotBook = false AND s.admin.id = :adminId")
    Optional<SmallerTimeSlot> findByDateAndStartTimeAndAvailabilityAndSlotBookAndAdminId(
            @Param("date") String date,
            @Param("startTime") String startTime,
            @Param("availability") String availability,
            @Param("adminId") Long adminId
    );
    @Query("SELECT s FROM SmallerTimeSlot s WHERE s.timeSlot.date = :date AND s.startTime = :startTime AND s.timeSlot.availability = :availability  AND s.admin.id = :adminId")
    Optional<SmallerTimeSlot> findByDateAndStartTimeAndAvailabilityAndAdminId(
            @Param("date") String date,
            @Param("startTime") String startTime,
            @Param("availability") String availability,
            @Param("adminId") Long adminId
    );
   
    @Query(value = "SELECT * FROM time_slot_details " +
            "WHERE time_slot_id IN (SELECT id FROM time_slot WHERE date = :date) " +
            "AND ((" +
            ":startTime > start_time AND :startTime < end_time) " +
            "OR " +
            "(:endTime > start_time AND :endTime < end_time))",
            nativeQuery = true)
List<Object[]> findOverlappingTimeSlots(@Param("date") String date,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime);


 
 
List<SmallerTimeSlot> findByTimeSlotDateAndTimeSlotAvailabilityAndAdminIdAndSlotBook(String date, String availability, Long adminId, boolean slotBook);



@Query(value = "SELECT start_time, end_time FROM time_slot_details " +
        "WHERE time_slot_id IN (SELECT id FROM time_slot WHERE date = :date AND admin_id = :adminId) " +
        "AND ((" +
        ":endTime > start_time AND :startTime < end_time) " +
        "OR " +
        "(:endTime > start_time AND :startTime < end_time) " +
        "OR " +
        "(:startTime <= start_time AND :endTime >= end_time))",
        nativeQuery = true)
List<Object[]> findOverlappingTimeSlots(@Param("date") String date,
                                 @Param("startTime") String startTime,
                                 @Param("endTime") String endTime,
                                 @Param("adminId") Long adminId);



List<SmallerTimeSlot> findByTimeSlotId(Long timeSlotId);

}