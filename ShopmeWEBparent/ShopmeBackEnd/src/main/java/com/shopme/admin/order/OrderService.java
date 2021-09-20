package com.shopme.admin.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.exception.OrderNotFoundException;

@Service
public class OrderService {
	
	@Autowired private OrderRepository repo;
	@Autowired
	private CountryRepository countryRepo;
	
	public static final int ROOT_ORDER=10;
	

	public List<Order> listAllOrder(){
		return (List<Order>) repo.findAll();
	}
	
	
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
	
     
     public List<Country> listAllCountry(){
 		return countryRepo.findAllByOrderByNameAsc();
 	}


     public void save(Order orderInForm) {
 		Order orderInDB = repo.findById(orderInForm.getId()).get();
 		orderInForm.setOrderTime(orderInDB.getOrderTime());
 		orderInForm.setCustomer(orderInDB.getCustomer());
 		
 		repo.save(orderInForm);
 	}	

     
     
     
     public void updateStatus(Integer orderId,String status) {
    	 Order orderInDB= repo.findById(orderId).get();
    	 OrderStatus orderUpdating= OrderStatus.valueOf(status);
    	 
    	 if(!orderInDB.hasStatus(orderUpdating)) {
    		List<OrderTrack> orderTrack= orderInDB.getOrderTracks();
    		OrderTrack oTrack= new OrderTrack();
    		oTrack.setOrder(orderInDB);
    		oTrack.setStatus(orderUpdating);
    		oTrack.setUpdatedTime(new Date());
    		oTrack.setNotes(orderUpdating.defaultDescription());
    		orderTrack.add(oTrack);
    		orderInDB.setStatus(orderUpdating);
    		repo.save(orderInDB);
    	 }
    	 
    	 
     }

}
