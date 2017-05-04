package com.example.administrator.vegetarians824.push;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016-11-18.
 */
public class PushMessageReceiver extends com.baidu.android.pushservice.PushMessageReceiver {
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.d("==============errorcode", i+"");
        Log.d("==============appid", s);
        Log.d("==============userid", s1);
        Log.d("==============channelid", s2);
        Log.d("==============requestid", s3);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
}
