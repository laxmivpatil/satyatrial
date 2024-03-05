package com.techverse.satya.Repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.techverse.satya.Model.PoliticalParty;

public interface PoliticalPartyRepository extends JpaRepository<PoliticalParty, Long> {
}
