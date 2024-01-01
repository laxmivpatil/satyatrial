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
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.Model.JWTRequest;
import com.techverse.satya.Model.SubAdmin; 
import com.techverse.satya.Security.JwtHelper;
import com.techverse.satya.Service.SubAdminService;

@RestController
@RequestMapping("/subadmin")
public class SubAdminController {
	

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;
    
    @Autowired
    SubAdminService subAdminService; 

    /****final***/
	 @PostMapping("/login")
	  public ResponseEntity<ApiResponse> authenticateUserlogin(@RequestBody JWTRequest jwtRequest) {
	      try {
	          Authentication authentication = authenticationManager.authenticate(
	                  new UsernamePasswordAuthenticationToken(jwtRequest.getMobileNo(), jwtRequest.getOtp()));

	          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	          String jwtToken = jwtHelper.generateToken(userDetails);
	          return ResponseEntity.ok(new ApiResponse(true, jwtToken));
	      } catch (BadCredentialsException e) {
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                  .body(new ApiResponse(false, "Invalid Mobile No  or Otp."));
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                  .body(new ApiResponse(false, "An error occurred during authentication."));
	      }
	  } 
	 
	
}
