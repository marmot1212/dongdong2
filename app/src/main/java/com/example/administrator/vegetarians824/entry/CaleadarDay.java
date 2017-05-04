package com.example.administrator.vegetarians824.entry;

import java.util.List;

/**
 * Created by Administrator on 2016-10-26.
 */
public class CaleadarDay {
    private String id;
    private String zangli;
    private String y;
    private String m;
    private String d;
    private String time;
    private String zanglis;
    private List<String> active;
    private boolean ismark;
    public CaleadarDay(){

    }

    public List<String> getActive() {
        return active;
    }

    public void setActive(List<String> active) {
        this.active = active;
    }

    public boolean ismark() {
        return ismark;
    }

    public void setIsmark(boolean ismark) {
        this.ismark = ismark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZangli() {
        return zangli;
    }

    public void setZangli(String zangli) {
        this.zangli = zangli;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZanglis() {
        return zanglis;
    }

    public void setZanglis(String zanglis) {
        this.zanglis = zanglis;
    }
}
