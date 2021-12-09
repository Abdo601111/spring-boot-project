package com.shopme;

import com.shopme.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;

@Controller
public class MainController {

	@Autowired private CategoryService service;
	@Autowired private ProductService productService;
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("listCategory", service.listNoChilderCategory());
		model.addAttribute("listProduct", productService.listAll());
		return "index";
	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/";
	}	

}
