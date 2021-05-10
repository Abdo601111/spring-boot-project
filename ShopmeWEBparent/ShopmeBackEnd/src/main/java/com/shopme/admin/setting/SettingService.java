package com.shopme.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Service
public class SettingService {
	
	@Autowired
	private SettingRepository repo;
	
	public List<Setting> listAll(){
		return (List<Setting>) repo.findAll();
	}

	public CeneralSettingBag getGenerarSetting() {
		List<Setting> generalSettings = new ArrayList<>();
				
		List<Setting> settings = repo.findBySettingCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findBySettingCategory(SettingCategory.CURRENCY);

		generalSettings.addAll(settings);
		generalSettings.addAll(currencySettings);
		
		return new CeneralSettingBag(generalSettings);
		
	}
	public void saveAll(Iterable<Setting> iterable) {
		repo.saveAll(iterable);
	}
	
	public List<Setting> getByMailServer(){
		return repo.findBySettingCategory(SettingCategory.MAIL_SERVER);
	}
	
	public List<Setting> getByMailTemlets(){
		return repo.findBySettingCategory(SettingCategory.MAIL_TEMPLATES);
	}
	
}
