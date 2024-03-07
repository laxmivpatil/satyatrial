package com.techverse.satya.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.DTO.EditUser;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.JWTRequestAdmin;
import com.techverse.satya.Model.JWTRequestUser;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Security.JwtHelper;

@Service
public class UserService {
	private final UserRepository userRepository; 
	@Autowired
    private StorageService service;
	@Autowired
    private AdminRepository adminRepository;
	 @Autowired
	    private PasswordEncoder passwordEncoder;

	     
	  @Autowired
	    private JwtHelper jwtHelper;
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public Users createUser(JWTRequestUser jwt) {
       Users user = new Users();
      // admin.setUsername(username);
   //    admin.setPassword(passwordEncoder.encode(password));
       if(jwt.getMobileNo().matches("^\\d{10}$"))
       {
    	   user.setPhoneNumber(jwt.getMobileNo());
       }
       else {
    	   user.setEmail(jwt.getMobileNo());
       }
      
       user.setOtp(passwordEncoder.encode(jwt.getOtp()));
          user.setName(jwt.getName());
       user.setGender(jwt.getGender());
       user.setRole("ROLE_USER");
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        // Format the current date and print it
        String formattedDate = currentDate.format(formatter);
        user.setJoineddate(formattedDate);
       return userRepository.save(user);
   }
	 
	public Optional<Users> findByPhoneNumberOrEmail(String phoneNumberOrEmail) {
    	 
		
        return userRepository.findByPhoneNumberOrEmail(phoneNumberOrEmail);
    }
	
	/****get Username from token*/
	 public Optional<Users> getUserByToken(String token) {
	    	String mobileNoOrEmail=jwtHelper.getUsernameFromToken(token);
		//	 System.out.println("hi "+userName);
			
	        return userRepository.findByPhoneNumberOrEmail(mobileNoOrEmail);
	    }

	 public void deleteUserById(Long id) {
	        userRepository.deleteById(id);
	  }

	  public Optional<Users> getUserById(Long id) {
	        return userRepository.findById(id);
   }
	public Optional<Users> findByPhoneNumber(String phoneNumber) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}
	public Optional<Users> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	
	public boolean editUser(EditUser editUser)
	{
		Optional<Users> optionalUser = userRepository.findById( editUser.getUserId());
	if (optionalUser.isPresent()) {
		Users user = optionalUser.get();

		// Update the user information
		 if(!StringUtils.isEmpty(editUser.getEmail()))
		 {
			 user.setEmail(editUser.getEmail());
		 }
		 // Update the user information
		 if(!StringUtils.isEmpty(editUser.getPhoneNumber()))
		 {
			 user.setPhoneNumber(editUser.getPhoneNumber());	
		 }
		 if(!StringUtils.isEmpty(editUser.getName()))
		 {
			 user.setName(editUser.getName());
		 }
		 if(!StringUtils.isEmpty(editUser.getOccupation()))
		 {
			 user.setOccupation(editUser.getOccupation());
		 }if(!StringUtils.isEmpty(editUser.getQualification()))
		 {
			 user.setQualification(editUser.getQualification());
		 }
		 if(!StringUtils.isEmpty(editUser.getAddress()))
		 {
			 user.setAddress(editUser.getAddress());
		 }
		 if(!StringUtils.isEmpty(editUser.getProfilePhoto()))
		 {
			user.setProfilePphoto(editUser.getProfilePhoto());
		 }
		  


		// Save the updated user to the database
		userRepository.save(user);
		 
		return true;
	} else {
		// Handle the case when the user with the given ID is not found
		return false;
	}
	}
	public Optional<Users> updateUserInformation(Long userId, String address, String pincode, String city,
			String constitution, String wardNo, String state,String photo,Long adminId) {
		// Retrieve the user from the database by userId
	
		Optional<Users> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			Users user = optionalUser.get();

			// Update the user information
			user.setAddress(address);
			user.setPincode(pincode);
			user.setCity(city);
			user.setConstitution(constitution);
			user.setWardNo(wardNo);
			user.setState(state);
			user.setProfilePphoto(photo);
			user.setAdmin(adminRepository.findById(adminId).get());
			
			 user.setRole("ROLE_USER");
			

			// Save the updated user to the database
			userRepository.save(user);
			System.out.println(userId);
			return optionalUser;
		} else {
			// Handle the case when the user with the given ID is not found
			return null;
		}
	}

	
	
	 public long getCountOfAppointmentsForUserInCurrentMonth(Long userId) {
	        // Get the current month and year
	        LocalDate currentDate = LocalDate.now();
	        int currentMonth = currentDate.getMonthValue();
	        int currentYear = currentDate.getYear();

	        // Call the repository method to get the count of appointments for the user in the current month
	        return userRepository.countAppointmentsByMonthAndUser(userId, currentMonth, currentYear);
	    }
	/*private String uploadDir="F:\\MyProject\\SatyaApp\\Files\\";

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
           /* if (fileName.contains("..")) {
                throw new IOException("Invalid file name: " + fileName);
            }
*/
            // Copy file to the target location
        /*    String filePath = uploadDir + File.separator + fileName;
            File targetFile = new File(filePath);
            file.transferTo(targetFile);

            return filePath;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file: " + fileName, ex);
        }
    }
    
*/
}
