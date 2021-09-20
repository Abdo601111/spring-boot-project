package com.shopme.admin.order;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
	
	@Autowired private OrderService service;
	
	@PostMapping("/order_shipping/update/{id}/{status}")
	public Response updateorderStatus(@PathVariable("id") int orderId,@PathVariable("status") String status) {
		
		service.updateStatus(orderId, status);
		return new Response(orderId,status);
		
		
	}

}

class Response{
	
	private int orderId;
	private String status;
	
	
	
	public Response(int orderId, String status) {
		super();
		this.orderId = orderId;
		this.status = status;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
