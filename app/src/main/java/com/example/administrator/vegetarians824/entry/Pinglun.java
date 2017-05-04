package com.example.administrator.vegetarians824.entry;

/**
 * 餐厅评论
 * 
 */
public class Pinglun {
	public String id;
	public String uid;
	public String content; // 餐馆内容
	public String user_head_img_th;// 评论头像
	public String create_time_text;// 评论时间
	public String username;// 发布人
	public String img_url_01;
	public String img_url_02;
	public String img_url_03;
	public String img_url_04;
	public String lv;

	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

	public String getImg_url_01() {
        return img_url_01;
    }

    public void setImg_url_01(String img_url_01) {
        this.img_url_01 = img_url_01;
    }

    public String getImg_url_02() {
        return img_url_02;
    }

    public void setImg_url_02(String img_url_02) {
        this.img_url_02 = img_url_02;
    }

    public String getImg_url_03() {
        return img_url_03;
    }

    public void setImg_url_03(String img_url_03) {
        this.img_url_03 = img_url_03;
    }

    public String getImg_url_04() {
        return img_url_04;
    }

    public void setImg_url_04(String img_url_04) {
        this.img_url_04 = img_url_04;
    }

    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Pinglun() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUser_head_img_th() {
		return user_head_img_th;
	}

	public void setUser_head_img_th(String user_head_img_th) {
		this.user_head_img_th = user_head_img_th;
	}

	public String getCreate_time_text() {
		return create_time_text;
	}

	public void setCreate_time_text(String create_time_text) {
		this.create_time_text = create_time_text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Pinglun [id=" + id + ", content=" + content
				+ ", user_head_img_th=" + user_head_img_th
				+ ", create_time_text=" + create_time_text + ", username="
				+ username + "]";
	}

	
	
	
	
	
}
