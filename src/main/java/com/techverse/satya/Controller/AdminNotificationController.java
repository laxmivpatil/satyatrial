package com.techverse.satya.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AppointmentResponse;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Repository.AdminNotificationRepository;
import com.techverse.satya.Repository.AppointmentRepository;
import com.techverse.satya.Service.AdminNotificationService;
import com.techverse.satya.Service.AppointmentService;
import com.techverse.satya.Service.SuggestionService;

@RestController
@RequestMapping("")
public class AdminNotificationController {

    private final AdminNotificationService notificationService;
    @Autowired
    AppointmentService appointmentService; 
    
     
    
    
    @Autowired
    SuggestionService suggestionService;
    @Autowired
    AdminNotificationRepository adminNotificationRepository; 

    @Autowired
    public AdminNotificationController(AdminNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/admin/notifications/unread")
    public ResponseEntity<?> getUnreadAdminNotifications() {
        List<AdminNotification> unreadNotifications = notificationService.getUnreadAdminNotifications();
     
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

         
    }
    @PutMapping("/admin/notifications/read")
    public ResponseEntity<?> markNotificationAsRead(@RequestParam Long notificationId) {
    	Optional<AdminNotification> notificationOptional = adminNotificationRepository.findById(notificationId);
        notificationOptional.ifPresent(notification -> {
            notification.setRead(true);
            adminNotificationRepository.save(notification);
        });
        Map<String, Object> responseBody = new HashMap<>();
        
        if(notificationOptional.get().getNotificationType().equals("appointment"))
        {
        	Appointment a=appointmentService.getAppointmentById(notificationOptional.get().getEntityId()).get();
        	 
        	AppointmentResponse ar=new  AppointmentResponse();
    		ar.setAppointmentType(a.getAppointmentType());
    		ar.setComment(a.getComment());
    		ar.setName(a.getUser().getName());
    		ar.setDate(a.getDate());
    		ar.setProfileurl(a.getUser().getProfilePphoto());
    		ar.setPurpose(a.getPurpose());
    		ar.setStatus(a.getStatus());
    		ar.setTime(a.getTime());
		
    		   responseBody.put("appointment", ar);
               return new ResponseEntity<>(responseBody, HttpStatus.OK); 
       
        }
        else if(notificationOptional.get().getNotificationType().equals("suggestion"))
        {
        	Suggestion suggestion =suggestionService.getSuggestionById(notificationOptional.get().getEntityId()).get();
        	 
        	  SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
             suggestionResponseDTO.setName(suggestion.getUser().getName());
             suggestionResponseDTO.setAddress(suggestion.getAddress());
             suggestionResponseDTO.setPurpose(suggestion.getPurpose());
             suggestionResponseDTO.setComment(suggestion.getComment());
             suggestionResponseDTO.setPhoto(suggestion.getPhotoUrl());
             suggestionResponseDTO.setVideo(suggestion.getVideoUrl());
             suggestionResponseDTO.setDateTime(suggestion.getDateTime());
             // You can set other fields like photo and video based on your logic

             responseBody.put("Suggestion", suggestionResponseDTO);
             return new ResponseEntity<>(responseBody, HttpStatus.OK); 
        }
        else  if(notificationOptional.get().getNotificationType().equals("cancelAppointment"))
        {
        	Appointment a=appointmentService.getAppointmentById(notificationOptional.get().getEntityId()).get();
       	 
        	AppointmentResponse ar=new  AppointmentResponse();
    		ar.setAppointmentType(a.getAppointmentType());
    		ar.setComment(a.getComment());
    		ar.setName(a.getUser().getName());
    		ar.setDate(a.getDate());
    		ar.setProfileurl(a.getUser().getProfilePphoto());
    		ar.setPurpose(a.getPurpose());
    		ar.setStatus(a.getStatus());
    		ar.setTime(a.getTime());
		
    		responseBody.put("appointment", ar);
            return new ResponseEntity<>(responseBody, HttpStatus.OK); 
        	 
        }
        else
        {
        	responseBody.put("message", "No new notification");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    }
    
}

