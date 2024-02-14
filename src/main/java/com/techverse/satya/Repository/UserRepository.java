package com.techverse.satya.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.satya.Model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByEmail(String email);
}
