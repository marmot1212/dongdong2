package com.example.administrator.vegetarians824.veganpass;

/**
 * Created by Administrator on 2017-05-22.
 */
public class Food {
    private String name;
    private int id;
    private boolean ischoose=false;
    public Food(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }
}
