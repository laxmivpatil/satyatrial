package com.techverse.satya.DTO;

import com.techverse.satya.Model.Admin;

public class AdminDTO {
    private Long id;
    private String otp;
    private String username;
    private String password;
    private String profilePhoto;
    private String name;
    private String gender;
    private String mobileNumber;
    private String email;
    private String qualification;
    private String officeAddress;
    private String homeAddress;
    private String pincode;
    private String city;
    private String constitution;
    private String state;
    private String joinedDate = "";
    private String proof;
    private String profession;
    private String party;
    private String verification;

    // Updated constructor to set default values
    public AdminDTO() {
        this.id = 0L;
         this.profilePhoto = "";
        this.name = "";
        this.gender = "";
        this.mobileNumber = "";
        this.email = "";
        this.qualification = "";
        this.officeAddress = "";
        this.homeAddress = "";
        this.pincode = "";
        this.city = "";
        this.constitution = "";
        this.state = "";
        this.joinedDate = "";
        this.proof = "";
        this.profession = "";
        this.party = "";
    }

    // Additional constructor to set values during creation
    public AdminDTO(Admin admin) {
        this(); // Call the default constructor to set default values

        // Set values based on the provided Admin instance
        if (admin != null) {
            this.id = admin.getId();
             this.profilePhoto = admin.getProfilePhoto();
            this.name = admin.getName();
            this.gender = admin.getGender();
            this.mobileNumber = admin.getMobileNumber();
            this.email = admin.getEmail();
            this.qualification = admin.getQualification();
            this.officeAddress = admin.getOfficeAddress();
            this.homeAddress = admin.getHomeAddress();
            this.pincode = admin.getPincode();
            this.city = admin.getCity();
            this.constitution = admin.getConstitution();
            this.state = admin.getState();
            // Set other fields based on Admin properties

            // Example: Set joinedDate based on admin's creation date
            this.joinedDate = admin.getJoineddate();
            this.verification=admin.getVerification();
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

 
	 
	 

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getConstitution() {
		return constitution;
	}

	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(String joinedDate) {
		this.joinedDate = joinedDate;
	}

	public String getProof() {
		return proof;
	}

	public void setProof(String proof) {
		this.proof = proof;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	
    // Getters and setters (generated or manually written)

    
    // ... Other getters and setters
    
    
    
}

