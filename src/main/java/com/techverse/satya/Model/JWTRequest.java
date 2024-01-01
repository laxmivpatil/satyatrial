package com.techverse.satya.Model;

import lombok.Builder;

@Builder
public class JWTRequest {
	
	private String mobileNo;
	
	private String otp;
	public JWTRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public JWTRequest(String mobileNo, String otp) {
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
	
	

}
