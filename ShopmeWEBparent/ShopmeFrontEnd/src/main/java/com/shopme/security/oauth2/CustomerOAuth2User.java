package com.shopme.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {

	private OAuth2User oauth2User;
	private String clientName;
	private String fullName;
	
	public CustomerOAuth2User(OAuth2User oauth2User,String clientName) {
	
		this.oauth2User = oauth2User;
		this.clientName= clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return oauth2User.getAuthorities();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return oauth2User.getAttribute("name");
	}
	
	public String getFullName() {
		// TODO Auto-generated method stub
		return fullName != null ? fullName : oauth2User.getAttribute("name");
	}

	public String getEmail() {
		// TODO Auto-generated method stub
		return oauth2User.getAttribute("email");	
		}

	public OAuth2User getOauth2User() {
		return oauth2User;
	}

	public void setOauth2User(OAuth2User oauth2User) {
		this.oauth2User = oauth2User;
	}

	public String getClientName() {
		return clientName;
	}

	public void setFullName(String fullName) {
      this.fullName= fullName;		
	}

	
	
	
	
}
