package com.techverse.satya.Service;
 
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobServiceClientBuilder;  
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID; 
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile; 


import com.techverse.satya.Repository.UserRepository; 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class StorageService {

	@Autowired
	UserRepository userRepository;
	 @Value("${azure.storage.account-name}")
	    private String storageAccountName;

	    @Value("${azure.storage.container-string}")
	    private String container_string;

	    @Value("${azure.storage.container-name}")
	    private String containerName;
 
 
  private String uploadDir="F:\\MyProject\\SatyaAdminApp\\Images";
  public String uploadFileOnAzure1(MultipartFile file) {
	    // Normalize file name
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	    try {
	        // Copy file to the target location
	        String filePath = uploadDir + File.separator + fileName;
	        File targetFile = new File(filePath);

	        // Log the file path for debugging
	        System.out.println("File Path: " + filePath);

	        // Transfer the file to the target location
	        file.transferTo(targetFile);

	        return filePath;
	    } catch (IOException ex) {
	        // Log the exception for debugging
	        ex.printStackTrace();
	        throw new RuntimeException("Could not store file: " + fileName, ex);
	    }
	}
  
  
  
  
  


  public String uploadVideoToBlobStorage(MultipartFile videoFile) {
      try {
          // Construct Azure Blob Storage URL
          String blobServiceUrl = "https://satyaprofilestorage.blob.core.windows.net";
          String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);
          System.out.println("hi");
          // Generate a unique name using timestamp and UUID
          String uniqueBlobName = UUID.randomUUID().toString();
          System.out.println("hi1");
          
          // Create BlobClient with connection string
          BlobClientBuilder blobClientBuilder = new BlobClientBuilder().connectionString(container_string)
                  .containerName(containerName).blobName(uniqueBlobName);
          System.out.println("hi2");
          
          // Upload the video file to Azure Blob Storage
          try (InputStream inputStream = videoFile.getInputStream()) {
              blobClientBuilder.buildClient().upload(inputStream, videoFile.getSize(), true);
          }
          System.out.println("hi3");
          
          OffsetDateTime expiryTime = OffsetDateTime.of(2099, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
          System.out.println("hi4");
          
            // Assign read permissions to the SAS token
          BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

          // Set the start time for the SAS token (optional)
          OffsetDateTime startTime = OffsetDateTime.now().minusMinutes(5);

          BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                  .setStartTime(startTime);

          // Generate SAS token for the blob
          String sasToken = blobClientBuilder.buildClient().generateSas(sasSignatureValues);

          // Return the URL of the uploaded video with the SAS token
          String videoUrlWithSas = String.format("%s/%s?%s", blobContainerUrl, uniqueBlobName, sasToken);
          System.out.println();
          return videoUrlWithSas;
      } catch (IOException e) {
          e.printStackTrace();
          return null;
      }
  }
  
  
  
  public String uploadImgOnAzure(File file) {
	  
	  try {
          // Construct Azure Blob Storage URL
          String blobServiceUrl = "https://satyaprofilestorage.blob.core.windows.net";
          String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);

          // Get the original file extension
          String originalFileName = file.getName();
          String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

          // Generate a unique name using timestamp and UUID
          String uniqueBlobName = Instant.now().toEpochMilli() + "_" + UUID.randomUUID().toString() + fileExtension;

          // Create BlobClient with connection string
          BlobClientBuilder blobClientBuilder = new BlobClientBuilder().connectionString(container_string)
                  .containerName(containerName).blobName(uniqueBlobName);

          // Upload the file to Azure Blob Storage with the unique blob name
          try (InputStream inputStream = new FileInputStream(file)) {
              blobClientBuilder.buildClient().upload(inputStream, file.length(), true);
          }

          // Create a SAS token that's valid for 1 hour (adjust duration as needed)
          // Create a SAS token without expiration time
          OffsetDateTime expiryTime = OffsetDateTime.of(2099, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

          
          // Assign read permissions to the SAS token
          BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

          // Set the start time for the SAS token (optional)
          OffsetDateTime startTime = OffsetDateTime.now().minusMinutes(5);

          BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                  .setStartTime(startTime);

          // Generate SAS token for the blob
          String sasToken = blobClientBuilder.buildClient().generateSas(sasSignatureValues);

          // Return the URL of the uploaded file with the SAS token
          String fileUrlWithSas = String.format("%s/%s?%s", blobContainerUrl, uniqueBlobName, sasToken);
          return fileUrlWithSas;
      } catch (IOException e) {
          e.printStackTrace();
          return null;
      }
  }

  
  public String uploadFileOnAzure(MultipartFile file) {
      try {
          // Construct Azure Blob Storage URL
          String blobServiceUrl = "https://satyaprofilestorage.blob.core.windows.net";
          String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);

          // Get the original file extension
          String originalFileName = file.getOriginalFilename();
          String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

          // Generate a unique name using timestamp and UUID
          String uniqueBlobName = Instant.now().toEpochMilli() + "_" + UUID.randomUUID().toString() + fileExtension;

          // Create BlobClient with connection string
          BlobClientBuilder blobClientBuilder = new BlobClientBuilder().connectionString(container_string)
                  .containerName(containerName).blobName(uniqueBlobName);

          // Upload the file to Azure Blob Storage with the unique blob name
          try (InputStream inputStream = file.getInputStream()) {
              blobClientBuilder.buildClient().upload(inputStream, file.getSize(), true);
          }

          // Create a SAS token that's valid for 1 hour (adjust duration as needed)
          // Create a SAS token without expiration time
          OffsetDateTime expiryTime = OffsetDateTime.of(2099, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

          
          // Assign read permissions to the SAS token
          BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);

          // Set the start time for the SAS token (optional)
          OffsetDateTime startTime = OffsetDateTime.now().minusMinutes(5);

          BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                  .setStartTime(startTime);

          // Generate SAS token for the blob
          String sasToken = blobClientBuilder.buildClient().generateSas(sasSignatureValues);

          // Return the URL of the uploaded file with the SAS token
          String fileUrlWithSas = String.format("%s/%s?%s", blobContainerUrl, uniqueBlobName, sasToken);
          return fileUrlWithSas;
      } catch (IOException e) {
          e.printStackTrace();
          return null;
      }
  }
 
  
}
