package com.techverse.satya.Controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AppointmentRequest;
import com.techverse.satya.DTO.AppointmentResponse;
import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AppointmentRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.AppointmentService;
import com.techverse.satya.Service.SmallerTimeSlotService;
import com.techverse.satya.Service.SubAdminService;
import com.techverse.satya.Service.UserService;

@RestController
@RequestMapping("")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    SmallerTimeSlotService smallerTimeSlotService;
    @Autowired
	 AppointmentRepository  appointmentRepository;
    @Autowired
	private UserService userService;
    
    @Autowired
   	private AdminService adminService;
    @Autowired
   	private SubAdminService subAdminService; 
    
    //create appointments by user
    @PostMapping("/user/appointments/create")
    public ResponseEntity<?> createAppointment(@RequestHeader("Authorization") String authorizationHeader,@RequestBody AppointmentRequest appointmentRequest) {
    	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
    	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {

        // Validation checks for appointmentRequest and userId can be added here
    	

        if (appointmentRequest == null) {
            return ResponseEntity.badRequest().body("Invalid request parameters");
        }
       
          Appointment appointment = appointmentService.createAppointment(appointmentRequest, user.get());
         if (appointment != null) {
            // Return success response with created appointment details
        	 
        	 responseBody.put("status", true);
     		responseBody.put("message","Appointment created Successfully");
     		responseBody.put("Appointment",toAppointmentResponseOne(appointment));
     		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

        } else {
            // Handle the case where the appointment could not be created
        	 responseBody.put("status", false);
      		responseBody.put("message","Appointment creation Error");
      		
      		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
   }
   	  }else {
   		responseBody.put("status",false);
          responseBody.put("message","Unauthorized Access");
          return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
      }
    }
    

    
    @GetMapping("/user/appointments/todaybyuserid")
    public ResponseEntity<?> getTodayAppointments(@RequestHeader("Authorization") String authorizationHeader) {
    	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
    	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {

    	List<Appointment> todayAppointments = appointmentService.getTodaysAppointments(user.get().getId()+"");
        
    	if(!todayAppointments.isEmpty()) {
    	responseBody.put("status", true);
		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		}
    	else
    	{
    		responseBody.put("status", false);
    		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        
    	}
   	 }else {
    		responseBody.put("status",false);
           responseBody.put("message","Unauthorized Access");
           return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
       }

         
    }

    @GetMapping("/user/appointments/upcomingbyuserid")
    public ResponseEntity<?> getUpcomingAppointments(@RequestHeader("Authorization") String authorizationHeader) {
   	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
	 Map<String, Object> responseBody = new HashMap<String, Object>();
	  if(user.isPresent()) {


    	List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointments(user.get().getId()+"");
    	if(!upcomingAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Upcoming Appointments",toAppointmentResponse( upcomingAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("Upcoming Appointments",toAppointmentResponse(upcomingAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                    
        	}
	  }else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    	 
    }

    @GetMapping("/user/appointments/pastbyuserid")
    public ResponseEntity<?> getPastAppointments(@RequestHeader("Authorization") String authorizationHeader) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
   	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {


    	List<Appointment> pastAppointments = appointmentService.getPastAppointments(user.get().getId()+"");
    	if(!pastAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Past Appointments",toAppointmentResponse( pastAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("past Appointments",toAppointmentResponse(pastAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                    
        	} 
    	}else {
          		responseBody.put("status",false);
                responseBody.put("message","Unauthorized Access");
                return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
            }
    }
    @GetMapping("/user/appointments/pendingbyuserid")
    public ResponseEntity<?> getPendingAppointmentsByUser(@RequestHeader("Authorization") String authorizationHeader) {
    	 	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
      	 Map<String, Object> responseBody = new HashMap<String, Object>();
      	try { 
      	 if(user.isPresent()) {    	
           
            List<Appointment> pendingAppointments = appointmentService.getPendingAppointmentsByUser(user.get().getId()+"");
            
            // Check if appointments are found
            if(!pendingAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("Pending Appointments",toAppointmentResponse( pendingAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("pending Appointments",toAppointmentResponse(pendingAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
    	  		responseBody.put("status",false);
    	         responseBody.put("message","Unauthorized Access");
    	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
    	     }
        } catch (NumberFormatException e) {
            // Handle invalid user ID (not a number)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/user/appointments/canceledbyuserid")
    public ResponseEntity<?> getcanceledAppointmentsByUser(@RequestHeader("Authorization") String authorizationHeader) {
     	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
     	 Map<String, Object> responseBody = new HashMap<String, Object>();
     	try { 
     	 if(user.isPresent()) { 
            List<Appointment> canceledAppointments = appointmentService.getCanceledAppointmentsByUser(user.get().getId()+"");
            
            // Check if appointments are found
            if(!canceledAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
 	  		responseBody.put("status",false);
 	         responseBody.put("message","Unauthorized Access");
 	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
 	     }
     } catch (NumberFormatException e) {
         // Handle invalid user ID (not a number)
         return ResponseEntity.badRequest().build();
     } catch (Exception e) {
         // Handle other exceptions
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
    }
    //admin start
    @GetMapping("/admin/appointments/todaybyadminid")
    public ResponseEntity<?> getTodayAppointmentsByAdmin(@RequestHeader("Authorization") String authorizationHeader) {
    	 Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {

    	List<Appointment> todayAppointments = appointmentService.getTodaysAppointmentsByAdmin(user.get().getId()+"");
        
    	if(!todayAppointments.isEmpty()) {
    	responseBody.put("status", true);
		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		}
    	else
    	{
    		responseBody.put("status", false);
    		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        
    	}
   	 }else {
    		responseBody.put("status",false);
           responseBody.put("message","Unauthorized Access");
           return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
       }

         
    }

    @GetMapping("/admin/appointments/upcomingbyadminid")
    public ResponseEntity<?> getUpcomingAppointmentsByAdmin(@RequestHeader("Authorization") String authorizationHeader) {
   	 Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
	 Map<String, Object> responseBody = new HashMap<String, Object>();
	  if(user.isPresent()) {


    	List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointmentsByAdmin(user.get().getId()+"");
    	if(!upcomingAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Upcoming Appointments",toAppointmentResponse( upcomingAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("Upcoming Appointments",toAppointmentResponse(upcomingAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                    
        	}
	  }else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    	 
    }

    @GetMapping("/admin/appointments/pastbyadminid")
    public ResponseEntity<?> getPastAppointmentsByAdmin(@RequestHeader("Authorization") String authorizationHeader) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
   	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {


    	List<Appointment> pastAppointments = appointmentService.getPastAppointmentsByAdmin(user.get().getId()+"");
    	if(!pastAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Past Appointments",toAppointmentResponse( pastAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("past Appointments",toAppointmentResponse(pastAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        	} 
    	}else {
          		responseBody.put("status",false);
                responseBody.put("message","Unauthorized Access");
                return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
            }
    }
    @GetMapping("/admin/appointments/pendingbyadminid")
    public ResponseEntity<?> getPendingAppointmentsByAdmin(@RequestHeader("Authorization") String authorizationHeader) {
    	 	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
      	 Map<String, Object> responseBody = new HashMap<String, Object>();
      	try { 
      	 if(user.isPresent()) {    	
            List<Appointment> pendingAppointments = appointmentService.getPendingAppointmentsByAdmin(user.get().getId()+"");
            
            // Check if appointments are found
            if(!pendingAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("Pending Appointments",toAppointmentResponse( pendingAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("pending Appointments",toAppointmentResponse(pendingAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
    	  		responseBody.put("status",false);
    	         responseBody.put("message","Unauthorized Access");
    	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
    	     }
        } catch (NumberFormatException e) {
            // Handle invalid user ID (not a number)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/admin/appointments/canceledbyadminid")
    public ResponseEntity<?> getcanceledAppointmentsByAdmin(@RequestHeader("Authorization") String authorizationHeader) {
     	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
     	 Map<String, Object> responseBody = new HashMap<String, Object>();
     	try { 
     	 if(user.isPresent()) { 
            List<Appointment> canceledAppointments = appointmentService.getCanceledAppointmentsByAdmin(user.get().getId()+"");
            
            // Check if appointments are found
            if(!canceledAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
 	  		responseBody.put("status",false);
 	         responseBody.put("message","Unauthorized Access");
 	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
 	     }
     } catch (NumberFormatException e) {
         // Handle invalid user ID (not a number)
         return ResponseEntity.badRequest().build();
     } catch (Exception e) {
         // Handle other exceptions
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
    }
    
    
    
    
    
    
    @GetMapping("/appointments/all")
    public ResponseEntity<?> getAllAppointments() {
    	Map<String, Object> responseBody = new HashMap<String, Object>();

    	List<Appointment> appointments = appointmentService.getAllAppointments();
    	if(!appointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("All Appointments",toAppointmentResponse(appointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("All Appointments",toAppointmentResponse(appointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                          
        	}
         
    }
    
        public List<AppointmentResponse>  toAppointmentResponse(List<Appointment> appointments)
    {
    	List<AppointmentResponse> arr=new ArrayList<AppointmentResponse>();
    	for(Appointment  a: appointments) {
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
        		ar.setEndTime(plusTime(a.getTime()));
        		
        		if(ar.getAppointmentType().equals("online"))
        		{
        			ar.setAppointmentAddress("");
        		}
        		else if(ar.getStatus().equals("cancel"))
        		{
        			ar.setAppointmentAddress("");
        		}
        		else
        		{
        			ar.setAppointmentAddress(a.getSmallerTimeSlot().getTimeSlot().getTimeSlotDetails().get(0).getAddress());
            		
        		}
        		arr.add(ar);
    	}
    	return arr;
    	
    }
        
       public String plusTime(String timeString)
       {
    	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
           LocalTime time = LocalTime.parse(timeString, formatter);

           // Add 15 minutes to the time
           LocalTime newTime = time.plusMinutes(15);

           // Format the new time as hh:mma
           String formattedNewTime = newTime.format(formatter);
           return formattedNewTime;
       }
    public  AppointmentResponse  toAppointmentResponseOne(Appointment a)
    {
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
        		ar.setEndTime(plusTime(a.getTime()));
        	 	//ar.setAppointmentAddress(a.getSmallerTimeSlot().getTimeSlot().getTimeSlotDetails().get(0).getAddress());
        		if(ar.getAppointmentType().equals("online"))
        		{
        			ar.setAppointmentAddress("");
        		}
        		else if(ar.getStatus().equals("cancel"))
        		{
        			ar.setAppointmentAddress("");
        		}
        		else
        		{
        			ar.setAppointmentAddress(a.getSmallerTimeSlot().getTimeSlot().getTimeSlotDetails().get(0).getAddress());
            		
        		}
    	
    	return ar;
    	
    }
    @GetMapping("/user/appointments/byappointmentid")
    public ResponseEntity<?> getAppointmentByIdUser(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
        
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
 	        
    	    	
        Optional<Appointment> appointmentOptional = appointmentService.getAppointmentById(appointmentId);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            responseBody.put("status", true);
            responseBody.put("Appointment Details", toAppointmentResponseOne(appointment));
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("status", false);
            responseBody.put("message", "Appointment not found with ID: " + appointmentId);
            return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                  }
    	 }else {
   	  		responseBody.put("status",false);
	         responseBody.put("message","Unauthorized Access");
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	     }
    }
    @GetMapping("/admin/appointments/byappointmentid")
    public ResponseEntity<?> getAppointmentByIdAdmin(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	 
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
    	        
    	Optional<Appointment> appointmentOptional = appointmentService.getAppointmentById(appointmentId);

        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            responseBody.put("status", true);
            responseBody.put("Appointment Details", toAppointmentResponseOne(appointment));
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("status", false);
            responseBody.put("message", "Appointment not found with ID: " + appointmentId);
            return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                  }
    	 }else {
  	  		responseBody.put("status",false);
	         responseBody.put("message","Unauthorized Access");
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	     }
    	 
    }
    @GetMapping("/admin/appointments/byType")
    public ResponseEntity<?> getAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String appointmentType) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	Map<String, Object> responseBody = new HashMap<>();

    	if(user.isPresent()) { 
    	    	
    
    	responseBody.put("status", true);
        
    	responseBody.put(appointmentType+" Appointments",toAppointmentResponse(appointmentRepository.findByAppointmentType(appointmentType)));
    	 
    	return ResponseEntity.ok(responseBody);
    	 }
    	 else {
    	  		responseBody.put("status",false);
 	         responseBody.put("message","Unauthorized Access");
 	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
 	     }
    }
    @PutMapping("/user/appointments/in-progress")
    public ResponseEntity<?> updateAppointmentStatusToInProgress(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
        
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
        Optional<Appointment> updatedAppointmentOptional = appointmentService.updateAppointmentStatusToInProgress(appointmentId);

        if (updatedAppointmentOptional.isPresent()) {
            Appointment updatedAppointment = updatedAppointmentOptional.get();
            responseBody.put("status", true);
            responseBody.put("message", "Appointment status updated to 'In Progress'.");
            responseBody.put("Updated Appointment", toAppointmentResponseOne(updatedAppointment));
            return ResponseEntity.ok(responseBody);
        } else {
            responseBody.put("status", false);
            responseBody.put("message", "Appointment not found with ID: " + appointmentId);
            return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
            
        }
    	 }
        else {
	  		responseBody.put("status",false);
	         responseBody.put("message","Unauthorized Access");
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	     }
    }
    // Other appointment-related endpoints (GET, PUT, DELETE) can be added as needed.
    //by user delete appointment  
    @PatchMapping("/user/appointments/delete")
    public ResponseEntity<?> deleteAppointment(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
        
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
        boolean isCanceled = appointmentService.deleteAppointmentbyUser(appointmentId);
        

        if (isCanceled) {
        	  responseBody.put("status", true);
              responseBody.put("message", "Appointment status updated to 'Deleted'.");
              return ResponseEntity.ok(responseBody);
   } else {
	   responseBody.put("status", false);
       responseBody.put("message", "Appointment not found with ID: " + appointmentId);
       return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
             }
        
    }
    else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    }
    @PatchMapping("/user/appointments/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId,@RequestParam String newTime) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
        
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
        boolean isReschedule = appointmentService.rescheduledAppointmentbyUser(appointmentId,newTime);
        

        if (isReschedule) {
        	  responseBody.put("status", true);
              responseBody.put("message", "Appointment Rescheduled done");
              return ResponseEntity.ok(responseBody);
   } else {
	   responseBody.put("status", false);
       responseBody.put("message", "Time Slot not available  " + newTime+" please select another timeslot");
       return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
             }
        
    }
    else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    }
    
    //by admin cancel the user appointment
    @PatchMapping("/admin/appointments/cancel")
    public ResponseEntity<?> cancelAppointmentByAdmin(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	Map<String, Object> responseBody = new HashMap<>();

    	if(user.isPresent()) { 
        boolean isCanceled = appointmentService.cancelAppointmentbyAdmin(appointmentId);
       
        if (isCanceled) {
        	  responseBody.put("status", true);
              responseBody.put("message", "Appointment status updated to 'Cancel'.");
              return ResponseEntity.ok(responseBody);
   } else {
	   responseBody.put("status", false);
       responseBody.put("message", "Appointment not found with ID: " + appointmentId);
       return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
       }
    	}
        else {
      		responseBody.put("status",false);
             responseBody.put("message","Unauthorized Access");
             return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
         }
    }
    
 
    @PatchMapping("/admin/appointments/reschedule")
public ResponseEntity<?> rescheduledAppointmentByAdmin(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId,@RequestParam String newTime) {
    	  
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	Map<String, Object> responseBody = new HashMap<>();

    	if(user.isPresent()) { 
       
    	  
    	boolean isRescheduled = appointmentService.rescheduledAppointmentbyAdmin(appointmentId,newTime);
    
     
    if (isRescheduled) {
        responseBody.put("status", true);
        responseBody.put("message", "Appointment Rescheduled successfully.");
        return ResponseEntity.ok(responseBody);
    } else {
        responseBody.put("status", false);
        responseBody.put("message", "Appointment allerady booked in the new time please choose another time  " + appointmentId);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
    }
    else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    }
    
    
    
    @PatchMapping("/admin/appointments/delete")
    public ResponseEntity<?> deleteAppointmentByAdmin(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long appointmentId) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	 
    	Map<String, Object> responseBody = new HashMap<>();
    	 if(user.isPresent()) { 
    	        
    		 boolean isDeleted = appointmentService.deleteAppointmentbyAdmin(appointmentId);
    		     

    		    if (isDeleted) {
    		        responseBody.put("status", true);
    		        responseBody.put("message", "Appointment deleted successfully.");
    		        return ResponseEntity.ok(responseBody);
    		    } else {
    		        responseBody.put("status", false);
    		        responseBody.put("message", "Appointment not found with ID: " + appointmentId);
    		        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    		    }
    	 }else {
  	  		responseBody.put("status",false);
	         responseBody.put("message","Unauthorized Access");
	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
	     }
    	 
    }

  
    @GetMapping("/admin/appointments/todaybytype")
    public ResponseEntity<?> getTodayAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String type) {
    	 Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
    	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {

    	List<Appointment> todayAppointments = appointmentService.getTodaysAppointmentsByType(user.get().getId()+"",type);
        
    	if(!todayAppointments.isEmpty()) {
    	responseBody.put("status", true);
		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
		}
    	else
    	{
    		responseBody.put("status", false);
    		responseBody.put("Todays Appointments",toAppointmentResponse( todayAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        
    	}
   	 }else {
    		responseBody.put("status",false);
           responseBody.put("message","Unauthorized Access");
           return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
       }

         
    }

    @GetMapping("/admin/appointments/upcomingbytype")
    public ResponseEntity<?> getUpcomingAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String type) {
   	 Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
	 Map<String, Object> responseBody = new HashMap<String, Object>();
	  if(user.isPresent()) {


    	List<Appointment> upcomingAppointments = appointmentService.getUpcomingAppointmentsByType(user.get().getId()+"",type);
    	if(!upcomingAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Upcoming Appointments",toAppointmentResponse( upcomingAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("Upcoming Appointments",toAppointmentResponse(upcomingAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                    
        	}
	  }else {
  		responseBody.put("status",false);
         responseBody.put("message","Unauthorized Access");
         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
     }
    	 
    }

    @GetMapping("/admin/appointments/pastbytype")
    public ResponseEntity<?> getPastAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String type) {
    	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
   	 Map<String, Object> responseBody = new HashMap<String, Object>();
   	  if(user.isPresent()) {


    	List<Appointment> pastAppointments = appointmentService.getPastAppointmentsByType(user.get().getId()+"",type);
    	if(!pastAppointments.isEmpty()) {
        	responseBody.put("status", true);
    		responseBody.put("Past Appointments",toAppointmentResponse( pastAppointments));
    		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
    		}
        	else
        	{
        		responseBody.put("status", false);
        		responseBody.put("past Appointments",toAppointmentResponse(pastAppointments));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        	} 
    	}else {
          		responseBody.put("status",false);
                responseBody.put("message","Unauthorized Access");
                return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
            }
    }
    @GetMapping("/admin/appointments/pendingbytype")
    public ResponseEntity<?> getPendingAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String type) {
    	 	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
      	 Map<String, Object> responseBody = new HashMap<String, Object>();
      	try { 
      	 if(user.isPresent()) {    	
            List<Appointment> pendingAppointments = appointmentService.getPendingAppointmentsByType(user.get().getId()+"",type);
            
            // Check if appointments are found
            if(!pendingAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("Pending Appointments",toAppointmentResponse( pendingAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("pending Appointments",toAppointmentResponse(pendingAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
    	  		responseBody.put("status",false);
    	         responseBody.put("message","Unauthorized Access");
    	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
    	     }
        } catch (NumberFormatException e) {
            // Handle invalid user ID (not a number)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
   
    
    
    
    
     @GetMapping("/admin/appointments/canceledbytype")
    public ResponseEntity<?> getcanceledAppointmentsByType(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String type) {
     	Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));
     	 Map<String, Object> responseBody = new HashMap<String, Object>();
     	try { 
     	 if(user.isPresent()) { 
            List<Appointment> canceledAppointments = appointmentService.getCanceledAppointmentsByType(user.get().getId()+"",type);
            
            // Check if appointments are found
            if(!canceledAppointments.isEmpty()) {
            	responseBody.put("status", true);
        		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments ));
        		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
        		}
            	else
            	{
            		responseBody.put("status", false);
            		responseBody.put("canceled Appointments",toAppointmentResponse(canceledAppointments));
            		return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);
                        
            	}
    	 }else {
 	  		responseBody.put("status",false);
 	         responseBody.put("message","Unauthorized Access");
 	         return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
 	     }
     } catch (NumberFormatException e) {
         // Handle invalid user ID (not a number)
         return ResponseEntity.badRequest().build();
     } catch (Exception e) {
         // Handle other exceptions
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
    }
    
    
    
     
     
     
  
     
    
     
    
}
