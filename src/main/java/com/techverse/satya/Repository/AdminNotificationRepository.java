package com.techverse.satya.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.AdminNotification;

@Repository
public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {
    // You can define custom query methods here if needed

    List<AdminNotification> findByReadFalse();
}
