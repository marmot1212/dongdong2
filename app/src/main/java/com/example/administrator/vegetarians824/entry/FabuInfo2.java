package com.example.administrator.vegetarians824.entry;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-10.
 */
public class FabuInfo2 implements Serializable{
    private String id;
    private String res_id;
    private String detail;
    private String title;
    private String finish_time;
    private String restaurant_title;
    public FabuInfo2(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public String getRestaurant_title() {
        return restaurant_title;
    }

    public void setRestaurant_title(String restaurant_title) {
        this.restaurant_title = restaurant_title;
    }
}
