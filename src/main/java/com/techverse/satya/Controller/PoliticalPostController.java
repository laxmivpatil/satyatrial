package com.techverse.satya.Controller;
 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
 
import com.techverse.satya.Model.PoliticalPost; 
import com.techverse.satya.Repository.PoliticalPostRepository;
import com.techverse.satya.Service.StorageService;

@RestController
@RequestMapping("/admin/politicalPosts")
public class PoliticalPostController {

	


	@Autowired
    private StorageService service;
    @Autowired
    private PoliticalPostRepository politicalPostRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPoliticalPosts() {
    	
    	Map<String, Object> response = new HashMap<>();
        response.put("politicalPost",politicalPostRepository.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(response);
      
    }

   /* @PostMapping
    public ResponseEntity<Map<String, Object>> createPoliticalPost(@RequestParam("name") String name,
                                               @RequestParam("symbol") MultipartFile symbolFile) {
    	String symbolUrl="";
    	 if (symbolFile != null && !symbolFile.isEmpty()) {
        symbolUrl = service.uploadFileOnAzure(symbolFile);
    	} 

        PoliticalPost politicalPost = new PoliticalPost();
        politicalPost.setName(name);
        politicalPost.setSymbol(symbolUrl); // Save the URL or file path
        politicalPostRepository.save(politicalPost);
        Map<String, Object> response = new HashMap<>();
        response.put("politicalPost",politicalPost);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }*/
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createPoliticalPosts(@RequestBody Map<String, Object> requestBody) {
        List<String> names = (List<String>) requestBody.get("names");
        String symbolUrl = (String) requestBody.get("symbolUrl");

        List<PoliticalPost> createdPosts = new ArrayList<>();

        for (String name : names) {
            PoliticalPost politicalPost = new PoliticalPost();
            politicalPost.setName(name);
            politicalPost.setSymbol(symbolUrl);
            // Assuming politicalPostRepository is an instance of your repository
            politicalPostRepository.save(politicalPost);
            createdPosts.add(politicalPost);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("politicalPosts", createdPosts);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
