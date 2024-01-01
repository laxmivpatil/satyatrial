package com.techverse.satya.DTO;

import java.util.List;

public class TimeSlotDetailDto {
    private Long id;
    private String availability;
    private String date; 
    private List<TimeSlotDetail> timeSlotDetails;

    public TimeSlotDetailDto(Long id, String availability, String date , List<TimeSlotDetail> timeSlotDetails) {
        this.id = id;
        this.availability = availability;
        this.date = date;
            this.timeSlotDetails = timeSlotDetails;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

 

	public List<TimeSlotDetail> getTimeSlotDetails() {
		return timeSlotDetails;
	}

	public void setTimeSlotDetails(List<TimeSlotDetail> timeSlotDetails) {
		this.timeSlotDetails = timeSlotDetails;
	}
    
    
    

    // Getters and setters
}