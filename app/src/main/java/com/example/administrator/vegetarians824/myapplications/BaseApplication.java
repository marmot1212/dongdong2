package com.example.administrator.vegetarians824.myapplications;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.HotCity;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.entry.Province;
import com.example.administrator.vegetarians824.entry.ProvinceCity;
import com.example.administrator.vegetarians824.entry.User;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-09-06.
 */
public class BaseApplication extends MultiDexApplication {

    // 拿到application上下文
    public static BaseApplication app;

    // 保存我的位置
    public MyLociation myLociation;

    public User user;

    public String acity;

    public String getAcity() {
        return acity;
    }

    public void setAcity(String acity) {
        this.acity = acity;
    }

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        Fresco.initialize(getApplicationContext());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BaseApplication() {
        super();
    }

    public static BaseApplication getApp() {
        return app;
    }

    public static void setApp(BaseApplication app) {
        BaseApplication.app = app;
    }


    public MyLociation getMyLociation() {
        return myLociation;
    }

    public void setMyLociation(MyLociation myLociation) {
        this.myLociation = myLociation;
    }


}
