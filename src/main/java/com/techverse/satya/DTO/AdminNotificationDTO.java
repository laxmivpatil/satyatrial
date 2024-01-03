package com.techverse.satya.DTO;

import java.time.LocalDateTime;

import com.techverse.satya.Model.AdminNotification;

public class AdminNotificationDTO {
    private Long id;
    private String message;
    private String notificationType;
    private Long entityId;
    private boolean read;
    private LocalDateTime createdAt;
    private String profilePhoto;
    private Long adminId;  // Id of the associated Admin

    // Constructors, getters, and setters

    public AdminNotificationDTO() {
        // Default constructor
    }

    public AdminNotificationDTO(Long id, String message, String notificationType, Long entityId,
                                boolean read, LocalDateTime createdAt, String profilePhoto, Long adminId) {
        this.id = id;
        this.message = message;
        this.notificationType = notificationType;
        this.entityId = entityId;
        this.read = read;
        this.createdAt = createdAt;
        this.profilePhoto = profilePhoto;
        this.adminId = adminId;
    }

    // Getters and setters
    // Omitted for brevity, but you need to include getters and setters for all fields

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public Long getAdminId() {
        return adminId;
    }

    // You can use a static method to convert an AdminNotification entity to DTO
    public static AdminNotificationDTO fromEntity(AdminNotification adminNotification) {
        return new AdminNotificationDTO(
                adminNotification.getId(),
                adminNotification.getMessage(),
                adminNotification.getNotificationType(),
                adminNotification.getEntityId(),
                adminNotification.isRead(),
                adminNotification.getCreatedAt(),
                adminNotification.getProfilePhoto(),
                adminNotification.getAdmin().getId()
        );
    }
    
    
    
}
