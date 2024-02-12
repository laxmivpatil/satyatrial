package com.techverse.satya.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.PushNotificationRequest;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Model.UserNotification;
import com.techverse.satya.Repository.UserNotificationRepository;
@Service
public class UserNotificationService {

	@Autowired
	UserNotificationRepository userNotificationRepository;
	@Autowired
	PushNotificationService pushNotificationService;
	
	  public void sendCancelAppointmentNotificationToUser(Appointment appointment, Users user) {
		  String message = String.format("\"Hello %s , your appointment scheduled for date %s and time %s has been Canceled by %s", user.getName(), appointment.getDate(),appointment.getTime(),user.getAdmin().getName());
		  try {    
	        Long entityId = appointment.getId();
	        String title="Appointment Canceled";
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	        if(user.isNotificationEnabled()) {
	        PushNotificationRequest p=new PushNotificationRequest(user.getDeviceToken(),"Appointment Canceled", message,"appointment");
	        pushNotificationService.sendPushNotificationToToken(p);
	        }
	        
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
	  public void sendDeleteAppointmentNotificationToUser(Appointment appointment, Users user) {
	        String message = String.format("\"Hello %s , your appointment scheduled for date %s and time %s has been canceled by  %s ", user.getName(), appointment.getDate(),appointment.getTime(),user.getAdmin().getName());
	        try {
	        Long entityId = appointment.getId();
	        String title="Appointment Canceled";
		    
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	       
	        if(user.isNotificationEnabled()) {
	 	       
	        PushNotificationRequest p=new PushNotificationRequest(user.getDeviceToken(),"Appointment Canceled",message,"appointment");
	        pushNotificationService.sendPushNotificationToToken(p);
	        }
	 	   
	        }
			  catch(Exception e){
				  System.out.println("error to send push notification");
			  }
	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
	  public void sendRescheduleAppointmentNotificationToUser(Appointment appointment, Users user,String oldTime) {
		   String message = String.format("Hello %s , your appointment scheduled for date %s and time %s has been rescheduled by  %s ", user.getName(), appointment.getDate(),appointment.getTime(),user.getAdmin().getName());
		              
				  
		  try {
	        Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
		    
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	        
	        if(user.isNotificationEnabled()) {
	 	       
	        PushNotificationRequest p=new PushNotificationRequest(user.getDeviceToken(),"Appointment Rescheduled" ,message,"appointment");
	        pushNotificationService.sendPushNotificationToToken(p);
	        }
		      
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
	    public List<UserNotification> getUnreadUserNotifications() {
	        return userNotificationRepository.findByReadFalse();
	}
	    
	    public List<UserNotification> getUnreadUserNotificationsByUserId(Long userId) {
	        return userNotificationRepository.findByUserIdAndReadFalse(userId);
	    }
	    public List<UserNotification> getAllUserNotificationsByUserId(Long userId) {
	        return userNotificationRepository.findByUserId(userId);
	    }
}
