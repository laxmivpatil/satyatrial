package com.techverse.satya.DTO;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class SuggestionResponseDTO {

	private Long Id;
	 private String name; // Add userId field
	 private String profile="";
	    private String address;
	    private String purpose;
	    private String comment;
	    private String photo;
	    private String video;
	    private LocalDateTime dateTime;
	    
	    
		public Long getId() {
			return Id;
		}
		public void setId(Long id) {
			Id = id;
		}
		public LocalDateTime getDateTime() {
			return dateTime;
		}
		public void setDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPurpose() {
			return purpose;
		}
		public void setPurpose(String purpose) {
			this.purpose = purpose;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public String getVideo() {
			return video;
		}
		public void setVideo(String video) {
			this.video = video;
		}
		public String getProfile() {
			return profile;
		}
		public void setProfile(String profile) {
			this.profile = profile;
		}
	    
	    
	 
}
