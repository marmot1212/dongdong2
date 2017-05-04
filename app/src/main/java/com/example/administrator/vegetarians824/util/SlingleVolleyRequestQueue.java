package com.example.administrator.vegetarians824.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/5/6.
 */
public class SlingleVolleyRequestQueue {
    private static SlingleVolleyRequestQueue slingleQueue;
    private RequestQueue requestQueue;
    private static Context context;

    //私有化构造
    private SlingleVolleyRequestQueue(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }
    //提供获取请求队列的方法
    public RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    //提供获取类对象的方法
    public static synchronized SlingleVolleyRequestQueue getInstance(Context context){
        if (slingleQueue == null){
            slingleQueue = new SlingleVolleyRequestQueue(context);
        }
        return slingleQueue;
    }
    public  void  addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }


}
