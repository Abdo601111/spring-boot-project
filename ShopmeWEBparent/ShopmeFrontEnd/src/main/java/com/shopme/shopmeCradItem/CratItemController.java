package com.shopme.shopmeCradItem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller
public class CratItemController {

	@Autowired private CardService service;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shippingService;
	
	@GetMapping("/cart")
	public String crat(Model model,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer = getAuthentication(request);
		List<CradItem> cartItem = service.findByCustomer(customer);
		
		float totalCart = 0.0F;
		
		for(CradItem item : cartItem) {
			totalCart += item.getSupTotal();
		}
		
		
		Address defaultAddress= addressService.getDefualtaddress(customer);
		ShippingRate shippingRate =null;
		boolean usePrimaryAddressDefualt = false;
		
		if(defaultAddress != null) {
			shippingRate = shippingService.getForAddress(defaultAddress);
		}else {
			usePrimaryAddressDefualt = true;
			shippingRate = shippingService.getByCountryAndState(customer);
		}
		
		System.out.println(usePrimaryAddressDefualt);
		System.out.println(shippingRate);
		
		model.addAttribute("usePrimaryAddressDefualt", usePrimaryAddressDefualt);
		model.addAttribute("shippingSupported", shippingRate !=null);
		model.addAttribute("cartItem", cartItem);
		model.addAttribute("totalCart", totalCart);
		return "card/shopping_cart";
	}
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		
		return customerService.getCustomerByEmail(email);
	}
	
}
