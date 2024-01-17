package com.techverse.satya.Config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;



@Configuration 
public class KeyVaultConfig {
	/* @Bean
	    public AzureSecretsManagerPropertySourceLocator azureSecretsManagerPropertySourceLocator(
	            AzureSecretsManagerProperties azureSecretsManagerProperties) {
	        return new AzureSecretsManagerPropertySourceLocator(azureSecretsManagerProperties);
	    }*/
	
	 @Bean
	    public StandardServletMultipartResolver multipartResolver() {
	        return new StandardServletMultipartResolver();
	    }
	
}
