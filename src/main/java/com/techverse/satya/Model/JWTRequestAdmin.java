package com.techverse.satya.Model;

import lombok.Builder;

@Builder
public class JWTRequestAdmin {
	
	private String name;
	private String gender;
	private String mobileNo;
	private String profession;
	private String party;
	private String otp;
	public JWTRequestAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JWTRequestAdmin(String mobileNo, String otp) {
		super();
		this.mobileNo = mobileNo;
		this.otp = otp;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
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
	public String getProfession() {
		return  profession;
	}
	public void setProfession(String profession) {
		this.profession =  profession;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public JWTRequestAdmin(String name, String gender, String mobileNo, String profession, String party, String otp) {
		super();
		this.name = name;
		this.gender = gender;
		this.mobileNo = mobileNo;
		this.profession = profession;
		this.party = party;
		this.otp = otp;
	}
	
	

}
