package com.techverse.satya.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.techverse.satya.DTO.OtpVerificationResult;
import com.techverse.satya.DTO.ResponseDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.OtpService;
import com.techverse.satya.Service.UserService;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import okhttp3.*;
import okhttp3.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
 
 

@RestController
@RequestMapping("")
public class OtpController {

	 @Autowired
    private OtpService otpService;
	
    
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    
    
    @Autowired
    UserRepository userRepository;
     
    
    
    @GetMapping("/generateOtp1")
    public String sendSMS(@RequestParam("to") String to, @RequestParam("message") String message1) throws IOException {
     
    	 String apiKey = "0Gt2VnzXhK5DxkyZpJvYLfCRPibl63dUqoSAgOecNIHw9sauQWkzbYeg2rD8j1G6UoZxFhNJyK0mTt34";
         String numbers = "7507568660";
         String message = "Your OTP is: 5599";

         HttpResponse<JsonNode> response = Unirest.get("https://www.fast2sms.com/dev/bulkV2")
                 .queryString("authorization", apiKey)
                 .queryString("route", "otp")
                 .queryString("numbers", numbers)
                 .queryString("message", message)
                 .header("cache-control", "no-cache")
                 .asJson();

         int status = response.getStatus();
         if (status == 200) {
             System.out.println("OTP sent successfully!");
             return message;
         } else {
             System.err.println("Failed to send OTP. Status code: " + status);
             System.err.println("Response body: " + response.getBody());
             return response.getBody().toString();
         }
    }
    
     
 


	
    
    /****final****/
    @GetMapping("/user/generateOtp")
    public ResponseEntity<ResponseDTO<String>> generateOtpAll(@RequestParam String mobileNo) {
   	  	String role="";
    	System.out.println();
   	
        ResponseDTO<String> response = new ResponseDTO<>();
        response.setData("");

        try {
            if (mobileNo != null && !mobileNo.isEmpty()) {
                
                     String otp = otpService.generateOtp();
                    if (otpService.sendOtp(mobileNo, otp)) {
                    	System.out.println("Otp is "+otp);
                        response.setStatus(true);
                        response.setMessage("OTP sent successfully."+otp);
                        return ResponseEntity.ok(response);
                    } else {
                        response.setStatus(false);
                        response.setMessage("Failed to send OTP.");
                       
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                 
            } else {
                response.setStatus(false);
                
                response.setMessage("Invalid Mobile No.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    } 
    @GetMapping("/admin/generateOtp")
    public ResponseEntity<ResponseDTO<String>> generateOtpAll1(@RequestParam String mobileNo) {
   	  	String role="";
    	System.out.println();
   	
        ResponseDTO<String> response = new ResponseDTO<>();
        response.setData("");

        try {
            if (mobileNo != null && !mobileNo.isEmpty()) {
                
                     String otp = otpService.generateOtp();
                    if (otpService.sendOtp(mobileNo, otp)) {
                    	System.out.println("Otp is "+otp);
                        response.setStatus(true);
                        response.setMessage("OTP sent successfully."+otp);
                        return ResponseEntity.ok(response);
                    } else {
                        response.setStatus(false);
                        response.setMessage("Failed to send OTP.");
                       
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                 
            } else {
                response.setStatus(false);
                
                response.setMessage("Invalid Mobile No.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    } 

    /****final****/
    @PostMapping("/validateOtp")
	public ResponseEntity<ResponseDTO<String>> validate(@org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
	    ResponseDTO<String> response = new ResponseDTO<>();
	    response.setData("");
	    String phoneNumber = request.get("phoneNumber");
	    String otp = request.get("otp");

	    if (StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(otp)) {
	    	 response.setStatus(false);
	        response.setMessage("Missing required credentials.");
	        return ResponseEntity.badRequest().body(response);
	    }

	    int otpVerificationResult = otpService.verifyOtp(phoneNumber, otp);

	    if (otpVerificationResult == OtpVerificationResult.SUCCESS) {
	        // Generate authentication token (you can use JWT)
	    //    String token = generateToken(phoneNumber);
	        response.setStatus(true);
	        response.setMessage("verification successful");
	      //  response.setData(token);
	        return ResponseEntity.ok(response);
	    } else if (otpVerificationResult == OtpVerificationResult.EXPIRED) {
	    	 response.setStatus(false);
	        response.setMessage("OTP has expired. Please request a new OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    } else {
	    	 response.setStatus(false);
	        response.setMessage("Invalid OTP. Please enter a valid OTP.");
	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}
    
    
    @PostMapping("/send-whatsapp")
    public void sendWhatsAppMessage(@org.springframework.web.bind.annotation.RequestBody Map<String, String> request) {
	      String phoneNumber = request.get("phoneNumber");
	    String message = request.get("message");
        otpService.sendWhatsAppMessage(phoneNumber, message);
    }

    
   
    
    
}


/*
 *spring.datasource.url=jdbc:mysql://localhost:3306/satya_data?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC 
  spring.jpa.properties.hibernate.connection.characterEncoding=utf8mb4
  spring.jpa.properties.hibernate.connection.CharSet=utf8mb4
  spring.jpa.properties.hibernate.connection.useUnicode=true
 spring.datasource.username=root
  spring.datasource.password=root
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
 spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.hbm2ddl.auto=update
 spring.jpa.show-sql=true 
  
 */
