package com.example.administrator.vegetarians824.myapplications;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by clawpo on 2017/3/21.
 */

public class SharePreferencesUtils {
    private static final String SHARE_PREFERENCE_NAME = "com.dongdong.vegetarians";
    private static final String LANGUAGE_TYPE = "language_type";
    private static final String PHONE_ID = "phone_id";
    private static final String VEGETARIAN_TYPE = "vegetarian_type";

    static SharePreferencesUtils instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharePreferencesUtils() {
        sharedPreferences = BaseApplication.getApp().
                getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharePreferencesUtils getInstance(){
        if (instance==null){
            instance = new SharePreferencesUtils();
        }
        return instance;
    }

    public void setLanguageType(String languageType){
        editor.putString(LANGUAGE_TYPE,languageType).commit();
    }

    public String getLanguageType(){
        return sharedPreferences.getString(LANGUAGE_TYPE,null);
    }

    public void setPhoneId(String phoneId){
        editor.putString(PHONE_ID,phoneId).commit();
    }

    public String getPhoneId(){
        return sharedPreferences.getString(PHONE_ID,null);
    }

    public void setVegetarianType(String vegetarianType){
        editor.putString(VEGETARIAN_TYPE,vegetarianType).commit();
    }

    public String getVegetarianType(){
        return sharedPreferences.getString(VEGETARIAN_TYPE,null);
    }

    public void removeUser(){
        editor.remove(LANGUAGE_TYPE).remove(PHONE_ID).remove(VEGETARIAN_TYPE).commit(); // 链式调用
    }

}
