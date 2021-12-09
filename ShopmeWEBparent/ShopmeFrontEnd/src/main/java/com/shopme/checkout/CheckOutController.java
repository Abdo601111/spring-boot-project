package com.shopme.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.shopme.sms.Smsrequest;
import com.shopme.sms.Smsservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.checkout.payPal.PayPalApiException;
import com.shopme.checkout.payPal.PayPalService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;
import com.shopme.customer.CustomerSettingBag;
import com.shopme.order.OrderService;
import com.shopme.setting.PaymentSettingPage;
import com.shopme.setting.SettingService;
import com.shopme.shipping.ShippingRateService;
import com.shopme.shopmeCradItem.CardService;

@Controller
public class CheckOutController {
	
	@Autowired private CheckOutServcie chechOutService;
	@Autowired private CustomerService customerService;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService sgippingRateService;
	@Autowired private CardService cardService;
	@Autowired private OrderService orderService;
	@Autowired private SettingService settingService;
	@Autowired private PayPalService paypalService;
	@Autowired private Smsservice smsservice;
	
	@GetMapping("/checkout")
	public String checkOut(Model model,HttpServletRequest request) throws CustomerNotFoundException {
		
		Customer customer = getAuthentication(request);
		
		Address defaultAddress= addressService.getDefualtaddress(customer);
		ShippingRate shippingRate =null;
		
		
		if(defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.getAddress());
			shippingRate = sgippingRateService.getForAddress(defaultAddress);
		}else {
			model.addAttribute("shippingAddress", customer.getAddress());

			
			shippingRate = sgippingRateService.getByCountryAndState(customer);
		}
		if(shippingRate == null) {
			return "redirect:/card";
		}
		List<CradItem> cartItem = cardService.findByCustomer(customer);
        CheckOutInfo  checkOutInfo = chechOutService.perperChickOut(cartItem, shippingRate);
    	String currencyCode = settingService.getCurrencyCode();
		PaymentSettingPage paymentSettings = settingService.paymentSettingBag();
		String paypalClientId = paymentSettings.getClientID();
		
		model.addAttribute("paypalClientId", paypalClientId);
		model.addAttribute("currencyCode", currencyCode);
		model.addAttribute("cartItems", cartItem);
		model.addAttribute("customer", customer);
		model.addAttribute("checkOutInfo", checkOutInfo);
		return "checkout/checkout";
	}
	
	
	
	
	
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		
		return customerService.getCustomerByEmail(email);
	}
	
	
	@PostMapping("/place_order")
	public String checkOutPlase(HttpServletRequest request) throws CustomerNotFoundException, UnsupportedEncodingException, MessagingException {
		
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod  paymentMethod = PaymentMethod.valueOf(paymentType);
		
         Customer customer = getAuthentication(request);
		
		Address defaultAddress= addressService.getDefualtaddress(customer);
		ShippingRate shippingRate =null;
		
		
		if(defaultAddress != null) {
			shippingRate = sgippingRateService.getForAddress(defaultAddress);
		}else {

			
			shippingRate = sgippingRateService.getByCountryAndState(customer);
		}
		if(shippingRate == null) {
			return "redirect:/card";
		}
		List<CradItem> cartItem = cardService.findByCustomer(customer);
        CheckOutInfo  checkOutInfo = chechOutService.perperChickOut(cartItem, shippingRate);
		
      Order order = orderService.createorder(customer, defaultAddress, cartItem, paymentMethod, checkOutInfo);
        cardService.deleteByCustomer(customer);
        sendConfirmationEmail(request,order);
		sendConfirmationPhone(order);
		
		return "checkout/order_completed";
	}

	private void sendConfirmationPhone(Order order) {
		Smsrequest smsrequest= new Smsrequest();
		String phoneNumber= order.getCustomer().getPhoneNumber().substring(1);
		String phone= "+20".concat(phoneNumber);
//		DateFormat dateFormate =  new SimpleDateFormat("HH:mm:ss E , dd MMM yyyy");
//		String orderTime = dateFormate.format(order.getOrderTime());
		String total= String.valueOf(order.getTotal());
//		String content="";
//		content = content.replace("[[name]]", order.getCustomer().getFullName());
//		content = content.replace("[[orderId]]",String.valueOf(order.getId()) );
//		content = content.replace("[[orderTime]]",orderTime );
//		content = content.replace("[[shippingAddress]]",order.getShippingAddress());
//		content = content.replace("[[total]]",total);
//		content = content.replace("[[paymentmethod]]",order.getPaymentMethod().toString());

		smsrequest.setNumber(phone);
		smsrequest.setMessage("Order Completed Successfully :" +
				"Total : "+total);
		smsservice.sendsms(smsrequest);

	}


	private void sendConfirmationEmail(HttpServletRequest request, Order order) throws UnsupportedEncodingException, MessagingException {
		CustomerSettingBag emailSettibg = settingService.emailSettingBag();	
		JavaMailSenderImpl mailSender=Utility.preperMailServer(emailSettibg);
		//mailSender.setDefaultEncoding("utf-8");
		
		String address = order.getCustomer().getEmail();
		String subject = emailSettibg.getOrderVerifaySubject();
		String content = emailSettibg.getOrderVerifayContent();
		
	
		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		System.out.println(message);
		
		helper.setFrom(emailSettibg.getFormAddress(),emailSettibg.getSendName());
		helper.setTo(address);
		helper.setSubject(subject);
		
		DateFormat dateFormate =  new SimpleDateFormat("HH:mm:ss E , dd MMM yyyy");
		String orderTime = dateFormate.format(order.getOrderTime());
		String total= String.valueOf(order.getTotal());
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]",String.valueOf(order.getId()) );
		content = content.replace("[[orderTime]]",orderTime );
		content = content.replace("[[shippingAddress]]",order.getShippingAddress());
		content = content.replace("[[total]]",total);
		content = content.replace("[[paymentmethod]]",order.getPaymentMethod().toString());
		helper.setText(content, true);
		System.out.println(message);
		System.out.println(content);
		mailSender.send(message);
		System.out.println(message);


	}
	
	
	@PostMapping("/process_paypal_order")
	public String processPaypalOrder(HttpServletRequest request,Model model) throws  UnsupportedEncodingException, CustomerNotFoundException, MessagingException {
		
		String orderId = request.getParameter("orderId");
		String pageTitle ="Checkout Failuer";
		String message = null;
		try {
			if(paypalService.validateOrder(orderId)) {
				checkOutPlase(request);
			}else {
				pageTitle ="Checkout Failuer";
				 message = "Error Transaction Could Not Be Because Order Information Is In Valied";
			}
		} catch (PayPalApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		model.addAttribute("message", message);
		model.addAttribute("pageTitle", pageTitle);
		return "message";
	}


	public static void main(String[] args) {
		String d= "01093463729".substring(1);
		String phone ="02".concat(d);
		System.out.println(phone);

	}
	
	
	
	
	
	


}
