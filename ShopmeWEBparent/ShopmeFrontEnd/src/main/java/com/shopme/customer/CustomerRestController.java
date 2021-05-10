package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
	 @Autowired
	private CustomerService service;
	
	@PostMapping("/customer/chick_email_unique")
	public String chickEmail(@Param("email") String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
		
	}
	

}
