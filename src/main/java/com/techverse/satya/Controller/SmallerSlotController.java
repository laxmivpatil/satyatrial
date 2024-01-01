package com.techverse.satya.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.SmallerTimeSlotService;
import com.techverse.satya.Service.UserService;

 
 

 
 
 
 
 

@RestController
@RequestMapping("")
public class SmallerSlotController {

	@Autowired
    private SmallerTimeSlotService smallerTimeSlotService;
	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
 @GetMapping("/user/timeslots/get")
public ResponseEntity<?> getSmallerTimeSlots(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String date, @RequestParam String availability) {
	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
     
	  Long adminId = user.get().getAdmin().getId();

	    Map<String, Object> responseBody = new HashMap<>();
	    boolean slotBook = false;

	    List<SmallerTimeSlot> smallerTimeSlots = smallerTimeSlotService.getSmallerTimeSlotsByAdminIdAndDateAndAvailability(adminId, date, availability, slotBook);

    List<Map<String, String>> morningSlots = new ArrayList<>();
    List<Map<String, String>> daySlots = new ArrayList<>();
    List<Map<String, String>> eveningSlots = new ArrayList<>();

    for (SmallerTimeSlot s : smallerTimeSlots) {
        String startTime = s.getStartTime();
        String endTime = s.getEndTime();
        Map<String, String> slot = new HashMap<>();
        slot.put("time", startTime + " to " + endTime);

        if (isMorningSlot(startTime)) {
            morningSlots.add(slot);
        } else if (isDaySlot(startTime)) {
            daySlots.add(slot);
        } else {
            eveningSlots.add(slot);
        }
    }

  /*  if (morningSlots.isEmpty() && daySlots.isEmpty() && eveningSlots.isEmpty()) {
        responseBody.put("success", false);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
*/
    responseBody.put("morningSlots", morningSlots);
    responseBody.put("daySlots", daySlots);
    responseBody.put("eveningSlots", eveningSlots);

    return new ResponseEntity<>(responseBody, HttpStatus.OK);
}
private boolean isMorningSlot(String startTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
    LocalTime time = LocalTime.parse(startTime.toUpperCase(), formatter);
    return time.isAfter(LocalTime.parse("5:30AM", formatter)) && time.isBefore(LocalTime.parse("12:00PM", formatter));
}

private boolean isDaySlot(String startTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
    LocalTime time = LocalTime.parse(startTime.toUpperCase(), formatter);
    return time.isAfter(LocalTime.parse("12:00PM", formatter)) && time.isBefore(LocalTime.parse("6:00PM", formatter));
}

@GetMapping("/admin/timeslots/getavailability")
public ResponseEntity<?> getAllSlotsFromCurrentTime(@RequestHeader("Authorization") String authorizationHeader) {
    Map<LocalDate, List<Map<String, Object>>> slotsByDate = new HashMap<>();
    Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
    Map<String, Object> response = new HashMap<>();
    System.out.println("admin "+admin.get().getEmail());
    if (admin.isPresent()) {

    // Get the current date and time
    LocalDateTime currentDateTime = LocalDateTime.now();
    // Retrieve all SmallerTimeSlot records from the database
    List<SmallerTimeSlot> allSlots = smallerTimeSlotService.getAllSlotsByAdmin(admin.get().getId());


    // Group slots by date
    for (SmallerTimeSlot s : allSlots) {
        // Parse slot start time to LocalDateTime
        String slotStartTimeString = s.getStartTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
        LocalTime slotStartTime = LocalTime.parse(slotStartTimeString, formatter);

        // Compare slot start time with current time
        if (slotStartTime.isAfter(LocalTime.now())) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("startTime", s.getStartTime());
            slot.put("endTime", s.getEndTime());

            if (s.isSlotBook()) {
                // If the slot is booked, add additional details
                Appointment appointment = s.getAppointment();
                Map<String, Object> appointmentDetails = new HashMap<>();
                appointmentDetails.put("appointmentId", appointment.getId());
                appointmentDetails.put("appointmentType", appointment.getAppointmentType());
                appointmentDetails.put("date", appointment.getDate());
                appointmentDetails.put("time", appointment.getTime());
                appointmentDetails.put("purpose", appointment.getPurpose());
                appointmentDetails.put("comment", appointment.getComment());
                appointmentDetails.put("status", appointment.getStatus());

                Users user = appointment.getUser();
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("userId", user.getId());
                userDetails.put("userName", user.getName());
                userDetails.put("profile", user.getProfilePphoto());
                // Add more user details as needed

                appointmentDetails.put("userDetails", userDetails);
                slot.put("appointmentDetails", appointmentDetails);
            } else {
                // If the slot is not booked, add a message
                slot.put("appointmentDetails", "No appointment");
            }

            // Parse the slot date from string to LocalDate
            String slotDateString = s.getTimeSlot().getDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate slotDate = LocalDate.parse(slotDateString, dateFormatter);

            // Group slots by date
            slotsByDate.computeIfAbsent(slotDate, k -> new ArrayList<>()).add(slot);
        }
    
    }
    } else {
    	 response.put("success", false);
         response.put("message", "Unauthorized access");
         return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    if (slotsByDate.isEmpty()) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("message", "No available slots found from the current date and time.");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    response.put("success", true);
    response.put("slots", slotsByDate);

    return new ResponseEntity<>(response, HttpStatus.OK);
    
}

@GetMapping("/admin/timeslots/getappointmentsbymonth")
public ResponseEntity<?> getAllSlotsByMonthYear(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam Long timeSlotId) {
    List<Map<String, Object>> slots = new ArrayList<>();
    Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
    Map<String, Object> response = new HashMap<>();
    
    if (admin.isPresent()) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<SmallerTimeSlot> allSlots = smallerTimeSlotService.getAllSlotsByTimeSlot(timeSlotId);

        Map<String, List<Map<String, Object>>> dateMap = new HashMap<>();

        for (SmallerTimeSlot s : allSlots) {
            String slotDateString = s.getTimeSlot().getDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate slotDate = LocalDate.parse(slotDateString, dateFormatter);
            String formattedMonth = String.format("%02d", slotDate.getMonthValue());

                 Map<String, Object> slot = new HashMap<>();
                slot.put("startTime", s.getStartTime());
                slot.put("endTime", s.getEndTime());

                Map<String, Object> appointmentDetails = new HashMap<>();
                if (s.isSlotBook()){
                    Appointment appointment = s.getAppointment();
                    appointmentDetails.put("date", appointment.getDate());
                    appointmentDetails.put("appointmentType", appointment.getAppointmentType());
                    appointmentDetails.put("purpose", appointment.getPurpose());
                    appointmentDetails.put("appointmentId", appointment.getId());
                    appointmentDetails.put("comment", appointment.getComment());
                    appointmentDetails.put("time", appointment.getTime());
                    appointmentDetails.put("status", appointment.getStatus());
                    appointmentDetails.put("startTime", s.getStartTime());
                    appointmentDetails.put("endTime", s.getEndTime());
                    Users user = appointment.getUser();
                    Map<String, Object> userDetails = new HashMap<>();
                    userDetails.put("userId", user.getId());
                    userDetails.put("userName", user.getName());
                    userDetails.put("profile", user.getProfilePphoto());

                    appointmentDetails.put("userDetails", userDetails);
                } else {
                    appointmentDetails.put("startTime", s.getStartTime());
                    appointmentDetails.put("endTime", s.getEndTime());
                }

                slot.computeIfAbsent("date", k -> {
                    List<Map<String, Object>> appointmentsList = dateMap.computeIfAbsent(slotDateString, key -> new ArrayList<>());
                    appointmentsList.add(Collections.singletonMap("appointmentDetails", appointmentDetails));
                    return slotDateString;
                });
            
        }

        dateMap.forEach((date, appointmentsList) -> {
            Map<String, Object> dateSlot = new HashMap<>();
            dateSlot.put("date", date);
            dateSlot.put("appointments", appointmentsList);
            slots.add(dateSlot);
        });
    } else {
        response.put("success", false);
        response.put("message", "Unauthorized access");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    if (slots.isEmpty()) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("message", "No available slots found for the specified month and year.");
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    response.put("success", true);
    response.put("slots", slots);

    return new ResponseEntity<>(response, HttpStatus.OK);
}

 
  
 //
//
	 
}