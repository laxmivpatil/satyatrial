package com.techverse.satya.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.SubAdminNotification;



	
	@Repository
	public interface SubAdminNotificationRepository extends JpaRepository<SubAdminNotification, Long> {
	    // You can define custom query methods here if needed

	    List<SubAdminNotification> findByReadFalse();
	    List<SubAdminNotification> findBySubAdminIdAndReadFalse(Long adminId);
	    
	}

