package com.techverse.satya.Model;

import lombok.Builder;

@Builder
public class JWTRequest {
	
	private String mobileNo;
	private String name;
	
	private String otp;
	public JWTRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JWTRequest(String mobileNo,String name, String otp) {
		super();
		this.mobileNo = mobileNo;
		this.name=name;
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
	
	

}
