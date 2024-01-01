package com.techverse.satya.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techverse.satya.Controller.AppointmentController;
import com.techverse.satya.Controller.SuggestionController;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.PushNotificationRequest;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.UserNotification;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminNotificationRepository;

@Service
public class AdminNotificationService {

	  private final AdminNotificationRepository adminNotificationRepository;
	  
	  @Autowired
	  PushNotificationService pushNotificationService;
	  

	    @Autowired
	    public AdminNotificationService(AdminNotificationRepository adminNotificationRepository) {
	        this.adminNotificationRepository = adminNotificationRepository;
	    }
	    
    public void sendAppointmentNotificationToAdmin(Appointment appointment, Users user) {
        String message = String.format("New appointment created by user %s. Appointment ID: %d", user.getName(), appointment.getId());
        String notificationType = "appointment";
        Long entityId = appointment.getId();
try {
        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"New Appointment","hello "+user.getAdmin().getName()+"New appointment create by user "+user.getName());
        pushNotificationService.sendPushNotificationToToken(p);
        
    }
	  catch(Exception e){
		  System.out.println("error to send push notification");
	  }
    }
    public void sendSuggestionNotificationToAdmin(Suggestion suggestion, Users user) {
        String message = String.format("New suggestion created by user %s. Suggestion ID: %d", user.getName(), suggestion.getId());
        String notificationType = "suggestion";
        Long entityId = suggestion.getId();
try {
        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"New Suggestion","hello "+user.getAdmin().getName()+"New suggestion added by user "+user.getName()+ "go to notification ");
        pushNotificationService.sendPushNotificationToToken(p);
        // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
        // sendNotificationToAdmin(message);
}
catch(Exception e){
	  System.out.println("error to send push notification");
}
    }
    
    public void sendCancelAppointmentNotificationToAdmin(Appointment appointment, Users user) {
        String message = String.format("Appontment Cancel by user %s. Appointment ID: %d", user.getName(), appointment.getId());
        String notificationType = "cancelAppointment";
        Long entityId = appointment.getId();
try {
        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);
 
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Cancel","hello "+user.getAdmin().getName()+"Appontment Cancel by user"+user.getName()+" Appointment ID:" +appointment.getId());
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
    	 String message = String.format("Appontment Reschedule by user %s. Appointment ID: %d", user.getName(), appointment.getId());
          Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
	        String notificationType = "rescheduleAppointment";
		  try {
	        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
	        adminNotificationRepository.save(adminNotification);	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	      
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Rescheduled","hello "+user.getAdmin().getName()+"Appontment Rescheduled by user"+user.getName()+" Appointment ID:" +appointment.getId()+"go to notification section to check appointment details");
	        pushNotificationService.sendPushNotificationToToken(p);
	        
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	        
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
    public List<AdminNotification> getUnreadAdminNotifications() {
        return adminNotificationRepository.findByReadFalse();
}
}
