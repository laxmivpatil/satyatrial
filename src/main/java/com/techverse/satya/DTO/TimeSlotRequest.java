package com.techverse.satya.DTO;

import java.util.List;

public class TimeSlotRequest {
    private List<String> dates;
    private List<TimeSlotDetail> timeSlotDetails;
    private String availability;
      
	public List<TimeSlotDetail> getTimeSlotDetails() {
		return timeSlotDetails;
	}
	public void setTimeSlotDetails(List<TimeSlotDetail> timeSlotDetails) {
		this.timeSlotDetails = timeSlotDetails;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
 
	public List<String> getDates() {
		return dates;
	}
	public void setDates(List<String> dates) {
		this.dates = dates;
	}
	public TimeSlotRequest(List<String> date, List<TimeSlotDetail> timeSlotDetails, String availability) {
		super();
		this.dates = date;
		this.timeSlotDetails = timeSlotDetails;
		this.availability = availability;
	}
	 
    
    
    // Getters and setters
}
