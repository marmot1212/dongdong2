package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2017-07-21.
 */
public class CollectInfo {
    private String id;
    private String title;
    private String content;
    private String img_url_1;
    private String type_mess_id;
    private String views;
    private String gather_count;
    public CollectInfo(){

    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getGather_count() {
        return gather_count;
    }

    public void setGather_count(String gather_count) {
        this.gather_count = gather_count;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url_1() {
        return img_url_1;
    }

    public void setImg_url_1(String img_url_1) {
        this.img_url_1 = img_url_1;
    }

    public String getType_mess_id() {
        return type_mess_id;
    }

    public void setType_mess_id(String type_mess_id) {
        this.type_mess_id = type_mess_id;
    }
}
