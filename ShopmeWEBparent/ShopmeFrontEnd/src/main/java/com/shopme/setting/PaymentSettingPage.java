package com.shopme.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class PaymentSettingPage  extends SettingBag{

	public PaymentSettingPage(List<Setting> listSettings) {
		super(listSettings);
		// TODO Auto-generated constructor stub
	}
	
	public String getUrl() {
		return super.getValue("PAYPAL_API_BASD_URL");
	}
	
	public String getClientID() {
		return super.getValue("PAYPAL_API_CLIENT_ID");
	}
	public String getClientSecret() {
		return super.getValue("PAYPAL_API_CLIENT_SECRET");
	}
	

}
