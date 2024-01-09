package com.techverse.satya.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.TimeSlotDetailDto;
import com.techverse.satya.DTO.TimeSlotRequest;
import com.techverse.satya.DTO.TimeSlotResponse;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.TimeSlot;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.TimeSlotRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.TimeSlotService;
import com.techverse.satya.Service.UserService;

import io.jsonwebtoken.lang.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("")
public class TimeSlotController {

	@Autowired
    private AdminService adminService;
	
	@Autowired
    private UserService userService;
	
	
	 
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotController.class);
   
    /***********admin create a timeslots*/
    @PostMapping("/admin/timeslots/create")
    public ResponseEntity<ResponseDTO> createTimeSlot1(@RequestHeader("Authorization") String authorizationHeader, @RequestBody TimeSlotRequest timeSlotRequest) {
        ResponseDTO<Object> response = new ResponseDTO<>();

       
        Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
        if (admin.isPresent()) {
            try {
                String createdTimeSlot = timeSlotService.createTimeSlot(timeSlotRequest.getDates(), timeSlotRequest.getTimeSlotDetails(), timeSlotRequest.getAvailability(), admin.get());

                if (createdTimeSlot.equals("Successfully created")) {
                    response.setStatus(true);
                    response.setMessage("Thank you for Submitting Time availability");
                    response.setData(timeSlotRequest); // Return an empty JSON object as data
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    response.setStatus(false);
                    response.setMessage("Failed to create TimeSlot due to Overlapping Slots");
                    response.setData(createdTimeSlot); // Return overlapping time slots details as data
                    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
                }
            } catch (RuntimeException e) {
                response.setStatus(false);
                response.setMessage("Failed to create TimeSlot");
                response.setData(e); // Return an empty JSON object as data
                System.out.println(e);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(false);
            response.setMessage("Unauthorized Access");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

  

    
    @GetMapping("/user/timeslots/address")
    public ResponseEntity<?> getAddressByDateAndStartTime(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String date,@RequestParam String startTime) {
    	Map<String, Object> responseBody = new HashMap<String, Object>();

    	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
         
   	  Long adminId = user.get().getAdmin().getId();

    	 
        // Call your service method to get the address based on date and start time
        String address="";
 
         address=timeSlotRepository.findAddressByDateStartTimeAndAdminId(date, startTime, adminId);
       // String address = timeSlotService.getAddressByDateAndStartTime(date, startTime);

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
	         responseBody.put("data", timeSlotService.getTimeSlotsByMonthYear(year, month));
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
			     		responseBody.put("message", "Availability with ID " + timeslotId + " has been rescheduled");
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
		     		responseBody.put("message", "Availability with ID " + timeslotId + " has not been rescheduled"+e);
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
			     		responseBody.put("message", "Availability with ID " + timeslotId + " has been deleted");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		        }
		        else {
			 		  responseBody.put("status", false);
			     		responseBody.put("message", "Unauthorized User");
			     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
		      }
	    	
	            
	        } catch (Exception e) {
	        	  responseBody.put("status", false);
		     		responseBody.put("message", "Availabilty with ID " + timeslotId + " has not been deleted."+e);
		     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	       }
	    }
}



   
     
