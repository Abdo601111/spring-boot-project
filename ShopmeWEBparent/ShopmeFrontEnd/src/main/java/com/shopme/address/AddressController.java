package com.shopme.address;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class AddressController {
	
	@Autowired
	private AddressService service;
	@Autowired
	private CustomerService customerService;
	
	
	@GetMapping("/address_book")
	public  String showAddressBook(Model model,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer =getAuthentication(request);
		List<Address> listAddress = service.listAllAdressBook(customer);
		
		boolean usePrimaryAddressDefault = true;
		for(Address address : listAddress) {
			if(address.isDefaultForshipping()) {
				usePrimaryAddressDefault =false;
				break;
			}
		}
		model.addAttribute("customer", customer);
		model.addAttribute("listAddress", listAddress);
		model.addAttribute("usePrimaryAddressDefault", usePrimaryAddressDefault);

		return "address_book/address";
	}
	
	@GetMapping("/address_book/new")
	public String newAddress(Model model) {
		
		model.addAttribute("countries", customerService.listall());
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");
		return "address_book/address_form";
	}
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/address_book/save")
	public String save(Address address,HttpServletRequest request,RedirectAttributes ra) throws CustomerNotFoundException {
		Customer customer =getAuthentication(request);
		address.setCustomer(customer);
		service.save(address);
		ra.addFlashAttribute("message", "The Address has Been Added");
		return "redirect:/address_book";
		
	}
	
	@GetMapping("/address_book/edit/{id}")
	public String edit(@PathVariable("id") Integer addressId,Model model,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer =getAuthentication(request);
		
		Address address = service.git(addressId, customer.getId());
		model.addAttribute("countries", customerService.listall());
		model.addAttribute("address", address);
		model.addAttribute("pageTitle", " Update Address");
		
		return "address_book/address_form";
	}
	
	@GetMapping("/address_book/delete/{id}")
	public String delete(@PathVariable("id") Integer addressId,RedirectAttributes ra,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer =getAuthentication(request);
		
		service.delete(addressId, customer.getId());
		
		ra.addFlashAttribute("message", "The Address has Been Deleted");

		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer defaultAddressId,HttpServletRequest request) throws CustomerNotFoundException {
		Customer customer =getAuthentication(request);
		
		service.setDefaultAddress(defaultAddressId, customer.getId());
		

		return "redirect:/address_book";
	}
	

}
