package com.techverse.satya.Controller;

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

import com.techverse.satya.Model.PoliticalParty;
import com.techverse.satya.Repository.PoliticalPartyRepository;
import com.techverse.satya.Service.StorageService;

@RestController
@RequestMapping("/admin/politicalParties")
public class PoliticalPartyController {

	


	@Autowired
    private StorageService service;
    @Autowired
    private PoliticalPartyRepository politicalPartyRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPoliticalParties() {
    	
    	Map<String, Object> response = new HashMap<>();
        response.put("politicalParty",politicalPartyRepository.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(response);
      
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPoliticalParty(@RequestParam("name") String name,
                                               @RequestParam("symbol") MultipartFile symbolFile) {
    	String symbolUrl="";
    	if (symbolFile != null && !symbolFile.isEmpty()) {
        symbolUrl = service.uploadFileOnAzure(symbolFile);
    	}

        PoliticalParty politicalParty = new PoliticalParty();
        politicalParty.setName(name);
        politicalParty.setSymbol(symbolUrl); // Save the URL or file path
        politicalPartyRepository.save(politicalParty);
        Map<String, Object> response = new HashMap<>();
        response.put("politicalParty",politicalParty);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}