package com.shopme.checkout;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckOutInfo {
	
	private float productCost;
	private float productTotal;
	private float productCostTotal;
	private float paymentTotal;
	private float shippinCostTotal;
	private int deliverDays;
	private Date deliverDate;
	private boolean codeSupported;
	
	public float getProductCost() {
		return productCost;
	}
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	public float getProductTotal() {
		return productTotal;
	}
	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}
	public float getProductCostTotal() {
		return productCostTotal;
	}
	public void setProductCostTotal(float productCostTotal) {
		this.productCostTotal = productCostTotal;
	}
	public float getPaymentTotal() {
		return paymentTotal;
	}
	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	public int getDeliverDays() {
		return deliverDays;
	}
	public void setDeliverDays(int deliverDays) {
		
		this.deliverDays = deliverDays;
	}
	public Date getDeliverDate() {
		Calendar calender =  Calendar.getInstance();
		calender.add(Calendar.DATE, deliverDays);
		return calender.getTime();
	}
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}
	public boolean isCodeSupported() {
		return codeSupported;
	}
	public void setCodeSupported(boolean codeSupported) {
		this.codeSupported = codeSupported;
	}
	public float getShippinCostTotal() {
		return shippinCostTotal;
	}
	public void setShippinCostTotal(float shippinCostTotal) {
		this.shippinCostTotal = shippinCostTotal;
	}
	
	public String getPaymentTotal4PayPal() {
		DecimalFormat formatter = new DecimalFormat("###,###.##");
		return formatter.format(paymentTotal);
	}

	
	

}
