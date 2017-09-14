package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2017-06-26.
 */
public class Subway {
    private String id;
    private String name;
    private String city_id;
    private String sort;
    private String status;
    private boolean ischoose=false;
    private String subway_id;
    private String longitude;
    private String latitude;
    public Subway(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean ischoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    public String getSubway_id() {
        return subway_id;
    }

    public void setSubway_id(String subway_id) {
        this.subway_id = subway_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
