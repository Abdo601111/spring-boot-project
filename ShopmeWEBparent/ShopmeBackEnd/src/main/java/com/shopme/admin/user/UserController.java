package com.shopme.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.common.entity.User;



@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listAll(Model model) {
		
		model.addAttribute("list_user", service.listAll());
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String cresteUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		user.setEnabled(true);
		model.addAttribute("list_role", service.listAllRole());
		model.addAttribute("pageTitle", "Create User");
		
		return "create_user";
	}
	
	@PostMapping("/user/save")
	public String save(User user ,RedirectAttributes r,@RequestParam("image") MultipartFile m) throws IOException {
		
		if(!m.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(m.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser= service.save(user);
			String upLoad= "user-photo/" + saveUser.getId();
			FileUploadUtil.cleanDir(upLoad);
			FileUploadUtil.saveFile(upLoad, fileName, m);
			
		}
		
		//service.save(user);
		r.addFlashAttribute("message", "The Users Save Successfully");
		
		return "redirect:/users";
	}
	
	@GetMapping("/user/edit/{id}")
	public String edit(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			User user = service.get(id);
			model.addAttribute("user", user);
			model.addAttribute("pageTitle", "Edit User :" +id);
			model.addAttribute("list_role", service.listAllRole());
			return "create_user";
			
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
		
	}
	
	
	@GetMapping("/user/delete/{id}")
	public String delete(@PathVariable("id") Integer id,RedirectAttributes r,Model model) {
		
		try {
			
			service.delete(id);
			r.addFlashAttribute("message", "The User Id :" +id +" Has been Deleted Successfuly");

		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			r.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledstatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes r) {
		
		service.updateEnabledStatus(id, enabled);
		
		String status = enabled ? "Enabled" : "Desabled";
		String message= "The User  Id :" +id +"has Been" +status;
		r.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	
}
