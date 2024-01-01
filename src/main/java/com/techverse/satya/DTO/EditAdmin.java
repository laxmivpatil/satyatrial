package com.techverse.satya.DTO;

public class EditAdmin {
	 
	 
	 
	Long adminId;
 String name;
    String phoneNumber;
       String email;
      String qualification;
     String proffession;
     String profilePhoto;
     
     
     
     
	 
	public String getProfilePhoto() {
		return profilePhoto;
	}
	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
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
	public String getOccupation() {
		return proffession;
	}
	public void setOccupation(String occupation) {
		this.proffession = occupation;
	}
	public EditAdmin( Long adminId, String name, String phoneNumber, String email,
			String qualification, String occupation) {
		super();
		  this.adminId = adminId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.qualification = qualification;
		this.proffession = occupation;
	}
     
     
}
