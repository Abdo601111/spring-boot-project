package com.shopme.review;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundExeption;
import com.shopme.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;
import com.shopme.customer.CustomerService;

@Controller
public class ReviewController {
	private String defaultRedirectURL = "redirect:/reviews/page/1?sortField=reviewTime&sortDir=desc";
	
	@Autowired private ReviewService reviewService;
	@Autowired private CustomerService customerService;
	@Autowired private ProductService productService;
	
	@GetMapping("/reviews")
	public String listFirstPage(Model model) {

		return defaultRedirectURL;
	}
	
	@GetMapping("/reviews/page/{pageNum}") 
	public String listReviewsByCustomerByPage(Model model, HttpServletRequest request,
							@PathVariable(name = "pageNum") int pageNum,
							String keyword, String sortField, String sortDir) {
		Customer customer = getAuthenticatedCustomer(request);
		Page<Review> page = reviewService.listByCustomerByPage(customer, keyword, pageNum, sortField, sortDir);		
		List<Review> listReviews = page.getContent();
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		model.addAttribute("moduleURL", "/reviews");
		
		model.addAttribute("listReviews", listReviews);

		long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);
		
		long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("endCount", endCount);
		
		return "reviews/reviews_customer";
	}


	@GetMapping("/ratings/{productAlias}/page/{pageNum}")
	public String listByProductByPage(Model model,
									  @PathVariable(name = "productAlias") String productAlias,
									  @PathVariable(name = "pageNum") int pageNum,
									  String sortField, String sortDir) {

		Product product = null;

		try {
			product = productService.getByAlias(productAlias);
		} catch (ProductNotFoundExeption ex) {
			return "error/404";
		}

		Page<Review> page = reviewService.listByProduct(product, pageNum, sortField, sortDir);
		List<Review> listReviews = page.getContent();

		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listReviews", listReviews);
		model.addAttribute("product", product);

		long startCount = (pageNum - 1) * ReviewService.REVIEWS_PER_PAGE + 1;
		model.addAttribute("startCount", startCount);

		long endCount = startCount + ReviewService.REVIEWS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}

		model.addAttribute("endCount", endCount);
		model.addAttribute("pageTitle", "Reviews for " + product.getName());

		return "reviews/reviews_product";
	}

	@GetMapping("/ratings/{productAlias}")
	public String listByProductFirstPage(@PathVariable(name = "productAlias") String productAlias, Model model) {
		return listByProductByPage(model, productAlias, 1, "reviewTime", "desc");
	}


	@GetMapping("/write_review/product/{productId}")
	public String showForm(@PathVariable("productId") Integer productId,Model model,
						   HttpServletRequest request) throws ProductNotFoundExeption {

		Review review = new Review();
		Product product=null;

		try{
			product=productService.getByAlias(productId);
		}catch (ProductNotFoundExeption ex){
           return "error/404";
		}

		Customer customer=getAuthenticatedCustomer(request);
		boolean customerReviewed =reviewService.didCustomerReviewProduct(customer,product.getId());

		if(customerReviewed){
			model.addAttribute("customerReviewed",customerReviewed);
		}else {
			boolean customerCanReviewed= reviewService.canCustomerReviewProduct(customer,product.getId());

			if(customerCanReviewed) {
				model.addAttribute("customerCanReviewed", customerCanReviewed);
			}else {
				model.addAttribute("NoReviewedPermission", true);

			}
		}


		model.addAttribute("product",product);
		model.addAttribute("review",review);

		return "reviews/review_form";

	}



	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);				
		return customerService.getCustomerByEmail(email);
	}
	
	@GetMapping("/reviews/detail/{id}")
	public String viewReview(@PathVariable("id") Integer id, Model model, 
			RedirectAttributes ra, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		try {
			Review review = reviewService.getByCustomerAndId(customer, id);
			model.addAttribute("review", review);
			
			return "reviews/review_detail_modal";
		} catch (ReviewNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;		
		}
	}


	@PostMapping("/post_review")
	public  String save(Model model,Review review,Integer productId,HttpServletRequest request){
		Customer customer =getAuthenticatedCustomer(request);
		Product product=null;

		try{
			product=productService.getByAlias(productId);
		}catch (ProductNotFoundExeption ex){
			return "error/404";
		}
		review.setProduct(product);
		review.setCustomer(customer);
		Review review1=reviewService.save(review);
		model.addAttribute("review",review1);
		return "reviews/review_done";

	}
}
