package com.techverse.satya.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.DTO.AppointmentRequest;
import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Appointment;
import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Model.TimeSlot;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Repository.AppointmentRepository;
import com.techverse.satya.Repository.SmallerTimeSlotRepository;
import com.techverse.satya.Repository.TimeSlotRepository;
import com.techverse.satya.Repository.UserRepository;

@Service
public class AppointmentService {

    // autowired fields...
	@Autowired
	 SmallerTimeSlotRepository smallerTimeSlotRepository;
	
	@Autowired
	 AppointmentRepository  appointmentRepository;
	
	@Autowired
	AdminNotificationService adminNotificationService;
	@Autowired
	UserNotificationService userNotificationService;
	@Autowired
	private TimeSlotRepository timeSlotRepository;
   
    // other methods...

	@Autowired
	UserRepository userRepository;
	@Autowired
	AdminRepository adminRepository;
   
   @Transactional
	public Appointment createAppointment(AppointmentRequest appointmentRequest, Users user) {
	    // Fetch the user from the database using userId
	    System.out.println("User ID: " + user.getId());

	    // Create a new appointment from the request
	    Admin admin = user.getAdmin();  // Assuming each user has an associated admin
	    Appointment appointment = new Appointment(appointmentRequest, user, admin);

	    // Parse the input time (in 06:30PM format) into the database format
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("hh:mma");
	    LocalTime parsedTime = LocalTime.parse(appointmentRequest.getTime(), inputFormatter);
	    String formattedTime = parsedTime.format(DateTimeFormatter.ofPattern("h:mma"));
	    System.out.println("Formatted Time: " + formattedTime);

	    // Find the smaller time slot by date and time
	    Optional<SmallerTimeSlot> optionalSmallerTimeSlot = smallerTimeSlotRepository.findByDateAndStartTimeAndAvailabilityAndSlotBookAndAdminId(appointmentRequest.getDate(), appointmentRequest.getTime(), appointmentRequest.getAppointmentType(), admin.getId());
	    
	    System.out.println("Smaller Time Slot: " + optionalSmallerTimeSlot.map(SmallerTimeSlot::getStartTime).orElse("Not found"));

	    if (optionalSmallerTimeSlot.isPresent()) {
	        SmallerTimeSlot smallerTimeSlot = optionalSmallerTimeSlot.get();

	        // Save the appointment first to generate an ID
	        Appointment savedAppointment = appointmentRepository.save(appointment);

	        // Update the smaller time slot properties
	        smallerTimeSlot.setSlotBook(true);

	        // Set the appointment for the smaller time slot
	        smallerTimeSlot.setAppointment(savedAppointment);

	        // Save the updated smaller time slot (this cascades to appointment due to the relationship)
	        smallerTimeSlotRepository.save(smallerTimeSlot);
	        
	        adminNotificationService.sendAppointmentNotificationToAdmin(savedAppointment, user);

	        return savedAppointment;
	    } else {
	        // Handle the case where the specified time slot is not available
	        // You can return null or handle it as needed
	        return null;
	    }
	}


    public List<Appointment> getTodaysAppointments(String userId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByUserIdAndDate(Long.parseLong(userId), formattedDate);
    }

    public List<Appointment> getUpcomingAppointments(String userId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByUserIdAndDateAfter(Long.parseLong(userId), formattedDate);
    }

    public List<Appointment> getPastAppointments(String userId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByUserIdAndDateBefore(Long.parseLong(userId), formattedDate);
    }

