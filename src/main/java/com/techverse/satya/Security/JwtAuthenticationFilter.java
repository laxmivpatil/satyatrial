package com.techverse.satya.Security;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techverse.satya.Service.TokenBlacklistService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

      @Autowired
     private UserDetailsService userDetailsService;
 
  
     @Autowired
     private TokenBlacklistService tokenBlacklistService; // Assuming you have a service for token blacklisting

    @Autowired
    private JwtHelper jwtHelper;
 

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	   String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
         
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7); // Remove "Bearer " to get the token
            System.out.println(requestHeader);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
             } catch (IllegalArgumentException e) {
                 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.println("{\"error\": \"Bad Request\", \"message\": \"Invalid JWT token.\"}");
                return;
            } catch (ExpiredJwtException e) {
                 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.println("{\"error\": \"Unauthorized\", \"message\": \"JWT token has expired.\"}");
                return;
            } catch (MalformedJwtException e) {
                 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.println("{\"error\": \"Bad Request\", \"message\": \"Malformed JWT token.\"}");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	 if (tokenBlacklistService.isTokenBlacklisted(token)) {
        		  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.println("{\"error\": \"Unauthorized\", \"message\": \"Token invalid.\"}");
                return;
            }
        	 UserDetails userDetails = userDetailsService.loadUserByUsername(username);
             	     
        	//UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Check if the user has the necessary role to access the resource
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            boolean hasAdminRole = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
            boolean hasSuperAdminRole = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_SUPERADMIN"));
            boolean hasSubAdminRole = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_SUBADMIN"));
            
            boolean hasUserRole = authorities.stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
            
            if (hasAdminRole || hasSuperAdminRole ||hasUserRole||hasSubAdminRole) {
                // Valid user with required role, proceed with authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.println("{\"error\": \"Forbidden\", \"message\": \"User does not have the necessary role to access the resource.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
