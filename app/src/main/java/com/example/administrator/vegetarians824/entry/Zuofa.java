package com.example.administrator.vegetarians824.entry;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016-09-19.
 */
public class Zuofa {
    private String step;
    private String pic="";
    private String dep;
    private String url;
    private String info;
    private Bitmap bp;

    public Bitmap getBp() {
        return bp;
    }

    public void setBp(Bitmap bp) {
        this.bp = bp;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Zuofa(){
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
