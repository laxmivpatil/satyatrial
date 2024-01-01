package com.techverse.satya.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techverse.satya.Model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByMobileNumber(String mobileNumber);
    List<Admin> findByConstitution(String constitution);
    List<Admin> findByVerification(String verificationStatus);
    @Modifying
    @Query("UPDATE Admin SET verification = :newStatus WHERE id = :adminId")
    int updateVerificationStatus(@Param("adminId") Long adminId, @Param("newStatus") String newStatus);

}
