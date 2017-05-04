package com.example.administrator.vegetarians824.entry;

/**
 * 城市列表，省份下的城市
 * @author Administrator
 *
 */
public class ProvinceCity {

	private String name;

	public ProvinceCity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProvinceCity(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProvinceCity [name=" + name + "]";
	}
	
	
	
	
}
