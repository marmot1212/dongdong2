package com.example.administrator.vegetarians824.myapplications;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by clawpo on 2017/3/21.
 */

public class SharePrefrenceUtils {
    private static final String SHARE_PREFERENCE_NAME = "com.dongdong.vegetarians";
    private static final String LANGUAGE_TYPE = "language_type";
    static SharePrefrenceUtils instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharePrefrenceUtils() {
        sharedPreferences = BaseApplication.getApp().
                getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePrefrenceUtils getInstance(){
        if (instance==null){
            instance = new SharePrefrenceUtils();
        }
        return instance;
    }

    public void setLanguageType(String languageType){
        editor.putString(LANGUAGE_TYPE,languageType).commit();
    }

    public String getLanguageType(){
        return sharedPreferences.getString(LANGUAGE_TYPE,null);
    }

    public void removeUser(){
        editor.remove(LANGUAGE_TYPE).commit();
    }

}
