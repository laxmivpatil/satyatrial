package com.techverse.satya.Service;



import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.techverse.satya.DTO.SuggestionDTO;
import com.techverse.satya.DTO.SuggestionResponseDTO;
import com.techverse.satya.Model.Suggestion;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.SuggestionRepository;
import com.techverse.satya.Repository.UserRepository;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
 
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;

@Service
public class SuggestionService {

	
	@Value("${azure.storage.container-string}")
    private String container_string;


	@Autowired
    private StorageService service;
	@Autowired
	private SuggestionRepository suggestionRepository;
	

	@Autowired
	AdminNotificationService adminNotificationService;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	public SuggestionResponseDTO  addSuggestion(SuggestionDTO suggestionDTO) {
		try {
		// Retrieve user by userId from the database
		SuggestionResponseDTO suggestionResponseDTO=new SuggestionResponseDTO();
		Optional<Users> user = userRepository.findById(suggestionDTO.getUserId());
		if(user.isPresent()) {
			// Create a new suggestion
			System.out.println("jdshfjdshjfhdsjhf");
			Suggestion suggestion = new Suggestion();
			suggestion.setAddress(suggestionDTO.getAddress());
			suggestion.setPurpose(suggestionDTO.getPurpose());
			suggestion.setComment(suggestionDTO.getComment());
			suggestion.setAdmin(user.get().getAdmin());
			suggestion.setStatus("new");
			suggestion.setEditable(true);
			// Handle photo upload logic
			MultipartFile photoFile = suggestionDTO.getPhoto();
			if (photoFile != null && !photoFile.isEmpty()) {
	 	//	String photoUrl= service.uploadSuggestionPhoto(photoFile, user.get().getId()+"");
		 
				String photoUrl= service.uploadFileOnAzure(photoFile );
				suggestion.setPhotoUrl(photoUrl);
			}

			 // Handle video upload logic
			MultipartFile videoFile = suggestionDTO.getVideo();
			if (videoFile != null && !videoFile.isEmpty()) {
				// Save videoFile to storage (local disk, AWS S3, etc.)
				//String videoUrl=service.uploadSuggestionVideo(videoFile, user.get().getId()+"");
				String videoUrl= service.uploadVideoToBlobStorage(videoFile);
				
			     //String thumbnailUrl = convert(videoUrl);
		          
			 	//suggestion.setThumbnail(thumbnailUrl);
				suggestion.setVideoUrl(videoUrl);
			}

			 suggestion.setUser(user.get());
			   Instant instant = Instant.parse(Instant.now().toString());

		        // Convert Instant to LocalDateTime in a specific time zone
		        ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Choose the appropriate time zone
		        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
			suggestion.setDateTime(localDateTime);
			suggestion.setEditTime(localDateTime);
			System.out.println(suggestion);
			// Save the suggestion to the database
			suggestionRepository.save(suggestion);
		    adminNotificationService.sendSuggestionNotificationToAdmin(suggestion, user.get());
		    System.out.println(suggestion.isEditable());
			return mapToDTO(suggestion);
		}
		return suggestionResponseDTO;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		
	}

	
	public SuggestionResponseDTO  editSuggestion(Suggestion suggestion,MultipartFile photoFile,MultipartFile videoFile,String address,String purpose,String comment) {
		try {
		// Retrieve user by userId from the database
	  		 if(!address.isEmpty()) {
	 			suggestion.setAddress( address );
	 		 }
	 		 if(!purpose.isEmpty()) {
		 			suggestion.setPurpose(purpose);
		 		 }
	 		 if(!comment.isEmpty()) {
		 			suggestion.setComment(comment);
		 		 }
			 			// Handle photo upload logic
			 
			if (photoFile != null && !photoFile.isEmpty()) {
	 	//	String photoUrl= service.uploadSuggestionPhoto(photoFile, user.get().getId()+"");
		 
				String photoUrl= service.uploadFileOnAzure(photoFile );
				suggestion.setPhotoUrl(photoUrl);
			}

			 // Handle video upload logic
			 if (videoFile != null && !videoFile.isEmpty()) {
				// Save videoFile to storage (local disk, AWS S3, etc.)
				//String videoUrl=service.uploadSuggestionVideo(videoFile, user.get().getId()+"");
				String videoUrl= service.uploadVideoToBlobStorage(videoFile);
				
			     //String thumbnailUrl = convert(videoUrl);
		          
			 	//suggestion.setThumbnail(thumbnailUrl);
				suggestion.setVideoUrl(videoUrl);
			}

			    Instant instant = Instant.parse(Instant.now().toString());

		        // Convert Instant to LocalDateTime in a specific time zone
		        ZoneId zoneId = ZoneId.of("Asia/Kolkata"); // Choose the appropriate time zone
		        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
			suggestion.setEditTime(localDateTime);
			System.out.println(suggestion);
			// Save the suggestion to the database
			suggestionRepository.save(suggestion);
		     adminNotificationService.sendSuggestionNotificationToAdmin(suggestion, suggestion.getUser());
		       
			return mapToDTO(suggestion);
		 
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

		
	}
	
	
	
	public Optional<Suggestion> getSuggestionById(Long suggestionId) {
		return suggestionRepository.findById(suggestionId);
	}
	
	public List<Suggestion> getSuggestionsByUserId(Long userId) {
		return suggestionRepository.findByUser_Id(userId);
	}

	public List<Suggestion> getSuggestionsByAdminId(Long adminId) {
		return suggestionRepository.findByAdmin_Id(adminId);
	}
	public List<Suggestion> getSuggestionsByAdminIdMonthYear(Long adminId,int month,int year) {
		
		return suggestionRepository.findByAdmin_Id(adminId);
	}
	public boolean deleteSuggestion(Long suggestionId) {
	    // Check if suggestion exists
	    Optional<Suggestion> existingSuggestion = suggestionRepository.findById(suggestionId);

	    if (existingSuggestion.isPresent()) {
	        // Implement deletion logic if necessary
	    existingSuggestion.get().setStatus("delete"); 
	    	suggestionRepository.save(existingSuggestion.get());
	       
	        return true; // Indicate that the suggestion was deleted
	    } else {
	        return false; // Indicate that the suggestion was not found
	    }
	}

    public List<SuggestionResponseDTO> getTodaySuggestionsByAdminId(Long adminId) {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.now().with(LocalTime.MAX);

        List<Suggestion> todaySuggestions = suggestionRepository.findByAdmin_IdAndDateTimeBetween(adminId, todayStart, todayEnd);

        return todaySuggestions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<SuggestionResponseDTO> getPastSuggestionsByAdminId(Long adminId) {
        LocalDateTime todayStart = LocalDateTime.now().with(LocalTime.MIN);

        List<Suggestion> pastSuggestions = suggestionRepository.findByAdmin_IdAndDateTimeBefore(adminId, todayStart);

        return pastSuggestions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private SuggestionResponseDTO mapToDTO(Suggestion suggestion) {
        SuggestionResponseDTO dto = new SuggestionResponseDTO();
        dto.setName(suggestion.getUser().getName());
        dto.setAddress(suggestion.getAddress());
        dto.setPurpose(suggestion.getPurpose());
        dto.setComment(suggestion.getComment());
        dto.setPhoto(suggestion.getPhotoUrl());
        dto.setVideo(suggestion.getVideoUrl());
        dto.setStatus(suggestion.getStatus());
        dto.setEditable(suggestion.isEditable());
        dto.setDateTime(suggestion.getDateTime());
        dto.setEditTime(suggestion.getEditTime());
        dto.setProfile(userRepository.findById(suggestion.getUser().getId()).get().getProfilePphoto());
        dto.setThumbnail(suggestion.getThumbnail());
        dto.setId(suggestion.getId());
        return dto;
    }
 
    public String convert(String videoUrl) throws MalformedURLException {
		 URL url = new URL(videoUrl);
 		 String outputFile = "Images\\output.mp4";
 		try (InputStream in = url.openStream()) {
           Path outputPath = Path.of(outputFile);
           Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
       
 		 int frameNumber = 0;
 		Picture picture = FrameGrab.getFrameFromFile(
				new File(outputFile), frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		File img=new File("Images\\" + UUID.randomUUID().toString() + ".jpeg");
		ImageIO.write(bufferedImage, "jpeg", img);
		 byte[] fileContent = Files.readAllBytes(img.toPath());
		 return service.uploadImgOnAzure(img);
 		 
 		}
 		catch (Exception e) {
           e.printStackTrace();
          
       }
 		return "";
	 }
	 

 
}