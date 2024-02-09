package com.techverse.satya.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title="";
   
	private String message;
    private Long appointmentId; // ID of the appointment or suggestion
    @Column(name = "is_read")  
    private boolean read=false; // Indicates whether the notification has been read or not
    private LocalDateTime createdAt;
	private Long userId;
	
	 String profilePhoto="";
	 
    
    public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	 public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
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
	public UserNotification(String message, Long appointmentId,Long userId,String title,String profile) {
	 
		this.message = message;
		this.appointmentId = appointmentId;
		this.userId=userId;
		Instant instant = Instant.parse(Instant.now().toString());

	     
		 ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Choose the appropriate time zone
	        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
     this.createdAt = localDateTime;
		  
		  this.title=title;
		  this.profilePhoto=profile;
	}
	public UserNotification() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
   

    
}

