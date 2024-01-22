package com.techverse.satya.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.DTO.TimeSlotDetailDto;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
	Optional<TimeSlot> findByDate(String date);
	@Query("SELECT tsd.address FROM TimeSlot ts " +
		       "JOIN ts.timeSlotDetails tsd " +
		       "WHERE ts.date = :date AND tsd.startTime = :startTime")
		String findAddressByDateAndStartTime(@Param("date") String date, @Param("startTime") String startTime);

  
	@Query("SELECT tsd.address FROM TimeSlot t " +
		       "JOIN t.timeSlotDetails tsd " +
		       "WHERE t.date = :date " +
		       "AND FUNCTION('STR_TO_DATE', :startTime, '%h:%i%p') >= FUNCTION('STR_TO_DATE', tsd.startTime, '%h:%i%p') " +
		       "AND FUNCTION('STR_TO_DATE', :startTime, '%h:%i%p') <= FUNCTION('STR_TO_DATE', tsd.endTime, '%h:%i%p') " +
		       "AND t.admin.id = :adminId")
		String findAddressByDateStartTimeAndAdminId(@Param("date") String date,
		                                            @Param("startTime") String startTime,
		                                            @Param("adminId") Long adminId);

	 
	 
	  
	  List<TimeSlot> findByAdminAndDate(Admin admin, String date);
	 
	  
	  @Query("SELECT tsd FROM TimeSlot t JOIN t.timeSlotDetails tsd WHERE t.admin = :admin AND t.date = :date " +
		       "AND (" +
		       "  (tsd.startTime < :newEndTime AND tsd.endTime > :newStartTime)" +
		       "  OR (tsd.startTime >= :newStartTime AND tsd.startTime < :newEndTime)" +
		       "  OR (tsd.endTime > :newStartTime AND tsd.endTime <= :newEndTime)" +
		       "  OR (tsd.startTime <= :newStartTime AND tsd.endTime >= :newEndTime)" +
		       "  OR (tsd.startTime <= :newEndTime AND tsd.endTime >= :newStartTime)" +
		       "  OR (tsd.startTime <= :newStartTime AND tsd.endTime >= :newEndTime)" +
		       "  OR (tsd.startTime >= :newStartTime AND tsd.endTime <= :newEndTime)" +
		       ")")
		List<TimeSlotDetail> findOverlappingTimeSlotDetails(@Param("admin") Admin admin,
		                                                   @Param("date") String date,
		                                                   @Param("newStartTime") String newStartTime,
		                                                   @Param("newEndTime") String newEndTime);




	  @Query("SELECT tsd FROM TimeSlot t JOIN t.timeSlotDetails tsd WHERE t.admin = :admin AND t.date = :date " +
		       "AND t.id != :excludeId " +  // Exclude the specified time slot ID
		       "AND (" +
		       "  (tsd.startTime < :newEndTime AND tsd.endTime > :newStartTime)" +
		       "  OR (tsd.startTime >= :newStartTime AND tsd.startTime < :newEndTime)" +
		       "  OR (tsd.endTime > :newStartTime AND tsd.endTime <= :newEndTime)" +
		       ")")
		List<TimeSlotDetail> findOverlappingTimeSlotDetails1(@Param("admin") Admin admin,
		                                                   @Param("date") String date,
		                                                   @Param("newStartTime") String newStartTime,
		                                                   @Param("newEndTime") String newEndTime,
		                                                   @Param("excludeId") Long excludeId);

	  
}
