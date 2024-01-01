package com.techverse.satya.DTO;

public class ApiDataResponse<T> {
	    private boolean status;
	    private String message;
	    private T data;

	    public ApiDataResponse(boolean status, String message, T data) {
	        this.status = status;
	        this.message = message;
	        this.data = data;
	    }

	    public boolean isStatus() {
	        return status;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public T getData() {
	        return data;
	    }

	    public void setStatus(boolean status) {
	        this.status = status;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public void setData(T data) {
	        this.data = data;
	    }
}