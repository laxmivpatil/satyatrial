package com.techverse.satya.Model;

public class PushNotificationRequest {
    private String title;
     private String body;
    private String token;
    private String topic;
    
    
    
  public PushNotificationRequest(String token , String title, String body) {
	 
		this.title = title;
		this.body = body;
		this.token = token;
	}


public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


public PushNotificationRequest() {
		super();
	}


public String getTitle() {
	return title;
}


public void setTitle(String title) {
	this.title = title;
}


public String getBody() {
	return body;
}


public void setBody(String body) {
	this.body = body;
}


public String getToken() {
	return token;
}


public void setToken(String token) {
	this.token = token;
}
  
  
	 
    
}
