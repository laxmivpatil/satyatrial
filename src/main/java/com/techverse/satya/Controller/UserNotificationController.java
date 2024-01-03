package com.techverse.satya.Controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AdminNotificationDTO;
import com.techverse.satya.DTO.AppointmentResponse;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.UserNotification;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.UserNotificationRepository;
import com.techverse.satya.Service.AppointmentService;
import com.techverse.satya.Service.UserNotificationService;
import com.techverse.satya.Service.UserService;

@RestController
@RequestMapping("")
public class UserNotificationController {

	@Autowired
	UserNotificationService userNotificationService;

	
	@Autowired
	UserService userService;
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
	  @GetMapping("/user/notifications/unread")
	    public ResponseEntity<?> getUnreadAdminNotificationsbyUserId(@RequestHeader("Authorization") String authorizationHeader  ) {
		  Map<String, Object> responseBody = new HashMap<>();
	       
		  Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
     	  if(user.isPresent()) {
		  
		  List<UserNotification> unreadNotifications = userNotificationService.getUnreadUserNotificationsByUserId(user.get().getId());
	     
	        if(!unreadNotifications.isEmpty()) {
	        	
	        	 LocalDate today = LocalDate.now();
	                LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
	                LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

	                List<UserNotification> todayNotifications = unreadNotifications.stream()
	                        .filter(notification -> notification.getCreatedAt().toLocalDate().isEqual(today))
	                        .collect(Collectors.toList());

	                List<UserNotification> thisWeekNotifications = unreadNotifications.stream()
	                        .filter(notification -> notification.getCreatedAt().isAfter(startOfWeek))
	                        .filter(notification -> !todayNotifications.contains(notification))
	                        .collect(Collectors.toList());

	                List<UserNotification> thisMonthNotifications = unreadNotifications.stream()
	                        .filter(notification -> notification.getCreatedAt().isAfter(startOfMonth))
	                        .filter(notification -> !todayNotifications.contains(notification))
	                        .filter(notification -> !thisWeekNotifications.contains(notification))
	                        .collect(Collectors.toList());

	                List<Map<String, Object>> notifications = new ArrayList<>();

	                if (!todayNotifications.isEmpty()) {
	                    notifications.add(buildNotificationMap("today", todayNotifications));
	                }

	                if (!thisWeekNotifications.isEmpty()) {
	                    notifications.add(buildNotificationMap("thisweek", thisWeekNotifications));
	                }

	                if (!thisMonthNotifications.isEmpty()) {
	                    notifications.add(buildNotificationMap("thismonth", thisMonthNotifications));
	                }
	                responseBody.put("status", true);

	                responseBody.put("notifications", notifications);

	                return new ResponseEntity<>(responseBody, HttpStatus.OK);
	        	
	        	
	      
	        }
	        else
	        {
	        	responseBody.put("status",false);
	        	 responseBody.put("Notifications", unreadNotifications);
	             return new ResponseEntity<>(responseBody, HttpStatus.OK); 
	           
	        }
     	  }
	  
 	  else
 	  {
 		 responseBody.put("status",false);
    	 responseBody.put("message", "User Not Found");
         return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED); 
 		 
 	  }

	         
	    }
	  private Map<String, Object> buildNotificationMap(String name, List<UserNotification> data) {
	        Map<String, Object> notificationMap = new HashMap<>();
	        notificationMap.put("name", name);
	        notificationMap.put("data", data);
	        return notificationMap;
	    }
	  @GetMapping("/user/notifications/all")
	  public ResponseEntity<Map<String, Object>> getAllUserNotificationsByUserId(@RequestHeader("Authorization") String authorizationHeader ) {
		  Map<String, Object> responseBody = new HashMap<>();
		  Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
     	  if(user.isPresent()) {
		  
		  
		  List<UserNotification> allNotifications = userNotificationService.getAllUserNotificationsByUserId(user.get().getId());

	      
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
	  
 	  else
 	  {
 		 responseBody.put("status",false);
    	 responseBody.put("message", "User Not Found");
         return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED); 
 		 
 	  }

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
