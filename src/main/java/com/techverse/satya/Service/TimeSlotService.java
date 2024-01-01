package com.techverse.satya.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.techverse.satya.DTO.TimeSlotDetail;
import com.techverse.satya.DTO.TimeSlotDetailDto;
import com.techverse.satya.DTO.TimeSlotRequest;
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
	private SmallerTimeSlotRepository smallerTimeSlotRepository;
	 private final DateTimeFormatter dateFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	 
	@Transactional
	public String createTimeSlot(List<String> dates, List<TimeSlotDetail> timeSlotDetails, String availability, Admin admin) {
	    for (String date : dates) {
	        TimeSlot timeSlot = new TimeSlot();
	        timeSlot.setDate(date);
	        timeSlot.setAvailability(availability);
	        timeSlot.setAdmin(admin);

	        List<SmallerTimeSlot> smallerTimeSlots = new ArrayList<>();

	        for (TimeSlotDetail detail : timeSlotDetails) {
	            LocalTime startTime = LocalTime.parse(detail.getStartTime(), DateTimeFormatter.ofPattern("hh:mma"));
	            LocalTime endTime = LocalTime.parse(detail.getEndTime(), DateTimeFormatter.ofPattern("hh:mma"));
	         //   System.out.println(timeSlot.getDate()+" "+ startTime +" "+ endTime+" "+ admin.getId());
	            List<TimeSlotDetail> overlappingTimeSlots = isOverlapping(timeSlot.getDate(), startTime, endTime, admin);
	            if (!overlappingTimeSlots.isEmpty()) {
	                String overlappingSlotsMessage = "Overlapping time slots found:\n ";
	                for (int i = 0; i < overlappingTimeSlots.size(); i++) {
	                    TimeSlotDetail t = overlappingTimeSlots.get(i);
	                    overlappingSlotsMessage +=timeSlot.getDate()+"===>"+ t.getStartTime() + " to " + t.getEndTime();
	                    if (i < overlappingTimeSlots.size() - 1) {
	                        overlappingSlotsMessage += ", ";
	                    }
	                }
	                return overlappingSlotsMessage;
	            }

	            while (startTime.isBefore(endTime)) {
	                LocalTime slotEndTime = startTime.plusMinutes(15);
	                if (slotEndTime.isAfter(endTime)) {
	                    slotEndTime = endTime;
	                }

	                SmallerTimeSlot smallerTimeSlot = new SmallerTimeSlot();
	                smallerTimeSlot.setStartTime(startTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                smallerTimeSlot.setEndTime(slotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                smallerTimeSlot.setTimeSlot(timeSlot);
	                smallerTimeSlot.setAdmin(admin);
	                smallerTimeSlots.add(smallerTimeSlot);
	                startTime = slotEndTime;
	            }
	        }

	        timeSlot.setSmallerTimeSlots(smallerTimeSlots);
	    	timeSlot.setTimeSlotDetails(timeSlotDetails);
	        timeSlotRepository.save(timeSlot);
	    }

	    return "Successfully created";
	}

	private List<TimeSlotDetail> isOverlapping(String date, LocalTime newStartTime, LocalTime newEndTime, Admin admin) {
	     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
	     String x=newStartTime.format(formatter);
	   
		List<Object[]> results = smallerTimeSlotRepository.findOverlappingTimeSlots(date, newStartTime.format(formatter), newEndTime.format(formatter) , admin.getId());
	  
		List<TimeSlotDetail> overlappingTimeSlots = new ArrayList<>();

	    for (Object[] result : results) {
	    	System.out.println(date + " " + x + "vbcbb " + newEndTime.format(formatter) + " " + admin.getId());

	        String startTimeFromDB = (String) result[0];
	        String endTimeFromDB = (String) result[1];
	        LocalTime dbStartTime = LocalTime.parse(startTimeFromDB, DateTimeFormatter.ofPattern("hh:mma"));
	        LocalTime dbEndTime = LocalTime.parse(endTimeFromDB, DateTimeFormatter.ofPattern("hh:mma"));

	       /* // Check for overlap
	        if ((newStartTime.isAfter(dbStartTime) && newStartTime.isBefore(dbEndTime)) ||
	            (newEndTime.isAfter(dbStartTime) && newEndTime.isBefore(dbEndTime)) ||
	            (newStartTime.isBefore(dbStartTime) && newEndTime.isAfter(dbEndTime))) {
*/
	            TimeSlotDetail overlappingSlot = new TimeSlotDetail();
	            overlappingSlot.setStartTime(startTimeFromDB);
	            overlappingSlot.setEndTime(endTimeFromDB);
	            overlappingTimeSlots.add(overlappingSlot);
	        //}
	    }

	    return overlappingTimeSlots;
	}

	@Transactional
	public String rescheduledTimeSlot(String date, List<TimeSlotDetail> timeSlotDetails, String availability, Admin admin) {
	    
	        TimeSlot timeSlot = new TimeSlot();
	        timeSlot.setDate(date);
	        timeSlot.setAvailability(availability);
	        timeSlot.setAdmin(admin);

	        List<SmallerTimeSlot> smallerTimeSlots = new ArrayList<>();

	        for (TimeSlotDetail detail : timeSlotDetails) {
	            LocalTime startTime = LocalTime.parse(detail.getStartTime(), DateTimeFormatter.ofPattern("hh:mma"));
	            LocalTime endTime = LocalTime.parse(detail.getEndTime(), DateTimeFormatter.ofPattern("hh:mma"));
	         //   System.out.println(timeSlot.getDate()+" "+ startTime +" "+ endTime+" "+ admin.getId());
	            List<TimeSlotDetail> overlappingTimeSlots = isOverlapping(timeSlot.getDate(), startTime, endTime, admin);
	            if (!overlappingTimeSlots.isEmpty()) {
	                String overlappingSlotsMessage = "Overlapping time slots found:\n ";
	                for (int i = 0; i < overlappingTimeSlots.size(); i++) {
	                    TimeSlotDetail t = overlappingTimeSlots.get(i);
	                    overlappingSlotsMessage +=timeSlot.getDate()+"===>"+ t.getStartTime() + " to " + t.getEndTime();
	                    if (i < overlappingTimeSlots.size() - 1) {
	                        overlappingSlotsMessage += ", ";
	                    }
	                }
	                return overlappingSlotsMessage;
	            }

	            while (startTime.isBefore(endTime)) {
	                LocalTime slotEndTime = startTime.plusMinutes(15);
	                if (slotEndTime.isAfter(endTime)) {
	                    slotEndTime = endTime;
	                }

	                SmallerTimeSlot smallerTimeSlot = new SmallerTimeSlot();
	                smallerTimeSlot.setStartTime(startTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                smallerTimeSlot.setEndTime(slotEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
	                smallerTimeSlot.setTimeSlot(timeSlot);
	                smallerTimeSlot.setAdmin(admin);
	                smallerTimeSlots.add(smallerTimeSlot);
	                startTime = slotEndTime;
	            }
	        }

	        timeSlot.setSmallerTimeSlots(smallerTimeSlots);
	    	timeSlot.setTimeSlotDetails(timeSlotDetails);
	        timeSlotRepository.save(timeSlot);
	    

	    return "Successfully created";
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
	public List<TimeSlotDetailDto> getTimeSlotsByMonthYear(int year, int month) {
	    // Fetch time slots by year and month from the repository
	    List<TimeSlot> timeSlots = timeSlotRepository.findAll();

	    // Filter time slots by year and month
	    List<TimeSlot> filteredTimeSlots = timeSlots.stream()
	            .filter(timeSlot -> {
	                LocalDate localDate = LocalDate.parse(timeSlot.getDate(), dateFormatter1);
	                return localDate.getYear() == year && localDate.getMonthValue() == month;
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
    
    
    /*
    public void deleteTimeSlotById(Long timeSlotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElse(null);
        System.out.println("hi"+timeSlot.getId());
        if (timeSlot != null) {
            List<SmallerTimeSlot> smallerTimeSlots = timeSlot.getSmallerTimeSlots();
            for (SmallerTimeSlot smallerTimeSlot : smallerTimeSlots) {
                if (smallerTimeSlot.isSlotBook()) {
                    // Send notification to user about deletion and update appointment status to "deleted"
                    // Implement the notification logic here
                	System.out.println("Notification Send");
                    // Set appointment status to "deleted"
                    smallerTimeSlot.getAppointment().setStatus("deleted");
                }
                smallerTimeSlotRepository.delete(smallerTimeSlot);
            }
            timeSlotRepository.delete(timeSlot);
        }
    }
	*/
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
    public void processSmallerTimeSlots(List<SmallerTimeSlot> smallerTimeSlots) {
        for (SmallerTimeSlot smallerTimeSlot : new ArrayList<>(smallerTimeSlots)) {
            if (smallerTimeSlot.isSlotBook()) {
                System.out.println("Notification Sent");
                smallerTimeSlot.getAppointment().setStatus("deleted");
                smallerTimeSlot.setAppointment(null);
                smallerTimeSlot.setTimeSlot(null);
                deletes(smallerTimeSlot);
            }
        }
    }
    @Transactional
    public void deletes(SmallerTimeSlot timeSlot) {
    	 smallerTimeSlotRepository.delete(timeSlot);
    }
    

}
