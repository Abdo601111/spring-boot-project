package com.shopme.shopmeCradItem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class CratItemController {

	@Autowired
	private CardService service;
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/cart")
	public String crat(Model model,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthentication(request);
		List<CradItem> cartItem = service.findByCustomer(customer);
		
		float totalCart = 0.0F;
		
		for(CradItem item : cartItem) {
			totalCart += item.getSupTotal();
		}
		
		model.addAttribute("cartItem", cartItem);
		model.addAttribute("totalCart", totalCart);
		return "card/shopping_cart";
	}
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		
		return customerService.getCustomerByEmail(email);
	}
	
}
