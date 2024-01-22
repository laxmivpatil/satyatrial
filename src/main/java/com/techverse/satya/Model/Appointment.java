package com.techverse.satya.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.techverse.satya.DTO.AppointmentRequest;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    					
    private String appointmentType; // "online" or "personal"
    private String date;
    private String time; // Store time in 10:00AM format
    private String purpose;
    private String comment;
    
    private String status="Pending";
     
    // Establishing one-to-one relationship with SmallerTimeSlot
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private SmallerTimeSlot smallerTimeSlot;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private Admin admin;
    
    
    private String channelName="";
    
    

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public SmallerTimeSlot getSmallerTimeSlot() {
		return smallerTimeSlot;
	}

	public void setSmallerTimeSlot(SmallerTimeSlot smallerTimeSlot) {
		this.smallerTimeSlot = smallerTimeSlot;
	}
	public Appointment(AppointmentRequest appointmentRequest) {
        this.appointmentType = appointmentRequest.getAppointmentType();
        this.date = appointmentRequest.getDate();
        this.time = appointmentRequest.getTime();
        this.purpose = appointmentRequest.getPurpose();
        this.comment = appointmentRequest.getComment();
        this.status = "pending";
        // Note: You might need to handle other fields based on your requirements
    }
	public Appointment(AppointmentRequest appointmentRequest,Users user,Admin admin) {
        this.appointmentType = appointmentRequest.getAppointmentType();
        this.date = appointmentRequest.getDate();
        this.time = appointmentRequest.getTime();
        this.purpose = appointmentRequest.getPurpose();
        this.comment = appointmentRequest.getComment();
        this.status = "pending";
        this.user=user;
        this.admin=admin;
        // Note: You might need to handle other fields based on your requirements
    }
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
    

    // Constructors, getters, and setters
}
