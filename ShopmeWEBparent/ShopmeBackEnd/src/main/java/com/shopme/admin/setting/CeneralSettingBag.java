package com.shopme.admin.setting;

import java.util.List;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

public class CeneralSettingBag extends SettingBag {

	public CeneralSettingBag(List<Setting> listSetting) {
		super(listSetting);
		// TODO Auto-generated constructor stub
	}
	
	public void updateSetting(String value) {
		super.update("CURRENCY_SEMBOL", value);
	}
	public void updateLogo(String value) {
		super.update("SITE_LOGO", value);
	}

}
