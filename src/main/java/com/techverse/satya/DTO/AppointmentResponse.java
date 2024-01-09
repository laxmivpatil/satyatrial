package com.techverse.satya.DTO;

import java.util.List;

import com.techverse.satya.Model.Appointment;

public class AppointmentResponse {
		Long appointmentId;
		String name;
		String profileurl;
		private String appointmentType; // "online" or "personal"
	    private String date;
	    private String time; // Store time in 10:00AM format
	    private String purpose;
	    private String comment;
	    private String appointmentAddress="";
	    private String status;
	    
	    private String endTime="";
	    
	    

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getAppointmentAddress() {
			return appointmentAddress;
		}

		public void setAppointmentAddress(String appointmentAddress) {
			this.appointmentAddress = appointmentAddress;
		}

		public Long getAppointmentId() {
			return appointmentId;
		}

		public void setAppointmentId(Long appointmentId) {
			this.appointmentId = appointmentId;
		}

		public AppointmentResponse() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProfileurl() {
			return profileurl;
		}

		public void setProfileurl(String profileurl) {
			this.profileurl = profileurl;
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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	    
	    
	    
	    

}
