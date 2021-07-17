package com.shopme.common.entity;

import java.beans.Transient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String addtess1;
	
	private String address2;
	
	private String city;
	
	private String state;
	
	private String postalCode;
	@ManyToOne
	private Country country;
	
	@ManyToOne
	private Customer customer;
	
	private boolean  defaultForshipping;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddtess1() {
		return addtess1;
	}

	public void setAddtess1(String addtess1) {
		this.addtess1 = addtess1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public boolean isDefaultForshipping() {
		return defaultForshipping;
	}

	public void setDefaultForshipping(boolean defaultForshipping) {
		this.defaultForshipping = defaultForshipping;
	}

	@Override
	public String toString() {
		return    ", " + firstName + "," + lastName + ", " + phone
				+ ", " + addtess1 + "," + address2 + "," + city + ", " + state
				+ ", " + postalCode + ", " + country + ", " 
				+ ", " ;
	}
	
	@Transient
	public String getAddress() {
		
		String address = firstName;
		
		if(lastName != null && !lastName.isEmpty()) address += " " +lastName;
		
		if(!addtess1.isEmpty()) address += " , " + addtess1;
		if(address2 != null && !address2.isEmpty()) address += " , " +address2;
		if(!city.isEmpty()) address += " , " + city;
		if(state != null && !state.isEmpty()) address += " , " +state;
		address+= " , " + country.getName();
		if(!postalCode.isEmpty()) address += " postalCode  : " + postalCode;
		if(!phone.isEmpty()) address += " phone Number  : " + phone;



		return address;
	}
	
	
	

}
