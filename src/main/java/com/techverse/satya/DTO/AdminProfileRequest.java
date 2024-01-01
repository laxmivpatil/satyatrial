package com.techverse.satya.DTO;

import org.springframework.web.multipart.MultipartFile;

public class AdminProfileRequest {
	private Long Id;
    private String profilePhoto;
    private String email;
    private String qualification;
   private String homeAddress;
   private String officeAddress;
    private String pincode;
    private String city;
    private String constitution;
    private String state;
    private String proof;
    
 
    
    
	public AdminProfileRequest( String email, String qualification, String homeAddress,
			String officeAddress, String pincode, String city, String constitution, String state ) {
		super();
		 this.email = email;
		this.qualification = qualification;
		this.homeAddress = homeAddress;
		this.officeAddress = officeAddress;
		this.pincode = pincode;
		this.city = city;
		this.constitution = constitution;
		this.state = state;
	 
	}
	
	 
	
	 
		
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
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
	public String getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
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
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
    
    
    
    
    
  
 



	 
    

    // getters and setters
}
