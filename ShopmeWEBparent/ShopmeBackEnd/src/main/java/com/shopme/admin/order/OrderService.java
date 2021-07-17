package com.shopme.admin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.shopme.common.entity.Order;

@Service
public class OrderService {
	
	@Autowired private OrderRepository repo;
	
	public static final int ROOT_ORDER=10;
	
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
				
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_ORDER, sort);
		
		if (keyword != null) {
			return repo.findAll(keyword, pageable);
		}
		
		return repo.findAll(pageable);		
	}

	
	public Order get(int id) {
		return repo.findById(id).get();
		
	}
	
     public  void delete(Integer id) throws OrderNotFoundException {
    	Long countById= repo.countById(id);
    	if(countById == null ||  countById==0) {
    		throw new OrderNotFoundException("Order Not Fount"+id);
    	}
    	repo.deleteById(id);
     }
	

}
