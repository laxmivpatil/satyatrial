package com.techverse.satya.DTO;

public class AppointmentRequest {
    private String appointmentType; // "online" or "personal"
    private String date;
    private String time; // Store time in 10:00AM format
    private String purpose;
    private String comment;
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

    
    
    // Constructors, getters, and setters
}
