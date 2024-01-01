package com.techverse.satya.DTO;

public class ResponseDTO<T> {
    private boolean status;
    private String message;
    private String token="";
    private T data;
    

    // Constructors, getters, and etters

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ResponseDTO() {
    }

    public ResponseDTO(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

    // Getters and setters
}