    public List<Appointment> getPendingAppointmentsByUser(String userId) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByUserIdAndStatus(Long.parseLong(userId), "Pending");
    }
    public List<Appointment> getCanceledAppointmentsByUser(String userId) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByUserIdAndStatus(Long.parseLong(userId), "Cancel");
    }
    public List<Appointment> getallappointmentbyuser(String userId) {
          return appointmentRepository.findByUserId(Long.parseLong(userId));
    }
    
    
    public List<Appointment> getTodaysAppointmentsByAdmin(String adminId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndDate(Long.parseLong(adminId), formattedDate);
    }

    public List<Appointment> getUpcomingAppointmentsByAdmin(String adminId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndDateAfter(Long.parseLong(adminId), formattedDate);
    }

    public List<Appointment> getPastAppointmentsByAdmin(String adminId) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndDateBefore(Long.parseLong(adminId), formattedDate);
    }

    public List<Appointment> getPendingAppointmentsByAdmin(String adminId) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByAdminIdAndStatus(Long.parseLong(adminId), "Pending");
    }
    public List<Appointment> getCanceledAppointmentsByAdmin(String adminId) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByAdminIdAndStatus(Long.parseLong(adminId), "Cancel");
    }
    public List<Appointment> getallappointmentByAdmin(String adminId) {
          return appointmentRepository.findByAdminId(Long.parseLong(adminId));
    }
    
    public List<Appointment> getTodaysAppointmentsByType(String adminId,String type) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndAppointmentTypeAndDate(Long.parseLong(adminId), type, formattedDate);
   
    }

    public List<Appointment> getUpcomingAppointmentsByType(String adminId,String type) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndAppointmentTypeAndDateAfter(Long.parseLong(adminId), type, formattedDate);
        		       }

    public List<Appointment> getPastAppointmentsByType(String adminId,String type) {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return appointmentRepository.findByAdminIdAndAppointmentTypeAndDateBefore(Long.parseLong(adminId), type, formattedDate);
           }

    public List<Appointment> getPendingAppointmentsByType(String adminId,String type) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByAdminIdAndAppointmentTypeAndStatus(Long.parseLong(adminId), type, "pending");
    }
    public List<Appointment> getCanceledAppointmentsByType(String adminId,String type) {
        // Assuming 'PENDING' is the status you are looking for
        return appointmentRepository.findByAdminIdAndAppointmentTypeAndStatus(Long.parseLong(adminId),type, "Deleted");
    }
    
    
    
    
    
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
     
    
    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }
    public Optional<Appointment> updateAppointmentStatusToInProgress(Long appointmentId) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            appointment.setStatus("progress"); // Set status to "In Progress"
            appointmentRepository.save(appointment); // Save the updated appointment
            return Optional.of(appointment);
        } else {
            return Optional.empty(); // Return empty optional if appointment not found
        }
    }
    
    public boolean deleteAppointmentbyUser(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            // Check if the appointment is in the future
            LocalDateTime appointmentDateTime = LocalDateTime.parse(appointment.getDate() + " " + appointment.getTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (currentDateTime.isBefore(appointmentDateTime)) {
                // Cancel the appointment and update slotBook to false
                appointment.setStatus("cancel");
                appointmentRepository.save(appointment);

                // Update the slotBook to false in SmallerTimeSlot entity
                SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
              smallerTimeSlot.setAppointment(null);
                smallerTimeSlot.setSlotBook(false);
                smallerTimeSlotRepository.save(smallerTimeSlot);
                adminNotificationService.sendCancelAppointmentNotificationToAdmin(appointment,appointment.getUser());
                
                return true;
            } else {
                // Appointment time has passed, cannot cancel
            	  appointment.setStatus("cancel");
            	  adminNotificationService.sendCancelAppointmentNotificationToAdmin(appointment,appointment.getUser());
                  
                  appointmentRepository.save(appointment);

                return true;
            }
            
        }
        return false; // Appointment not found
    }
    public boolean rescheduledAppointmentbyUser(Long appointmentId,String newTime) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        String oldTime="";
        if (optionalAppointment.isPresent()) {
        	 oldTime=optionalAppointment.get().getTime();
            Appointment appointment = optionalAppointment.get();
            Optional<SmallerTimeSlot> smallerTime= smallerTimeSlotRepository.findByDateAndStartTimeAndAvailabilityAndAdminId(appointment.getDate(), newTime, appointment.getAppointmentType(), appointment.getAdmin().getId());

            
          
            
            if(smallerTime.isPresent())
            	{
            	 if(smallerTime.get().isSlotBook()) {
            		 return false;
            	 }
            	 else {
            		  SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
                      smallerTimeSlot.setAppointment(null);
                      smallerTimeSlot.setSlotBook(false);
                      smallerTimeSlotRepository.save(smallerTimeSlot);
                      
            		 SmallerTimeSlot smallerTimeS=smallerTime.get();
                 	smallerTimeS.setSlotBook(true);

         	        // Set the appointment for the smaller time slot
         	        smallerTimeS.setAppointment(appointment);

         	        // Save the updated smaller time slot (this cascades to appointment due to the relationship)
         	        smallerTimeSlotRepository.save(smallerTimeS);
         	       appointment.setTime(newTime);
                    appointmentRepository.save(appointment);

                    // Delete the appointment
                   
                    // Update the slotBook to false in SmallerTimeSlot entity
                   

                    // Send notification to the user about the cancellation
                    adminNotificationService.sendRescheduleAppointmentNotificationToAdmin(appointment, appointment.getUser(),oldTime);

                    return true;
            	 }
            	
    	        
            	}
            else {
            	 return false;      
            }
            
           
        }

        return false; // Appointment not found
    }
    public boolean cancelAppointmentbyAdmin(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            // Check if the appointment is in the future
            LocalDateTime appointmentDateTime = LocalDateTime.parse(appointment.getDate() + " " + appointment.getTime(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma"));
            LocalDateTime currentDateTime = LocalDateTime.now();

            if (currentDateTime.isBefore(appointmentDateTime)) {
                // Cancel the appointment and update slotBook to false
                appointment.setStatus("cancel");
                appointmentRepository.save(appointment);

                // Update the slotBook to false in SmallerTimeSlot entity
                SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
                smallerTimeSlot.setSlotBook(false);
                smallerTimeSlotRepository.save(smallerTimeSlot);
                userNotificationService.sendCancelAppointmentNotificationToUser(appointment,appointment.getUser());
                
                return true;
            } else {
                // Appointment time has passed, cannot cancel
            	  appointment.setStatus("cancel");
            	  userNotificationService.sendCancelAppointmentNotificationToUser(appointment,appointment.getUser());
                  
                  appointmentRepository.save(appointment);

                return true;
            }
            
        }
        return false; // Appointment not found
    }
    public boolean deleteAppointmentbyAdmin(Long appointmentId) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus("cancel");
            appointmentRepository.save(appointment);

            // Delete the appointment
           
            // Update the slotBook to false in SmallerTimeSlot entity
            SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
            smallerTimeSlot.setSlotBook(false);
            smallerTimeSlot.setAppointment(null);
            smallerTimeSlotRepository.save(smallerTimeSlot);

            // Send notification to the user about the cancellation
            userNotificationService.sendDeleteAppointmentNotificationToUser(appointment, appointment.getUser());

            return true;
        }

        return false; // Appointment not found
    }
    
    
    public boolean rescheduledAppointmentbyAdmin(Long appointmentId,String newTime) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
        String oldTime="";
        if (optionalAppointment.isPresent()) {
        	 oldTime=optionalAppointment.get().getTime();
            Appointment appointment = optionalAppointment.get();
            Optional<SmallerTimeSlot> smallerTime= smallerTimeSlotRepository.findByDateAndStartTimeAndAvailabilityAndAdminId(appointment.getDate(), newTime, appointment.getAppointmentType(), appointment.getAdmin().getId());

            
          
            
            if(smallerTime.isPresent())
            	{
            	 if(smallerTime.get().isSlotBook()) {
            		 return false;
            	 }
            	 else {
            		  SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
                      smallerTimeSlot.setAppointment(null);
                      smallerTimeSlot.setSlotBook(false);
                      smallerTimeSlotRepository.save(smallerTimeSlot);
                      
            		 SmallerTimeSlot smallerTimeS=smallerTime.get();
                 	smallerTimeS.setSlotBook(true);

         	        // Set the appointment for the smaller time slot
         	        smallerTimeS.setAppointment(appointment);
         	        appointment.setTime(newTime);
         	        // Save the updated smaller time slot (this cascades to appointment due to the relationship)
         	        smallerTimeSlotRepository.save(smallerTimeS);
            	 }
            	
    	        
            	}
            else {
            	appointment.setTime(newTime);
            	  SmallerTimeSlot smallerTimeSlot = appointment.getSmallerTimeSlot();
                  smallerTimeSlot.setAppointment(null);
                  smallerTimeSlot.setSlotBook(false);
                  smallerTimeSlotRepository.save(smallerTimeSlot);
            	
            	  TimeSlot timeSlot = new TimeSlot();
      	        timeSlot.setDate(appointment.getDate());
      	        timeSlot.setAvailability(appointment.getAppointmentType());
      	        timeSlot.setAdmin(appointment.getAdmin());
      	      SmallerTimeSlot smallerTimeSlot1 = new SmallerTimeSlot();
      	    List<SmallerTimeSlot> smallerTimeSlots = new ArrayList<>();
      	    LocalTime startTime = LocalTime.parse(newTime, DateTimeFormatter.ofPattern("hh:mma"));
	          
      	    LocalTime slotEndTime = startTime.plusMinutes(15);
              smallerTimeSlot1.setStartTime(startTime.format(DateTimeFormatter.ofPattern("hh:mma")));
              smallerTimeSlot1.setEndTime(slotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
              smallerTimeSlot1.setTimeSlot(timeSlot);
              smallerTimeSlot1.setAdmin(appointment.getAdmin());
              smallerTimeSlot1.setAppointment(appointment);
              smallerTimeSlots.add(smallerTimeSlot1);
              timeSlot.setSmallerTimeSlots(smallerTimeSlots);
              List<TimeSlotDetail> timeSlotDetails=new ArrayList<>();
              TimeSlotDetail t=new TimeSlotDetail();
              t.setAddress(appointment.getSmallerTimeSlot().getTimeSlot().getTimeSlotDetails().get(0).getAddress());
  	    	timeSlot.setTimeSlotDetails(timeSlotDetails);
  	        timeSlotRepository.save(timeSlot);
  	      
            }
            
            
            appointmentRepository.save(appointment);

            // Delete the appointment
           
            // Update the slotBook to false in SmallerTimeSlot entity
           

            // Send notification to the user about the cancellation
            userNotificationService.sendRescheduleAppointmentNotificationToUser(appointment, appointment.getUser(),oldTime);

            return true;
        }

        return false; // Appointment not found
    }
    // other methods...
}
