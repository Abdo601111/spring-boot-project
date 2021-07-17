package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CradItem;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;

@Service
public class CheckOutServcie {
	
	public static final int DMS_DEVISOR=139;
	
	public CheckOutInfo perperChickOut(List<CradItem> cradItem, ShippingRate shippingRate) {
		
		CheckOutInfo checkOutInfo = new CheckOutInfo();
		float productCost= calculateProductCost(cradItem);
		float productTotal = calculateProductTotal(cradItem);
		float shippingCostTotal = calculateShippingCost(cradItem,shippingRate);
		float paymentTotal = productTotal + shippingCostTotal;
		
		
		checkOutInfo.setProductCost(productCost);
		checkOutInfo.setProductTotal(productTotal);
		checkOutInfo.setDeliverDays(shippingRate.getDate());
		checkOutInfo.setCodeSupported(shippingRate.isCodeSupported());
		checkOutInfo.setShippinCostTotal(shippingCostTotal);
		checkOutInfo.setPaymentTotal(paymentTotal);
		return checkOutInfo;
		
	}

	private float calculateShippingCost(List<CradItem> cradItem, ShippingRate shippingRate) {
		float shippingCostTota=0.0f;
		
		for(CradItem item: cradItem) {
			Product product = item.getProduct();
			float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DMS_DEVISOR;
			float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
			float shippingCost =  item.getQuantity()* shippingRate.getRate();
			
			item.setShippingCost(shippingCost);
			
			shippingCostTota += shippingCost;
		}
		
		return shippingCostTota;
	}
	
	
	
	

	

	private float calculateProductTotal(List<CradItem> cradItem) {
		float total=0.0f;
		for(CradItem item :cradItem) {
			total +=  item.getSupTotal();
			
		}
		return total;
	}
	private float calculateProductCost(List<CradItem> cradItem) {
		float cost=0.0f;
		for(CradItem item :cradItem) {
			cost +=  item.getQuantity()*item.getProduct().getCost();
			
		}
		return cost;
	}

}
