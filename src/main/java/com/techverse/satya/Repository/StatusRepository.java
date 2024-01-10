package com.techverse.satya.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.satya.Model.OtpEntity;
import com.techverse.satya.Model.Status;

public interface StatusRepository extends JpaRepository<Status, Long>{

}
