package com.techverse.satya.Controller;
 
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techverse.satya.DTO.AdminDTO;
import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.DTO.EditUser;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.UserDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Security.JwtHelper;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.OtpService;
import com.techverse.satya.Service.StorageService;
import com.techverse.satya.Service.SubAdminService;
import com.techverse.satya.Service.UserService;

import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("")
public class UserController {
	 private static final Logger logger = LoggerFactory.getLogger(UserController.class);


	Map<String, Object> responseBody = new HashMap<String, Object>();

	private final List<String> uniquePinCodes = Arrays.asList(
            "452006", "452016", "452005", "452007", "452013", "452001", "452002", "452010", "452015",
            "452018", "453115", "453551", "453111", "453441", "453001", "453220", "453771", "453661",
            "453556", "453331", "453446", "453552", "483119"
    );
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private StorageService service;
	
	@Autowired
	JwtHelper jwtHelper;
	
	   @Autowired
	    private AdminService adminService;
	    
	   @Autowired
	    private SubAdminService subAdminService;
	 

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	
	 
//final
  	@PostMapping("/user/update")
	public ResponseEntity<?> update(@RequestHeader("Authorization") String authorizationHeader, @RequestPart("address") String address,
	        @RequestPart("pincode") String pincode, @RequestPart("city") String city,
	        @RequestPart("constitution") String constitution, @RequestPart("wardNo") String wardNo,
	        @RequestPart("state") String state  , @RequestPart("adminId") String adminId ,@RequestPart(value="profilePhoto", required = false) MultipartFile file) {
	     ResponseDTO<Object> response = new ResponseDTO<>();
	     Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
	     
	      if(user.isPresent()) {

	     
	     
	    String photo1 ="file";
	    System.out.println("hidbngfhdsgfhgs");
	    try {
	        // Handle file upload
	        if (!file.isEmpty()) {
	            // Save the file to the server or process it as needed
	           photo1 = service.uploadFileOnAzure(file );
	            System.out.println(photo1);
	        }

	        Optional<Users> userOptional = userService.updateUserInformation(user.get().getId(), address, pincode, city, constitution, wardNo,
	                state, photo1,Long.parseLong(adminId));

	        if (userOptional.isPresent()) {
	            
	            UserDTO userDTO = new UserDTO(userOptional.get());
	            response.setStatus(true);
	            response.setMessage("User updated successfully");
	            response.setData(userDTO);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.setStatus(false);
	            response.setMessage("User Not Found");
	            response.setData(new String()); // Return an empty JSON object as data
	            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	        }
	    } catch (Exception e) {
	        response.setStatus(false);
	        response.setMessage("Failed to update user information");
	        response.setData(new String()); // Return an empty JSON object as data
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
     	  }
     	  else
     	  {
     		 response.setStatus(false);
	            response.setMessage("User Not Found");
	            response.setData(new String()); // Return an empty JSON object as data
	            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
     	  }
	}
//final
	@PostMapping("/user/edit")
	public ResponseEntity<?> edit(@RequestHeader("Authorization") String authorizationHeader,@RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
			@RequestPart(value = "name", required = false) String name,
			@RequestPart(value = "phoneNumber", required = false) String phoneNumber,
			@RequestPart(value = "email", required = false) String email,
			@RequestPart(value = "qualification", required = false) String qualification,
			@RequestPart(value = "occupation", required = false) String occupation,
	@RequestPart(value = "address", required = false) String address){
		String newToken="";
		   ResponseDTO<Object> responseBody = new ResponseDTO<>();
		   
		   Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
		   
	     	
	     	  if(user.isPresent()) {

try {
	String userName=jwtHelper.getUsernameFromToken(authorizationHeader.substring(7));
	String str="";
		if(!user.get().getEmail().equals(email) && email!=null) {
			if(userService.findByEmail(email).isPresent()||adminService.getAdminByEmail(email).isPresent()||subAdminService.getSubAdminBymobileNoOrEmail(email).isPresent())
			{
				str="Email allready Registered  please enter another email";
			}
			
		}
		if(!user.get().getPhoneNumber().equals(phoneNumber) && phoneNumber!=null) {
			if(userService.findByPhoneNumber(phoneNumber).isPresent()||adminService.getAdminBymobileNo(phoneNumber).isPresent()||subAdminService.getSubAdminBymobileNo(phoneNumber).isPresent()) {
				if(str.isEmpty())
					str="phone number allready registered please enter another no";
				else
					str="Email and phone number allready registered ";
			}
			
		}
		if(!str.equals(null) && !str.isBlank())
		{
			responseBody.setStatus(false);
			responseBody.setMessage(str);
			responseBody.setData(new String());
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		}
		
		if(!userName.matches("^\\d{10}$")) {
			newToken=jwtHelper.generateToken1(email);
			otpService.updatePhoneNumber(user.get().getEmail(), email);
			
		}
		if(userName.matches("^\\d{10}$")) {
			newToken=jwtHelper.generateToken1(phoneNumber);
			otpService.updatePhoneNumber(user.get().getPhoneNumber(), phoneNumber);
		}
	System.out.println("new Token=>"+newToken);
			EditUser editUser = new EditUser();
			editUser.setUserId(user.get().getId());
			editUser.setName(name);
			editUser.setPhoneNumber(phoneNumber);
			editUser.setEmail(email);
			editUser.setQualification(qualification);
			editUser.setOccupation(occupation);
			editUser.setAddress(address);
			

	         if (profilePhoto != null && !profilePhoto.isEmpty()) {
	         	 String p= service.uploadFileOnAzure(profilePhoto);
	        	editUser.setProfilePhoto(p);
	         }
			
			
			if (userService.editUser(editUser)) {
				
				 UserDTO userDTO = new UserDTO(user.get());
		           
				responseBody.setStatus(true);
				responseBody.setMessage(newToken);
				responseBody.setData(userDTO);
				return new ResponseEntity<>(responseBody, HttpStatus.OK);
			} else {
				responseBody.setStatus(false);
				responseBody.setMessage("User not found");
				responseBody.setData(new String());
				return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			responseBody.setStatus(false);
			responseBody.setMessage("Failed to edit user information: " + e.getMessage());
			return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	     	  }
else
 {
	responseBody.setStatus(false);
	responseBody.setMessage("User not found");
	responseBody.setData(new String());
	return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
 }
	}
	
	  	 
	 //final
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
	    ResponseDTO<String> responseBody = new ResponseDTO<>();

		//Map<String, String> responseBody = new HashMap<>();
	    try {
	        Long id =  userId ;
	        userService.deleteUserById(id);
	        responseBody.setStatus(true);
	        responseBody.setMessage("User deleted successfully.");
	        return ResponseEntity.ok(responseBody);
	    } catch (Exception e) {
	        responseBody.setStatus(false);
	        responseBody.setMessage( "Failed to delete user.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
	    }
	}
	
	//final
	@GetMapping("/user/checkuserbymobileoremail")
	public ResponseEntity<?> checkUserByIdentifier(@RequestParam String mobileoremail) {
		 
	    ApiResponse responseBody = new ApiResponse();
	    try {
	        Optional<Admin> adminByPhone = adminService.getAdminBymobileNo(mobileoremail);
	        Optional<Admin> adminByEmail  = adminService.getAdminByEmail(mobileoremail);
	        if (adminByPhone.isPresent() || adminByEmail.isPresent()) {
	        	responseBody.setStatus(false);
                responseBody.setMessage("You are already registered as a Politician");
                  return new ResponseEntity<>(responseBody, HttpStatus.OK);
	             
	        } else {
	            Optional<Users> userByPhone = userService.findByPhoneNumber(mobileoremail);
	            Optional<Users> userByEmail = userService.findByEmail(mobileoremail);
	            if (userByPhone.isPresent() || userByEmail.isPresent()) {
	                 responseBody.setStatus(false);
	                responseBody.setMessage("You are already registered as a Citizen");
	                

	                return new ResponseEntity<>(responseBody, HttpStatus.OK);
	            } else {
	                responseBody.setStatus(true);
	                responseBody.setMessage("You are not registered Please Signup first");

	                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	            }
	        }
	    } catch (Exception e) {
	     System.out.println("hi "+e);
	        responseBody.setStatus(false);
	        responseBody.setMessage("Failed to retrieve user." + e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
	    }
	}

	//final
		@GetMapping("/user/getuser")
		public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@RequestHeader("Authorization") String authorizationHeader) {
			 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
		     
			ResponseDTO<UserDTO> responseBody = new ResponseDTO<>();
			  	try {
		         if (user.isPresent()) {
		            responseBody.setStatus(true);
		            responseBody.setMessage("User retrieved successfully.");
		            responseBody.setData(new UserDTO(user.get())); // Convert user object to string if needed
		            return ResponseEntity.ok(responseBody);
		        } else {
		        	 responseBody.setStatus(false);
		   		    responseBody.setMessage("User not found.");
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		        }
		    } catch (Exception e) {
		    	 responseBody.setStatus(false);
			        responseBody.setMessage( "Failed to retrive user.");
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
			    }
		}
 
		
		@GetMapping("/user/adddevicetoken")
		public ResponseEntity<ResponseDTO<?>> addDeviceToken(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(value="deviceToken", required=true)String token) {
			 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
		     
			ResponseDTO<String> responseBody = new ResponseDTO<>();
			  	try {
		         if (user.isPresent()) {
		        	 	 System.out.println("token updated");
		        	 user.get().setDeviceToken(token);
		        	 
		        	 userRepository.save(user.get());
		        	 
		            responseBody.setStatus(true);
		            responseBody.setMessage("device token Saved successfully.");
		            responseBody.setData("");
		            return ResponseEntity.ok(responseBody);
		        } else {
		        	 responseBody.setStatus(false);
		   		    responseBody.setMessage("User not found.");
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		        }
		    } catch (Exception e) {
		    	 responseBody.setStatus(false);
			        responseBody.setMessage( "Failed to retrive user.");
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
			    }
		}
 
		@GetMapping("/admin/checkadminbymobileoremail")
		public ResponseEntity<?> checkAdminByIdentifier(@RequestParam String mobileoremail) {
			  ApiResponse responseBody = new ApiResponse();
			     try {
		        // First, check if the identifier is registered as a Citizen
		        if (userService.findByPhoneNumber(mobileoremail).isPresent() || userService.findByEmail(mobileoremail).isPresent()) {
		        	  responseBody.setStatus(false);
		                responseBody.setMessage("you are already registered as a Citizen");
		               return new ResponseEntity<>(responseBody, HttpStatus.OK);
		              
		        }

		        // Then, check if the identifier is registered as an Admin/Politician
		        Optional<Admin> adminByPhone = adminService.getAdminBymobileNo(mobileoremail);
		        Optional<Admin> adminByEmail  = adminService.getAdminByEmail(mobileoremail);
		        if (adminByPhone.isPresent() || adminByEmail.isPresent()) {
		        	
		         Admin admin  = adminByPhone.orElseGet(() -> adminByEmail.orElseThrow());
 		            AdminDTO adminDTO = new AdminDTO(admin);
		            String verificationStatus = adminDTO.getVerification();
		            switch (verificationStatus) {
		                case "pending":
		                    responseBody.setStatus(false);
		                     responseBody.setMessage("Politician already registered but verification pending, please wait for verification process.");
		                    break;
		                case "verified":
		                    responseBody.setStatus(false);
		                     responseBody.setMessage("Politician already registered and verified successfully.");
		                    break;
		                default: // Assuming this is for "updation pending" or any other status
		                    responseBody.setStatus(false);
		                     responseBody.setMessage("Politician already registered but updation pending.");
		                    break;
		            }
		             
		            return new ResponseEntity<>(responseBody, HttpStatus.OK);
		        }
		        responseBody.setMessage("You are not registered Please Signup first");
		           
		        responseBody.setStatus(true);
                 return new ResponseEntity<>(responseBody, HttpStatus.OK);

		    } catch (Exception e) {
		        responseBody.setStatus(false);
		        responseBody.setMessage("Failed to retrieve user. " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		    }
		}
		
		
		@GetMapping("/user/currentMonthAppointmentCount")
		public ResponseEntity<ApiDataResponse<Long>> getCountOfAppointmentsForUserInCurrentMonth(@RequestHeader("Authorization") String authorizationHeader) {
		    Long count = 0L;
		    Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
		    ApiDataResponse<Long> responseBody = new ApiDataResponse<>();

		    try {
		        if (user.isPresent()) {
		            count = userService.getCountOfAppointmentsForUserInCurrentMonth(user.get().getId());

		            if (count == 5) {
		                responseBody.setStatus(true);
		                responseBody.setMessage("You allready exceeds limit  to create an  appointment for this month");
		            }
		            else {
		                responseBody.setStatus(false);
		                responseBody.setMessage("Successfully retrieved the count.");
		            }
		            responseBody.setData(count);
		            return ResponseEntity.ok(responseBody);
		        } else {
		            responseBody.setStatus(false);
		            responseBody.setMessage("User not found.");
		            responseBody.setData(null); // Explicitly setting data to null
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
		        }
		    } catch (Exception e) {
		        responseBody.setStatus(false);
		        responseBody.setMessage("Failed to retrieve data.");
		        responseBody.setData(null); // Explicitly setting data to null
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		    }
		}


}
