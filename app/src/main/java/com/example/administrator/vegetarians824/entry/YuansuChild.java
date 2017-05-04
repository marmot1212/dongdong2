package com.example.administrator.vegetarians824.entry;

import java.util.List;

/**
 * Created by Administrator on 2016-09-20.
 */
public class YuansuChild {
    private int y;
    private String name;
    private String content;
    private String contents;
    private List<Shuguo> sg_list;
    private String id;
    private boolean isexpand;
    public YuansuChild(){

    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isexpand() {
        return isexpand;
    }

    public void setIsexpand(boolean isexpand) {
        this.isexpand = isexpand;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Shuguo> getSg_list() {
        return sg_list;
    }

    public void setSg_list(List<Shuguo> sg_list) {
        this.sg_list = sg_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
