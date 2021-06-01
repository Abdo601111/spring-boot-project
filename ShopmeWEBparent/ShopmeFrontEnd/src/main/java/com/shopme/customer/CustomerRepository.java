package com.shopme.customer;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	public Customer findByEmail (String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verivicationCode = ?1")
	public Customer findByVerivicationCode (String verivicationCode);
	
	@Query("UPDATE Customer c SET  c.enabled = true , c.verivicationCode = null WHERE c.id= ?1")
	@Modifying
	public void enabled(Integer id);
	
	@Query("UPDATE Customer c SET  c.authenticationType = ?2  WHERE c.id= ?1")
	@Modifying
	public void updateAuthenticationType(Integer id,AuthenticationType authenticationType);

	public Customer findByResetPasswordToken(String token);
}
