package com.example.administrator.vegetarians824.entry;

/**
 * 自定义对象，经纬度
 * @author Administrator
 *
 */
public class LatLng_jw {

	private String longitude;//经度
	private String latitude;//纬度
	
	public LatLng_jw() {
		super();
	}

	public LatLng_jw(String longitude, String latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
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
		return "LatLng_jw [longitude=" + longitude + ", latitude=" + latitude
				+  "]";
	}

	
	
	
	
	
}

