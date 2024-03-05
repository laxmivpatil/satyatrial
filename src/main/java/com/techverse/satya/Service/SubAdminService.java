package com.techverse.satya.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techverse.satya.DTO.SubAdminDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Repository.SubAdminRepository;
import com.techverse.satya.Security.JwtHelper;

@Service
public class SubAdminService {
	
	
	@Autowired
	SubAdminRepository subAdminRepository;
	
	@Autowired
	OtpService otpService;
	@Autowired
	EmailService emailService;
	
    @Autowired
    private JwtHelper jwtHelper;
	
    
    public List<SubAdminDTO> getSubAdminsByAdminId(Long adminId) {
    	List<SubAdmin> list=subAdminRepository.findByAdminId(adminId);
    	List<SubAdminDTO> subAdminDtos = list.stream()
                .map(subAdmin -> new SubAdminDTO(
                        subAdmin.getId(),
                        subAdmin.getMobileNumber(),
                        subAdmin.getEmail(),
                        subAdmin.getJoineddate(),
                        subAdmin.getName()                       
                ))
                .collect(Collectors.toList());
        return subAdminDtos;
    }

	/****get Username from token*/
	 public Optional<SubAdmin> getSubAdminByToken(String token) {
	    	String mobileNo=jwtHelper.getUsernameFromToken(token);
		 	 System.out.println("hi "+mobileNo);
			
	        return  subAdminRepository.findByMobileNumberOrEmail(mobileNo);
	    }
	
	 public void deleteSubAdminById(Long subAdminId) {
	        subAdminRepository.deleteById(subAdminId);
	    }
	public  boolean  createSubAdmin(String mobileNo,Admin admin)
	{
		
		boolean send=true;
		if(mobileNo.matches("^\\d{10}$")) {
			send=otpService.sendmessagetosubadmin(mobileNo);
		}
		else {
			 String playStoreUrl = "https://play.google.com/store/apps/details?id=com.techverse.satya";
		      String messageBody = "Welcome Subadmin to satya app please click on "+playStoreUrl +" to login into satya application" ;

			send=emailService.sendEmail(mobileNo, "SubAdmin Registration", messageBody);
		}
		if(send) {
		 SubAdmin  subAdmin=new SubAdmin();
		 if(mobileNo.matches("^\\d{10}$")) {
		 subAdmin.setMobileNumber(mobileNo);
		 }
		 else {
			 subAdmin.setEmail(mobileNo);
		 }
		 subAdmin.setAdmin(admin);
		 subAdmin.setRole("ROLE_SUBADMIN");
		 LocalDate currentDate = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
         // Format the current date and print it
         String formattedDate = currentDate.format(formatter);
         subAdmin.setJoineddate(formattedDate);
         subAdminRepository.save(subAdmin);
         return true;
		}
		else {
		 return false;
		}
	}
	
	   public Optional<SubAdmin> getSubAdminBymobileNo(String mobileNo) {
	        return subAdminRepository.findByMobileNumber(mobileNo);
	    }
	   public Optional<SubAdmin> getSubAdminBymobileNoOrEmail(String mobileNoOrEmail) {
	        return subAdminRepository.findByMobileNumberOrEmail(mobileNoOrEmail);
	    }
	    
}
