package com.techverse.satya.Controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AdminNotificationDTO;
import com.techverse.satya.DTO.AppointmentResponse;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.AdminNotification;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Repository.AdminNotificationRepository;
import com.techverse.satya.Repository.AppointmentRepository;
import com.techverse.satya.Service.AdminNotificationService;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.AppointmentService;
import com.techverse.satya.Service.SuggestionService;

@RestController
@RequestMapping("")
public class AdminNotificationController {

    private final AdminNotificationService notificationService;
    @Autowired
    AppointmentService appointmentService; 
    @Autowired
    AdminService adminService;
    
     
    
    
    @Autowired
    SuggestionService suggestionService;
    @Autowired
    AdminNotificationRepository adminNotificationRepository; 

    @Autowired
    public AdminNotificationController(AdminNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/admin/notifications/unread")
    public ResponseEntity<Map<String, Object>> getUnreadAdminNotifications(@RequestHeader("Authorization") String authorizationHeader) {
        Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
        Map<String, Object> responseBody = new HashMap<>();

        try {
            System.out.println("hi token");
            if (user.isPresent()) {

                List<AdminNotificationDTO> unreadNotifications = notificationService.getUnreadAdminNotificationDTOs(user.get().getId());

                // Filter notifications for today, this week, and this month
                LocalDate today = LocalDate.now();
                LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
                LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);

                List<AdminNotificationDTO> todayNotifications = unreadNotifications.stream()
                        .filter(notification -> notification.getCreatedAt().toLocalDate().isEqual(today))
                        .collect(Collectors.toList());

                List<AdminNotificationDTO> thisWeekNotifications = unreadNotifications.stream()
                        .filter(notification -> notification.getCreatedAt().isAfter(startOfWeek))
                        .filter(notification -> !todayNotifications.contains(notification))
                        .collect(Collectors.toList());

                List<AdminNotificationDTO> thisMonthNotifications = unreadNotifications.stream()
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
            } else {
                responseBody.put("status", false);
                responseBody.put("message", "Admin Not Found");
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            responseBody.put("status", false);
            responseBody.put("message", "Admin Not Found");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Map<String, Object> buildNotificationMap(String name, List<AdminNotificationDTO> data) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("name", name);
        notificationMap.put("data", data);
        return notificationMap;
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

