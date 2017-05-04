package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2016-10-12.
 */
public class UserRank {
    private String id;
    private String username;
    private String growth_index;
    private String user_head_img;
    private String lv;

    public UserRank(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrowth_index() {
        return growth_index;
    }

    public void setGrowth_index(String growth_index) {
        this.growth_index = growth_index;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_head_img() {
        return user_head_img;
    }

    public void setUser_head_img(String user_head_img) {
        this.user_head_img = user_head_img;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }
}
