package com.shopme.order;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.Product;
import com.shopme.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class OrderController {
	
	@Autowired private OrderService orderservice;
	@Autowired private CustomerService customerService;
	@Autowired private ReviewService reviewService;
	
	@GetMapping("/orders")
	public String ListOrderFirtPage(Model model,HttpServletRequest request) throws CustomerNotFoundException {
		
		return listOrderByPage(model,request,1,"orderTime","desc",null);
	}
	@GetMapping("/orders/page/{pageNum}")
	public String listOrderByPage(Model model, HttpServletRequest request,@PathVariable(name="pageNum") int pageNum
			, String sortFiled, String sortDir,
			String orderKeyword) throws CustomerNotFoundException {
		
		Customer customer =getAuthentication(request);
		Page<Order> page= orderservice.listForCustomerByPage(customer, pageNum, sortFiled, sortDir, orderKeyword);
		List<Order> listOrder= page.getContent();
		
		
				
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortFiled);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("keyword", orderKeyword);		
		model.addAttribute("listOrders", listOrder);
		
		
		long startCount = (pageNum-1) * orderservice.ORDER_PAGES+1;
		model.addAttribute("startCount", startCount);
		
		long endCount = startCount+ orderservice.ORDER_PAGES-1;
		
		if(endCount>page.getTotalElements()) {
			endCount =page.getTotalElements();
		}
		model.addAttribute("endCount", endCount);
		
		
		return "order/order_customer";
	}
	
	private Customer getAuthentication(HttpServletRequest request) throws CustomerNotFoundException {
		String email= Utility.getEmailOfAuthenticatedCustomer(request);
		
		
		return customerService.getCustomerByEmail(email);
	}
	
	
	
	
	
	
	@GetMapping("/order/detail/{id}")
	public String viewOrderDetails(Model model,@PathVariable( name="id") int id,HttpServletRequest request) throws CustomerNotFoundException {
		
		Customer customer =getAuthentication(request);
		Order order= orderservice.getOrder(id, customer);

		setProductReviewedByStatus(customer,order);
		model.addAttribute("order", order);
		return "order/order_details";
	}

	private void setProductReviewedByStatus(Customer customer, Order order) {

		Iterator<OrderDetail> iterator =order.getOrderDetails().iterator();

		while (iterator.hasNext()){

			OrderDetail orderDetail = iterator.next();
			Product product = orderDetail.getProduct();
			Integer productId= product.getId();
			boolean didCustomerReviewProduct= reviewService.didCustomerReviewProduct(customer,productId);
			product.setReviewedByCustomer(didCustomerReviewProduct);

			if(!didCustomerReviewProduct){
				boolean canCustomerReviewProduct = reviewService.canCustomerReviewProduct(customer,productId);
				product.setCustomerCanReview(canCustomerReviewProduct);
			}


		}


	}


}
