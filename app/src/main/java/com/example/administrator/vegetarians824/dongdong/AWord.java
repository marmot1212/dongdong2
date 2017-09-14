package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class AWord extends AppCompatActivity {
    private LinearLayout chunsu,dannaisu;
    private String veg,egg;
    private TextView show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aword);
        StatusBarUtil.setColorDiff(this,0xffffffff);
        show=(TextView)findViewById(R.id.trip_aword);
        chunsu=(LinearLayout) findViewById(R.id.trip_chunsu);
        dannaisu=(LinearLayout)findViewById(R.id.trip_dannaisu);
        initView();
        ImageView close=(ImageView)findViewById(R.id.trip_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initView(){
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        StringRequest request=new StringRequest(URLMannager.Trip_WORD + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void myJson(String s){
        try {
            JSONObject js1=new JSONObject(s);
            JSONObject js2=js1.getJSONObject("Result");
            veg=js2.getString("veg");
            egg=js2.getString("egg");

            show.setText(veg);
            chunsu.setBackgroundColor(0xff00aff0);
            initoperate();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void initoperate(){
        chunsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.setText(veg);
                chunsu.setBackgroundColor(0xff00aff0);
                dannaisu.setBackgroundColor(0xffb2b2b2);
            }
        });
        dannaisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.setText(egg);
                chunsu.setBackgroundColor(0xffb2b2b2);
                dannaisu.setBackgroundColor(0xff00aff0);
            }
        });
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
