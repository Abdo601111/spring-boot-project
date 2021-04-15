package com.shopme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.category.CategoryService;

@Controller
public class MainController {

	@Autowired private CategoryService service;
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("listCategory", service.listNoChilderCategory());
		return "index";
	}
}
