package com.example.administrator.vegetarians824.entry;

import java.util.List;

/**
 * 城市列表，省份
 * @author Administrator
 *
 */
public class Province {

	private String name;
	private List<ProvinceCity> provinceCity;//省份内的城市
	
	public Province(String name, List<ProvinceCity> provinceCity) {
		super();
		this.name = name;
		this.provinceCity = provinceCity;
	}
	public Province() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProvinceCity> getProvinceCity() {
		return provinceCity;
	}
	public void setProvinceCity(List<ProvinceCity> provinceCity) {
		this.provinceCity = provinceCity;
	}
	@Override
	public String toString() {
		return "Province [name=" + name + ", provinceCity=" + provinceCity
				+ "]";
	}
	
	
	
	
	
	
	
	
}
