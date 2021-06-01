package com.shopme.customer;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
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
	customer.setAuthenticationType(AuthenticationType.DATABASE);
	repo.save(customer);
	
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


public void updateCustomerAuthenticationType(Customer customer,AuthenticationType authenticationType) {
	
	if(!customer.getAuthenticationType().equals(authenticationType)) {
		repo.updateAuthenticationType(customer.getId(), authenticationType);
	}
	
}

public Customer getCustomerByEmail(String email) {
	// TODO Auto-generated method stub
	return repo.findByEmail(email);
}

public void addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
		AuthenticationType authenticationType) {
	Customer customer = new Customer();
	customer.setEmail(email);
	setName(name,customer);
	customer.setEnabled(true);
	customer.setCreatedDate(new Date());
	customer.setAuthenticationType(authenticationType);
	customer.setPassword("");
	customer.setAddressLine1("");

	customer.setSity("");
	customer.setState("");
	customer.setPhoneNumber("");
	customer.setPostalode("");
	customer.setCountry(countryRepo.findByCode(countryCode));
	
	repo.save(customer);
	
};

private void setName(String name,Customer customer) {
	String [] nameArray = name.split(" ");
	if(nameArray.length < 2) {
		customer.setFirstName(name);
		customer.setLastName("");
	}else {
		String firstName= nameArray[0];
		customer.setFirstName(firstName);
		String lastName = name.replace(firstName + " ", "");
		customer.setLastName(lastName);
	}
}

public void update(Customer customerInForm) {
	Customer customerInDB = repo.findById(customerInForm.getId()).get();
	
	if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}		
	} else {
		customerInForm.setPassword(customerInDB.getPassword());
	}
	
	customerInForm.setEnabled(customerInDB.isEnabled());
	customerInForm.setCreatedDate(customerInDB.getCreatedDate());
	customerInForm.setVerivicationCode(customerInDB.getVerivicationCode());
	customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
	customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
	repo.save(customerInForm);
}

public String  updateResetPassworedToken(String email) throws CustomerNotFoundException {
	Customer customer = repo.findByEmail(email);
	if(customer != null) {
		String token=RandomString.make(30);
		customer.setResetPasswordToken(token);
		repo.save(customer);
		return token;
	}else {
		throw new CustomerNotFoundException("Could Not Find Any Customer With The Email" +email);
	}
	
}	
public Customer getByResetPasswordToken(String token) {
	return repo.findByResetPasswordToken(token);
}

public void updateCustomer (String token,String newPassword) throws CustomerNotFoundException {
	Customer customer = repo.findByResetPasswordToken(token);
	if(customer == null) {
		throw new CustomerNotFoundException("Not Customer Found invalid Token" );
 
	}else {
		customer.setPassword(newPassword);
		customer.setResetPasswordToken(null);
		enCodePasswored(customer);
		repo.save(customer);
		
	}
}



}
