package com.shopme.review;

import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.Product;
import com.shopme.order.OrderDetailsRepository;
import com.shopme.product.ProductRepository;
import com.shopme.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Service
@Transactional
public class ReviewService {
	public static final int REVIEWS_PER_PAGE = 5;
	
	@Autowired private ReviewRepository repo;
	@Autowired private ProductService productService;
	@Autowired private ProductRepository productRepository;
    @Autowired private OrderDetailsRepository orderDetailsRepository;

	public Page<Review> listByCustomerByPage(Customer customer, String keyword, int pageNum, 
			String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);
		
		if (keyword != null) {
			return repo.findByCustomer(customer.getId(), keyword, pageable);
		}
		
		return repo.findByCustomer(customer.getId(), pageable);
	}
	
	public Review getByCustomerAndId(Customer customer, Integer reviewId) throws ReviewNotFoundException {
		Review review = repo.findByCustomerAndId(customer.getId(), reviewId);
		if (review == null) 
			throw new ReviewNotFoundException("Customer doesn not have any reviews with ID " + reviewId);
		
		return review;
	}

	public Page<Review> findByProduct(Product product){
		Sort sort = Sort.by("reviewTime").descending();
		Pageable pageable =PageRequest.of(0,3,sort);
		return repo.findByProduct(product,pageable);

	}

	public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

		return repo.findByProduct(product, pageable);
	}

    public  boolean didCustomerReviewProduct(Customer customer,Integer productId){

       Long count= repo.countByCustomerAndProduct(customer.getId(),productId);
       return count >0;
    }
    public  boolean canCustomerReviewProduct(Customer customer,Integer productId){

        Long count= orderDetailsRepository.countByProductAndCustomerAndOrderStatus(productId,customer.getId(), OrderStatus.DELIVERED);
        return count >0;
    }

	public Review save(Review review){
		review.setReviewTime(new Date());
		Review review1=repo.save(review);
		Integer productId= review1.getProduct().getId();

		productRepository.updateReviewAverageRating(productId);
		return review1;

	}








}
