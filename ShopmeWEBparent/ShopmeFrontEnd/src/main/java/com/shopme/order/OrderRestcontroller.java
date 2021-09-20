package com.shopme.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.common.exception.OrderNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class OrderRestcontroller {
	
	@Autowired private OrderService orderService;
	@Autowired private CustomerService customerService;
	
	
	
	@PostMapping("order/return")
	public ResponseEntity<?> handelOrderReturnRequedt(@RequestBody OrderReturnedRequest requestReturn
			,HttpServletRequest serveltrequest) {
		
		Customer customer = null;
		  try {
			 customer = getAuthentication(serveltrequest);
		} catch (CustomerNotFoundException e) {
			return new ResponseEntity<>("Authentication Requered",HttpStatus.BAD_REQUEST);
			
		}
		  try {
			orderService.setOrderReturnedRequest(requestReturn, customer);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);

		}
		return new ResponseEntity<>(new OrderReturnedResponse(requestReturn.getOrderId()),HttpStatus.OK);
	}
	
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		if(email == null) {
			
			throw new CustomerNotFoundException("Not authentecated Customer") ;
		}
		return customerService.getCustomerByEmail(email);
	}

}
