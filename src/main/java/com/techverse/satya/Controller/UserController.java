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
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.DTO.EditUser;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.UserDTO;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.OtpService;
import com.techverse.satya.Service.StorageService;
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
	private StorageService service;
	   @Autowired
	    private AdminService adminService;
	    

	@Autowired
	private OtpService otpService;

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
		   ResponseDTO<Object> responseBody = new ResponseDTO<>();
		   
		   Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
	     	  if(user.isPresent()) {

try {
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
				responseBody.setMessage("User edited successfully");
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
	
	 
	 
	 
//for all region in city (remove comment)
	 /* @GetMapping("/pincode/{pincode}")
	    public ResponseEntity<List<Map<String, String>>> getLocationDetails(@PathVariable String pincode) {
	        String apiUrl = "https://api.postalpincode.in/pincode/{pincode}";

	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.getForObject(apiUrl, String.class, pincode);

	        // Parse the JSON response and extract names of post offices, district, and state details
	        List<Map<String, String>> responseList = parseJsonResponse(result);

	        if (responseList.isEmpty()) {
	            // If PIN code is not available or there is an error, return an empty response
	        	 Map<String, String> response = new HashMap<>();

	               // response.put("name",""); for all region for eg jalgaon-->navipeth,ramanand etc
	                response.put("district","");
	                response.put("state", "");

	                responseList.add(response);
	                return ResponseEntity.ok(responseList);
	        } else {
	            return ResponseEntity.ok(responseList);
	        }
	    }

	    private List<Map<String, String>> parseJsonResponse(String jsonResponse) {
	        List<Map<String, String>> responseList = new ArrayList<>();

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(jsonResponse);

	            // Assuming a basic JSON structure, modify as per the actual structure
	            JsonNode postOfficeArray = rootNode.get(0).get("PostOffice");

	            for (JsonNode postOfficeNode : postOfficeArray) {
	                Map<String, String> response = new HashMap<>();

	                response.put("name", postOfficeNode.get("Name").asText());
	                response.put("district", postOfficeNode.get("District").asText());
	                response.put("state", postOfficeNode.get("State").asText());

	                responseList.add(response);
	            }
	        } catch (IOException | NullPointerException e) {
	            e.printStackTrace();
	            // Handle exception
	        }

	        return responseList;
	    }
*/
	 @GetMapping("/user/getcity")
	    public ResponseEntity<Map<String, String>> getLocationDetails(@RequestParam  String pincode) {
	        String apiUrl = "https://api.postalpincode.in/pincode/{pincode}";

	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.getForObject(apiUrl, String.class, pincode);

	        // Parse the JSON response and extract city and state details
	        Map<String, String> response = parseJsonResponse(result);

	        if (response.isEmpty()) {
	        	 response.put("city/district", "");
		            response.put("state", "");
		            response.put("message", "city/district not found");
		            return ResponseEntity.status(HttpStatus.OK).body(response);
	        } else {
	            return ResponseEntity.ok(response);
	        }
	    }
	 @GetMapping("/admin/getcity")
	    public ResponseEntity<Map<String, String>> getLocationDetails1(@RequestParam String pincode) {
	        String apiUrl = "https://api.postalpincode.in/pincode/{pincode}";

	        RestTemplate restTemplate = new RestTemplate();
	        String result = restTemplate.getForObject(apiUrl, String.class, pincode);

	        // Parse the JSON response and extract city and state details
	        Map<String, String> response = parseJsonResponse(result);

	        if (response.isEmpty()) {
	            // If PIN code is not available or there is an error, return an empty response
	        	 response.put("city/district", "");
		            response.put("state", "");
		            response.put("message", "city/district not found");
	            return ResponseEntity.status(HttpStatus.OK).body(response);
	        } else {
	            return ResponseEntity.ok(response);
	        }
	    }

	    private Map<String, String> parseJsonResponse(String jsonResponse) {
	        Map<String, String> response = new HashMap<>();

	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode rootNode = objectMapper.readTree(jsonResponse);

	            // Assuming a basic JSON structure, modify as per the actual structure
	            JsonNode postOfficeNode = rootNode.get(0).get("PostOffice").get(0);
	            String city = postOfficeNode.get("District").asText();
	            String state = postOfficeNode.get("State").asText();

	            response.put("city/district", city);
	            response.put("state", state);
	        } catch (IOException | NullPointerException e) {
	            e.printStackTrace();
	            // Handle exception
	        }

	        return response;
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
	@GetMapping("/user/checkuserbymobileno")
	public ResponseEntity<?> checkuserbymobileno(@RequestParam String mobileNo) {
		  ResponseDTO<UserDTO> responseBody = new ResponseDTO<>();
		  try {  
		  if(adminService.getAdminBymobileNo(mobileNo).isPresent())
	    	{
	    		return ResponseEntity.status(HttpStatus.FOUND)
	            .body(new ApiResponse(true, "User Allready Registered as a Politician Please Login as a Politician"));
	    	}
	    
		  else if(userService.findByPhoneNumber(mobileNo).isPresent()) {
			  UserDTO userDTO = new UserDTO(userService.findByPhoneNumber(mobileNo).get());
	          responseBody.setStatus(true);
	          responseBody.setMessage("User Allready Registered as a Citizen");
	           responseBody.setData(userDTO);
	          return new ResponseEntity<>(responseBody, HttpStatus.OK);	  
		 
	    		 
		  }
		  
		  return ResponseEntity.status(HttpStatus.OK)
				  .body(new ApiResponse(false, "User not Registered"));
	     
	    } catch (Exception e) {
	    	 responseBody.setStatus(false);
		        responseBody.setMessage( "Failed to retrive user.");
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
 
		@GetMapping("/admin/checkadminbymobileno")
		public ResponseEntity<?> checkadminbymobileno(@RequestParam String mobileNo) {
			  ResponseDTO<AdminDTO> responseBody = new ResponseDTO<>();
			  try {  
				  if(userService.findByPhoneNumber(mobileNo).isPresent())
		    	{
		    		return ResponseEntity.status(HttpStatus.FOUND)
		            .body(new ApiResponse(true, "User Allready Registered as a Citizen Please Login as a Citizen"));
		    	}
		    
			  else if(adminService.getAdminBymobileNo(mobileNo).isPresent()) {
				  AdminDTO adminDTO = new AdminDTO(adminService.getAdminBymobileNo(mobileNo).get());
		         if(adminDTO.getVerification().equals("pending")) {
				  responseBody.setStatus(false);
		          responseBody.setMessage("Politician Allready Registered but verification pending, please wait for verification process");
		           responseBody.setData(adminDTO);
		          return new ResponseEntity<>(responseBody, HttpStatus.OK);	
		          }
		         else if(adminDTO.getVerification().equals("verified")) {
					  responseBody.setStatus(true);
			          responseBody.setMessage("Politician Allready Registered and verified successfully");
			           responseBody.setData(adminDTO);
			          return new ResponseEntity<>(responseBody, HttpStatus.OK);	
			          }
		         else {
		        	 responseBody.setStatus(false);
			          responseBody.setMessage("Politician Allready Registered but updation Pending");
			           responseBody.setData(adminDTO);
			          return new ResponseEntity<>(responseBody, HttpStatus.OK);	
		         }
		    		 
			  }
			  
			  return ResponseEntity.status(HttpStatus.OK)
					  .body(new ApiResponse(false, "Politician not Registered"));
		     
		    } catch (Exception e) {
		    	 responseBody.setStatus(false);
			        responseBody.setMessage( "Failed to retrive user.");
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
			}
		}
}
