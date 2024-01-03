package com.techverse.satya.Config;

 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
            .antMatchers("/pendingverification").permitAll()   
            .antMatchers("/updateverification").permitAll()   
            .antMatchers("/user/generateOtp").permitAll()   //done
             .antMatchers("/user/checkuserbymobileno").permitAll()		//done
             .antMatchers("/user/firstlogin").permitAll()    
            .antMatchers("/user/login").permitAll() 
            .antMatchers("/user/byConstitution").hasRole("USER")
            .antMatchers("/user/getcity").permitAll() 
            .antMatchers("/user/constitutions").permitAll() 
            .antMatchers("/user/update").hasRole("USER")
            .antMatchers("/user/edit").hasRole("USER")
            .antMatchers("/user/getuser").hasRole("USER")
            .antMatchers("/admin/checkadminbymobileno").permitAll()
            .antMatchers("/admin/generateOtp").permitAll()   //done
            .antMatchers("/admin/firstlogin").permitAll() 
            .antMatchers("/admin/login").permitAll()          
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
            .antMatchers("/admin/suggestions/all").hasRole("ADMIN")
            .antMatchers("/admin/suggestions/today").hasRole("ADMIN")
            .antMatchers("/admin/suggestions/past").hasRole("ADMIN")
            .antMatchers("/admin/suggestions").hasRole("ADMIN")
            .antMatchers("/admin/getaddresses").hasRole("ADMIN")
                 .antMatchers("/admin/timeslots/create").hasRole("ADMIN")
                 .antMatchers("/admin/timeslots/getappointmentsbymonth").hasRole("ADMIN")
                  .antMatchers("/admin/timeslots/all").hasRole("ADMIN")
                 .antMatchers("/admin/timeslots/allbymonthyear").hasRole("ADMIN")
                 .antMatchers("/admin/timeslots/delete/{id}").hasRole("ADMIN")
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
                .antMatchers("/admin/appointments/todaybyadminid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/upcomingbyadminid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/pastbyadminid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/pendingbyadminid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/canceledbyadminid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/byappointmentid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/byType").hasRole("ADMIN")
                .antMatchers("/admin/appointments/todaybytype").hasRole("ADMIN")
                .antMatchers("/admin/appointments/upcomingbytype").hasRole("ADMIN")
                .antMatchers("/admin/appointments/pastbytype").hasRole("ADMIN")
                .antMatchers("/admin/appointments/pendingbytype").hasRole("ADMIN")
                .antMatchers("/admin/appointments/canceledbytype").hasRole("ADMIN")
                .antMatchers("/admin/appointments/byappointmentid").hasRole("ADMIN")
                .antMatchers("/admin/appointments/cancel").hasRole("ADMIN")
                .antMatchers("/admin/appointments/reschedule").hasRole("ADMIN")
                .antMatchers("/admin/notifications/unread").hasRole("ADMIN")
                .antMatchers("/superadmin/create-admin").permitAll()
                
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


