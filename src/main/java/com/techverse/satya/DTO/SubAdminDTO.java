package com.techverse.satya.DTO;

import com.techverse.satya.Model.SubAdmin;

public class SubAdminDTO {
 private Long id;
 private String mobileNumber;
 private String joineddate;
 private String name="";
 private String role="subadmin";
 
 // Constructors, getters, and setters

 // Default constructor
 public SubAdminDTO() {
	 this.name="";
 }

 // Parameterized constructor
 public SubAdminDTO(Long id, String mobileNumber, String joineddate, String name) {
     this.id = id;
     this.mobileNumber = mobileNumber;
     this.joineddate = joineddate;
     this.name = name;
     this.role="subadmin";
    
 }
 
 public SubAdminDTO(SubAdmin subAdmin) {
     this.id = subAdmin.getId();
     this.mobileNumber = subAdmin.getMobileNumber();
     this.joineddate = subAdmin.getJoineddate();
     this.name = subAdmin.getName();
     this.role="subadmin";
    
 }

 // Getters and setters

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getMobileNumber() {
     return mobileNumber;
 }

 public void setMobileNumber(String mobileNumber) {
     this.mobileNumber = mobileNumber;
 }

 public String getJoineddate() {
     return joineddate;
 }

 public void setJoineddate(String joineddate) {
     this.joineddate = joineddate;
 }

 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

public String getRole() {
	return role;
}

public void setRole(String role) {
	this.role = role;
}

  
 
}
