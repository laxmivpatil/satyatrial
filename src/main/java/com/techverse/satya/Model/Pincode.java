package com.techverse.satya.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pincode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pincode;
    private String district;
    private String state;

    // Constructors, getters, and setters
    
    public Pincode() {
    }

    public Pincode(String pincode, String district, String state) {
        this.pincode = pincode;
        this.district = district;
        this.state = state;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Pincode [id=" + id + ", pincode=" + pincode + ", district=" + district + ", state=" + state + "]";
	}
    
    
    

    // Getters and setters
    // ...
}
