package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	@Autowired
	private CategoryService service;
	
	
	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/categories/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
			@Param("sortDir") String sortDir, 
			@Param("keyword") String keyword,
			Model model) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories1 = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		List<Category> listCategories = service.listAll();
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", pageInfo.getTotalPage());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);		
		
		model.addAttribute("listCategories", listCategories1);
		model.addAttribute("reverseSortDir", reverseSortDir);
		
		return "category/categorys";		
	}
	
	
	

	
	@GetMapping("/category/new")
	public String newCategory(Model model) {
		List<Category> categorys =service.listCategoriesUsedInForm();
		model.addAttribute("listCategories", categorys);
		model.addAttribute("category", new Category());
		model.addAttribute("pageTitle", "Create New Category");

		return "category/category_form";
		
	}

	@PostMapping("/category/save")
	public String save(Category category,@RequestParam("photoFile")  MultipartFile multiPart,RedirectAttributes r) throws IOException {
		if (!multiPart.isEmpty()) {
		String fileName= StringUtils.cleanPath(multiPart.getOriginalFilename());
		category.setImage(fileName);
		Category categorySave = service.save(category);
		String uploadDir = "../categorys-image/" + categorySave.getId(); 
		FileUploadUtil.cleanDir(uploadDir);
		FileUploadUtil.saveFile(uploadDir, fileName, multiPart);
		}else {
			service.save(category);
		}
		r.addFlashAttribute("message", "The Category Save Successfully");
		
		return "redirect:/categories";
	}
	
	
	

	
	
	
	
	@GetMapping("/category/edit/{id}")
	public String edit(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			List<Category> categorys =service.listCategoriesUsedInForm();
			model.addAttribute("listCategories", categorys);
			Category category = service.get(id);
			model.addAttribute("category", category);
			model.addAttribute("pageTitle", "Edit User :" +id);
			
			return "category/category_form";
			
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
		
	}
	
	
	@GetMapping("/category/delete/{id}")
	public String delete(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			service.delete(id);
			r.addFlashAttribute("message", "The User Id :" +id +" Has been Deleted Successfuly");

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/category/{id}/enabled/{status}")
	public String updateUserEnabledstatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes r) {
		
		service.updateEnabledStatus(id, enabled);
		
		String status = enabled ? "Enabled" : "Desabled";
		String message= "The User  Id :" +id +"has Been" +status;
		r.addFlashAttribute("message", message);
		return "redirect:/categories";
	}
	
	
	@GetMapping("/category/export/csv")
	public void export(HttpServletResponse response) throws IOException {
		List<Category> listUser = service.listAll();
		CategoryCvsExport exportUser = new CategoryCvsExport();
		exportUser.export(listUser, response);
	}
	
	
	
}
