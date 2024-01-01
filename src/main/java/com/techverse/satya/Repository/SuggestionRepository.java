package com.techverse.satya.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.satya.Model.Suggestion;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUser_Id(Long userId);
    List<Suggestion> findByAdmin_Id(Long adminId);
    
    List<Suggestion> findByAdmin_IdAndDateTimeBetween(Long adminId, LocalDateTime start, LocalDateTime end);
    List<Suggestion> findByAdmin_IdAndDateTimeBefore(Long adminId, LocalDateTime dateTime);

}