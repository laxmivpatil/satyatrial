package com.techverse.satya.Controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;
 

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.jcodec.common.io.NIOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	
	  @GetMapping("/generate-thumbnail")
    public String generateThumbnail(@RequestParam("videoPath") String videoUrl) throws Exception {
		  int frameNumber = 0;
			DirectoryStream.Filter<Path> filter = file -> {
				return file.toString().endsWith(".mp4") || file.toString().endsWith(".MP4")
						|| file.toString().endsWith(".mov") || file.toString().endsWith(".MOV");
			};
			Path dirName = Paths.get("F:\\MyProject\\Woof\\Files");
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirName, filter)) {
				stream.forEach(path -> {
					try {
						Picture picture = FrameGrab.getFrameFromFile(
								new File(path.toString()), frameNumber);
						BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
						ImageIO.write(bufferedImage, "png", new File(
								"F:\\MyProject\\Woof\\Files" + UUID.randomUUID().toString() + ".png"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return "fbgmhdfghj";
	    }
	
	
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
	          	String thumbnailUrl=convert(videoUrl);	  
	          	s.get().setThumbnail(thumbnailUrl);
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
	  
	 public String convert(String videoUrl) throws MalformedURLException {
		 URL url = new URL(videoUrl);
  		 String outputFile = "F:\\MyProject\\Woof\\Files\\output.mp4";
  		try (InputStream in = url.openStream()) {
            Path outputPath = Path.of(outputFile);
            Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
        
  		 int frameNumber = 0;
  		Picture picture = FrameGrab.getFrameFromFile(
				new File(outputFile), frameNumber);
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		File img=new File("F:\\MyProject\\Woof\\Files\\" + UUID.randomUUID().toString() + ".png");
		ImageIO.write(bufferedImage, "png", img);
		 byte[] fileContent = Files.readAllBytes(img.toPath());
		 return service.uploadImgOnAzure(img);
  		 
  		}
  		catch (Exception e) {
            e.printStackTrace();
           
        }
  		return "";
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
	    
	/*  
	 @GetMapping("/user/getstatusvideo")
	    public ResponseEntity<?> getVideo() {
	        Map<String, Object> responseBody = new HashMap<>();

	        try {
	            Long id = 1L;
	            Optional<Status> s = statusRepository.findById(id);
	            if (s.isPresent()) {
	                URL url = new URL(s.get().getVideoUrl());
	                String outputFile = "F:\\MyProject\\Woof\\Files\\output.mp4";
	                String outputDirectory = "F:\\MyProject\\Woof\\Files\\frames\\";

	                // Download video
	                try (InputStream in = url.openStream()) {
	                    Path outputPath = Path.of(outputFile);
	                    Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
	                }

	                // Extract frames and save as images
	                List<String> imagePaths = extractFrames(outputFile, outputDirectory);
	                responseBody.put("imagePaths", imagePaths);

	                responseBody.put("status", true);
	                responseBody.put("message", "Frames retrieved successfully");
	                responseBody.put("VideoStatus", s.get());
	                return new ResponseEntity<>(responseBody, HttpStatus.OK);
	            } else {
	                responseBody.put("status", false);
	                responseBody.put("message", "Failed to get status");
	                return new ResponseEntity<>(responseBody, HttpStatus.OK);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            responseBody.put("status", false);
	            responseBody.put("message", "Failed to get status");
	            return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    private List<String> extractFrames(String videoFilePath, String outputDirectory) throws IOException, JCodecException {
	        int frameNumber = 0;
	        List<String> imagePaths = new ArrayList<>();

	        while (true) {
	            Picture picture = FrameGrab.getFrameFromFile(new File(videoFilePath), frameNumber++);
	            if (picture == null) {
	                break; // No more frames
	            }

	            BufferedImage bufferedImage = PictureToBufferedImage(picture);
	            String imagePath = saveFrameAsImage(bufferedImage, outputDirectory, frameNumber);
	            imagePaths.add(imagePath);
	        }

	        return imagePaths;
	    }

	    private BufferedImage PictureToBufferedImage(Picture picture) {
	        // ... (same as before)
	    	
	    	return AWTUtil.toBufferedImage(picture);
	    }

	    private String saveFrameAsImage(BufferedImage image, String outputDirectory, int frameNumber) throws IOException {
	        String imagePath = outputDirectory + "frame" + frameNumber + ".png";
	        ImageIO.write(image, "png", new File(imagePath));
	        return imagePath;
	    }
	    */
}
