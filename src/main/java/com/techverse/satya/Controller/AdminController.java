package com.techverse.satya.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techverse.satya.DTO.AddressInfoDTO;
import com.techverse.satya.DTO.AdminDTO;
import com.techverse.satya.DTO.AdminProfileRequest;
import com.techverse.satya.DTO.ApiDataResponse;
import com.techverse.satya.DTO.ApiResponse;
import com.techverse.satya.DTO.EditAdmin;
import com.techverse.satya.DTO.ResponseDTO; 
import com.techverse.satya.Model.Admin; 
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Service.AdminService;
import com.techverse.satya.Service.SubAdminService;
import com.techverse.satya.Service.UserService;
@RestController
@RequestMapping("")
public class AdminController {
	// Admin endpoints for managing resources

	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;

	@Autowired
	private SubAdminService subAdminService;

	@Autowired
	private AdminRepository adminRepository;
	@GetMapping("/user/constitutions")
	public Map<String, List<Map<String, String>>> getAllConstitutions() {
		Map<String, List<Map<String, String>>> response = new HashMap<>();
		List<Map<String, String>> constitutionList = new ArrayList<>();
		List<String> constitutions = adminRepository.findAllConstitutions();
		for (String constitution : constitutions) {
			Map<String, String> constitutionMap = new HashMap<>();
			constitutionMap.put("name", constitution);
			constitutionList.add(constitutionMap);
		}

		response.put("constitution", constitutionList);
		return response;
	}



