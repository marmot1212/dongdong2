package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2017-08-03.
 */
public class RestaurantTag {
    private String id;
    private String title;
    private String status;
    private String count;
    private String click_status;
    public RestaurantTag(){

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getClick_status() {
        return click_status;
    }

    public void setClick_status(String click_status) {
        this.click_status = click_status;
    }
}
