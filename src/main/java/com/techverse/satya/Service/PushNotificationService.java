package com.techverse.satya.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.PushNotificationRequest;
 
@Service
public class PushNotificationService {
	
    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    
    private FCMService fcmService;
    
    public PushNotificationService(FCMService fcmService) {
        this.fcmService = fcmService;
    }
    
    
    public  ResponseEntity<String> sendPushNotificationToToken(PushNotificationRequest request) {
    	ResponseEntity<String> response=null;
    	try {
          
     //      fcmService.sendPushNotification(request.getToken(), request.getTitle(), request.getBody(),request.getType());
         response= fcmService.sendPushNotificationV1(request.getToken(), request.getTitle(), request.getBody(),request.getType());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    	
        return response;

    }
   
}