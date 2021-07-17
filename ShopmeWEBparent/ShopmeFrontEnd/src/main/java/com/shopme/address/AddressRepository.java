package com.shopme.address;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;

public interface AddressRepository  extends CrudRepository<Address, Integer>
		{
	
	public List<Address> findByCustomer(Customer customer);
	
	@Query("SELECT a FROM Address a WHERE a.id=?1 AND a.customer.id=?2")
	public Address findByIdAndCustomer(int id,int customer);
	
	
	@Query("DELETE  FROM Address a WHERE a.id=?1 AND a.customer.id=?2")
	@Modifying
	public void deleteByIdAndCustomer(Integer id,int customer);
	
	@Query("UPDATE Address a SET a.defaultForshipping=true WHERE a.id=?1")
	@Modifying
	public void setDefaultAddress(Integer id);
	
	@Query("UPDATE  Address a SET a.defaultForshipping=false WHERE a.id !=?1 AND a.customer.id=?2 ")
	@Modifying
	public void setNonAddress(Integer defaultAddressId,Integer id);
	
	@Query("SELECT a FROM Address a WHERE  a.customer.id=?1 AND a.defaultForshipping=true")
   public Address findDefualtByCustomer(Integer customerId);
	

}
