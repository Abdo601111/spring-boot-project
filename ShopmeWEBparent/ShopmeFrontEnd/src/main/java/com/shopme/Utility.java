package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.customer.CustomerSettingBag;
import com.shopme.security.oauth2.CustomerOAuth2User;

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
	
	
	public static  String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		if(principal == null )return null;
		String customerEmail = null;
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}
		
		return customerEmail;
	}
	
	

	
	
}
