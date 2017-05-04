package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateAreaCity extends AppCompatActivity {
    String id,province,mycity,flag;
    private List<String> city;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_area_city);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        city=new ArrayList<>();
        listView=(ListView)findViewById(R.id.city);
        Intent intent=getIntent();
        id=intent.getStringExtra("pid");
        province=intent.getStringExtra("province");
        flag=intent.getStringExtra("flag");
        initop();
        getCity();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit6_city_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getCity(){

        StringRequest request=new StringRequest("http://www.isuhuo.com/plainLiving/androidapi/user/update_province/pid/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        String p=jo.getString("name");
                        city.add(p);
                    }
                    listView.setAdapter( new ArrayAdapter<>(UpdateAreaCity.this,android.R.layout.simple_list_item_1,city) );
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            mycity=city.get(i);
                            if(flag.equals("1"))
                            {
                            doPost();
                            } else
                                if(flag.equals("2")){
                                    BaseApplication.app.setAcity(mycity);
                                    UpdateArea.instance.finish();
                                    finish();
                            }
                        }
                    });
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

    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/update_areas", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    String code=js.getString("Code");
                    if(code.equals("1")){
                        BaseApplication.app.getUser().setProvince(province);
                        BaseApplication.app.getUser().setCity(mycity);
                        SharedPreferences preferences=UpdateAreaCity.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("province",province);
                        editor.putString("city",mycity);
                        editor.commit();//提交数据
                       // Bundle bundle = new Bundle();
                        //bundle.putString("which","my_commentlist");
                        //this.setArguments(bundle);

                        UpdateArea.instance.finish();
                        finish();
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("province",province);
        spr.putValue("city",mycity);
        SlingleVolleyRequestQueue.getInstance(UpdateAreaCity.this).addToRequestQueue(spr);
    }
}
