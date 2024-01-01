package com.techverse.satya.DTO;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.techverse.satya.Model.TimeSlot;

@Embeddable
public class TimeSlotDetail {
    private String startTime;
    private String endTime;
    private String address="";

     
    
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	 public TimeSlotDetail(String startTime, String endTime) {
	        this.startTime = startTime;
	        this.endTime = endTime;
	    }
	public TimeSlotDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "TimeSlotDetail [startTime=" + startTime + ", endTime=" + endTime + ", getStartTime()=" + getStartTime()
				+ ", getEndTime()=" + getEndTime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	 
    // Constructors, getters, and setters
}
