package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2016-09-29.
 */
public class User {
    private String name;
    private String id;
    private String pic;
    private String pwd;
    private String mobile;
    private String status;
    private String province;
    private String city;
    private String sex;
    private String intro;
    private String jifen;
    private String type;
    private String tenans="2";
    private boolean islogin=false;
    public User(){
    }

    public String getTenans() {
        return tenans;
    }

    public void setTenans(String tenans) {
        this.tenans = tenans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public void initUser(){
        name=null;
        id=null;
        islogin=false;
        pwd=null;
        pic=null;
        mobile=null;
        status=null;
        province=null;
        city=null;
        sex=null;
        intro=null;
        jifen=null;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean islogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }
}
