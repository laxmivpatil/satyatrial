package com.techverse.satya.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.DTO.SubAdminDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.JWTRequest;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Repository.SubAdminRepository;
import com.techverse.satya.Security.JwtHelper;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.SubAdminService;
import com.techverse.satya.Service.UserService;

@RestController
@RequestMapping("/subadmin")
public class SubAdminController {

	@Autowired
	SubAdminRepository subAdminRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;
    
    @Autowired
    SubAdminService subAdminService; 
    
    @Autowired
    private AdminService adminService;
	@Autowired
    private UserService userService;

    /****final***/
	 @PostMapping("/login")
	  public ResponseEntity<Object> authenticateUserlogin(@RequestBody JWTRequest jwtRequest) {
	     
		 
		 try {
	          Authentication authentication = authenticationManager.authenticate(
	                  new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(), jwtRequest.getOtp()));

	          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	          String jwtToken = jwtHelper.generateToken(userDetails);
	          
	          
	          Optional<SubAdmin> subAdmin = subAdminService.getSubAdminByToken(jwtToken);
	          subAdmin.get().setName(jwtRequest.getName());
	          subAdminRepository.save(subAdmin.get());
	          return ResponseEntity.ok(new ApiDataResponse(true, jwtToken,new SubAdminDTO(subAdmin.get())));
	      } catch (BadCredentialsException e) {
	          return ResponseEntity.status(HttpStatus.OK)
	                  .body(new ApiResponse(false, "Invalid Mobile No  or Otp."));
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body(new ApiResponse(false, "An error occurred during authentication."));
	      }
	  } 
	 
	 /****final***/
	 @GetMapping("/get")
	  public ResponseEntity<ApiResponse> geSubAdmin(@RequestHeader("Authorization") String authorizationHeader) {
	      try {
	    		 Optional<SubAdmin> subAdmin = subAdminService.getSubAdminByToken(authorizationHeader.substring(7));
	    		        return ResponseEntity.ok(new ApiResponse(true, subAdmin.get().getMobileNumber()));
	      } catch (BadCredentialsException e) {
	          return ResponseEntity.status(HttpStatus.OK)
	                  .body(new ApiResponse(false, "Invalid Mobile No  or Otp."));
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body(new ApiResponse(false, "An error occurred during authentication."));
	      }
	  } 
	 
	 
	 
	 @GetMapping("/findbymobileno")
	  public ResponseEntity<Object> findbymobileno(@RequestParam String mobileno) {
	     
		 
		 try {
	           
 
	          Optional<SubAdmin> subAdmin = subAdminService.getSubAdminBymobileNo(mobileno);
	          if(subAdmin.isPresent()) {
	          return ResponseEntity.ok(new ApiDataResponse(true,"data available",new SubAdminDTO(subAdmin.get())));
	          }
	          else {
	        	  return ResponseEntity.status(HttpStatus.OK)
		                  .body(new ApiResponse(false, "Invalid Mobile No "));
	          }
	      } catch (BadCredentialsException e) {
	          return ResponseEntity.status(HttpStatus.OK)
	                  .body(new ApiResponse(false, "Invalid Mobile No "));
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body(new ApiResponse(false, "An error occurred during authentication."));
	      }
	  } 
	 
	 
	 /****new***********/
	 @GetMapping("/subadmin/adddevicetoken")
		public ResponseEntity<ResponseDTO<?>> addDeviceToken(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(value="deviceToken", required=true)String token) {
			Optional<SubAdmin> user = adminService.getAdminByToken1(authorizationHeader.substring(7));

			ResponseDTO<String> responseBody = new ResponseDTO<>();
			try {
				System.out.println("hi token");
				if(user.isPresent()) {
					user.get().setDeviceToken(token);
					subAdminRepository.save(user.get());
					responseBody.setStatus(true);
					responseBody.setMessage("Sub Admin device token saved successfully.");
					responseBody.setData("");
					return ResponseEntity.ok(responseBody);
				} else {
					responseBody.setStatus(false);
					responseBody.setMessage("Sub Admin not found.");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
				}
			} catch (Exception e) {
				responseBody.setStatus(false);
				responseBody.setMessage( "Failed to retrive Sub Admin.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
			}
		}
		
	
}
