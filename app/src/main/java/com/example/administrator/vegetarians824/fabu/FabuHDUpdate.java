package com.example.administrator.vegetarians824.fabu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.FabuInfo2;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FabuHDUpdate extends AppCompatActivity {
    private  FabuInfo2 fbinfo;
    private Calendar calendar;
    private AutoCompleteTextView autext;
    private EditText title,content;
    private HashMap map;
    private List<String> arr;
    private LinearLayout line1,line2;
    private TextView data,time;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_hdupdate);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        fbinfo=(FabuInfo2) intent.getSerializableExtra("huodong");
        initop();
        initview();
        getData();
        dopost();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu004_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout del=(LinearLayout)findViewById(R.id.fabupd004_del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delPost();
            }
        });
    }
    public void  initview(){
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        autext=(AutoCompleteTextView)findViewById(R.id.fabupd004_et1);
        autext.setText(fbinfo.getRestaurant_title());
        autext.setThreshold(1);
        title=(EditText) findViewById(R.id.fabupd004_et2);
        title.setText(fbinfo.getTitle());
        content=(EditText) findViewById(R.id.fabupd004_et3);
        content.setText(fbinfo.getDetail());
        line1=(LinearLayout)findViewById(R.id.fabupd004_date1);
        line2=(LinearLayout)findViewById(R.id.fabupd004_time1);
        data=(TextView)findViewById(R.id.fabupd004_tv1);
        time=(TextView)findViewById(R.id.fabupd004_tv2);
        String s=fbinfo.getFinish_time();
        String [] ar=s.split(" ");
        data.setText(ar[0]);
        time.setText(ar[1]);
        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(FabuHDUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        data.setText(i+"-"+(i1+1)+"-"+i2);
                    }
                },year, month, day);
                datePicker.show();
            }
        });
        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker=new TimePickerDialog(FabuHDUpdate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time.setText(i+":"+i1);
                    }
                },hour, minute, true);
                timePicker.show();
            }
        });
    }
    public void getData(){
        map=new HashMap();
        arr=new ArrayList<>();
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/Androidapi/userCenter/mysharetenans_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            map.put(jo.getString("title"),jo.getString("id"));
                            arr.add(jo.getString("title"));
                        }
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,arr);
                    autext.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid", BaseApplication.getApp().getUser().getId());
        spr.putValue("type","1");
        spr.putValue("p","1");
        spr.putValue("t","100");
        SlingleVolleyRequestQueue.getInstance(FabuHDUpdate.this).addToRequestQueue(spr);
    }
    public void dopost(){
        Button send=(Button)findViewById(R.id.fabupd004_bt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autext.getText().equals("")){
                    Toast.makeText(getBaseContext(),"请添加餐厅",Toast.LENGTH_SHORT).show();
                }else {
                    if(title.getText().equals("")){
                        Toast.makeText(getBaseContext(),"请添加活动标题",Toast.LENGTH_SHORT).show();
                    }else {
                        if(content.getText().equals("")||content.getText().length()<10){
                            Toast.makeText(getBaseContext(),"请添加活动内容，内容请多与10个字",Toast.LENGTH_SHORT).show();
                        }else {
                            if(data.getText().equals("")||time.equals("")){
                                Toast.makeText(getBaseContext(),"请添加失效时间",Toast.LENGTH_SHORT).show();
                            }else {
                                admit();
                            }
                        }
                    }
                }
            }
        });
    }

    public void admit(){
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/Androidapi/addapi/edit_restaurantPackages", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("==============d",s);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        spr.putValue("id",fbinfo.getId());
        spr.putValue("res_id",map.get(autext.getText().toString()).toString());
        spr.putValue("title",title.getText().toString());
        spr.putValue("detail",content.getText().toString());
        spr.putValue("day",data.getText().toString()+" "+time.getText().toString());
        SlingleVolleyRequestQueue.getInstance(FabuHDUpdate.this).addToRequestQueue(spr);
    }

    public void delPost(){
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/Androidapi/userCenter/del_tenans", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.getApp().getUser().getId());
        spr.putValue("mess_id",fbinfo.getId());
        spr.putValue("type","3");
        SlingleVolleyRequestQueue.getInstance(FabuHDUpdate.this).addToRequestQueue(spr);
    }
}
