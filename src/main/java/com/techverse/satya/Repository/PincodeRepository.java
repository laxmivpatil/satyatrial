package com.techverse.satya.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.Pincode;

@Repository
public interface PincodeRepository extends JpaRepository<Pincode, Long> {

    // You can define custom query methods here if needed
    // For example, finding Pincode by district
    List<Pincode> findByDistrict(String district);

    Optional<Pincode> findByPincode(String pincode);
    // You can add more custom queries based on your requirements

}

