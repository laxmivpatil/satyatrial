package com.techverse.satya.DTO;

import java.util.List;

public class TimeSlotResponse {
    private Long id;
    private String date;
    private String availability;
    private String address;
    private List<SmallerTimeSlotResponse> smallerTimeSlots;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<SmallerTimeSlotResponse> getSmallerTimeSlots() {
		return smallerTimeSlots;
	}
	public void setSmallerTimeSlots(List<SmallerTimeSlotResponse> smallerTimeSlots) {
		this.smallerTimeSlots = smallerTimeSlots;
	}

    // Constructors, getters, and setters
    
    
    
}

