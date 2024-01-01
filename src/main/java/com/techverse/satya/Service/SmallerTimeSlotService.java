package com.techverse.satya.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.SmallerTimeSlot;
import com.techverse.satya.Repository.SmallerTimeSlotRepository;

@Service
public class SmallerTimeSlotService {

    @Autowired
    private SmallerTimeSlotRepository smallerTimeSlotRepository;

    public List<SmallerTimeSlot> getSmallerTimeSlotsByAdminIdAndDateAndAvailability(Long adminId, String date, String availability, boolean slotBook) {
    	      // Call the repository method to fetch smaller time slots based on date and availability
    	List<SmallerTimeSlot> smallerTimeSlots = smallerTimeSlotRepository.findByTimeSlotDateAndTimeSlotAvailabilityAndAdminIdAndSlotBook(date, availability, adminId, slotBook);
        // Filter the result based on slotBook status
        return smallerTimeSlots.stream()
                .filter(smallerTimeSlot -> smallerTimeSlot.isSlotBook() == slotBook)
                .collect(Collectors.toList());
    }
    public List<SmallerTimeSlot> getAllSlots() {
        return smallerTimeSlotRepository.findAll();
    }
    public List<SmallerTimeSlot> getAllSlotsByTimeSlot(Long timeSlotId) {
        return smallerTimeSlotRepository.findByTimeSlotId(timeSlotId);
    }

    public List<SmallerTimeSlot> getAllSlotsByAdmin(Long adminId) {
        return smallerTimeSlotRepository.findByAdminId(adminId);
    }
    // Other service methods...
}

