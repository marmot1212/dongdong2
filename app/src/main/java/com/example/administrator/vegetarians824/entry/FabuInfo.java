package com.example.administrator.vegetarians824.entry;

/**
 * Created by Administrator on 2016-10-11.
 */
public class FabuInfo {
    private String id;
    private String title;
    private String create_time;
    private String status;
    private String type_mess_id;
    private String list_id;
    private String mess_id;

    public String getMess_id() {
        return mess_id;
    }

    public void setMess_id(String mess_id) {
        this.mess_id = mess_id;
    }

    public FabuInfo(){

    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType_mess_id() {
        return type_mess_id;
    }

    public void setType_mess_id(String type_mess_id) {
        this.type_mess_id = type_mess_id;
    }
}
