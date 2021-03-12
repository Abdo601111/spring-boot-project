package com.shopme.admin.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	private String account(@AuthenticationPrincipal ShopmeUserDetails userDetails,Model model) {
		
		String email= userDetails.getUsername();
		User user =service.getUserEmail(email);
		model.addAttribute("user",user);
		return "account_form";
		
		
		
	}
	
	@PostMapping("/user/update")
	public String update(User user ,RedirectAttributes r,@RequestParam("image") MultipartFile m) throws IOException {
		
		if(!m.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(m.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser= service.updateAccount(user);
			String upLoad= "user-photo/" + saveUser.getId();
			FileUploadUtil.cleanDir(upLoad);
			FileUploadUtil.saveFile(upLoad, fileName, m);
			
		}else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null) ;
				service.updateAccount(user);
		
			}
		
		//service.save(user);
		r.addFlashAttribute("message", "The Users Account update S Successfully");
		
		return "redirect:/account";
	}
	
	
	

}
