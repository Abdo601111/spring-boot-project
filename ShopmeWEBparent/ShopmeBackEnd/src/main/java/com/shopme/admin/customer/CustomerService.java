package com.shopme.admin.customer;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;





@Service
@Transactional
public class CustomerService {
	@Autowired
	private CustomerRepository repo;
	@Autowired  private  PasswordEncoder passwordEncoder;
	
	public static  final int  PAGE_NUMPER = 10;
	
	

	public List<Customer> listAll(){
		
		return (List<Customer>) repo.findAll();
	}
	
	
	public Page<Customer>ListPage(int number,String keyWord){
		Pageable pageable = PageRequest.of(number -1, PAGE_NUMPER);
		
		if(keyWord != null) {
			return repo.findAll(keyWord,pageable);
		}
		
		return repo.findAll(pageable);
		
		
	}
	
	
	public void registerCustpmer(Customer customer) {
		enCodePasswored(customer);
		customer.setEnabled(true);
		customer.setCreatedDate(new Date());
		
		
		repo.save(customer);
		
	}
	
	public void save(Customer customerInForm) {
		Customer customerInDB = repo.findById(customerInForm.getId()).get();
		
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
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
	

	
	


	private void enCodePasswored(Customer customer) {
		String enCoder =passwordEncoder.encode(customer.getPassword());	
		customer.setPassword(enCoder);
	}

	
	public Customer get(int id) throws UserNotFoundException, CustomerNotFoundException {
		// TODO Auto-generated method stub
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			throw new CustomerNotFoundException("Not Found Excption"+id);
		}
	}
	
	public void delete(Integer id) throws UserNotFoundException, CustomerNotFoundException {
		Long countById= repo.countById(id);
		
		if(countById == null || countById == 0) {
			
			throw new CustomerNotFoundException("Not Found Excption"+id);
			
		}
		repo.deleteById(id);
		
	}
	
	public void updateEnabledStatus(Integer id,boolean enabled) {
		
		repo.updateEnabledStatus(id, enabled);
	}
	

}
