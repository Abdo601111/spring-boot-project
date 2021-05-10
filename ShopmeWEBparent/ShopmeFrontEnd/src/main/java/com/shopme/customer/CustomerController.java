package com.shopme.customer;

import java.io.UnsupportedEncodingException;

import java.util.List;

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
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

import com.shopme.setting.SettingService;



@Controller
public class CustomerController {
	
	@Autowired private CustomerService service;
	@Autowired private SettingService settingService;
	
	@GetMapping("/register")
	public String register (Model model) {
		model.addAttribute("countries", service.listall());
		model.addAttribute("pageTitle", "Customer Registeration");
		model.addAttribute("customer", new Customer());
		return "register/register_form";
	}
	
	@PostMapping("/create/customer")
	public String saveCustomer(Customer customer,Model model,HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		service.registerCustpmer(customer);
		sendVerificationEmail(request,customer);
		model.addAttribute("pageTitle", "registration Success");
		return "register/register_success";
		
	}

	
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		
		CustomerSettingBag emailSettings = settingService.emailSettingBag();
		JavaMailSenderImpl mailSender = Utility.preperMailServer(emailSettings);
		
		String toAddress = customer.getEmail();
		String subject = emailSettings.getCustomerVerifaySubject();
		String content = emailSettings.getCustomerVerifayContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFormAddress(), emailSettings.getSendName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", customer.getFullName());
		
		String verifyURL = Utility.getSitURL(request) + "/verify?code=" + customer.getVerivicationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}

	
	@GetMapping("verify")
	public String verifyAccount(@Param("code") String code,Model model) {
		
		boolean verifed = service.verify(code);
		return "/register/" + (verifed ? "verify_success" :"verify_fail");
		
	}
	
	

}
