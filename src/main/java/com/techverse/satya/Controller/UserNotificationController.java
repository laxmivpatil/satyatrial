package com.techverse.satya.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AppointmentResponse;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.UserNotification;
import com.techverse.satya.Repository.UserNotificationRepository;
import com.techverse.satya.Service.AppointmentService;
import com.techverse.satya.Service.UserNotificationService;

@RestController
@RequestMapping("")
public class UserNotificationController {

	@Autowired
	UserNotificationService userNotificationService;

	@Autowired
	AppointmentService appointmentService;
	@Autowired
	UserNotificationRepository userNotificationRepository;
	
	 /* @GetMapping("/notifications/unread")
	    public ResponseEntity<?> getUnreadAdminNotifications() {
	        List<UserNotification> unreadNotifications = userNotificationService.getUnreadUserNotifications();
	     
	        Map<String, Object> responseBody = new HashMap<>();
	        if(!unreadNotifications.isEmpty()) {
	        	responseBody.put("status",true);
	 	   responseBody.put("appointment", unreadNotifications);
	       return new ResponseEntity<>(responseBody, HttpStatus.OK); 
	        }
	        else
	        {
	        	responseBody.put("status",false);
	        	 responseBody.put("appointment", unreadNotifications);
	             return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND); 
	           
	        }

	         
	    }*/
	  @GetMapping("/notifications/unread")
	    public ResponseEntity<?> getUnreadAdminNotificationsbyUserId(@RequestParam Long userId) {
	        List<UserNotification> unreadNotifications = userNotificationService.getUnreadUserNotificationsByUserId(userId);
	     
	        Map<String, Object> responseBody = new HashMap<>();
	        if(!unreadNotifications.isEmpty()) {
	        	responseBody.put("status",true);
	 	   responseBody.put("Notifications", unreadNotifications);
	       return new ResponseEntity<>(responseBody, HttpStatus.OK); 
	        }
	        else
	        {
	        	responseBody.put("status",false);
	        	 responseBody.put("Notifications", unreadNotifications);
	             return new ResponseEntity<>(responseBody, HttpStatus.OK); 
	           
	        }

	         
	    }
	/*  @GetMapping("/notifications/all")
	  public ResponseEntity<?> getAllUserNotificationsByUserId(@RequestParam Long userId) {
	      List<UserNotification> allNotifications = userNotificationService.getAllUserNotificationsByUserId(userId);

	      Map<String, Object> responseBody = new HashMap<>();
	     
	      
	     if (!allNotifications.isEmpty()) {
	    	 for(UserNotification u:allNotifications) {
	    		 Appointment a=appointmentService.getAppointmentById(u.getAppointmentId()).get();
	    		 
	    	 }
	          responseBody.put("status", true);
	          responseBody.put("notifications", allNotifications);
	      } else {
	          responseBody.put("status", false);
	          responseBody.put("notifications", allNotifications);
	      }

	      return new ResponseEntity<>(responseBody, HttpStatus.OK);
	      
	  }
	  */
	  @GetMapping("/notifications/all")
	  public ResponseEntity<Map<String, Object>> getAllUserNotificationsByUserId(@RequestParam Long userId) {
	      List<UserNotification> allNotifications = userNotificationService.getAllUserNotificationsByUserId(userId);

	      Map<String, Object> responseBody = new HashMap<>();
	      List<Map<String, Object>> notificationDetailsList = new ArrayList<>();

	      for (UserNotification userNotification : allNotifications) {
	          Map<String, Object> notificationDetails = new HashMap<>();
	          Map<String, Object> notificationData = new HashMap<>();
	          Map<String, Object> appointmentData = new HashMap<>();

	          notificationData.put("id", userNotification.getId());
	          notificationData.put("title", userNotification.getTitle());
	          notificationData.put("message", userNotification.getMessage());
	          notificationData.put("appointmentId", userNotification.getAppointmentId());
	          notificationData.put("read", userNotification.isRead());
	          notificationData.put("createdAt", userNotification.getCreatedAt());
	          notificationData.put("userId", userNotification.getUserId());

	          // Fetch appointment details using userNotification's appointmentId
	          Optional<Appointment> optionalAppointment = appointmentService.getAppointmentById(userNotification.getAppointmentId());
	          if (optionalAppointment.isPresent()) {
	              Appointment appointment = optionalAppointment.get();
	              appointmentData.put("appointmentId", appointment.getId());
	              appointmentData.put("name", appointment.getUser().getName());
	              appointmentData.put("profileurl", appointment.getUser().getProfilePphoto());
	              appointmentData.put("appointmentType", appointment.getAppointmentType());
	              appointmentData.put("date", appointment.getDate());
	              appointmentData.put("time", appointment.getTime());
	              appointmentData.put("purpose", appointment.getPurpose());
	              appointmentData.put("comment", appointment.getComment());
	              appointmentData.put("status", appointment.getStatus());
	          } else {
	              appointmentData = null; // No appointment found
	          }

	          notificationDetails.put("notification", notificationData);
	          notificationDetails.put("appointment", appointmentData);
	          notificationDetailsList.add(notificationDetails);
	      }

	      responseBody.put("status", true);
	      responseBody.put("notificationDetails", notificationDetailsList);

	      return new ResponseEntity<>(responseBody, HttpStatus.OK);
	  }


	  @PutMapping("/notifications/read")
	    public ResponseEntity<?> markNotificationAsRead(@RequestParam Long notificationId) {
	    	Optional<UserNotification> notificationOptional = userNotificationRepository.findById(notificationId);
	        notificationOptional.ifPresent(notification -> {
	            notification.setRead(true);
	           userNotificationRepository.save(notification);
	        });
	        Map<String, Object> responseBody = new HashMap<>();
	        
	        if(!notificationOptional.isEmpty())
	        {
	        	Appointment a=appointmentService.getAppointmentById(notificationOptional.get().getAppointmentId()).get();
	        	 
	        	AppointmentResponse ar=new  AppointmentResponse();
	        	ar.setAppointmentId(a.getId());
	    		ar.setAppointmentType(a.getAppointmentType());
	    		ar.setComment(a.getComment());
	    		ar.setName(a.getUser().getName());
	    		ar.setDate(a.getDate());
	    		ar.setProfileurl(a.getUser().getProfilePphoto());
	    		ar.setPurpose(a.getPurpose());
	    		ar.setStatus(a.getStatus());
	    		ar.setTime(a.getTime());
			
	    		   responseBody.put("Notifications", ar);
	               return new ResponseEntity<>(responseBody, HttpStatus.OK); 
	       
	        }
	        
	        else
	        {
	        	responseBody.put("message", "No new notification");
	            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
	        }
	    }
}
