package com.techverse.satya.Controller;
 

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.DTO.AdminDTO;
import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.UserDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.JWTRequest;
import com.techverse.satya.Model.JWTRequestAdmin;
import com.techverse.satya.Model.JWTRequestUser;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.OtpRepository;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Security.JwtHelper;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.OtpService;
import com.techverse.satya.Service.StorageService;
import com.techverse.satya.Service.TokenBlacklistService;
import com.techverse.satya.Service.UserService;

 
 

@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AdminService adminService;
    
    //form user controller start 
       
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
   	private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;

   	
   	@Autowired
   	private OtpRepository otpRepository;

   	@Autowired
   	private UserService userService;

  	 

    
   	@Autowired
   	private UserRepository userRepository;

   	@PostMapping("/updateuser")
	public ResponseEntity<?> update(@RequestPart("userId") String userId, @RequestPart("address") String address,
	        @RequestPart("pincode") String pincode, @RequestPart("city") String city,
	        @RequestPart("constitution") String constitution, @RequestPart("wardNo") String wardNo,
	        @RequestPart("state") String state  , @RequestPart("adminId") String adminId ,@RequestPart(value="profilePhoto", required = false) MultipartFile file) {
	     ResponseDTO<Object> response = new ResponseDTO<>();

	    String photo1 ="file";
	    System.out.println("hidbngfhdsgfhgs");
	    try {
	        // Handle file upload
	        if (!file.isEmpty()) {
	            // Save the file to the server or process it as needed
	           // photo1 = service.uploadFile(file, userId);
	            System.out.println(photo1);
	        }

	        Optional<Users> userOptional = userService.updateUserInformation(Long.parseLong(userId), address, pincode, city, constitution, wardNo,
	                state, photo1,Long.parseLong(adminId));

	        if (userOptional.isPresent()) {
	            
	            UserDTO userDTO = new UserDTO(userOptional.get());
	            response.setStatus(true);
	            response.setMessage("User updated successfully");
	            response.setData(userDTO);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } else {
	            response.setStatus(false);
	            response.setMessage("UserId Not Found");
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
 

    
  @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUserlogin(@RequestBody JWTRequest jwtRequest) {
	  ResponseDTO<Object> response = new ResponseDTO<>();
		 
		  
	  try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(), jwtRequest.getOtp()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtHelper.generateToken(userDetails);
            UserDTO userDTO = new UserDTO(userService.getUserByToken(jwtToken).get());
            response.setStatus(true);
            response.setMessage("User login successfully");
             response.setToken(jwtToken);
            response.setData(userDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid Mobile No  or Otp."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An error occurred during authentication."));
        }
    } 
   
  
  @PostMapping("/admin/login")
  public ResponseEntity<?> adminlogin(@RequestBody JWTRequest jwtRequest) {
	  ResponseDTO<Object> response = new ResponseDTO<>();
   try {
          Authentication authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(), jwtRequest.getOtp()));

          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          String jwtToken = jwtHelper.generateToken(userDetails);
          AdminDTO adminDTO = new AdminDTO(adminService.getAdminByToken(jwtToken).get());
          response.setStatus(true);
          response.setMessage("Admin login successfully");
           response.setToken(jwtToken);
          response.setData(adminDTO);
           
          return new ResponseEntity<>(response, HttpStatus.OK);
          
           
      } catch (BadCredentialsException e) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(new ApiResponse(false, "Invalid Mobile No  or Otp."));
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new ApiResponse(false, "An error occurred during authentication."));
      }
  } 
  
  
  /***final******/
  @PostMapping("/user/firstlogin")
  public ResponseEntity<?> userfirstlogin(@RequestBody JWTRequestUser jwtRequest) {
	  ResponseDTO<Object> response = new ResponseDTO<>();
	  
	  try {
    	if(adminService.getAdminBymobileNoOrEmail(jwtRequest.getMobileNo()).isPresent())
    	{
    		return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse(false, "Please Login as a politician"));
    	}
    
    	if(!userService.findByPhoneNumberOrEmail(jwtRequest.getMobileNo()).isPresent()) {
    		
    	 
    		Users user=userService.createUser(jwtRequest);
    		 
    	}
    	 
    	   
    	
      	System.out.println("authuser"+jwtRequest.getMobileNo());
          Authentication authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(),jwtRequest.getOtp()));

          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          String jwtToken = jwtHelper.generateToken(userDetails);
          Users user=userService.getUserByToken(jwtToken).get();
          UserDTO userDTO = new UserDTO(user);
          System.out.println(user.getAdmin());
          if(user.getAdmin()!=null)
          {
        	  
        	  userDTO.setAdmin(user.getAdmin().getName());
          }
          response.setStatus(true);
          response.setMessage("User login successfully");
           response.setToken(jwtToken);
         response.setData(userDTO);
          return new ResponseEntity<>(response, HttpStatus.OK);
 
          
           } catch (BadCredentialsException e) {
          return ResponseEntity.status(HttpStatus.OK)
                  .body(new ApiResponse(false, "Invalid Otp."));
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new ApiResponse(false, "An error occurred during authentication."+e));
      }
  }
 
 
  /***final******/
  @PostMapping("/admin/firstlogin")
  public ResponseEntity<?> authenticateAdmin(@RequestBody JWTRequestAdmin jwtRequest) {
	  ResponseDTO<Object> response = new ResponseDTO<>();
	   
	  try {
    	if(userService.findByPhoneNumberOrEmail(jwtRequest.getMobileNo()).isPresent())
    	{
    		return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse(false, "Please Login as a citizen"));
    	}
    
    	if(!adminService.getAdminBymobileNoOrEmail(jwtRequest.getMobileNo()).isPresent()) {
    		Admin admin=adminService.createAdmin(jwtRequest);
    	}
    	
    	
      	System.out.println("authuser"+jwtRequest.getMobileNo());
          Authentication authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(),jwtRequest.getOtp()));

          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          String jwtToken = jwtHelper.generateToken(userDetails);
         AdminDTO adminDTO = new AdminDTO(adminService.getAdminByToken(jwtToken).get());
         response.setStatus(true);
         response.setMessage("Admin login successfully");
         response.setToken(jwtToken);
         response.setData(adminDTO);
          
         return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (BadCredentialsException e) {
          return ResponseEntity.status(HttpStatus.OK)
                  .body(new ApiResponse(false, "Invalid Otp."));
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new ApiResponse(false, "An error occurred during authentication."));
      }
  }
  
  
  

	 @GetMapping("/admin/logout")
	    public ResponseEntity<Object> adminLogout(@RequestHeader("Authorization") String authorizationHeader) {
	       
		  Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
	        if (admin.isPresent()) {
	            try {
	            	 String extractedToken = authorizationHeader.substring(7);

	                 // Add the token to the blacklist
	                 tokenBlacklistService.blacklistToken(extractedToken);
	           	  
	           	 return ResponseEntity.status(HttpStatus.OK)
	                        .body(new ApiDataResponse(true, "logout Succesfully", ""));
	                     } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(new ApiDataResponse(false, "Failed to logout Please try again later.",""));
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new ApiDataResponse(false, "Invalid token. Please login again.", ""));
	        }
	
	    }
	 @GetMapping("/user/logout")
	    public ResponseEntity<Object> userLogout(@RequestHeader("Authorization") String authorizationHeader) {
	       
		  Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
	        if (user.isPresent()) {
	            try {
	            	 String extractedToken = authorizationHeader.substring(7);

	                 // Add the token to the blacklist
	                 tokenBlacklistService.blacklistToken(extractedToken);
	           	  
	           	 return ResponseEntity.status(HttpStatus.OK)
	                        .body(new ApiDataResponse(true, "logout Succesfully", ""));
	                     } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(new ApiDataResponse(false, "Failed to logout Please try again later.",""));
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(new ApiDataResponse(false, "Invalid token. Please login again.", ""));
	        }
	
	    }
	 @GetMapping("/subadmin/logout")
	    public ResponseEntity<Object> subAdminLogout(@RequestHeader("Authorization") String authorizationHeader) {
	       
		         try {
	            	 String extractedToken = authorizationHeader.substring(7);

	                 // Add the token to the blacklist
	                 tokenBlacklistService.blacklistToken(extractedToken);
	           	  
	           	 return ResponseEntity.status(HttpStatus.OK)
	                        .body(new ApiDataResponse(true, "logout Succesfully", ""));
	                     } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body(new ApiDataResponse(false, "Failed to logout Please try again later.",""));
	            }
	        
	
	    }

    
}

