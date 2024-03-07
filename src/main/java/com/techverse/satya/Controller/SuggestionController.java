package com.techverse.satya.Controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.poi.util.SystemOutLogger;
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
import com.techverse.satya.Repository.SuggestionRepository;
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
    private SuggestionRepository suggestionRepository;
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
    
    @GetMapping("/user/suggestions/bymonth")
    public ResponseEntity<?> getSuggestionsByUserMonthYear(	@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam int year,
	         @RequestParam int month) {
    	Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
    	
    	Map<String, Object> responseBody = new HashMap<>();
    	 if (user.isPresent()) {
 	        
    	      


        List<SuggestionResponseDTO> suggestionResponseDTOs = new ArrayList<>();
        List<Suggestion> suggestions = suggestionService.getSuggestionsByUserId(user.get().getId());
        
        List<Suggestion> filteredSuggestions = suggestions.stream()
                .filter(suggestion -> {
                    LocalDate localDate = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));

                    // Add condition for year and month
                    boolean isMatchingDate = localDate.getYear() == year && localDate.getMonthValue() == month ;
                    if(suggestion.isEditable() && localDate.isBefore(LocalDate.now())){
                    	System.out.println("change");
                    	suggestion.setEditable(false);
                    	suggestionRepository.save(suggestion);
                    }

                    // Add condition for adminId and status
                     boolean isStatusMatch = suggestion.getStatus() == null || !suggestion.getStatus().equalsIgnoreCase("delete");

                    return isMatchingDate  && isStatusMatch;
                })
                .collect(Collectors.toList());
        if (filteredSuggestions.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", filteredSuggestions);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        
        for (Suggestion suggestion : filteredSuggestions) {
            SuggestionResponseDTO suggestionResponseDTO = new SuggestionResponseDTO();
            suggestionResponseDTO.setName(suggestion.getUser().getName());
            suggestionResponseDTO.setEditable(suggestion.isEditable());
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
        responseBody.put("status", true);
        
         responseBody.put("suggestions", suggestionResponseDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    	 }
    	 else {
  		   responseBody.put("status", false);
             responseBody.put("message", "User not valid");
             return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	   
             }
    }
    @GetMapping("/user/suggestions/byfilter")
    public ResponseEntity<?> getSuggestionsByUserMonthYear(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) String filter) {

        Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));

        Map<String, Object> responseBody = new HashMap<>();
        if (!user.isPresent()) {
            responseBody.put("status", false);
            responseBody.put("message", "User not valid");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }

        List<Suggestion> suggestions = suggestionService.getSuggestionsByUserId(user.get().getId());
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);
        LocalDate endOfLastWeek = endOfWeek.minusWeeks(1);
        LocalDate startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfThisMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        Predicate<Suggestion> timeFilter;
        switch (filter != null ? filter : "") {
            case "allTime":
                timeFilter = suggestion -> true;
                break;
            case "thisWeek":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
                };
                break;
            case "lastWeek":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfLastWeek) && !date.isAfter(endOfLastWeek);
                };
                break;
            case "thisMonth":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfThisMonth) && !date.isAfter(endOfThisMonth);
                };
                break;
            default: // Default to filtering by the specified month and year if no or unknown filter is provided.
            	 timeFilter = suggestion -> true;
                 break;
        }

        List<SuggestionResponseDTO> suggestionResponseDTOs = suggestions.stream()
                .filter(timeFilter)
                .filter(suggestion -> {
                    if (suggestion.isEditable() && LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)).isBefore(LocalDate.now())) {
                        suggestion.setEditable(false);
                        suggestionRepository.save(suggestion);
                    }
                    return suggestion.getStatus() == null || !suggestion.getStatus().equalsIgnoreCase("delete");
                })
                .map(suggestion -> {
                    SuggestionResponseDTO dto = new SuggestionResponseDTO();
                    dto.setName(suggestion.getUser().getName());
                    dto.setEditable(suggestion.isEditable());
                    dto.setAddress(suggestion.getAddress());
                    dto.setPurpose(suggestion.getPurpose());
                    dto.setComment(suggestion.getComment());
                    dto.setPhoto(suggestion.getPhotoUrl());
                    dto.setVideo(suggestion.getVideoUrl());
                    dto.setStatus(suggestion.getStatus());
                    dto.setDateTime(suggestion.getDateTime());
                    dto.setProfile(userRepository.findById(user.get().getId()).get().getProfilePphoto());
                    dto.setEdit(suggestion.isEdit());
                    dto.setId(suggestion.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        if (suggestionResponseDTOs.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", suggestionResponseDTOs);
        } else {
            responseBody.put("status", true);
            responseBody.put("suggestions", suggestionResponseDTOs);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
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
               return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	 
               }
    	 
    }
    
    @GetMapping("/admin/suggestions/byfilter")
    public ResponseEntity<?> getSuggestionsByAdminFilter(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(required = false) String filter) {


    	
	
        Optional<Admin> admin= adminService.getAdminByToken(authorizationHeader.substring(7));
      	 
        Map<String, Object> responseBody = new HashMap<>();
        if (!admin.isPresent()) {
            responseBody.put("status", false);
            responseBody.put("message", "User not valid");
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }

        List<Suggestion> suggestions = suggestionService.getSuggestionsByAdminId(admin.get().getId());
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = startOfWeek.minusWeeks(1);
        LocalDate endOfLastWeek = endOfWeek.minusWeeks(1);
        LocalDate startOfThisMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfThisMonth = now.with(TemporalAdjusters.lastDayOfMonth());

        Predicate<Suggestion> timeFilter;
        switch (filter != null ? filter : "") {
            case "allTime":
                timeFilter = suggestion -> true;
                break;
            case "thisWeek":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
                };
                break;
            case "lastWeek":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfLastWeek) && !date.isAfter(endOfLastWeek);
                };
                break;
            case "thisMonth":
                timeFilter = suggestion -> {
                    LocalDate date = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    return !date.isBefore(startOfThisMonth) && !date.isAfter(endOfThisMonth);
                };
                break;
            default: // Default to filtering by the specified month and year if no or unknown filter is provided.
            	 timeFilter = suggestion -> true;
                 break;
        }

        List<SuggestionResponseDTO> suggestionResponseDTOs = suggestions.stream()
                .filter(timeFilter)
                .filter(suggestion -> {
                    if (suggestion.isEditable() && LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE)).isBefore(LocalDate.now())) {
                        suggestion.setEditable(false);
                        suggestionRepository.save(suggestion);
                    }
                    return suggestion.getStatus() == null || !suggestion.getStatus().equalsIgnoreCase("delete");
                })
                .map(suggestion -> {
                    SuggestionResponseDTO dto = new SuggestionResponseDTO();
                    dto.setName(suggestion.getUser().getName());
                    dto.setEditable(suggestion.isEditable());
                    dto.setAddress(suggestion.getAddress());
                    dto.setPurpose(suggestion.getPurpose());
                    dto.setComment(suggestion.getComment());
                    dto.setPhoto(suggestion.getPhotoUrl());
                    dto.setVideo(suggestion.getVideoUrl());
                    dto.setStatus(suggestion.getStatus());
                    dto.setDateTime(suggestion.getDateTime());
                    dto.setEdit(suggestion.isEdit());
                    dto.setProfile(userRepository.findById(suggestion.getUser().getId()).get().getProfilePphoto());
                    dto.setId(suggestion.getId());
                    return dto;
                })
                .collect(Collectors.toList());

        if (suggestionResponseDTOs.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", suggestionResponseDTOs);
        } else {
            responseBody.put("status", true);
            responseBody.put("suggestions", suggestionResponseDTOs);
        }
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }


    
    @GetMapping("/admin/suggestions/bymonth")
    public ResponseEntity<?> getSuggestionsByAdminIdMonthYear(	@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam int year,
	         @RequestParam int month) {
    	Optional<Admin> admin= adminService.getAdminByToken(authorizationHeader.substring(7));
    	 
    	Map<String, Object> responseBody = new HashMap<>();
       
    	 if (admin.isPresent()) {
  	        
    	    	

        List<SuggestionResponseDTO> suggestionResponseDTOs = new ArrayList<>();
        List<Suggestion> suggestions = suggestionService.getSuggestionsByAdminId(admin.get().getId());
        
        List<Suggestion> filteredSuggestions = suggestions.stream()
                .filter(suggestion -> {
                    LocalDate localDate = LocalDate.parse(suggestion.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE));

                    // Add condition for year and month
                    boolean isMatchingDate = localDate.getYear() == year && localDate.getMonthValue() == month;

                    // Add condition for adminId and status
                     boolean isStatusMatch = suggestion.getStatus() == null || !suggestion.getStatus().equalsIgnoreCase("delete");

                    return isMatchingDate  && isStatusMatch;
                })
                .collect(Collectors.toList());

        // Convert filtered Suggestion entities to DTOs
       
        if (filteredSuggestions.isEmpty()) {
            responseBody.put("status", false);
            responseBody.put("suggestions", filteredSuggestions);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }
        
        for (Suggestion suggestion : filteredSuggestions) {
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
        responseBody.put("status", true);
         responseBody.put("suggestions", suggestionResponseDTOs);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    	 }
    	 else {
    		   responseBody.put("status", false);
               responseBody.put("message", "User not valid");
               return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);	 
               }
    	 
    }
    
    private SuggestionResponseDTO convertToDto(Suggestion suggestion) {
        // Implement conversion logic here
    	
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
         suggestionResponseDTO.setEdit(suggestion.isEdit());
    	
        return suggestionResponseDTO;
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
            suggestionResponseDTO.setEdit(suggestion.isEdit());
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
            suggestionResponseDTO.setEdit(suggestion.isEdit());
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
