package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Order;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.entity.OrderStatus;
import com.shopme.common.entity.OrderTrack;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {

	
	@Autowired private OrderRepository repo;
	@Autowired private TestEntityManager entityManger;
	
	@Test
	public void testCreateNewOrderWithSingleProduct() {
		Customer customer = entityManger.find(Customer.class, 39);
		Product product = entityManger.find(Product.class, 3);
		
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAddressFromCustomer();
		mainOrder.setShippingCost(10);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0);
		mainOrder.setSubTotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10);
		
		mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
		mainOrder.setStatus(OrderStatus.NEW);
		mainOrder.setDeliverDay(new Date());
		mainOrder.setDeliverDays(1);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrderDetails(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10);
		orderDetail.setQuantity(1);
		orderDetail.setSubTotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail);
		
		Order savedOrder = repo.save(mainOrder);
		
		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}
	
	
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId = 20;
		Order order = repo.findById(orderId).get();
		
		OrderTrack newTrack = new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setUpdatedTime(new Date());
		newTrack.setStatus(OrderStatus.NEW);
		newTrack.setNotes(OrderStatus.NEW.defaultDescription());

		OrderTrack processingTrack = new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setUpdatedTime(new Date());
		processingTrack.setStatus(OrderStatus.PROCESSING);
		processingTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(newTrack);
		orderTracks.add(processingTrack);
		
		Order updatedOrder = repo.save(order);
		
		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}

	
	
	
	
}
