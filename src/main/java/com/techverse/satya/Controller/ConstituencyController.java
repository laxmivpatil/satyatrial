package com.techverse.satya.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techverse.satya.Model.Constituency;
import com.techverse.satya.Model.Pincode;
import com.techverse.satya.Service.ConstituencyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/constituencies")
public class ConstituencyController {
    private final ConstituencyService constituencyService;

    @Autowired
    public ConstituencyController(ConstituencyService constituencyService) {
        this.constituencyService = constituencyService;
    }

    @GetMapping
    public List<Constituency> getAllConstituencies() {
        return constituencyService.listAllConstituencies();
    }
    @GetMapping("/district")
    public ResponseEntity<Map<String, Object>> getConstituenciesByDistrict(@RequestParam String districtName) {
        try {
            List<Constituency> constituencies = constituencyService.getConstituenciesByDistrictName(districtName);
               Map<String, Object> response = new HashMap<>();
                response.put("district", districtName);
                response.put("constituency", constituencies);

                return ResponseEntity.status(HttpStatus.OK).body(response);
            
            
        } catch (Exception e) {
            // Handle the exception appropriately (log it, return an error response, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
    @PostMapping
    public List<Constituency> createConstituencies(@RequestBody Map<String, Object> request) {
        String districtName = (String) request.get("districtName");
        List<String> constituencyNames = (List<String>) request.get("constituencyNames");

        List<Constituency> constituencies = new ArrayList<>();
        for (String constituencyName : constituencyNames) {
            Constituency constituency = new Constituency(districtName, constituencyName);
            constituencies.add(constituencyService.saveConstituency(constituency));
        }

        return constituencies;
    }
/*
    @PostMapping
    public Constituency createConstituency(@RequestBody Constituency constituency) {
        return constituencyService.saveConstituency(constituency);
    }
*/
    @GetMapping("/{id}")
    public Constituency getConstituencyById(@PathVariable Long id) {
        return constituencyService.getConstituencyById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteConstituency(@PathVariable Long id) {
        constituencyService.deleteConstituency(id);
    }
}
