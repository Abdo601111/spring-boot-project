package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundExeption;

@Controller
public class ProductController {
	@Autowired
	private CategoryService service;
	@Autowired
	private ProductService serv;
	
	
	
	
	@GetMapping("/c/{alias_category}")
	public String viewFirstProductByCategory(@PathVariable("alias_category")String alias,Model model
			) {
		
		return viewProductByCategory(alias,model,1);
	}
		
	
	
	@GetMapping("/c/{alias_category}/{pageNum}")
	public String viewProductByCategory(@PathVariable("alias_category")String alias,Model model,
			@PathVariable("pageNum")Integer pageNum) {
		
		Category category = service.getCategpriesByAlias(alias);
		if(category == null) {
			return "error/404";
			
		}
		List<Category> listCategoryParent = service.getCategoryParents(category);
		model.addAttribute("pageTitle", category.getName());
		model.addAttribute("listCategoryParent", listCategoryParent);
		
     Page<Product> listPageProduct= serv.listByCategory(pageNum, category.getId());
     List<Product> listProduct=listPageProduct.getContent();

		
		long startCount = (pageNum - 1) * serv.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + serv.PRODUCTS_PER_PAGE - 1;
		if (endCount > listPageProduct.getTotalElements()) {
			endCount = listPageProduct.getTotalElements();
		}
		
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", listPageProduct.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", listPageProduct.getTotalElements());
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("category", category);

		
		return "product_by_category";
		
	}
	
	
	@GetMapping("/p/{alias}")
	public String ListPaoductDetails(@PathVariable("alias")String alias,Model model
			) {
		try {
			Product product=serv.getByAlias(alias);
			List<Category> listCategoryParent = service.getCategoryParents(product.getCategory());
			model.addAttribute("listCategoryParent", listCategoryParent);
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", product.getName());



			return "product_details";
		} catch (ProductNotFoundExeption e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
		
		
		
	}
		
		

}
