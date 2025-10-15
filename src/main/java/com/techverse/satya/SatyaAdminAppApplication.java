package com.techverse.satya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 

@SpringBootApplication
public class SatyaAdminAppApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(SatyaAdminAppApplication.class, args);
		String serviceAccount = System.getenv("FIREBASE_SERVICE_ACCOUNT");
	    if (serviceAccount != null) {
	        System.out.println("✅ Environment variable is accessible!");
	        System.out.println("Length: " + serviceAccount.length());
	    } 
	}
	
	 
}
