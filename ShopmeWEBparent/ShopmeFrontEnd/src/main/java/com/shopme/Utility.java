package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.customer.CustomerSettingBag;

public class Utility {
	
	public static String getSitURL(HttpServletRequest request) {
		String siteURL =  request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	} 

	
	public static JavaMailSenderImpl preperMailServer(CustomerSettingBag  email) {
		
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		
		javaMailSenderImpl.setHost(email.getHost());
		javaMailSenderImpl.setPort(email.getPort());
		javaMailSenderImpl.setUsername(email.getUserName());
		javaMailSenderImpl.setPassword(email.getPassword());
		Properties mailProperties = new  Properties();
		mailProperties.setProperty("mail.smtp.auth", email.getSMTPAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", email.getSMTPSecurity());
		
		javaMailSenderImpl.setJavaMailProperties(mailProperties);
		return javaMailSenderImpl;
		
		
	}
	
	
	
	
}
