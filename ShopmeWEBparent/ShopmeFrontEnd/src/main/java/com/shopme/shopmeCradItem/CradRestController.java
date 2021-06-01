package com.shopme.shopmeCradItem;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class CradRestController {
	
	@Autowired
	private CardService service;
	
	@Autowired
	private CustomerService customerService;
	
	
	@PostMapping("/card/add/{productId}/{quantity}")
	public String addCard(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity,HttpServletRequest request) {
		try {
			Customer customer = getAuthentication(request);
			
		Integer productqQuantity =	service.addCard(productId, quantity, customer);
		return  productqQuantity + "items Of This product were  Added In Shopping Card ";
		} catch (Exception e) {
			return "You Must Login To add This Product Card";
		}
		
	
	}
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		if(email == null) {
			
			throw new CustomerNotFoundException("Not authentecated Customer") ;
		}
		return customerService.getCustomerByEmail(email);
	}

	@PostMapping("/card/update/{productId}/{quantity}")
	public String updateCard(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity,HttpServletRequest request) {
		try {
			Customer customer = getAuthentication(request);
			float supTotal=service.updateCard(productId, quantity, customer);
		return  String.valueOf(supTotal);
		} catch (Exception e) {
			return "You Must Login To add This Product Card";
		}
		
	
	}
	
	@DeleteMapping("/card/delete/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId,HttpServletRequest request) {
		
		try {
			Customer customer = getAuthentication(request);
			service.remove(customer, productId);
			return "The Product has Been Removed From Your Shopping Cart";
		} catch (CustomerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "You MUst Login To Remove product";
		}
		
		
	}
	
	
	
	
}
