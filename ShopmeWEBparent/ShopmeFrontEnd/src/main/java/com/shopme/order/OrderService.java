package com.shopme.order;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.checkout.CheckOutInfo;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.OrderNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repo;
	
	
	public static final int ORDER_PAGES=5;
	

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
		
		
		OrderTrack track= new OrderTrack();
		track.setOrder(newOrder);
		track.setStatus(OrderStatus.NEW);
		track.setUpdatedTime(new Date());
		track.setNotes(OrderStatus.NEW.defaultDescription());
		
		newOrder.getOrderTracks().add(track);
		
		return repo.save(newOrder);
		
	}
	
	
	public Page<Order> listForCustomerByPage(Customer coustomer
			,int pageNum,String sortFiled,String sortDir,String keyword){
		
		Sort sort= Sort.by(sortFiled);
		sort=sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable= PageRequest.of(pageNum-1,ORDER_PAGES,sort);
		
		if(keyword !=null) {
			return repo.listAll(keyword, coustomer.getId(), pageable);
		}else {
			return repo.listAll( coustomer.getId(), pageable);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	public Order getOrder(int id,Customer customer) {
		return repo.findByIdAndCustomer(id, customer);
	}
	
	
	public void setOrderReturnedRequest(OrderReturnedRequest request,Customer customer) throws OrderNotFoundException {
		Order order= repo.findByIdAndCustomer(request.getOrderId(), customer);
		if(order == null) {
			throw new OrderNotFoundException("Order Not Fount");
		}
		
		if(order.isReturnRequested()) return;
		OrderTrack track= new OrderTrack();
		track.setOrder(order);
		track.setUpdatedTime(new Date());
		track.setStatus(OrderStatus.RETURN_REQUEST);
		
		String notes= "Reason  : " +request.getReason();
		if(!"".equals(request.getNote())) {
			notes += ". " +request.getNote();
		}
		track.setNotes(notes);
		order.getOrderTracks().add(track);
		order.setStatus(OrderStatus.RETURN_REQUEST);
		repo.save(order);
		
	}
	
	
	
	
	
}
