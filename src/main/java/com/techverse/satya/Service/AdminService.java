package com.techverse.satya.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.DTO.AdminDTO;
import com.techverse.satya.DTO.AdminProfileRequest;
import com.techverse.satya.DTO.EditAdmin;
import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.JWTRequestAdmin;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Repository.SubAdminRepository;
import com.techverse.satya.Security.JwtHelper;

@Service
public class AdminService {
	
	String uploadPathurl="F:\\MyProject\\SatyaAdminApp\\Images\\";
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SubAdminRepository subAdminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    StorageService service; 
    
    

    @Autowired
    private JwtHelper jwtHelper;
	
    
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    		
    @PostConstruct
    public void init() {
        createDefaultSuperAdmin();
    }
    
    public List<AdminDTO> getAdminsPendingVerification() {
        List<Admin> admins = adminRepository.findByVerification("pending"); // Assuming you have a method in your repository for this

        return admins.stream()
                .map(AdminDTO::new) // Use the AdminDTO constructor to convert Admin to AdminDTO
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateVerificationStatus(Long adminId, String newStatus) {
        adminRepository.updateVerificationStatus(adminId, newStatus);
    }

    public void deleteAdmin(Long adminId) {
        adminRepository.deleteById(adminId);
    }

    public void createDefaultSuperAdmin() {
        logger.debug("Creating default super admin...");
        Optional<Admin> existingAdmin = adminRepository.findByUsername("superadmin");
        if (existingAdmin.isEmpty()) {
            Admin superAdmin = new Admin();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("superadminpassword"));

            // Add role to the admin
            superAdmin.getRoles().add("ROLE_SUPERADMIN");

            adminRepository.save(superAdmin);
            logger.debug("Default super admin created successfully.");
        } else {
            logger.debug("Default super admin already exists.");
        }
    }

    public Admin createAdmin(JWTRequestAdmin jwt) {
         Admin admin = new Admin();
       // admin.setUsername(username);
    //    admin.setPassword(passwordEncoder.encode(password));
         admin.setMobileNumber(jwt.getMobileNo());
        admin.setOtp(passwordEncoder.encode(jwt.getOtp()));
        admin.setProfession(jwt.getProfession());
        admin.setParty(jwt.getParty());
        admin.setName(jwt.getName());
        admin.setGender(jwt.getGender());
         admin.getRoles().add("ROLE_ADMIN");
         LocalDate currentDate = LocalDate.now();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
         // Format the current date and print it
         String formattedDate = currentDate.format(formatter);
         admin.setJoineddate(formattedDate);
        return adminRepository.save(admin);
    }
    public Optional<Admin> getAdminBymobileNoOrEmail(String mobileNoOrEmail) {
        return adminRepository.findByMobileNumberOrEmail(mobileNoOrEmail);
    }
    public Optional<Admin> getAdminBymobileNo(String mobileNo) {
        return adminRepository.findByMobileNumber(mobileNo);
    }
    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    public Optional<Admin> getAdminByToken(String token) {
    	Optional<Admin> admin;
    	String mobileNo=jwtHelper.getUsernameFromToken(token);
    	 Optional<SubAdmin> subAdmin=subAdminRepository.findByMobileNumber(mobileNo);
    	 if(subAdmin.isPresent()) {
    		 admin=Optional.of(subAdmin.get().getAdmin());
    	 }
    	 else {
    		 admin=adminRepository.findByMobileNumber(mobileNo);
    	 }
   
	//	 System.out.println("hi "+userName);
		
        return  admin;
    }
    
    public Optional<SubAdmin> getAdminByToken1(String token) {
    	String mobileNo=jwtHelper.getUsernameFromToken(token);
    	Optional<SubAdmin> subAdmin=subAdminRepository.findByMobileNumber(mobileNo);
    	  
     
	//	 System.out.println("hi "+userName);
		
        return  subAdmin;
    }
    
    public Admin updateAdminProfile(Long adminId, AdminProfileRequest adminProfileRequest, MultipartFile profilePhoto,MultipartFile proof) throws IOException {
        // Retrieve the existing admin by adminId from the database
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // Update admin profile details from adminProfileRequest
            
            admin.setEmail(adminProfileRequest.getEmail());
            admin.setQualification(adminProfileRequest.getQualification());
            admin.setHomeAddress(adminProfileRequest.getHomeAddress());
            admin.setOfficeAddress(adminProfileRequest.getOfficeAddress());
            admin.setPincode(adminProfileRequest.getPincode());
            admin.setCity(adminProfileRequest.getCity());
            admin.setConstitution(adminProfileRequest.getConstitution());
             admin.setState(adminProfileRequest.getState());
             admin.setVerification("pending");

            // Handle profile photo update if provided
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                // Implement logic to handle profile photo upload, save to storage, etc.
                // For example, you can save the file to a specific directory or cloud storage.
                // Update admin's profile photo URL in the database accordingly.
                // admin.setProfilePhotoUrl(savedProfilePhotoUrl);
            	String path=service.uploadFileOnAzure(profilePhoto);
            	admin.setProfilePhoto(path);
            	            }

            // Handle profile photo update if provided
            if (proof != null && !proof.isEmpty()) {
                // Implement logic to handle profile photo upload, save to storage, etc.
                // For example, you can save the file to a specific directory or cloud storage.
                // Update admin's profile photo URL in the database accordingly.
                // admin.setProfilePhotoUrl(savedProfilePhotoUrl);
            	String path=service.uploadFileOnAzure(proof);
            	admin.setProof(path);
            	            }

            // Save the updated admin to the database
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } else {
            // Admin not found with the given ID, handle the error accordingly
            throw new EntityNotFoundException("Admin not found with ID: " + adminId);
        }
    }
    
    public Admin editAdminProfile(Long adminId, EditAdmin adminProfileRequest, MultipartFile profilePhoto) throws IOException {
        // Retrieve the existing admin by adminId from the database
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // Update admin profile details from adminProfileRequest
             admin.setEmail(adminProfileRequest.getEmail());
            admin.setQualification(adminProfileRequest.getQualification());
          admin.setMobileNumber(adminProfileRequest.getPhoneNumber());
          admin.setProfession(adminProfileRequest.getOccupation());
          admin.setName(adminProfileRequest.getName());
          
              

            // Handle profile photo update if provided
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                // Implement logic to handle profile photo upload, save to storage, etc.
                // For example, you can save the file to a specific directory or cloud storage.
                // Update admin's profile photo URL in the database accordingly.
                // admin.setProfilePhotoUrl(savedProfilePhotoUrl);
            	String path=service.uploadFileOnAzure(profilePhoto);
            	admin.setProfilePhoto(path);
            	            }

            // Handle profile photo update if provided
            

            // Save the updated admin to the database
            Admin updatedAdmin = adminRepository.save(admin);
            return updatedAdmin;
        } else {
            // Admin not found with the given ID, handle the error accordingly
            throw new EntityNotFoundException("Admin not found with ID: " + adminId);
        }
    }
    
    public List<Admin> getAdminsByConstitution(String constitution) {
        return adminRepository.findByConstitution(constitution);
    }
    private boolean isPhoneNumber(String input) {
        // Use a regular expression for phone number validation
        String phoneRegex = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isEmail(String input) {
        // Use a regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    // Other methods for managing admins...
}

