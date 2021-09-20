package com.shopme.common.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "order_table")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String addtess1;
	
	private String address2;
	
     private String city;
	
	private String state;
	
	private String postalCode;
	
	private String country;
	
	private Date orderTime;
	
	private float shippingCost;
	private float productCost;
	private float subTotal;
	private float tax;
	private float total;
	
	private int deliverDays;
	private Date deliverDay;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	private Customer customer;
	
	@OneToMany(mappedBy = "orderDetails", cascade = CascadeType.ALL,orphanRemoval = true)
	private Set<OrderDetail> orderDetails  = new HashSet<>();
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddtess1() {
		return addtess1;
	}

	public void setAddtess1(String addtess1) {
		this.addtess1 = addtess1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	@Transient
	public String getDestination() {
		String destination =  city + ", ";
		if (state != null && !state.isEmpty()) destination += state + ", ";
		destination += country;
		
		return destination;
	}

	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhone(customer.getPhoneNumber());
		setAddtess1(customer.getAddressLine1());
		setAddress2(customer.getAddressLine2());
		setCity(customer.getSity());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalode());
		setState(customer.getState());		
	}

	public void copyAddressFromAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhone(address.getPhone());
		setAddtess1(address.getAddress());
		setAddress2(address.getAddress2());
		setCity(address.getCity());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
		setState(address.getState());		
	}

	@Transient
	public String getShippingAddress() {
		
		String address = firstName;
		
		if(lastName != null && !lastName.isEmpty()) address += " " +lastName;
		
		if(!addtess1.isEmpty()) address += " , " + addtess1;
		if(address2 != null && !address2.isEmpty()) address += " , " +address2;
		if(!city.isEmpty()) address += " , " + city;
		if(state != null && !state.isEmpty()) address += " , " +state;
		address+= " , " + country;
		if(!postalCode.isEmpty()) address += " postalCode  : " + postalCode;
		if(!phone.isEmpty()) address += " phone Number  : " + phone;



		return address;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDay);
	}	
	
	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
 		
		try {
			this.deliverDay = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		} 		
	}
	public String getDeliverDay() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(this.deliverDay);
	}

	public void setDeliverDay(Date deliverDay) {
		this.deliverDay = deliverDay;
	}
	
	
	@Transient
	public String getRecipientName() {
		
String name = firstName;
		
		if(lastName != null && !lastName.isEmpty()) name += " " +lastName;
		
		return name;
		
	}
	
	
	
	
	@Transient
	public String getRecipientAddress() {
		
		String address = addtess1;
		
		
		if(address2 != null && !address2.isEmpty()) address += " , " +address2;
		if(!city.isEmpty()) address += " , " + city;
		if(state != null && !state.isEmpty()) address += " , " +state;
		address+= " , " + country;
		if(!postalCode.isEmpty()) address += " postalCode  : " + postalCode;


		return address;
		
	}
	
	@Transient
	public boolean isCOD() {
		
		return paymentMethod.equals(PaymentMethod.COD);
	}
	
	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}
	
	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}
	
	@Transient
	public boolean isDelevered() {
		return hasStatus(OrderStatus.DELIVERED);
	}
	
	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}
	
	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}
	
	
	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUEST);
	}

	
	public boolean hasStatus(OrderStatus status) {
		
		for (OrderTrack aTrack : orderTracks) {
			if(aTrack.getStatus().equals(status)) {
				return true;
			}
			
		}
		return false;
	}
	
	
	@Transient
	public String getProductNames() {
		String productNames="";
		
		productNames="<ul>";
		
		for (OrderDetail orderDetail : orderDetails) {
			productNames+= "<li>" + orderDetail.getProduct().getName()+"</li>";
			
		}
		productNames+= "</ul>";
		
		return productNames;
		
	}
	

	
	
	
	
}
