package com.shopme.address;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

@Service
@Transactional
public class AddressService {
	
	@Autowired
	private AddressRepository repo;
	
	public List<Address> listAllAdressBook(Customer customer){
		return repo.findByCustomer(customer);
	}
	
	public void save(Address address) {
		repo.save(address);
	}
	
	public Address git(Integer addressId,Integer customerId) {
		return repo.findByIdAndCustomer(addressId, customerId);
	}
	
	public void delete(Integer addressId,Integer customerId) {
		 repo.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId,Integer customerId) {
		
			repo.setDefaultAddress(defaultAddressId);
	
		
		repo.setNonAddress(defaultAddressId, customerId);
	}
	
	public Address getDefualtaddress(Customer customer) {
		return repo.findDefualtByCustomer(customer.getId());
	}
	
	

}
