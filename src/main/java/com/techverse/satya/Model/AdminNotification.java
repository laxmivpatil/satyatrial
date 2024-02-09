package com.techverse.satya.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class AdminNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String notificationType; // "appointment" or "suggestion"
     
    private Long entityId; // ID of the appointment or suggestion
    @Column(name = "is_read")  
    private boolean read=false; // Indicates whether the notification has been read or not
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id") // Adjust the column name to match your schema
    private Users user;

    @ManyToOne
    @JoinColumn(name = "appointment_id") // adjust the column name accordingly
    private Appointment appointment;
    // Constructors, getters, and setters
    
    @ManyToOne
    @JoinColumn(name = "suggestion_id") // adjust the column name accordingly
    private Suggestion suggestion;
    

    public AdminNotification() {
    	 Instant instant = Instant.parse(Instant.now().toString());

	        // Convert Instant to LocalDateTime in a specific time zone
	        ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Choose the appropriate time zone
	        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        this.createdAt = localDateTime;
        this.read = false; // By default, the notification is unread when created
    }
    
    
    

    public Suggestion getSuggestion() {
		return suggestion;
	}




	public void setSuggestion(Suggestion suggestion) {
		this.suggestion = suggestion;
	}

//admin rescheduled appointment


	public Appointment getAppointment() {
		return appointment;
	}




	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}




	public Users getUser() {
		return user;
	}




	public void setUser(Users user) {
		this.user = user;
	}



 
/*

	public AdminNotification(String message, String notificationType, Long entityId,String profilePhoto,Admin admin,String appointmentType, String appointmentStatus) {
        this();
        this.message = message;
        this.notificationType = notificationType;
        this.entityId = entityId;
        this.profilePhoto=profilePhoto;
        this.admin=admin;
        this.appointmentType=appointmentType;
        this.appointmentStatus=appointmentStatus;
        
    }*/
	
	public AdminNotification(String message, String notificationType,Admin admin,Appointment appointment,Users user) {
        this();
        this.message = message;
        this.notificationType = notificationType;
      
        this.entityId=appointment.getId();
         this.admin=admin;
        this.user=user;
        this.appointment=appointment;
        this.suggestion=null;
        
        
    }
	public AdminNotification(String message, String notificationType,Admin admin,Suggestion suggestion,Users user) {
        this();
        this.message = message;
        this.notificationType = notificationType;
      
        this.entityId=suggestion.getId();
         this.admin=admin;
        this.user=user;
        this.appointment=null;
        this.suggestion=suggestion;
        
        
    }

	public Long getId() {
		return id;
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
