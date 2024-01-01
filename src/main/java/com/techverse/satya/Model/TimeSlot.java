package com.techverse.satya.Model;
import javax.persistence.*;

import com.techverse.satya.DTO.TimeSlotDetail;

import java.util.List;

@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    @ElementCollection
    @CollectionTable(name = "time_slot_details", joinColumns = @JoinColumn(name = "time_slot_id"))
    private List<TimeSlotDetail> timeSlotDetails;
    
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String availability;  // "personal" or "online"

    
    @OneToMany(mappedBy = "timeSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SmallerTimeSlot> smallerTimeSlots;

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

	 

	public List<SmallerTimeSlot> getSmallerTimeSlots() {
		return smallerTimeSlots;
	}

	public void setSmallerTimeSlots(List<SmallerTimeSlot> smallerTimeSlots) {
		this.smallerTimeSlots = smallerTimeSlots;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
    
    
    

    // Constructors, getters, and setters
}
