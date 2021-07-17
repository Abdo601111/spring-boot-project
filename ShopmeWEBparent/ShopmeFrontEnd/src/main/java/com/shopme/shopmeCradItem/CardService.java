package com.shopme.shopmeCradItem;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class CardService {
	
	@Autowired
	private CartItemRepository repo;
	@Autowired
	private ProductRepository productRepo;
	
	public Integer addCard(Integer id,Integer quntity,Customer customer) throws ShoppingNotfoundException {

          Integer updatedQuantity = quntity;
          Product product = new Product(id);
         CradItem cradItrem= repo.findByCustomerAndProduct(customer,product);
         
         if(cradItrem != null ) {
        	 updatedQuantity= cradItrem.getQuantity()+quntity;
        	 if(updatedQuantity <= 5) {
 				throw new ShoppingNotfoundException("Can Not add More" +quntity+"because There alert"
 						+cradItrem.getQuantity()+"In Your Shopping Cart Maximum Allawed Quantity Is 5 .");
 			}
         }else {
        	 cradItrem = new CradItem();
        	 cradItrem.setCustomer(customer);
        	 cradItrem.setProduct(product);
         }
         cradItrem.setQuantity(updatedQuantity);
       repo.save(cradItrem);
       return updatedQuantity;
		
	}

	
	public List<CradItem> findByCustomer(Customer customer){

		return repo.findByCustomer(customer);
	}
	
	public float updateCard(Integer producrId,Integer quantity,Customer customer) {
		
		repo.updateQuantity(quantity, customer.getId(), producrId);
		Product product = productRepo.findById(producrId).get();
		float supTotal = product.getDiscountPrice()*quantity;
		return supTotal;
		
		
	}
	
	public void remove(Customer customer,Integer productId) {
		repo.deleteByCustomerAndProduct(customer.getId(), productId);
		
		
	}
	
	public void deleteByCustomer(Customer customer) {
		repo.deleteByCustomer(customer.getId());
		
		
	}
	
	
}
