package com.techverse.satya.Controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.Model.Status;
import com.techverse.satya.Repository.StatusRepository;
import com.techverse.satya.Service.StorageService;

@RestController
@RequestMapping("")
public class StatusController {

	@Autowired
    private StorageService service;
	
	
	@Autowired
    private StatusRepository statusRepository;
	
	 @PostMapping("/user/setstatusvideo")
	    public ResponseEntity<?> handleFileUpload(@RequestParam("videofile") MultipartFile file) {
		 
		 Map<String, Object> responseBody = new HashMap<>();
	        if (file.isEmpty()) {
	        	 responseBody.put("status", false);
	             responseBody.put("message","Please select a file to upload");
	             return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	           
	        }

	        try {
	          	 String videoUrl= service.uploadFileOnAzure(file);
	          	Long id=1L;
	          	 Optional<Status> s=statusRepository.findById(id);
	          	
	          	  
	          	 s.get().setVideoUrl(videoUrl);
	          	 statusRepository.save(s.get());
	          	 responseBody.put("status", true);
	             responseBody.put("message","File uploaded successfully");
	             responseBody.put("VideoStatus",s);
	             return new ResponseEntity<>(responseBody, HttpStatus.OK);
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	            responseBody.put("status", false);
	            responseBody.put("message","Failed to upload file");
	            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	 
	 @GetMapping("/user/getstatusvideo")
	    public ResponseEntity<?> getVideo() {		 
		 Map<String, Object> responseBody = new HashMap<>();
	        
	        try {
	        	Long id=1L;
	          	 Optional<Status> s=statusRepository.findById(id);
	          	 if(s.isPresent()) {
	          	 responseBody.put("status", true);
	             responseBody.put("message","File retrived successfully");
	             responseBody.put("VideoStatus",s.get());
	             return new ResponseEntity<>(responseBody, HttpStatus.OK);
	          	 }
	          	 else {
	          		responseBody.put("status", false);
		             responseBody.put("message","Failed to get  status ");
		             
		             return new ResponseEntity<>(responseBody, HttpStatus.OK);
	          	 }
	             
	        } catch (Exception e) {
	            e.printStackTrace();
	            responseBody.put("status", false);
	            responseBody.put("message","Failed to get status");
	            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
}
