package com.techverse.satya.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.satya.Model.Constituency;

public interface ConstituencyRepository extends JpaRepository<Constituency, Long> {
	List<Constituency> findByDistrictNameIgnoreCase(String districtName);
}