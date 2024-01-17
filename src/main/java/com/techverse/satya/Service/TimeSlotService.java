package com.techverse.satya.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
 
import java.util.List;
import java.util.Optional; 
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

 
import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.DTO.TimeSlotDetailDto;
 
import com.techverse.satya.Model.Admin; 
import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Model.TimeSlot; 
import com.techverse.satya.Repository.SmallerTimeSlotRepository;
import com.techverse.satya.Repository.TimeSlotRepository;

@Service
public class TimeSlotService {

	@Autowired
	private TimeSlotRepository timeSlotRepository;
	
	 
	@Autowired
	UserNotificationService userNotificationService;

	 
	@Autowired
	private SmallerTimeSlotRepository smallerTimeSlotRepository;
	 private final DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 @Autowired
	 EntityManager entityManager;  // Inject the EntityManager

	 
	 
	 @Transactional
		public String createTimeSlot(List<String> dates, List<TimeSlotDetail> timeSlotDetails, String availability, Admin admin) {
		 String overlappingSlotsMessage = "Overlapping time slots found:\n ";
		 int flag=0;
         
		 for (String date : dates) {
			 for (TimeSlotDetail detail : timeSlotDetails) {
			        
			 List<TimeSlotDetail> overlappingTimeSlots = isOverlapping(date, detail.getStartTime(), detail.getEndTime(), admin);
			 if (!overlappingTimeSlots.isEmpty()) {
				 flag=1;
	                    for (int i = 0; i < overlappingTimeSlots.size(); i++) {
	                    //TimeSlotDetail t = overlappingTimeSlots.get(i);
	                    overlappingSlotsMessage +=date+"===>"+ overlappingTimeSlots.get(i).getStartTime()+ " to " + overlappingTimeSlots.get(i).getEndTime();
	                    if (i < overlappingTimeSlots.size() - 1) {
	                        overlappingSlotsMessage += ", ";
	                    }
	                }
	                 
	            }
			 }
		 }
		 if(flag==1)
		 {
			 System.out.println(overlappingSlotsMessage);
			 return overlappingSlotsMessage;
		 }
		 else {
			 for (String date : dates) {
			        System.out.println(date);
			        
			        for (TimeSlotDetail detail1 : timeSlotDetails) {
			        	TimeSlotDetail detail=new TimeSlotDetail(detail1.getStartTime(),detail1.getEndTime(),detail1.getAddress());
			            LocalTime startTime = LocalTime.parse(detail.getStartTime(), DateTimeFormatter.ofPattern("hh:mma"));
			            LocalTime endTime = LocalTime.parse(detail.getEndTime(), DateTimeFormatter.ofPattern("hh:mma"));
			         //   System.out.println(timeSlot.getDate()+" "+ startTime +" "+ endTime+" "+ admin.getId());
			           // List<TimeSlotDetail> overlappingTimeSlots = isOverlapping(date, startTime, endTime, admin);
			            TimeSlot timeSlot = new TimeSlot();
				        timeSlot.setDate(date);
				        timeSlot.setAvailability(availability);
				        timeSlot.setAdmin(admin);
				        List<SmallerTimeSlot> smallerTimeSlots = new ArrayList<>();

			            while (startTime.isBefore(endTime)) {
			                LocalTime slotEndTime = startTime.plusMinutes(15);
			                
			                if (slotEndTime.isAfter(endTime)) {
			                    break; // Stop generating slots if the remaining time is less than 15 minutes
			                }
			              /*  if (slotEndTime.isAfter(endTime)) {
			                    slotEndTime = endTime;
			                }*/

			                SmallerTimeSlot smallerTimeSlot = new SmallerTimeSlot();
			                smallerTimeSlot.setStartTime(startTime.format(DateTimeFormatter.ofPattern("hh:mma")));
			                smallerTimeSlot.setEndTime(slotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
			                smallerTimeSlot.setTimeSlot(timeSlot);
			                smallerTimeSlot.setAdmin(admin);
			                smallerTimeSlots.add(smallerTimeSlot);
			                startTime = slotEndTime;
			            }
			        
			            timeSlot.setSmallerTimeSlots(smallerTimeSlots);
			            List<TimeSlotDetail> list=new ArrayList<>();
			            list.add(detail);
				    	timeSlot.setTimeSlotDetails(list);
				        timeSlotRepository.save(timeSlot);
				        entityManager.flush();
			        }
		 }
		 }
		 
		 
		    return "Successfully created";
		}
	 
	 private List<TimeSlotDetail> isOverlapping(String date, String newStartTime, String newEndTime, Admin admin) {
			List<TimeSlotDetail> overlappingDetails = timeSlotRepository.findOverlappingTimeSlotDetails(admin, date,newStartTime, newEndTime);

			   return overlappingDetails;
			}
	 private List<TimeSlotDetail> isOverlapping1(String date, String newStartTime, String newEndTime, Admin admin,Long id) {
			List<TimeSlotDetail> overlappingDetails = timeSlotRepository.findOverlappingTimeSlotDetails1(admin, date,newStartTime, newEndTime,id);

			   return overlappingDetails;
			}
	public boolean doTimeRangesOverlap(LocalTime rangeStart, LocalTime rangeEnd, LocalTime slotStart, LocalTime slotEnd) {
	    return !slotStart.isBefore(rangeStart) && !slotEnd.isAfter(rangeEnd);
	}



	 @Transactional
	    public String rescheduledTimeSlot(Long timeSlotId, Admin admin,String startTime, String endTime) {
		String  overlappingSlotsMessage="Overlapping slots====>";
		 Optional<TimeSlot> timeSlot = timeSlotRepository.findById(timeSlotId);

			LocalTime startTime1 = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("hh:mma"));
			LocalTime endTime1 = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("hh:mma"));