	@PutMapping("/admin/updateProfile")
	public ResponseEntity<Object> updateAdminProfile(@RequestHeader("Authorization") String authorizationHeader,
			@RequestPart("email") String email,
			@RequestPart("qualification") String qualification,
			@RequestPart("homeAddress") String homeAddress,
			@RequestPart("pincode") String pincode,
			@RequestPart("city") String city,
			@RequestPart("constitution") String constitution,

			@RequestPart("state") String state,
			@RequestPart("officeAddress") String officeAddress,
			@RequestPart("profilePhoto") MultipartFile profilePhoto,
			@RequestPart("proof") MultipartFile proof){

		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				AdminProfileRequest adminProfileRequest = new AdminProfileRequest(email,  qualification,  homeAddress,
						officeAddress,   pincode,  city, constitution,   state ); 


				Admin updatedAdmin = adminService.updateAdminProfile(admin.get().getId(), adminProfileRequest, profilePhoto,proof);
				adminProfileRequest.setId(updatedAdmin.getId());
				adminProfileRequest.setProfilePhoto(updatedAdmin.getProfilePhoto());
				adminProfileRequest.setProof(updatedAdmin.getProof());
				return ResponseEntity.ok()
						.body(new ApiDataResponse(true, "Admin profile updated successfully.", adminProfileRequest));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiDataResponse(false, "Failed to update admin profile. Please try again later.", ""));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiDataResponse(false, "Invalid token. Please login again.", ""));
		}
	}

	@PutMapping("/admin/edit")
	public ResponseEntity<Object> editAdminProfile(@RequestHeader("Authorization") String authorizationHeader,
			@RequestPart("email") String email,
			@RequestPart("qualification") String qualification,
			@RequestPart("name") String name,
			@RequestPart("phoneNumber") String phoneNumber,
			@RequestPart("profession") String profession,

			@RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto
			){

		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				EditAdmin adminProfileRequest = new EditAdmin( admin.get().getId(), name,phoneNumber,  email,
						qualification, profession);

				Admin updatedAdmin = adminService.editAdminProfile(admin.get().getId(), adminProfileRequest, profilePhoto);
				adminProfileRequest.setProfilePhoto(updatedAdmin.getProfilePhoto());
				return ResponseEntity.ok()
						.body(new ApiDataResponse(true, "Admin profile updated successfully.", adminProfileRequest));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiDataResponse(false, "Failed to update admin profile. Please try again later.", ""));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiDataResponse(false, "Invalid token. Please login again.", ""));
		}
	}

	@GetMapping("/admin/addSubAdmin")
	public ResponseEntity<Object> createSubAdmin(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam("mobileNumber") String mobileNumber){

		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				if(userService.findByPhoneNumber(mobileNumber).isPresent()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ApiResponse(false, "User Allready Present as citizen Not able to add"));

				}
				if(adminService.getAdminBymobileNo(mobileNumber).isPresent())
				{
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ApiResponse(false, "User Allready Present as a politician Not able to add"));

				}
				if(subAdminService.getSubAdminBymobileNo(mobileNumber).isPresent()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new ApiResponse(false, "sub admin Allready Present Not able to add"));

				}
				if(subAdminService.createSubAdmin(mobileNumber,admin.get())){
					return ResponseEntity.ok()
							.body(new ApiResponse(true, "SubAdmin Created Successfully"));

				}
				else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ApiResponse(false, "Failed to send message to subadmin. Please try again later."));

				}

			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse(false, "Failed to add subadmin Please try again later."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid token. Please login again."));
		}
	}

	@GetMapping("/admin/getallsubadmin")
	public ResponseEntity<Object> getSubAdminsByAdminId(@RequestHeader("Authorization") String authorizationHeader) {

		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {

				return ResponseEntity.status(HttpStatus.OK)
						.body(new ApiDataResponse(true, "Subadmin list", subAdminService.getSubAdminsByAdminId(admin.get().getId())));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse(false, "Failed to add subadmin Please try again later."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid token. Please login again."));
		}

	}

	@DeleteMapping("/admin/deletesubadmin")
	public ResponseEntity<?> deleteSubAdminById(@RequestHeader("Authorization") String authorizationHeader,@RequestParam Long subAdminId) {


		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				subAdminService.deleteSubAdminById(subAdminId);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ApiResponse(true, "Subadmin deleted Successfully"));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse(false, "Failed to delete subadmin Please try again later."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid token. Please login again."));
		}
	}

	@GetMapping("/user/byConstitution")
	public ResponseEntity<?>  getAdminsByConstitution(@RequestHeader("Authorization") String authorizationHeader,@RequestParam String constitution) {
		Optional<Users> user = userService.getUserByToken(authorizationHeader.substring(7));
		Map<String, Object> responseBody = new HashMap<String, Object>();

		if(user.isPresent()) {
			List<Admin> admins = adminService.getAdminsByConstitution(constitution);
			responseBody.put("status",true);
			responseBody.put("data",admins.stream()
					.map(admin -> {
						Map<String, Object> adminMap = Map.of(
								"id", admin.getId(),
								"profilePhoto", admin.getProfilePhoto(),
								"name", admin.getName()
								);
						return adminMap;
					})
					.collect(Collectors.toList()));
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.OK);

		}
		else {
			responseBody.put("status",false);
			responseBody.put("message","Unauthorized Access");
			return new ResponseEntity<Map<String, Object>>(responseBody, HttpStatus.UNAUTHORIZED);
		}

	}

	@GetMapping("/admin/getaddresses")
	public ResponseEntity<?> getaddresses(@RequestHeader("Authorization") String authorizationHeader ) {


		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				AddressInfoDTO addressDTO = new AddressInfoDTO();
				addressDTO.setHomeAddress(admin.get().getHomeAddress());
				addressDTO.setOfficeAddress(admin.get().getOfficeAddress());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ApiDataResponse(true, "Admin Addresses", addressDTO));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse(false, "Failed to fetch admin addresses Please try again later."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid token. Please login again."));
		}



	}
	@GetMapping("/admin/editaddresses")
	public ResponseEntity<?> editaddresses(  @RequestHeader("Authorization") String authorizationHeader,
			@RequestParam(value = "homeAddress", required = false) String homeAddress,
			@RequestParam(value = "officeAddress", required = false) String officeAddress) {


		Optional<Admin> admin = adminService.getAdminByToken(authorizationHeader.substring(7));
		if (admin.isPresent()) {
			try {
				System.out.println("hiiii");
				if (homeAddress != null) {
					admin.get().setHomeAddress(homeAddress);
				}
				if (officeAddress != null) {
					admin.get().setOfficeAddress(officeAddress);
				}
				adminRepository.save(admin.get());
				AddressInfoDTO addressDTO = new AddressInfoDTO();
				addressDTO.setHomeAddress(admin.get().getHomeAddress());
				addressDTO.setOfficeAddress(admin.get().getOfficeAddress());
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ApiDataResponse(true, "Admin Addresses", addressDTO));
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse(false, "Failed to edit admin addresses Please try again later."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(false, "Invalid token. Please login again."));
		}



	}

	//final
	@GetMapping("/admin/getadmin")
	public ResponseEntity<ResponseDTO<AdminDTO>> getUserById(@RequestHeader("Authorization") String authorizationHeader) {
		Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));

		ResponseDTO<AdminDTO> responseBody = new ResponseDTO<>();
		try {
			if (user.isPresent()) {
				responseBody.setStatus(true);
				responseBody.setMessage("Admin retrieved successfully.");
				responseBody.setData(new AdminDTO(user.get())); // Convert user object to string if needed
				return ResponseEntity.ok(responseBody);
			} else {
				responseBody.setStatus(false);
				responseBody.setMessage("Admin not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
			}
		} catch (Exception e) {
			responseBody.setStatus(false);
			responseBody.setMessage( "Failed to retrive admin.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}
	@GetMapping("/admin/adddevicetoken")
	public ResponseEntity<ResponseDTO<?>> addDeviceToken(@RequestHeader("Authorization") String authorizationHeader,@RequestParam(value="deviceToken", required=true)String token) {
		Optional<Admin> user = adminService.getAdminByToken(authorizationHeader.substring(7));

		ResponseDTO<String> responseBody = new ResponseDTO<>();
		try {
			System.out.println("hi token");
			if(user.isPresent()) {
				System.out.println("hi token");
				System.out.println("token updated");
				user.get().setDeviceToken(token);
				adminRepository.save(user.get());
				responseBody.setStatus(true);
				responseBody.setMessage("Admin device saved successfully.");
				responseBody.setData("");
				return ResponseEntity.ok(responseBody);
			} else {
				responseBody.setStatus(false);
				responseBody.setMessage("Admin not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
			}
		} catch (Exception e) {
			responseBody.setStatus(false);
			responseBody.setMessage( "Failed to retrive admin.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
		}
	}
	
	
}
