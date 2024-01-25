package com.techverse.satya.Controller;

 
import java.time.Duration; 
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;
import java.util.Optional;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.DTO.TimeSlotRequest; 
import com.techverse.satya.Model.Admin; 
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.TimeSlotRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.TimeSlotService;
import com.techverse.satya.Service.UserService;
 

 
@RestController
@RequestMapping("")
public class TimeSlotController {

	@Autowired
    private AdminService adminService;
	
	@Autowired
    private UserService userService;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mma");

	
	 
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    /***********admin create a timeslots*/
    @PostMapping("/admin/timeslots/create1")
    public ResponseEntity<ResponseDTO> createTimeSlot1(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TimeSlotRequest timeSlotRequest) {
    	 Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));

        if (admin.isPresent()) {
            try {
            	
            	// Validate time slot details
               /* if (!isValidTimeSlotDetails(timeSlotRequest.getTimeSlotDetails())) {
                	System.out.println("fhdgjfgf");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO<>(false, "Invalid TimeSlot details. Minimum 15 minutes difference required.", ""));
                }*/
                String createdTimeSlot = timeSlotService.createTimeSlot(
                        timeSlotRequest.getDates(),
                        timeSlotRequest.getTimeSlotDetails(),
                        timeSlotRequest.getAvailability(),
                        admin.get());

                if ("Successfully created".equals(createdTimeSlot)) {
                    return ResponseEntity.ok(new ResponseDTO<>(true, "Thank you for Submitting Time availability", timeSlotRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Failed to create TimeSlot due to Overlapping Slots", createdTimeSlot));
                }
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>(false, "Failed to create TimeSlot", e));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO<>(false, "Unauthorized Access",""));
        }
    }

    @PostMapping("/admin/timeslots/create")
    public ResponseEntity<ResponseDTO> createTimeSlot(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TimeSlotRequest timeSlotRequest) {
    	 Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));

        if (admin.isPresent()) {
            try {
            	
            	// Validate time slot details
                if (!isValidTimeSlotDetails(timeSlotRequest.getTimeSlotDetails())) {
                	    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Invalid Availability details. Minimum 15 minutes difference required or availabilty timeslots must be different", ""));
                }
                
               String createdTimeSlot = timeSlotService.createTimeSlot(
                        timeSlotRequest.getDates(),
                        timeSlotRequest.getTimeSlotDetails(),
                        timeSlotRequest.getAvailability(),
                        admin.get());

                if ("Successfully created".equals(createdTimeSlot)) {
                    return ResponseEntity.ok(new ResponseDTO<>(true, "Thank you for Submitting Time availability", timeSlotRequest));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Failed to create TimeSlot due to Overlapping Slots", createdTimeSlot));
                } 
            } catch ( Exception e) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Failed to create TimeSlot", ""));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO<>(false, "Unauthorized Access",""));
        }
    }

     

    private boolean isValidTimeSlotDetails(List<TimeSlotDetail> timeSlotDetails) {
        for (int i = 0; i < timeSlotDetails.size(); i++) {
            LocalTime startTime1 = LocalTime.parse(timeSlotDetails.get(i).getStartTime(), FORMATTER);
            LocalTime endTime1 = LocalTime.parse(timeSlotDetails.get(i).getEndTime(), FORMATTER);

            for (int j = i + 1; j < timeSlotDetails.size(); j++) {
                LocalTime startTime2 = LocalTime.parse(timeSlotDetails.get(j).getStartTime(), FORMATTER);
                LocalTime endTime2 = LocalTime.parse(timeSlotDetails.get(j).getEndTime(), FORMATTER);

                // Check for overlap
                if (startTime1.isBefore(endTime2) && endTime1.isAfter(startTime2)) {
                    return false; // Overlapping time slots
                }
            }

            // Check if the duration is at least 15 minutes
            long durationMinutes = Duration.between(startTime1, endTime1).toMinutes();
            if (durationMinutes < 15) {
                return false;
            }
        }
        return true;
    }
    
    @GetMapping("/user/timeslots/address")
    public ResponseEntity<?> getAddressByDateAndStartTime(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String date,@RequestParam String startTime) {
    	Map<String, Object> responseBody = new HashMap<String, Object>();

    	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
         
   	  Long adminId = user.get().getAdmin().getId();

    	 System.out.println(adminId);
        // Call your service method to get the address based on date and start time
        String address="";
        System.out.println(date +"jhgjhg"+ startTime+"chxjjvhddx"+ adminId);
         address=timeSlotRepository.findAddressByDateStartTimeAndAdminId(date, startTime, adminId);
       // String address = timeSlotService.getAddressByDateAndStartTime(date, startTime);
System.out.println(address+" hfjgjdfhghdfh");
        if (address != null) {
        	 responseBody.put("status", true);
     		responseBody.put("address", address);
     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

             
        } else {
       	responseBody.put("status", false);
  		responseBody.put("address", address);
  		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        }
    }

	 @GetMapping("/admin/timeslots/all")
	    public ResponseEntity<?> getAllTimeSlots(@RequestHeader("Authorization") String authorizationHeader) {
		  Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		   Map<String, Object> responseBody = new HashMap<String, Object>();
			
		   if (admin.isPresent()) {
	      
		 			 responseBody.put("status", true);
	     		responseBody.put("all", timeSlotService.getAllTimeSlots());
	     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		   }
	        else {
		 		  responseBody.put("status", false);
		     		responseBody.put("message", "Unauthorized User");
		     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	      }
		  
	  }

	 @GetMapping("/admin/timeslots/allbymonthyear")
	 @ResponseBody
	 public ResponseEntity<?> getAllTimeSlotsByMonthYear(
	         @RequestHeader("Authorization") String authorizationHeader,
	         @RequestParam int year,
	         @RequestParam int month) {
	     Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
	     Map<String, Object> responseBody = new HashMap<>();

	     if (admin.isPresent()) {
	         responseBody.put("status", true);
	         responseBody.put("data", timeSlotService.getTimeSlotsByMonthYear(year, month,admin.get().getId()));
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
	     } else {
	         responseBody.put("status", false);
	         responseBody.put("message", "Unauthorized User");
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	     }
	 }
	 
	  
	  @DeleteMapping("/admin/timeslots/rescheduledavailability")
		   public ResponseEntity<?> rescheduleTimeSlot(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long timeslotId,@RequestParam String startTime,@RequestParam String endTime) {
		    	 Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
			   Map<String, Object> responseBody = new HashMap<String, Object>();
			   try {	
			   if (admin.isPresent()) {
		      
					  String op= timeSlotService.rescheduledTimeSlot(timeslotId, admin.get(),startTime,endTime);
		             
					  if(op.equals("Successfully created")) {
					  responseBody.put("status", true);
			     		responseBody.put("message", "Availability  has been rescheduled");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
					  }
					  else if(op.startsWith("Overlapping slots====>")){
						  responseBody.put("status", true);
				     		responseBody.put("message", " overlapping slots");
				     		responseBody.put("data", op);
				     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.CONFLICT);
						  
					  }
					  else {
						  responseBody.put("status", false);
				     		responseBody.put("message", "Unauthorized User");
				     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
					  }
					  
		        }
		        else {
			 		  responseBody.put("status", false);
			     		responseBody.put("message", "Unauthorized User");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
		      }
	    	
	            
	        } catch (Exception e) {
	        	  responseBody.put("status", false);
		     		responseBody.put("message", "Availability has not been rescheduled"+e);
		     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	       }
	    }
	    @DeleteMapping("/admin/timeslots/deleteavailability")
	    public ResponseEntity<?> deleteTimeSlot(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long timeslotId) {
	    	 Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
			   Map<String, Object> responseBody = new HashMap<String, Object>();
			   try {	
			   if (admin.isPresent()) {
		      
				  timeSlotService.deleteTimeSlotById(timeslotId);
			 		  responseBody.put("status", true);
			     		responseBody.put("message", "Availability  has been deleted");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		        }
		        else {
			 		  responseBody.put("status", false);
			     		responseBody.put("message", "Unauthorized User");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
		      }
	    	
	            
	        } catch (Exception e) {
	        	  responseBody.put("status", false);
		     		responseBody.put("message", "Availabilty  has not been deleted."+e);
		     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	       }
	    }
}



   
     
