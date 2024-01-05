package com.techverse.satya.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import javax.persistence.*;

@Entity
public class SmallerTimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startTime;
    private String endTime;
    
    @ManyToOne 
    @JoinColumn(name = "admin_id")
    private Admin admin;
    
    
    @Column(name = "slot_book") // specify the correct column name if it's different
    private boolean slotBook=false;


   @OneToOne 
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    
    
    
    
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public boolean isSlotBook() {
		return slotBook;
	}

	public void setSlotBook(boolean slotBook) {
		this.slotBook = slotBook;
	}

	 @ManyToOne 
	@JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    // Constructors, getters, and setters

    public Long getId() {
        return id;
    }

    

	public void setId(Long id) {
        this.id = id;
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

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
    
    
    
}
