package com.techverse.satya.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.SystemParameterOrBuilder;
import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.SuggestionDTO;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.UserRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.SuggestionService;
import com.techverse.satya.Service.UserService;

@RestController
@RequestMapping("")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
   	private UserService userService;
    @Autowired
   	private AdminService adminService;
       
      
/***final***/
    @PostMapping("/user/suggestions/add")
    public ResponseEntity<?> addSuggestion(
    		@RequestHeader("Authorization") String authorizationHeader,
            @RequestPart(name = "photo", required = false) MultipartFile photo,
            @RequestPart(name = "video", required = false) MultipartFile video,
            @RequestPart(name = "address", required = false) String address,
            @RequestPart(name = "purpose", required = false) String purpose,
            @RequestPart(name = "comment", required = false) String comment
            ) {
    	SuggestionResponseDTO suggestionResponseDTO;
   	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
   	// System.out.println(user.get().getAdmin().getId());
	   Map<String, Object> responseBody = new HashMap<>();
	   if (user.isPresent()) {
	        
        try {
            SuggestionDTO suggestionDTO = new SuggestionDTO();
            suggestionDTO.setAddress(address);
            suggestionDTO.setPurpose(purpose);
            suggestionDTO.setComment(comment);
            suggestionDTO.setPhoto(photo);
            suggestionDTO.setVideo(video);
            suggestionDTO.setUserId(user.get().getId());
            suggestionDTO.setAdminId(user.get().getAdmin().getId());
            System.out.println("fhdjghjdfgjfdj"+user.get().getAdmin().getId());
            suggestionResponseDTO=suggestionService.addSuggestion(suggestionDTO);
             if (!suggestionResponseDTO.getName().isEmpty()) {
                responseBody.put("status", true);
                responseBody.put("message", "Suggestion added successfully");
                responseBody.put("Suggestion", suggestionResponseDTO);
                return new ResponseEntity<>(responseBody, HttpStatus.OK);
            } else {
                responseBody.put("status", false);
                responseBody.put("message", "User Not Found");
                return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
            } 
         
        } catch (Exception e) {
            responseBody.put("status", false);
            responseBody.put("message", "Failed to update user information");
            responseBody.put("error", e);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	   }
	   else {
		   responseBody.put("status", false);
           responseBody.put("message", "User not valid");
           return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
           }
    }
    @PutMapping("/user/suggestions/edit")
    public ResponseEntity<?> editSuggestion(
    		@RequestHeader("Authorization") String authorizationHeader,
    	  @RequestPart(name = "photo", required = false) MultipartFile photo,
            @RequestPart(name = "video", required = false) MultipartFile video,
       	 @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "purpose", required = false) String purpose,
            @RequestParam(name = "comment", required = false) String comment
            ) {
    	SuggestionResponseDTO suggestionResponseDTO;
   	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
   	// System.out.println(user.get().getAdmin().getId());
	   Map<String, Object> responseBody = new HashMap<>();
	   if (user.isPresent()) {
	        
        try {
        	  Optional<Suggestion> suggestion=suggestionService.getSuggestionById(id);
        	   if(suggestion.isPresent()) {
        		   suggestionResponseDTO=suggestionService.editSuggestion(suggestion.get(),photo,video,address,purpose,comment);
        		   responseBody.put("status", true);
                   responseBody.put("message", "Suggestion edit successfully");
                   responseBody.put("Suggestion", suggestionResponseDTO);
                   return new ResponseEntity<>(responseBody, HttpStatus.OK);
        	   }
        	   else {
        		   responseBody.put("status", false);
                   responseBody.put("message", "Suggestion not Found");
                   return new ResponseEntity<>(responseBody, HttpStatus.OK);
        	   }
          
            
         
        } catch (Exception e) {
            responseBody.put("status", false);
            responseBody.put("message", "Failed to update user information");
            responseBody.put("error", e);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	   }
	   else {
		   responseBody.put("status", false);
           responseBody.put("message", "User not valid");
           return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
           }
    }

    @DeleteMapping("/user/suggestions/delete")
    public ResponseEntity<?> deleteSuggestion(
    		@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam(name = "id", required = false) Long id
            ) {
    	SuggestionResponseDTO suggestionResponseDTO;
   	 Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
   	// System.out.println(user.get().getAdmin().getId());
	   Map<String, Object> responseBody = new HashMap<>();
	   if (user.isPresent()) {
	        
        try {
         	   if(suggestionService.deleteSuggestion(id)) {
         		   responseBody.put("status", true);
                   responseBody.put("message", "Suggestion deleted successfully");
                   return new ResponseEntity<>(responseBody, HttpStatus.OK);
        	   }
        	   else {
        		   responseBody.put("status", false);
                   responseBody.put("message", "Suggestion not Found");
                   return new ResponseEntity<>(responseBody, HttpStatus.OK);
        	   }
          
            
         
        } catch (Exception e) {
            responseBody.put("status", false);
            responseBody.put("message", "Failed to update user information");
            responseBody.put("error", e);
            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }
	   }
	   else {
		   responseBody.put("status", false);
           responseBody.put("message", "User not valid");
           return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
           }
    }

    @GetMapping("/user/suggestions/all")
    public ResponseEntity<?> getSuggestionsByUser(	@RequestHeader("Authorization") String authorizationHeader) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
    	
    	Map<String, Object> responseBody = new HashMap<>();
    	 if (user.isPresent()) {
 	        
    	      


        List<SuggestionResponseDTO> suggestionResponseDTOs = new ArrayList<>();
        List<Suggestion> suggestions = suggestionService.getSuggestionsByUserId(user.get().getId());
        
        
        if (suggestions.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", suggestions);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        
        for (Suggestion suggestion : suggestions) {
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setName(suggestion.getUser().getName());
            suggestionResponseDTO.setAddress(suggestion.getAddress());
            suggestionResponseDTO.setPurpose(suggestion.getPurpose());
            suggestionResponseDTO.setComment(suggestion.getComment());
            suggestionResponseDTO.setPhoto(suggestion.getPhotoUrl());
            suggestionResponseDTO.setVideo(suggestion.getVideoUrl());
            suggestionResponseDTO.setStatus(suggestion.getStatus());
            // You can set other fields like photo and video based on your logic
            suggestionResponseDTO.setDateTime(suggestion.getDateTime());
            suggestionResponseDTO.setProfile(userRepository.findById(user.get().getId()).get().getProfilePphoto());
            suggestionResponseDTO.setId(suggestion.getId());
            suggestionResponseDTOs.add(suggestionResponseDTO);
        }
       
         responseBody.put("suggestions", suggestionResponseDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    	 }
    	 else {
  		   responseBody.put("status", false);
             responseBody.put("message", "User not valid");
             return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
             }
    }
    
    
    @GetMapping("/admin/suggestions/all")
    public ResponseEntity<?> getSuggestionsByAdmin(	@RequestHeader("Authorization") String authorizationHeader) {
    	Optional<Admin> admin= adminService.getAdminByToken(authorizationHeader.substring(7));
    	 
    	Map<String, Object> responseBody = new HashMap<>();
       
    	 if (admin.isPresent()) {
  	        
    	    	

        List<SuggestionResponseDTO> suggestionResponseDTOs = new ArrayList<>();
        List<Suggestion> suggestions = suggestionService.getSuggestionsByAdminId(admin.get().getId());
        
        
        if (suggestions.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", suggestions);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        
        for (Suggestion suggestion : suggestions) {
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setName(suggestion.getUser().getName());
            suggestionResponseDTO.setAddress(suggestion.getAddress());
            suggestionResponseDTO.setPurpose(suggestion.getPurpose());
            suggestionResponseDTO.setComment(suggestion.getComment());
            suggestionResponseDTO.setPhoto(suggestion.getPhotoUrl());
            suggestionResponseDTO.setVideo(suggestion.getVideoUrl());
            // You can set other fields like photo and video based on your logic
            suggestionResponseDTO.setStatus(suggestion.getStatus());
            suggestionResponseDTO.setDateTime(suggestion.getDateTime());
            suggestionResponseDTO.setProfile(userRepository.findById(suggestion.getUser().getId()).get().getProfilePphoto());
            suggestionResponseDTO.setId(suggestion.getId());
            suggestionResponseDTOs.add(suggestionResponseDTO);
        }
       
         responseBody.put("suggestions", suggestionResponseDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    	 }
    	 else {
    		   responseBody.put("status", false);
               responseBody.put("message", "User not valid");
               return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   }
    	 
    }
    @GetMapping("/user/suggestions/bysuggestionid")
    public ResponseEntity<?> getSuggestionById(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long suggestionId) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
    	
    	Map<String, Object> responseBody = new HashMap<>();
    	 if (user.isPresent()) {
 	        
       
        Optional<Suggestion> optionalSuggestion = suggestionService.getSuggestionById(suggestionId);
        if (optionalSuggestion.isPresent()) {
            Suggestion suggestion = optionalSuggestion.get();
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setId(suggestionId);
            suggestionResponseDTO.setName(suggestion.getUser().getName());
            suggestionResponseDTO.setAddress(suggestion.getAddress());
            suggestionResponseDTO.setPurpose(suggestion.getPurpose());
            suggestionResponseDTO.setComment(suggestion.getComment());
            suggestionResponseDTO.setPhoto(suggestion.getPhotoUrl());
            suggestionResponseDTO.setVideo(suggestion.getVideoUrl());
            suggestionResponseDTO.setDateTime(suggestion.getDateTime());
            suggestionResponseDTO.setStatus(suggestion.getStatus());
            // You can set other fields like photo and video based on your logic
            suggestionResponseDTO.setProfile(suggestion.getUser().getProfilePphoto());
            responseBody.put("suggestion", suggestionResponseDTO);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("status", false);
            responseBody.put("message", "Suggestion not found for the given ID");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
    	 }
    	 else {
  		   responseBody.put("status", false);
             responseBody.put("message", "User not valid");
             return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
             }
    }
    @GetMapping("/admin/suggestions/bysuggestionid")
    public ResponseEntity<?> getSuggestionById1(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long suggestionId) {
    	Optional<Admin> admin= adminService.getAdminByToken(authorizationHeader.substring(7));
   	 
        Map<String, Object> responseBody = new HashMap<>();
   	 if (admin.isPresent()) {
	        
        Optional<Suggestion> optionalSuggestion = suggestionService.getSuggestionById(suggestionId);
        if (optionalSuggestion.isPresent()) {
            Suggestion suggestion = optionalSuggestion.get();
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setId(suggestionId);
            suggestionResponseDTO.setStatus(suggestion.getStatus());
            suggestionResponseDTO.setName(suggestion.getUser().getName());
            suggestionResponseDTO.setAddress(suggestion.getAddress());
            suggestionResponseDTO.setPurpose(suggestion.getPurpose());
            suggestionResponseDTO.setComment(suggestion.getComment());
            suggestionResponseDTO.setPhoto(suggestion.getPhotoUrl());
            suggestionResponseDTO.setVideo(suggestion.getVideoUrl());
            suggestionResponseDTO.setDateTime(suggestion.getDateTime());
            suggestionResponseDTO.setProfile(suggestion.getUser().getProfilePphoto());
            // You can set other fields like photo and video based on your logic

            responseBody.put("suggestion", suggestionResponseDTO);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } else {
            responseBody.put("status", false);
            responseBody.put("message", "Suggestion not found for the given ID");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
   	}
	 else {
		   responseBody.put("status", false);
           responseBody.put("message", "User not valid");
           return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   }
	 
    }
   
    
    @GetMapping("/admin/suggestions/today")
    public ResponseEntity<Map<String, Object>> getTodaySuggestionsByAdmin(
            @RequestHeader("Authorization") String authorizationHeader) {
        Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));

        Map<String, Object> responseBody = new HashMap<>();

        if (admin.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("message", "Admin not found");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Long adminId = admin.get().getId();
        List<SuggestionResponseDTO> todaySuggestions = suggestionService.getTodaySuggestionsByAdminId(adminId);

        responseBody.put("suggestions", todaySuggestions);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/admin/suggestions/past")
    public ResponseEntity<Map<String, Object>> getPastSuggestionsByAdmin(
            @RequestHeader("Authorization") String authorizationHeader) {
        Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));

        Map<String, Object> responseBody = new HashMap<>();

        if (admin.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("message", "Admin not found");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        Long adminId = admin.get().getId();
        List<SuggestionResponseDTO> pastSuggestions = suggestionService.getPastSuggestionsByAdminId(adminId);

        responseBody.put("suggestions", pastSuggestions);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
    
    
    
}
