package com.shopme.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.customer.CustomerSettingBag;

@Service
public class SettingService {
	
	@Autowired
	private SettingRepository repo;
	@Autowired private CurrencyRepository currencyRepo;
	
	
	public List<Setting> getGenerarSetting() {
//		List<Setting> generalSettings = new ArrayList<>();
//				
//		List<Setting> settings = repo.findBySettingCategory(SettingCategory.GENERAL);
//		List<Setting> currencySettings = repo.findBySettingCategory(SettingCategory.CURRENCY);
//
//		generalSettings.addAll(settings);
//		generalSettings.addAll(currencySettings);
//		
//		return  settings;
		return repo.findByToSettingCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
		
	}
	
	public CustomerSettingBag emailSettingBag() {
		List<Setting> setting=repo.findBySettingCategory(SettingCategory.MAIL_SERVER);
		setting.addAll(repo.findBySettingCategory(SettingCategory.MAIL_TEMPLATES));
		return new CustomerSettingBag(setting);
	}
	
	
	public PaymentSettingPage paymentSettingBag() {
		List<Setting> setting=repo.findBySettingCategory(SettingCategory.PAYMENT);
		return new  PaymentSettingPage(setting);
	}
	
	
	
	
	public String getCurrencyCode() {
		Setting setting = repo.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepo.findById(currencyId).get();
		
		return currency.getCode();
	}


}


