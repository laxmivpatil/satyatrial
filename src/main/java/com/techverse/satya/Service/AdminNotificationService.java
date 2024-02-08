package com.techverse.satya.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.techverse.satya.Controller.AppointmentController;
import com.techverse.satya.Controller.SuggestionController;
import com.techverse.satya.DTO.AdminNotificationDTO;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.PushNotificationRequest;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Model.SubAdminNotification;
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
        String message = String.format("Hello "+user.getAdmin().getName()+ " New appointment created by user %s.  ", user.getName());
        String notificationType = "appointment";
        Long entityId = appointment.getId();
try {
        AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),appointment,user);
        adminNotificationRepository.save(adminNotification);
        if(user.getAdmin().isNotificationEnabled()) {
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"New Appointment",message);
        pushNotificationService.sendPushNotificationToToken(p);
        }
        
    }
	  catch(Exception e){
		  System.out.println("error to send push notification");
	  }
    }
    public void sendSuggestionNotificationToAdmin(Suggestion suggestion, Users user) {
        String message = String.format("Hello "+user.getAdmin().getName()+ "New suggestion created by user %s.", user.getName());
        String notificationType = "suggestion";
        Long entityId = suggestion.getId();
try {
	 AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),suggestion,user);
       adminNotificationRepository.save(adminNotification);
        if(user.getAdmin().isNotificationEnabled()) {
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"New Suggestion",message);
        pushNotificationService.sendPushNotificationToToken(p);
        }
        // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
        // sendNotificationToAdmin(message);
}
catch(Exception e){
	  System.out.println("error to send push notification");
}
    }
    
    public void sendCancelAppointmentNotificationToAdmin(Appointment appointment, Users user) {
        String message = String.format("Hello " +user.getAdmin().getName() +" Appointment Cancel by user %s", user.getName() + " Please check the app for more details.");
        System.out.println("Notification message=>"+message);
        String notificationType = "appointment";
        Long entityId = appointment.getId();
try {
	 AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),appointment,user);
       adminNotificationRepository.save(adminNotification);
        if(user.getAdmin().isNotificationEnabled()) {
            
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Cancel",message);
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
      public void sendRescheduleAppointmentNotificationToAdmin(Appointment appointment, Users user,String oldTime) {
        String message = String.format("Hello " +user.getAdmin().getName() +" Appointment Rescheduled by user %s", user.getName() + " Please check the app for more details.");
          Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
	        String notificationType = "appointment";
		  try {
			  AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),appointment,user);
		        adminNotificationRepository.save(adminNotification);	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        if(user.getAdmin().isNotificationEnabled()) {
	            
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Rescheduled",message);
	        pushNotificationService.sendPushNotificationToToken(p);
	        }
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	        
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
    
    public void sendRescheduleAppointmentNotificationToAdminBySubAdmin(Appointment appointment, Users user,String oldTime,SubAdmin subAdmin) {
        String message = String.format("Hello " +user.getAdmin().getName() +" Appointment Rescheduled by subAdmin %s", subAdmin.getName() + " Please check the app for more details.");
          Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
	        String notificationType = "appointment";
		  try {
			  AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),appointment,user);
		         adminNotificationRepository.save(adminNotification);	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        if(user.getAdmin().isNotificationEnabled()) {
	            
	        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Rescheduled",message);
	        pushNotificationService.sendPushNotificationToToken(p);
	        }
		  }
		  catch(Exception e){
			  System.out.println("error to send push notification");
		  }
	        
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
    public void sendCancelAppointmentNotificationToAdminBySubAdmin(Appointment appointment,Users user, SubAdmin subAdmin ) {
        String message = String.format("Hello " +subAdmin.getAdmin().getName() +" Appointment Cancel by subAdmin %s", subAdmin.getName() + " Please check the app for more details.");
        System.out.println("Notification message=>"+message);
        String notificationType = "appointment";
        Long entityId = appointment.getId();
try {
	 AdminNotification adminNotification = new AdminNotification(message, notificationType,user.getAdmin(),appointment,user);
       adminNotificationRepository.save(adminNotification);
        if(user.getAdmin().isNotificationEnabled()) {
        PushNotificationRequest p=new PushNotificationRequest(user.getAdmin().getDeviceToken(),"Appointment Cancel",message);
        pushNotificationService.sendPushNotificationToToken(p);
        }
}
catch(Exception e){
	  
}
        // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
        // sendNotificationToAdmin(message);
    }
 
    public List<AdminNotificationDTO> getUnreadAdminNotificationDTOs(Long adminId) {
        List<AdminNotification> unreadNotifications = adminNotificationRepository.findByAdminIdAndReadFalse(adminId);

        // Convert AdminNotification objects to AdminNotificationDTO objects
        return unreadNotifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AdminNotificationDTO convertToDTO(AdminNotification adminNotification) {
    	         return AdminNotificationDTO.fromEntity(adminNotification);
         

     
    }

}
