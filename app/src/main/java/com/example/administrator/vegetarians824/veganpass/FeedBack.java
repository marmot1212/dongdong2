package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedBack extends AppCompatActivity {
    private String country,language;
    private String country_id="",language_id="";
    private FrameLayout fanhui;
    private EditText content;
    private Button bt;
    private SharedPreferences pre;
    private String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        StatusBarUtil.setColorDiff(this,0x8e000000);
        Intent intent=getIntent();
        country=intent.getStringExtra("country");
        language=intent.getStringExtra("language");
        Log.d("==========info",country+" "+language);
        pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        code=pre.getString("phoneid","");
        initView();
        getCounryId();
        getLanguageId();
    }
    public void initView(){
        fanhui=(FrameLayout)findViewById(R.id.feedback_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        content=(EditText)findViewById(R.id.feedback_content);
        bt=(Button)findViewById(R.id.feedback_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!content.getText().toString().equals("")){
                    admit();
                }else {
                    Toast.makeText(getBaseContext(),"请填写反馈内容",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getCounryId(){
        StringRequest request=new StringRequest(URLManager.CountryList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){

                        JSONArray ja=js1.getJSONArray("data");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            if(jo.getString("name").equals(country)){
                                country_id=jo.getString("id");
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }

    public void getLanguageId(){
        StringPostRequest spr=new StringPostRequest(URLManager.Language, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){

                        JSONArray ja=js1.getJSONArray("data");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            if(jo.getString("name").equals(language)){
                                language_id=jo.getString("id");
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("country",country);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void admit(){
        Log.d("==========aa",country_id+" "+language_id);
        if(!country_id.equals("")&&!language_id.equals("")){
            StringPostRequest spr=new StringPostRequest(URLManager.FeedBack, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject js1=new JSONObject(s);
                        if(js1.getString("status").equals("success")){
                            finish();
                            Toast.makeText(getBaseContext(),js1.getString("msg"),Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            spr.putValue("content",content.getText().toString());
            spr.putValue("country_mess_id",country_id);
            spr.putValue("language_mess_id",language_id);
            spr.putValue("code",code);
            SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
        }else {
            Toast.makeText(getBaseContext(),"获取网络失败",Toast.LENGTH_SHORT).show();
        }
    }

}
