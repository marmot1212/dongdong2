package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.TripListAdapter;
import com.example.administrator.vegetarians824.entry.TripInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Jiudian extends AppCompatActivity {
    private String city;
    private List<TripInfo> list;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiudian);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        try {
            city= URLEncoder.encode(intent.getStringExtra("city"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        list=new ArrayList<>();
        listView=(ListView)findViewById(R.id.jiudian_list);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.jiudian_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }
    public void initView(){
        StringRequest request=new StringRequest(URLMannager.Mishi_CityCanting+city+"/map_type/6", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("list");
                    for(int i=0;i<ja.length();i++){
                        JSONObject info=ja.getJSONObject(i);
                        TripInfo tripInfo = new TripInfo();
                        tripInfo.setId(info.getString("id"));
                        tripInfo.setTitle(info.getString("title"));
                        tripInfo.setContent(info.getString("content"));
                        tripInfo.setType(info.getString("type"));
                        tripInfo.setImg_url_1(info.getString("img_url_1"));
                        tripInfo.setTel(info.getString("tel"));
                        tripInfo.setUnit_price(info.getString("unit_price"));
                        tripInfo.setLongitude(info.getString("longitude"));
                        tripInfo.setLatitude(info.getString("latitude"));
                        tripInfo.setCreate_time(info.getString("create_time"));
                        list.add(tripInfo);
                    }
                    listView.setAdapter(new TripListAdapter(list,Jiudian.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
