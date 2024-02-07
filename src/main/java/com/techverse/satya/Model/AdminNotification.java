package com.techverse.satya.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class AdminNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String notificationType; // "appointment" or "suggestion"
    private String appointmentType=""; // "appointment" or "suggestion"
    private String appointmentStatus=""; // "appointment" or "suggestion"
    
    private Long entityId; // ID of the appointment or suggestion
    @Column(name = "is_read")  
    private boolean read=false; // Indicates whether the notification has been read or not
    private LocalDateTime createdAt;
    String profilePhoto;

    // Constructors, getters, and setters

    public AdminNotification() {
        this.createdAt = LocalDateTime.now();
        this.read = false; // By default, the notification is unread when created
    }
    
    
    

    public String getAppointmentType() {
		return appointmentType;
	}




	public String getAppointmentStatus() {
		return appointmentStatus;
	}




	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}




	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}




	public AdminNotification(String message, String notificationType, Long entityId,String profilePhoto,Admin admin,String appointmentType, String appointmentStatus) {
        this();
        this.message = message;
        this.notificationType = notificationType;
        this.entityId = entityId;
        this.profilePhoto=profilePhoto;
        this.admin=admin;
        this.appointmentType=appointmentType;
        this.appointmentStatus=appointmentStatus;
        
    }

	public Long getId() {
		return id;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	 @ManyToOne
	    @JoinColumn(name = "admin_id")  // adjust the column name accordingly
	    private Admin admin;
    // Getters and setters

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	 
	 
}
