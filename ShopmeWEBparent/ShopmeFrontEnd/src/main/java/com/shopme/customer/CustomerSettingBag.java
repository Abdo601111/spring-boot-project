package com.shopme.customer;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class CustomerSettingBag extends SettingBag {

	public CustomerSettingBag(List<Setting> listSettings) {
		super(listSettings);
		// TODO Auto-generated constructor stub
	}

	
	public String getHost() {
		return super.getValue("MAIL_HOST");
	}
	
	public int getPort() {
		return Integer.parseInt(super.getValue("MAIL_PORT"));
	}
	
	public String getUserName() {
		return super.getValue("MAIL_USERNAME");
	}
	
	public String getPassword() {
		return super.getValue("MAIL_PASSWORD");
	}
	
	public String getSMTPAuth() {
		return super.getValue("SMTP_AUTH");
	}
	
	public String getSMTPSecurity() {
		return super.getValue("SMTP_SECURED");
	}
	
	public String getFormAddress() {
		return super.getValue("MAIL_FROM");
	}
	
	public String getSendName() {
		return super.getValue("MAIL_SERVER_NAME");
	}
	
	public String getCustomerVerifaySubject (){
		return super.getValue("CUSTOMER_VERVAY_SUBJECT");
	}
	
	public String getCustomerVerifayContent() {
		return super.getValue("CUSTOMER_VERFAY_CONTENT");
	}
	
	
}
