package com.techverse.satya.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techverse.satya.Model.Admin;
import com.techverse.satya.Model.OtpEntity;
import com.techverse.satya.Model.SubAdmin;
import com.techverse.satya.Model.Users;
import com.techverse.satya.Repository.AdminRepository;
import com.techverse.satya.Repository.OtpRepository;
import com.techverse.satya.Repository.SubAdminRepository;
import com.techverse.satya.Repository.UserRepository;
import org.springframework.security.core.userdetails.User;

@Service("customUserDetailsService")

public class CustomUserDetailsService implements UserDetailsService {

   
	
	@Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private SubAdminRepository subAdminRepository;
    
    @Autowired
    private OtpRepository otpRepository;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = userRepository.findByPhoneNumber(username);
        Optional<Admin> adminOptional = adminRepository.findByMobileNumber(username);
        Optional<SubAdmin> subAdminOptional = subAdminRepository.findByMobileNumber(username);
        
        Optional<OtpEntity> otpEntityOptional = otpRepository.findByPhoneNumber(username);
 System.out.println("hsjhgjsdjgfsgdhdsgf");
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return buildUserDetails(user.getPhoneNumber(), otpEntityOptional.get().getOtp(), "ROLE_USER");
        } else if (subAdminOptional.isPresent()) {
            SubAdmin subAdmin = subAdminOptional.get();
            return buildUserDetails(subAdmin.getMobileNumber(), otpEntityOptional.get().getOtp(), "ROLE_SUBADMIN");
        }
        else if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return buildUserDetails(admin.getMobileNumber(), otpEntityOptional.get().getOtp(), "ROLE_ADMIN");
        }else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private UserDetails buildUserDetails(String username, String password, String role) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return new User(username, password, authorities);
    }
}
