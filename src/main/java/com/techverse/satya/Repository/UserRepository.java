package com.techverse.satya.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techverse.satya.Model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByEmail(String email);
    
    @Query("SELECT u FROM Users u WHERE u.phoneNumber = :value OR u.email = :value")
    Optional<Users> findByPhoneNumberOrEmail(@Param("value") String phoneNumberOrEmail);
    
    @Query(value = "SELECT COUNT(a.id) FROM Appointment a " +
            "WHERE a.user.id = :userId " +
            "AND FUNCTION('MONTH', a.date) = :currentMonth " +
            "AND FUNCTION('YEAR', a.date) = :currentYear")
     long countAppointmentsByMonthAndUser(@Param("userId") Long userId, @Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear);


}
