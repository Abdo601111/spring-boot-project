package com.shopme.admin.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer,Integer> {

	@Query("SELECT u FROM Customer u WHERE CONCAT(u.id ,' ',u.firstName,' ',"+" u.lastName,' ',u.email ) LIKE %?1%")
	public Page<Customer> findAll(String keyWord,Pageable page);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Customer u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id,boolean enabled);
}
