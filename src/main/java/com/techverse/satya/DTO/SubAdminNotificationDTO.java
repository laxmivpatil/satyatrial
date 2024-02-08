package com.techverse.satya.DTO;

import java.time.LocalDateTime;

import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.SubAdminNotification;

public class SubAdminNotificationDTO {
    private Long id;
    private String message;
    private String notificationType;
    private Long entityId;
    private boolean read;
    private LocalDateTime createdAt;
    private String profilePhoto;
    private Long subAdminId;  // Id of the associated Admin
    private String appointmentType="";
    
     private String appointmentStatus=""; // "appointment" or "suggestion"
    
   

    // Constructors, getters, and setters

    public SubAdminNotificationDTO() {
        // Default constructor
    }

    public SubAdminNotificationDTO(Long id, String message, String notificationType, Long entityId,
                                boolean read, LocalDateTime createdAt, String profilePhoto, Long subAdminId,String appointmentType,String appointmentStatus) {
        this.id = id;
        this.message = message;
        this.notificationType = notificationType;
        this.entityId = entityId;
        this.read = read;
        this.createdAt = createdAt;
        this.profilePhoto = profilePhoto;
        this.subAdminId = subAdminId;
        this.appointmentType=appointmentType;
        this.appointmentStatus=appointmentStatus;
    }
    
    

    // Getters and setters
    // Omitted for brevity, but you need to include getters and setters for all fields

    public Long getSubAdminId() {
		return subAdminId;
	}

	public void setSubAdminId(Long subAdminId) {
		this.subAdminId = subAdminId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

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

    
    

    public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	// You can use a static method to convert an AdminNotification entity to DTO
    public static SubAdminNotificationDTO fromEntity(SubAdminNotification adminNotification) {
        return new SubAdminNotificationDTO(
                adminNotification.getId(),
                adminNotification.getMessage(),
                adminNotification.getNotificationType(),
                adminNotification.getEntityId(),
                adminNotification.isRead(),
                adminNotification.getCreatedAt(),
                adminNotification.getProfilePhoto(),
                adminNotification.getSubAdmin().getId(),
                adminNotification.getAppointmentType(),
                adminNotification.getAppointmentStatus()
        );
    }
    
    
    
}
