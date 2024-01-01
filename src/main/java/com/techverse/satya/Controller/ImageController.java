package com.techverse.satya.Controller;

import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("")
public class ImageController {

    @Value("${azure.storage.account-name}")
    private String storageAccountName;

    @Value("${azure.storage.container-string}")
    private String storageAccountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Construct Azure Blob Storage URL
            String blobServiceUrl = String.format("https://satyastorage.blob.core.windows.net");
            String blobContainerUrl = String.format("%s/%s", blobServiceUrl, containerName);

            // Upload the image to Azure Blob Storage
            new BlobClientBuilder()
                    .connectionString(storageAccountKey)
                    .containerName(containerName)
                    .blobName(file.getOriginalFilename())
                    .buildClient()
                    .upload(file.getInputStream(), file.getSize(), true);

            // Return the URL of the uploaded image
            String imageUrl = String.format("%s/%s", blobContainerUrl, file.getOriginalFilename());
            return ResponseEntity.ok("Image uploaded successfully. Image URL: " + imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }
}
