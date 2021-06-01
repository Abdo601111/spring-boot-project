package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.setting.SettingService;

@Controller
public class ForgotPasswordController {
	
	@Autowired
	private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	
	@GetMapping("/forgot_password")
	public String forgotPasswordForm() {
		
		return "customer/forgot_password_form";
	}
	
	
	@PostMapping("/forgot_password")
	public String processRequest(HttpServletRequest request,Model model) {
		String email = request.getParameter("email");
		
		try {
			String token=customerService.updateResetPassworedToken(email);
			String link=Utility.getSitURL(request)+"/reset_password?token="+token;
			senMail(link,email);
			model.addAttribute("message", "We Have Send  a Reset Password Link To Your Email");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException e) {
			model.addAttribute("error", "Could not Send Email");
		} catch (MessagingException e) {
			model.addAttribute("error", "Could not Send Email");
		} 
		return "customer/forgot_password_form";
	}
	
	private void senMail(String link,String email) throws UnsupportedEncodingException, MessagingException {
		
		CustomerSettingBag emailSettings = settingService.emailSettingBag();
		JavaMailSenderImpl mailSender = Utility.preperMailServer(emailSettings);
		
		String toAddress = email;
		
		
		String subject = "Hear's The Link To reset Password";
		String content = "<p>Hello</p>"
				+ "<p>You Have To request To Reset Password</p>"
				+ "<p>Click The Link Below To Chenge The Password</p>"
				+ "<p><a href=\""+link+"\">Chenge To My Password</a></p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFormAddress(), emailSettings.getSendName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(@Param("token")String token,Model model) {
		
		Customer customer = customerService.getByResetPasswordToken(token);
		
		if(customer != null) {
			model.addAttribute("token", token);
		}
		else {
			model.addAttribute("message", "invalid Token");
			model.addAttribute("pageTitle", "invalid Token");
			return "message";
		}
		return "customer/reset_password";
	}

	
	@PostMapping("/reset_password")
	public String shoeResetForm(HttpServletRequest request,Model model) {
		
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		try {
			customerService.updateCustomer(token, password);
			model.addAttribute("title", "Reset Your Password");
			model.addAttribute("message", "You Have Changed Success Password");
			return "message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("message", e.getMessage());
			model.addAttribute("pageTitle", "invalid Token");
			return "message";
		}

	}
		
}
