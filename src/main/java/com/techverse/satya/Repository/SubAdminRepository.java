package com.techverse.satya.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.SubAdmin;

@Repository
public interface SubAdminRepository extends JpaRepository<SubAdmin, Long> {
    List<SubAdmin> findByAdminId(Long adminId);
    Optional<SubAdmin> findByMobileNumber(String mobileNumber);
    
    
    @Query("SELECT a FROM SubAdmin a WHERE a.mobileNumber = :value OR a.email = :value")
    Optional<SubAdmin> findByMobileNumberOrEmail(@Param("value") String mobileNumberOrEmail);
}