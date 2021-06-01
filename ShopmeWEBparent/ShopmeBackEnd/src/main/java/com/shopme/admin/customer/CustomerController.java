package com.shopme.admin.customer;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.country.CountryRepository;
import com.shopme.admin.user.UserNotFoundException;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;




@Controller
public class CustomerController {
	@Autowired
	private CustomerService service;
	@Autowired
	private CountryRepository cuntryRepo;
	
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";

	
	@GetMapping("/customers")
public String listAll(Model model) {
		
		model.addAttribute("list_customer", service.listAll());
		
		return listByPage(1,model,null);
	}
	
	@GetMapping("/customer/page/{pageNum}")
	public String listByPage(@PathVariable(name ="pageNum")int pageNum,Model model,@Param("keyWord")String keyWord ) {
		Page <Customer>pageUser= service.ListPage(pageNum,keyWord);
		List<Customer> listuser= pageUser.getContent();
		long startCount = (pageNum -1) * service.PAGE_NUMPER +1;
		long endCount = startCount +service.PAGE_NUMPER -1;
		if(endCount > pageUser.getTotalElements()) {
			endCount = pageUser.getTotalElements();
		}
		model.addAttribute("currentPage",pageNum);
		model.addAttribute("totalPage",pageUser.getTotalPages());
		model.addAttribute("startCount",startCount);
		model.addAttribute("endCount",endCount);

		model.addAttribute("totalItem",pageUser.getTotalElements());
         model.addAttribute("list_customer",listuser);
         model.addAttribute("keyWord",keyWord);

		
		return "customer/customers";
	}
	
	
	@PostMapping("/create/customer")
	public String saveCustomer(Customer customer,RedirectAttributes r,Model model) {
		service.registerCustpmer(customer);
		
		r.addFlashAttribute("message", "Customer Has been Updated Successfuly");
		
		return "customer/customers";
		
	}

	
	
	@GetMapping("/customer/edit/{id}")
	public String edit(@PathVariable("id") Integer id,RedirectAttributes r,Model model) throws CustomerNotFoundException {
		
		try {
			
			Customer customer = service.get(id);
			
			model.addAttribute("customer", customer);
			model.addAttribute("countries", cuntryRepo.findAll());
			model.addAttribute("pageTitle", "Edit Customer :" +id);
	
			return "customer/register_form";
			
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
		}
		
	}
	
	
	@GetMapping("/customer/delete/{id}")
	public String delete(@PathVariable("id") Integer id,RedirectAttributes r,Model model) throws CustomerNotFoundException {
		
		try {
			
			service.delete(id);
			r.addFlashAttribute("message", "The User Id :" +id +" Has been Deleted Successfuly");

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/customers";
	}
	
	@GetMapping("/customer/{id}/enabled/{status}")
	public String updateUserEnabledstatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes r) {
		
		service.updateEnabledStatus(id, enabled);
		
		String status = enabled ? "Enabled" : "Desabled";
		String message= "The User  Id :" +id +"has Been" +status;
		r.addFlashAttribute("message", message);
		return "redirect:/customers";
	}
	
	@GetMapping("/customer/export/csv")
	public void export(HttpServletResponse response) throws IOException {
		List<Customer> listUser = service.listAll();
		CustomerScvExporter exportUser = new CustomerScvExporter();
		exportUser.export(listUser, response);
	}
	
	@GetMapping("/customer/details/{id}")
	public String viewOroductDetails(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			Customer customer = service.get(id);
			
			model.addAttribute("customer", customer);
		return "customer/customer_form_details";
		} catch (Exception e) {
			r.addFlashAttribute("message", e.getMessage());
		}
		
		return "redirect:/customers";
		
	}
	
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		service.save(customer);
		ra.addFlashAttribute("message", "The customer ID " + customer.getId() + " has been updated successfully.");
		return defaultRedirectURL;
	}


	

}
