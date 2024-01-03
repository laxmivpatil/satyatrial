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
		  String message = String.format("\"Dear %s , your appointment scheduled for date %s and time %s has been Canceled by our administration team.", user.getName(), appointment.getDate(),appointment.getTime());
		  try {    
	        Long entityId = appointment.getId();
	        String title="Appointment Canceled";
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	        
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Canceled","hello "+user.getName()+"Appontment Canceled by admin"+user.getAdmin().getName()+" Appointment ID:" +appointment.getId()+"go to notification section to check appointment details");
	        pushNotificationService.sendPushNotificationToToken(p);
	        
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
	  public void sendDeleteAppointmentNotificationToUser(Appointment appointment, Users user) {
	        String message = String.format("\"Dear %s , your appointment scheduled for date %s and time %s has been canceled by our administration team.", user.getName(), appointment.getDate(),appointment.getTime());
	        try {
	        Long entityId = appointment.getId();
	        String title="Appointment Canceled";
		    
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Canceled","hello "+user.getName()+"Appontment Canceled by admin"+user.getAdmin().getName()+" Appointment ID:" +appointment.getId()+"go to notification section to check appointment details");
	        pushNotificationService.sendPushNotificationToToken(p);
	 	   
	        }
			  catch(Exception e){
				  System.out.println("error to send push notification");
			  }
	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
	  public void sendRescheduleAppointmentNotificationToUser(Appointment appointment, Users user,String oldTime) {
		  String message = String.format("Dear %s,\n\nWe would like to inform you that your appointment has been rescheduled by our administration team.\n\nOriginal Appointment Details:\nDate: %s\nTime: %s\n\nNew Appointment Details:\nDate: %s\nTime: %s\n\nWe apologize for any inconvenience caused and appreciate your understanding.\n\nBest regards,\n[Your Company/Organization Name]", 
                
				  user.getName(), 
                  appointment.getDate(), 
                  oldTime,
                  appointment.getDate(),
                  appointment.getTime());  
		  message="rescheduled";
		  try {
	        Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
		    
	        UserNotification userNotification = new UserNotification(message,entityId,user.getId(),title,user.getAdmin().getProfilePhoto());
	        userNotificationRepository.save(userNotification);
	        
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Rescheduled","hello "+user.getName()+"Appontment Rescheduled by admin"+user.getAdmin().getName()+" Appointment ID:" +appointment.getId()+"go to notification section to check appointment details");
	        pushNotificationService.sendPushNotificationToToken(p);
		      
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
