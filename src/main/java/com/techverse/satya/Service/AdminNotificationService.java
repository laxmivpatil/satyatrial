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
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.UserNotification;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminNotificationRepository;

@Service
public class AdminNotificationService {

	  private final AdminNotificationRepository adminNotificationRepository;
	  
	  
	  

	    @Autowired
	    public AdminNotificationService(AdminNotificationRepository adminNotificationRepository) {
	        this.adminNotificationRepository = adminNotificationRepository;
	    }
	    
    public void sendAppointmentNotificationToAdmin(Appointment appointment, Users user) {
        String message = String.format("New appointment created by user %s. Appointment ID: %d", user.getName(), appointment.getId());
        String notificationType = "appointment";
        Long entityId = appointment.getId();

        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);

        // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
       // sendNotificationToAdmin(message);
    }
    public void sendSuggestionNotificationToAdmin(Suggestion suggestion, Users user) {
        String message = String.format("New suggestion created by user %s. Suggestion ID: %d", user.getName(), suggestion.getId());
        String notificationType = "suggestion";
        Long entityId = suggestion.getId();

        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);

        // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
        // sendNotificationToAdmin(message);
    }
    
    public void sendCancelAppointmentNotificationToAdmin(Appointment appointment, Users user) {
        String message = String.format("Appontment Cancel by user %s. Appointment ID: %d", user.getName(), appointment.getId());
        String notificationType = "cancelAppointment";
        Long entityId = appointment.getId();

        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
        adminNotificationRepository.save(adminNotification);
  // Here you can add logic to send the notification to admin using email, SMS, etc.
        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
        // sendNotificationToAdmin(message);
    }
    public void sendRescheduleAppointmentNotificationToUser(Appointment appointment, Users user,String oldTime) {
    	 String message = String.format("Appontment Reschedule by user %s. Appointment ID: %d", user.getName(), appointment.getId());
          Long entityId = appointment.getId();
	        String title="Appointment Rescheduled";
	        String notificationType = "rescheduleAppointment";
		  
	        AdminNotification adminNotification = new AdminNotification(message, notificationType, entityId,user.getProfilePphoto());
	        adminNotificationRepository.save(adminNotification);	  // Here you can add logic to send the notification to admin using email, SMS, etc.
	        // For example, you can use the previously defined sendNotificationToAdmin method for this purpose.
	        // sendNotificationToAdmin(message);
	    }
    public List<AdminNotification> getUnreadAdminNotifications() {
        return adminNotificationRepository.findByReadFalse();
}
}
