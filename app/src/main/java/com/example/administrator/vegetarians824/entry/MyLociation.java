package com.example.administrator.vegetarians824.entry;

/**
 * 我的位置，初始化保存自己的定位
 * @author Administrator
 *
 */
public class MyLociation {

	private String longitude;//经度
	private String latitude;//纬度
	private String myCity;//我的城市
	private String myaddress;
	public MyLociation() {
		super();
	}
	
	public MyLociation(String longitude, String latitude, String myCity) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.myCity = myCity;
	}
	public MyLociation(String longitude, String latitude, String myCity,String myaddress) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.myCity = myCity;
		this.myaddress=myaddress;
	}
	public String getMyaddress() {
		return myaddress;
	}

	public void setMyaddress(String myaddress) {
		this.myaddress = myaddress;
	}

	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Override
	public String toString() {
		return "My_Lociation [longitude=" + longitude + ", latitude="
				+ latitude + "]";
	}

	public String getMyCity() {
		return myCity;
	}

	public void setMyCity(String myCity) {
		this.myCity = myCity;
	}
	
	
	
	
	
}