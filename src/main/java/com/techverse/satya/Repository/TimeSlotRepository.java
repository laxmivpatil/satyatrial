package com.techverse.satya.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techverse.satya.DTO.TimeSlotDetailDto;
import com.techverse.satya.Model.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
	Optional<TimeSlot> findByDate(String date);
	@Query("SELECT tsd.address FROM TimeSlot ts " +
		       "JOIN ts.timeSlotDetails tsd " +
		       "WHERE ts.date = :date AND tsd.startTime = :startTime")
		String findAddressByDateAndStartTime(@Param("date") String date, @Param("startTime") String startTime);

	@Query("SELECT tsd.address FROM TimeSlot t " +
		       "JOIN t.timeSlotDetails tsd " +
		       "WHERE t.date = :date AND tsd.startTime = :startTime AND t.admin.id = :adminId")
		String findAddressByDateStartTimeAndAdminId(@Param("date") String date, @Param("startTime") String startTime, @Param("adminId") Long adminId);
}
