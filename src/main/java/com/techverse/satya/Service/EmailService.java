package com.techverse.satya.Service;

import java.time.LocalDateTime;
import java.util.Properties;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import com.techverse.satya.Model.OtpEntity;
import com.techverse.satya.Repository.OtpRepository;
 

@Service
public class EmailService {
	
	 
	 @Autowired
	    private OtpRepository otpRepository;
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	 private String senderEmail="laxmipatil070295@gmail.com";
	
 
     private String senderPassword= "mfaxitwotgaxhkst";
	
	 
     private String host= "smtp.gmail.com";
	
	 
     int port=587;
	
	public boolean sendEmail(String recipientEmail,String OTP)
	{
		 OtpEntity otpEntity = new OtpEntity(recipientEmail, passwordEncoder.encode(OTP), LocalDateTime.now().plusMinutes(5));
         otpRepository.save(otpEntity);
        
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a mail session with the specified properties
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        }); 
        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Techverse Email Verification ");
            message.setText("Your OTP for satya application is ."+OTP);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
            return true;

        } catch (MessagingException e) {
        	
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
	
	public boolean sendEmail(String recipientEmail,String emailSubject, String emailBody)
	{
		Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create a mail session with the specified properties
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(emailSubject);
            message.setContent(emailBody, "text/html");
            // message.setContent();

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully.");
            return true;

        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            return false;
        }
    }
	 

}

