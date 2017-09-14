package com.example.administrator.vegetarians824.entry;

/**
 * 地图页面，底部的数据（标记物数据）
 * @author Administrator
 * 
 */

public class CantingInfo {
	public String id;
	public String title;
	public String tel;
	public String content;
	public String unit_pric;
	public String longitude;
	public String latitude;
	public String img_url_1;
	public String img_url_2;
	public String img_url_3;
	public String img_url_4;
	public String img_url_5;
	public String type;
	public String create_time;
	public String uid;
	public String address;
	public String img_url_th_1;//
	public String user_head_img;
	public String user_head_img_th;
	public String username;
	public String distance;
	public String parking;
	public String subway_status="";
	public String vege_status;
	public String vege_lv="0";


	public String getVege_lv() {
		return vege_lv;
	}

	public void setVege_lv(String vege_lv) {
		this.vege_lv = vege_lv;
	}

	public String getSubway_status() {
		return subway_status;
	}

	public void setSubway_status(String subway_status) {
		this.subway_status = subway_status;
	}

	public CantingInfo() {
		super();

	}

	public String getVege_status() {
		return vege_status;
	}

	public void setVege_status(String vege_status) {
		this.vege_status = vege_status;
	}

	public CantingInfo(String id, String title, String tel, String content,
					   String unit_pric, String longitude, String latitude,
					   String img_url_1, String img_url_2, String img_url_3,
					   String img_url_4, String img_url_5, String type,
					   String create_time, String uid, String address,
					   String img_url_th_1, String user_head_img, String user_head_img_th,
					   String username, String distance) {
		super();
		this.id = id;
		this.title = title;
		this.tel = tel;
		this.content = content;
		this.unit_pric = unit_pric;
		this.longitude = longitude;
		this.latitude = latitude;
		this.img_url_1 = img_url_1;
		this.img_url_2 = img_url_2;
		this.img_url_3 = img_url_3;
		this.img_url_4 = img_url_4;
		this.img_url_5 = img_url_5;
		this.type = type;
		this.create_time = create_time;
		this.uid = uid;
		this.address = address;
		this.img_url_th_1 = img_url_th_1;
		this.user_head_img = user_head_img;
		this.user_head_img_th = user_head_img_th;
		this.username = username;
		this.distance = distance;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUnit_pric() {
		return unit_pric;
	}

	public void setUnit_pric(String unit_pric) {
		this.unit_pric = unit_pric;
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

	public String getImg_url_1() {
		return img_url_1;
	}

	public void setImg_url_1(String img_url_1) {
		this.img_url_1 = img_url_1;
	}

	public String getImg_url_2() {
		return img_url_2;
	}

	public void setImg_url_2(String img_url_2) {
		this.img_url_2 = img_url_2;
	}

	public String getImg_url_3() {
		return img_url_3;
	}

	public void setImg_url_3(String img_url_3) {
		this.img_url_3 = img_url_3;
	}

	public String getImg_url_4() {
		return img_url_4;
	}

	public void setImg_url_4(String img_url_4) {
		this.img_url_4 = img_url_4;
	}

	public String getImg_url_5() {
		return img_url_5;
	}

	public void setImg_url_5(String img_url_5) {
		this.img_url_5 = img_url_5;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImg_url_th_1() {
		return img_url_th_1;
	}

	public void setImg_url_th_1(String img_url_th_1) {
		this.img_url_th_1 = img_url_th_1;
	}

	public String getUser_head_img() {
		return user_head_img;
	}

	public void setUser_head_img(String user_head_img) {
		this.user_head_img = user_head_img;
	}

	public String getUser_head_img_th() {
		return user_head_img_th;
	}

	public void setUser_head_img_th(String user_head_img_th) {
		this.user_head_img_th = user_head_img_th;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "CantingInfo [id=" + id + ", title=" + title + ", tel=" + tel
				+ ", content=" + content + ", unit_pric=" + unit_pric
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", img_url_1=" + img_url_1 + ", img_url_2=" + img_url_2
				+ ", img_url_3=" + img_url_3 + ", img_url_4=" + img_url_4
				+ ", img_url_5=" + img_url_5 + ", type=" + type
				+ ", create_time=" + create_time + ", uid=" + uid
				+ ", address=" + address + ", img_url_th_1=" + img_url_th_1
				+ ", user_head_img=" + user_head_img + ", user_head_img_th="
				+ user_head_img_th + ", username=" + username + ", distance="
				+ distance + "]";
	}


	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}
}
