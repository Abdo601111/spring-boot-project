package com.shopme.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Setting {
	
	@Id
	@Column(name="key_setting",nullable = false,length = 128)
	private String key;
	@Column(length = 1024,nullable = false)
	private String value ;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SettingCategory settingCategory;
	
	
	
	public Setting() {
		super();
	}
	public Setting(String key, String value, SettingCategory settingCategory) {
		super();
		this.key = key;
		this.value = value;
		this.settingCategory = settingCategory;
	}
	public Setting(String key) {
		super();
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
	public SettingCategory getSettingCategory() {
		return settingCategory;
	}
	public void setSettingCategory(SettingCategory settingCategory) {
		this.settingCategory = settingCategory;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Setting other = (Setting) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	
	
	
	
}
