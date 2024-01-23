package com.techverse.satya.DTO;

import java.util.List;
 
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.Users;
 

public class UserDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String gender;
    private String profilePhoto;
    private String address;
    private String pincode;
    private String city;
    private String constitution;
    private String wardNo;
    private String state;
    private String joinedDate;
    private String qualification;
    private String email;
    private String occupation;
    private String admin="";
    private boolean isNotificationEnabled;
    
    
    
   public boolean isNotificationEnabled() {
		return isNotificationEnabled;
	}
	public void setNotificationEnabled(boolean isNotificationEnabled) {
		this.isNotificationEnabled = isNotificationEnabled;
	}
public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
public Long getId() {
		return id;
   }
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getWardNo() {
		return wardNo;
	}
	public void setWardNo(String wardNo) {
		this.wardNo = wardNo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	 

    // Constructors, getters, and setters

    // Other fields, getters, and setters
    
    
	public UserDTO(Users user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.gender = user.getGender();
        this.profilePhoto = user.getProfilePphoto();
        this.address = user.getAddress();
        this.pincode = user.getPincode();
        this.city = user.getCity();
        this.constitution = user.getConstitution();
        this.wardNo = user.getWardNo();
        this.state = user.getState();
        this.joinedDate=user.getJoineddate();
        this.qualification=user.getQualification();
        this.occupation=user.getOccupation();
        this.email=user.getEmail();
        this.isNotificationEnabled=user.isNotificationEnabled();
                 
    }
	public String getJoinedDate() {
		return joinedDate;
	}
	public void setJoinedDate(String joinedDate) {
		this.joinedDate = joinedDate;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}

    
}
