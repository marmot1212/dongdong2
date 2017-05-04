package com.example.administrator.vegetarians824.entry;

import java.util.List;

/**
 * 详情界面的bean对象
 * 
 * @author Administrator
 */
public class CanTingXq {
	public String id; // 餐厅id
	public String title; // 餐厅标题
	public String unit_price; // 餐厅的价格
	public Double longitude; // 餐厅经度
	public Double latitude; // 餐厅纬度
	public List<IMg_Url_w> img_url_w_pic; // 动态图集合
	public String type;// 餐厅的类型暂时不用
	public String address="";// 地址
	public String tel;// 电话
	public String content;
	public CanTingXq() {
		super();

	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public List<IMg_Url_w> getImg_url_w_pic() {
		return img_url_w_pic;
	}

	public void setImg_url_w_pic(List<IMg_Url_w> img_url_w_pic) {
		this.img_url_w_pic = img_url_w_pic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "CanTingXq [id=" + id + ", title=" + title + ", unit_price="
				+ unit_price + ", longitude=" + longitude + ", latitude="
				+ latitude + ", img_url_w_pic=" + img_url_w_pic + ", type="
				+ type + ", address=" + address + ", tel=" + tel + "]";
	}

	
	
	
	
}
