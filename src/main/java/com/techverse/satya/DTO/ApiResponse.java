package com.techverse.satya.DTO;

public class ApiResponse {
    private boolean status;
    private String message;

    public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(boolean success, String message) {
        this.status = success;
        this.message = message;
    }

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
    

    // Getters and setters
}

