package com.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.setting.CountryRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class CustomerService {
	
@Autowired	private CustomerRepository repo;
@Autowired	private CountryRepository countryRepo;
@Autowired  private  PasswordEncoder passwordEncoder;




public List<Country> listall(){
	return countryRepo.findAllByOrderByNameAsc();
}

public boolean isEmailUnique(String email) {
	Customer customer = repo.findByEmail(email);
	return customer == null;
	
}

public void registerCustpmer(Customer customer) {
	enCodePasswored(customer);
	customer.setEnabled(false);
	customer.setCreatedDate(new Date());
	String randomCode=RandomString.make(64);
	customer.setVerivicationCode(randomCode);
	
}


private void enCodePasswored(Customer customer) {
	String enCoder =passwordEncoder.encode(customer.getPassword());	
	customer.setPassword(enCoder);
}

public boolean verify(String verificationCode) {
	Customer customer = repo.findByVerivicationCode(verificationCode);
	if(customer == null || customer.isEnabled() ) {
		return false;
	}else {
		repo.enabled(customer.getId());
		return true;
	}
	
	
}


}
