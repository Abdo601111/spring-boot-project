package com.shopme.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false,unique = true,length = 45)
	private String email;
	@Column(nullable = false,length = 64)
	private String password;
	@Column(nullable = false,length = 45)
	private String firstName;
	@Column(nullable = false,length = 45)
	private String lastName;
	@Column(length = 15)
	private String phoneNumber;
	@Column(length = 64)
	private String addressLine1;
	@Column(length = 64)
	private String addressLine2;
	@Column(length = 64)
	private String sity;
	@Column(length = 64)
	private String state;
	@Column(nullable = false,length = 64)
	private String postalode;
	@Column(length = 64)
	private String verivicationCode;
	private boolean enabled;
	private Date createdDate;
	
	@ManyToOne
	private Country country;

	public Customer() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getSity() {
		return sity;
	}

	public void setSity(String sity) {
		this.sity = sity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalode() {
		return postalode;
	}

	public void setPostalode(String postalode) {
		this.postalode = postalode;
	}

	public String getVerivicationCode() {
		return verivicationCode;
	}

	public void setVerivicationCode(String verivicationCode) {
		this.verivicationCode = verivicationCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", phoneNumber=" + phoneNumber + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", sity=" + sity + ", state=" + state + ", postalode=" + postalode
				+ ", verivicationCode=" + verivicationCode + ", enabled=" + enabled + ", createdDate=" + createdDate
				+ ", country=" + country + "]";
	}
	
	
	public String getFullName() {
		return firstName +" " + lastName;
	}
	
	
	
	
	
	

}
