package com.techverse.satya.Config;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techverse.satya.Security.JwtAuthenticationEntryPoint;
import com.techverse.satya.Security.JwtAuthenticationFilter;

 
 
 
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
 
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()  
            
            .antMatchers("/api/pincode/upload").permitAll()  
            .antMatchers("/send-sms").permitAll()  
            .antMatchers("/user/setstatusvideo").permitAll()  
            .antMatchers("/user/setstatusvideo1").permitAll()  
            .antMatchers("/user/getstatusvideo").permitAll()  
            .antMatchers("/pendingverification").permitAll()   
            .antMatchers("/updateverification").permitAll()   
            .antMatchers("/user/generateOtp").permitAll()   //done
            .antMatchers("/subadmin/generateOtp").permitAll()   //done
             .antMatchers("/user/checkuserbymobileno").permitAll()		//done
             .antMatchers("/user/firstlogin").permitAll()    
            .antMatchers("/user/login").permitAll() 
            .antMatchers("/user/logout").hasRole("USER")
            .antMatchers("/user/byConstitution").hasRole("USER")
            .antMatchers("/user/getcity").permitAll() 
            .antMatchers("/admin/getcity").permitAll() 
            .antMatchers("/user/constitutions").permitAll() 
            .antMatchers("/user/update").hasRole("USER")
            .antMatchers("/user/edit").hasRole("USER")
            .antMatchers("/user/getuser").hasRole("USER")
            .antMatchers("/admin/checkadminbymobileno").permitAll()
            .antMatchers("/admin/generateOtp").permitAll()   //done
            .antMatchers("/admin/firstlogin").permitAll() 
            .antMatchers("/admin/login").permitAll()     
            .antMatchers("/admin/logout").hasRole("ADMIN")
            .antMatchers("/user/getcity/{pincode}").permitAll() 
            .antMatchers("/admin/updateProfile").permitAll()
            .antMatchers("/admin/edit").hasRole("ADMIN")
            .antMatchers("/admin/editaddresses").hasRole("ADMIN")
            .antMatchers("/admin/addSubAdmin").hasRole("ADMIN")
            .antMatchers("/admin/getallsubadmin").hasRole("ADMIN")
            .antMatchers("/admin/deletesubadmin").hasRole("ADMIN")
            .antMatchers("/subadmin/login").permitAll()
            .antMatchers("/subadmin/get").hasRole("SUBADMIN")
            .antMatchers("/user/suggestions/add").hasRole("USER")
            .antMatchers("/user/suggestions/all").hasRole("USER")
            .antMatchers("/user/suggestions").hasRole("USER")
            .antMatchers("/user/suggestions/delete").hasRole("USER")
            .antMatchers("/admin/suggestions/all").hasAnyRole("ADMIN","SUBADMIN")
            .antMatchers("/admin/suggestions/today").hasAnyRole("ADMIN","SUBADMIN")
            .antMatchers("/admin/suggestions/past").hasAnyRole("ADMIN","SUBADMIN")
             
            .antMatchers("/admin/suggestions").hasAnyRole("ADMIN","SUBADMIN")
            .antMatchers("/admin/getaddresses").hasAnyRole("ADMIN","SUBADMIN")
                 .antMatchers("/admin/timeslots/create").hasAnyRole("ADMIN","SUBADMIN")
                 .antMatchers("/admin/timeslots/getappointmentsbymonth").hasAnyRole("ADMIN","SUBADMIN")
                  .antMatchers("/admin/timeslots/all").hasAnyRole("ADMIN","SUBADMIN")
                 .antMatchers("/admin/timeslots/allbymonthyear").hasAnyRole("ADMIN","SUBADMIN")
                 .antMatchers("/admin/timeslots/delete/{id}").hasAnyRole("ADMIN","SUBADMIN")
                  
                 .antMatchers("/user/notifications/unread").hasRole("USER")
                 
                 
                .antMatchers("/user/timeslots/get").hasRole("USER")
                .antMatchers("/user/timeslots/address").hasRole("USER")
                .antMatchers("/user/appointments/todaybyuserid").hasRole("USER")
                .antMatchers("/user/appointments/upcomingbyuserid").hasRole("USER")
                .antMatchers("/user/appointments/pastbyuserid").hasRole("USER")
                .antMatchers("/user/appointments/pendingbyuserid").hasRole("USER")
                .antMatchers("/user/appointments/canceledbyuserid").hasRole("USER")
                .antMatchers("/user/appointments/byappointmentid").hasRole("USER")
                .antMatchers("/user/appointments/delete").hasRole("USER")
                .antMatchers("/user/appointments/reschedule").hasRole("USER")
                .antMatchers("/user/appointments/in-progress/{appointmentId}").hasRole("USER")  
                .antMatchers("/admin/appointments/todaybyadminid").hasAnyRole("ADMIN","SUBADMIN")
                  
                .antMatchers("/admin/appointments/upcomingbyadminid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/pastbyadminid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/pendingbyadminid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/canceledbyadminid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/byappointmentid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/byType").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/todaybytype").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/upcomingbytype").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/pastbytype").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/pendingbytype").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/canceledbytype").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/byappointmentid").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/cancel").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/appointments/reschedule").hasAnyRole("ADMIN","SUBADMIN")
                .antMatchers("/admin/notifications/unread").hasAnyRole("ADMIN","SUBADMIN")
                
                
                .antMatchers("/subadmin/findbymobileno").permitAll()
                
                
                
                
                .antMatchers("/superadmin/create-admin").permitAll()
                
                
                .antMatchers("/subadmin/logout").hasRole("SUBADMIN")
                

                
                .antMatchers("/login/generateOtp").permitAll() 
                .antMatchers("/signup/generateOtp").permitAll() 
                .antMatchers("/signup/**").permitAll()            
                .antMatchers("/update").permitAll() 
                 .antMatchers("/send-whatsapp").permitAll()
                .antMatchers("/send-update").permitAll()
                .antMatchers("/notification/token").permitAll()
                
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(point)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
   
}


