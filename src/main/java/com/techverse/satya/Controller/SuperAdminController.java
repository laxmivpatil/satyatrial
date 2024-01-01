package com.techverse.satya.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techverse.satya.DTO.AdminDTO;
import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Service.AdminService;

@RestController
@RequestMapping("")
public class SuperAdminController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/pendingverification")
    public ResponseEntity<?> getAdminsPendingVerification() {
        List<AdminDTO> admins = adminService.getAdminsPendingVerification();
        return ResponseEntity.ok()
                .body(new ApiDataResponse(true, "List of pending verification admin", admins));

         
    }
    
    @PutMapping("/updateverification")
    public ResponseEntity<?> updateVerificationStatus(
            @RequestParam Long adminId,
            @RequestParam String newStatus) {
    	try {
        adminService.updateVerificationStatus(adminId, newStatus);
        return ResponseEntity.ok()
                .body(new ApiDataResponse(true, "Status Changed successfully ", "adminId=>"+adminId+", status=>" +newStatus));

    	}
    	catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiDataResponse(false, "Failed to change admin status. Please try again later.", null));
        }
    }

    @DeleteMapping("/delete-admin/{adminId}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}



