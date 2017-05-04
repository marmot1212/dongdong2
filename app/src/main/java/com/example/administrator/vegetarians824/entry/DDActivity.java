package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2016-08-26.
 */
public class DDActivity {
    private String activity_id;//活动id
    private String activity_title;//活动标题
    private String activity_start_time;//活动开始时间
    private String activity_finish_time;//活动结束时间
    private String activity_province;//活动省份
    private String activity_content;//活动内容
    private String activity_img_url_1;//详情大图；
    private String activity_img_url_2;//列表显示图
    private String activity_type;//活动类型
    private String activity_img_url_th_1;//详情缩略图

    public DDActivity(){
        super();
    }

    public DDActivity(String activity_id, String activity_title, String activity_start_time,
                      String activity_finish_time, String activity_province, String activity_content,
                      String activity_img_url_1, String activity_img_url_2, String activity_type, String activity_img_url_th_1) {
        this.activity_id = activity_id;
        this.activity_title = activity_title;
        this.activity_start_time = activity_start_time;
        this.activity_finish_time = activity_finish_time;
        this.activity_province = activity_province;
        this.activity_content = activity_content;
        this.activity_img_url_1 = activity_img_url_1;
        this.activity_img_url_2 = activity_img_url_2;
        this.activity_type = activity_type;
        this.activity_img_url_th_1 = activity_img_url_th_1;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getActivity_start_time() {
        return activity_start_time;
    }

    public void setActivity_start_time(String activity_start_time) {
        this.activity_start_time = activity_start_time;
    }

    public String getActivity_finish_time() {
        return activity_finish_time;
    }

    public void setActivity_finish_time(String activity_finish_time) {
        this.activity_finish_time = activity_finish_time;
    }

    public String getActivity_province() {
        return activity_province;
    }

    public void setActivity_province(String activity_province) {
        this.activity_province = activity_province;
    }

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getActivity_img_url_1() {
        return activity_img_url_1;
    }

    public void setActivity_img_url_1(String activity_img_url_1) {
        this.activity_img_url_1 = activity_img_url_1;
    }

    public String getActivity_img_url_2() {
        return activity_img_url_2;
    }

    public void setActivity_img_url_2(String activity_img_url_2) {
        this.activity_img_url_2 = activity_img_url_2;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getActivity_img_url_th_1() {
        return activity_img_url_th_1;
    }

    public void setActivity_img_url_th_1(String activity_img_url_th_1) {
        this.activity_img_url_th_1 = activity_img_url_th_1;
    }
}