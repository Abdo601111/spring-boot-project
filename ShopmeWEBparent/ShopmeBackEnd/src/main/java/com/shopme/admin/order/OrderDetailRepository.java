package com.shopme.admin.order;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.OrderDetail;

public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer> {
	
	

	@Query("SELECT NEW com.shopme.common.entity.OrderDetail(d.product.category.name, d.quantity, d.productCost, "
			+ " d.shippingCost, d.subTotal) FROM OrderDetail d WHERE"
			+ " d.orderDetails.orderTime BETWEEN ?1 AND ?2 ")
	public List<OrderDetail> findWithCategoryAndTimeBetween(Date startDate,Date endDate);
	
	
	
	
	
   @Query("SELECT NEW com.shopme.common.entity.OrderDetail(d.quantity,d.product.name,d.productCost,"
           + " d.shippingCost, d.subTotal) FROM OrderDetail d WHERE"
		    + " d.orderDetails.orderTime BETWEEN ?1 AND ?2 ")
	public List<OrderDetail> findWithProductAndTimeBetween(Date startDate,Date endDate);

}
