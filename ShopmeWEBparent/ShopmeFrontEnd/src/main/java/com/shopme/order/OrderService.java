package com.shopme.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckOutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repo;

	public Order createorder(Customer customer,Address address,List<CradItem> cradItem,PaymentMethod paymentMethod
			,CheckOutInfo checkOutInfo) {
		Order newOrder = new Order();
		newOrder.setOrderTime(new Date());
		if(paymentMethod.equals(PaymentMethod.PAYPAL)) {
			newOrder.setStatus(OrderStatus.PAID);
		}else {
			newOrder.setStatus(OrderStatus.NEW);

		}
		newOrder.setCustomer(customer);
		newOrder.setSubTotal(checkOutInfo.getProductTotal());
		newOrder.setProductCost(checkOutInfo.getProductCost());
		newOrder.setShippingCost(checkOutInfo.getShippinCostTotal());
		newOrder.setTax(0.0f);
		newOrder.setTotal(checkOutInfo.getPaymentTotal());
		newOrder.setDeliverDays(checkOutInfo.getDeliverDays());
		newOrder.setDeliverDay(checkOutInfo.getDeliverDate());
		newOrder.setPaymentMethod(paymentMethod);
		
		if(address == null) {
			newOrder.copyAddressFromCustomer();
		}else {
			newOrder.copyAddressFromAddress(address);
		}
		
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		for(CradItem crad : cradItem) {
			Product product = crad.getProduct();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderDetails(newOrder);
			orderDetail.setProduct(product);
			orderDetail.setQuantity(crad.getQuantity());
			orderDetail.setProductCost(product.getCost() * crad.getQuantity());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			orderDetail.setSubTotal(crad.getSupTotal());
			orderDetail.setShippingCost(crad.getShippingCost());
			orderDetails.add(orderDetail);
		}
		
		return repo.save(newOrder);
		
	}
	
	
}
