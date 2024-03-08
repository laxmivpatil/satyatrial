package com.techverse.satya.Model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OtpEntity {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
    
    @Column(name = "phone_number")
    private String phoneNumber;

    private String otp;

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public OtpEntity(String phoneNumber, String otp, LocalDateTime expiryTime) {
		super();
		this.phoneNumber = phoneNumber;
		this.otp = otp;
		this.expiryTime = expiryTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OtpEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }
    
	public void updateOtp(String otp) {
        this.otp=otp;
    }
    
    // Constructors, getters, and setters
}
