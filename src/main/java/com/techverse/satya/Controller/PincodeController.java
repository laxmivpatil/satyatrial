package com.techverse.satya.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.Model.Pincode;
import com.techverse.satya.Service.ExcelReader;
import com.techverse.satya.Service.PincodeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PincodeController {

    private final PincodeService pincodeService;

    @Autowired
    public PincodeController(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    @GetMapping("/user/getcity")
    public ResponseEntity<Map<String, String>> getLocationDetails(@RequestParam  String pincode) {
      
	 Optional<Pincode> city=pincodeService.getcitystate(pincode);
    	
	 Map<String, String> response=new HashMap<>();
	 if(city.isPresent())
	 {
		 response.put("city/district", city.get().getDistrict());
            response.put("state", city.get().getState());
             
	 }
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
	 Optional<Pincode> city=pincodeService.getcitystate(pincode);
 	
	 Map<String, String> response=new HashMap<>();
	 if(city.isPresent())
	 {
		 response.put("city/district", city.get().getDistrict());
            response.put("state", city.get().getState());
             
	 }
        if (response.isEmpty()) {
        	
        	 response.put("city/district", "");
	            response.put("state", "");
	            response.put("message", "city/district not found");
	            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping("/api/pincode/upload")
    public ResponseEntity<String> uploadPincodeData(@RequestPart("file") MultipartFile file) {
        try {
        	
        	 File tempFile = File.createTempFile("temp", null);

             // Save the MultipartFile to the temporary file
             try {
                 Path tempFilePath = tempFile.toPath();
                 Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);
             } catch (IOException e) {
                 // Handle exception, e.g., log or throw it
                 e.printStackTrace();
             }

             // Get the absolute path of the temporary file
             String absolutePath = tempFile.getAbsolutePath();

             // Now 'absolutePath' contains the file path as a string

            
            List<Pincode> pincodeList = pincodeService.readAndStoreData(absolutePath);
            
           pincodeService.saveAll(pincodeList);
            return new ResponseEntity<>("Pincode data uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to read Excel file: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload Pincode data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

