package com.techverse.satya.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Constituency {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String districtName;
    private String constituencyName;
    private String state="";
    
    
    

    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	// Constructors, Getters, and Setters
    public Constituency() {}

    public Constituency(String districtName, String constituencyName) {
        this.districtName = districtName;
        this.constituencyName = constituencyName;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getConstituencyName() {
		return constituencyName;
	}

	public void setConstituencyName(String constituencyName) {
		this.constituencyName = constituencyName;
	}

    // getters and setters
}

