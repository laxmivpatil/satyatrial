package com.techverse.satya.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.UserNotification;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    // You can define custom query methods here if needed

    List<UserNotification> findByReadFalse(); 
    List<UserNotification> findByUserIdAndReadFalse(Long userId);
    List<UserNotification> findByUserId(Long userId);


}