			if (timeSlot.isPresent()) {
				//check for overlapping
				 List<TimeSlotDetail> overlappingTimeSlots = isOverlapping1(timeSlot.get().getDate(), startTime,endTime, admin,timeSlot.get().getId());
				 if (!overlappingTimeSlots.isEmpty()) {
					 
		                    for (int i = 0; i < overlappingTimeSlots.size(); i++) {
		                    //TimeSlotDetail t = overlappingTimeSlots.get(i);
		                    overlappingSlotsMessage +=timeSlot.get().getDate()+"===>"+ overlappingTimeSlots.get(i).getStartTime()+ " to " + overlappingTimeSlots.get(i).getEndTime();
		                    if (i < overlappingTimeSlots.size() - 1) {
		                        overlappingSlotsMessage += ", ";
		                    }
		                }
		                    return overlappingSlotsMessage;
		                 
		            }
				 
				
				
				
				
				//end
				List<SmallerTimeSlot> list = timeSlot.get().getSmallerTimeSlots();
				List<SmallerTimeSlot> list1 = new ArrayList<>();

				for (SmallerTimeSlot s : new ArrayList<>(list)) {
					LocalTime dbStartTime = LocalTime.parse(s.getStartTime(), DateTimeFormatter.ofPattern("hh:mma"));
					LocalTime dbEndTime = LocalTime.parse(s.getEndTime(), DateTimeFormatter.ofPattern("hh:mma"));
					System.out.println(startTime1+" "+ endTime1+" "+ dbStartTime+" " +dbEndTime+(doTimeRangesOverlap(startTime1, endTime1, dbStartTime, dbEndTime)));
					if (doTimeRangesOverlap(startTime1, endTime1, dbStartTime, dbEndTime)) {
						list1.add(s);
						System.out.println("Appointment Book and need not cancel=>" + s.getId() + "=>" + dbStartTime + " "
								+ dbEndTime);
					} else if (!doTimeRangesOverlap(startTime1, endTime1, dbStartTime, dbEndTime)) {
						 System.out.println("hi deltete"+(doTimeRangesOverlap(startTime1, endTime1, dbStartTime, dbEndTime)));
						processSmallerTimeSlot(s);

					}  
				}
				
	          
	            List<SmallerTimeSlot> newSmallerTimeSlots = new ArrayList<>();
	           LocalTime slotEndTime =  startTime1;

	            while (slotEndTime.isBefore(endTime1)) {
	                LocalTime currentSlotEndTime = slotEndTime.plusMinutes(15);
	                if (currentSlotEndTime.isAfter(endTime1)) {
	                    currentSlotEndTime = endTime1;
	                }

	                boolean overlapExists = false;

	                for (SmallerTimeSlot s : list1) {
	                     System.out.println(slotEndTime +"   "+ currentSlotEndTime );
	                    if (doTimeRangesOverlap(slotEndTime, currentSlotEndTime,LocalTime.parse(s.getStartTime(), DateTimeFormatter.ofPattern("hh:mma")),LocalTime.parse(s.getEndTime(), DateTimeFormatter.ofPattern("hh:mma")))) {
	                       System.out.println("hi exist   ");
	                    	overlapExists = true;
	                        break; // Exit the loop once an overlap is found
	                    }
	                }
	                if (!overlapExists) {
	                    System.out.println("No overlap for start time " + slotEndTime);
	                    SmallerTimeSlot newSmallerTimeSlot = new SmallerTimeSlot();
	                    newSmallerTimeSlot.setStartTime(slotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                    newSmallerTimeSlot.setEndTime(currentSlotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                    newSmallerTimeSlot.setTimeSlot(timeSlot.get());
	                    newSmallerTimeSlot.setAdmin(admin);
	                    newSmallerTimeSlots.add(newSmallerTimeSlot);
	                }

	                slotEndTime=currentSlotEndTime;
	            }

	            timeSlot.get().getSmallerTimeSlots().addAll(newSmallerTimeSlots);
	              timeSlot.get().getTimeSlotDetails().get(0).setStartTime(startTime );
	              timeSlot.get().getTimeSlotDetails().get(0).setEndTime(endTime );
	              
	            timeSlotRepository.save(timeSlot.get());
 
	            return "Successfully created";
	        } else {
	        	System.out.println("shgjsjgfjsdgjgfj");
	            return "Time slot not found";
	        }
	    }

	 
	 
	public Optional<TimeSlot> getTimeSlotById(Long id) {
		return timeSlotRepository.findById(id);
	}
	public List<TimeSlotDetailDto> getAllTimeSlots() {
        // Fetch all time slots from the repository
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();

        // Convert TimeSlot entities to TimeSlotDetailDto objects using Java 8 stream API
        return timeSlots.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
	 
	public List<TimeSlotDetailDto> getTimeSlotsByMonthYear(int year, int month, Long adminId) {
	    // Fetch time slots by year and month from the repository
	    List<TimeSlot> timeSlots = timeSlotRepository.findAll();

	    // Filter time slots by year, month, and adminId
	    List<TimeSlot> filteredTimeSlots = timeSlots.stream()
	            .filter(timeSlot -> {
	                LocalDate localDate = LocalDate.parse(timeSlot.getDate(), dateFormatter1);

	                // Add condition for year and month
	                boolean isMatchingDate = localDate.getYear() == year && localDate.getMonthValue() == month;

	                // Add condition for adminId
	                boolean isAdminIdMatch = adminId.equals(timeSlot.getAdmin().getId());  // Adjust as per your entity structure

	                return isMatchingDate && isAdminIdMatch;
	            })
	            .collect(Collectors.toList());

	    // Convert TimeSlot entities to TimeSlotDetailDto objects using Java 8 stream API
	    return filteredTimeSlots.stream()
	            .map(this::convertToDto)
	            .collect(Collectors.toList());
	}

	     
    // Convert TimeSlot entity to TimeSlotDetailDto
    private TimeSlotDetailDto convertToDto(TimeSlot timeSlot) {
        return new TimeSlotDetailDto(
                timeSlot.getId(),
                timeSlot.getAvailability(),
                timeSlot.getDate(),
                timeSlot.getTimeSlotDetails()
        );
    }
    
    
    
    @Transactional
    public void processSmallerTimeSlots(List<SmallerTimeSlot> smallerTimeSlots) {
        for (SmallerTimeSlot smallerTimeSlot : new ArrayList<>(smallerTimeSlots)) {
            if ( smallerTimeSlot.isSlotBook()) {
            	 userNotificationService.sendCancelAppointmentNotificationToUser(smallerTimeSlot.getAppointment(),smallerTimeSlot.getAppointment().getUser());
                   
                System.out.println("Notification Sent");
                smallerTimeSlot.getAppointment().setStatus("cancel");
                smallerTimeSlot.setAppointment(null);
                smallerTimeSlot.setTimeSlot(null);
                deletes(smallerTimeSlot);
            }
            else
            {
            	  smallerTimeSlot.setTimeSlot(null);
                  deletes(smallerTimeSlot);
            }
        }
    }
    @Transactional
    public void processSmallerTimeSlot(SmallerTimeSlot smallerTimeSlot) {
               System.out.println("Notification Sent");
            
               if(smallerTimeSlot.isSlotBook()) {
            	   userNotificationService.sendCancelAppointmentNotificationToUser(smallerTimeSlot.getAppointment(),smallerTimeSlot.getAppointment().getUser());
                  	
                smallerTimeSlot.getAppointment().setStatus("cancel");
                smallerTimeSlot.setAppointment(null);
                smallerTimeSlot.setTimeSlot(null);
                deletes1(smallerTimeSlot);
                
               }
               else {
            	   smallerTimeSlot.setTimeSlot(null);
                   deletes1(smallerTimeSlot);
               }
                
                
          
    }
    
    @Transactional
    public void deleteTimeSlotById(Long timeSlotId) {
        try {
            TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElse(null);

            if (timeSlot != null) {
                processSmallerTimeSlots(timeSlot.getSmallerTimeSlots());
                 timeSlotRepository.delete(timeSlot);
            }

        } catch (EntityNotFoundException | JpaObjectRetrievalFailureException e) {
            // Log or handle the specific exception
            throw new RuntimeException("Error deleting time slot with ID " + timeSlotId, e);
        }
    }
    @Transactional
    public void deletes(SmallerTimeSlot timeSlot) {
    	 smallerTimeSlotRepository.delete(timeSlot);
    	 
    }
    @Transactional
    public void deletes1(SmallerTimeSlot timeSlot) {
    	 smallerTimeSlotRepository.deleteById(timeSlot.getId());
    	 
    }

}
