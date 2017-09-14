package com.example.administrator.vegetarians824.fabu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.fragment.Nongli;
import com.example.administrator.vegetarians824.mannager.URLMannager;
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
import java.util.Map;


public class FabuHD extends AppCompatActivity {
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
        setContentView(R.layout.activity_fabu_hd);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initop();
        initview();
        getData();
        dopost();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu03_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        autext=(AutoCompleteTextView)findViewById(R.id.fabu004_et1);
        autext.setThreshold(1);
        title=(EditText) findViewById(R.id.fabu004_et2);
        content=(EditText) findViewById(R.id.fabu004_et3);
        line1=(LinearLayout)findViewById(R.id.fabu004_date1);
        line2=(LinearLayout)findViewById(R.id.fabu004_time1);
        data=(TextView)findViewById(R.id.fabu004_tv1);
        time=(TextView)findViewById(R.id.fabu004_tv2);
        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(FabuHD.this, new DatePickerDialog.OnDateSetListener() {
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
                TimePickerDialog timePicker=new TimePickerDialog(FabuHD.this, new TimePickerDialog.OnTimeSetListener() {
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
        StringPostRequest spr=new StringPostRequest(URLMannager.Mysharetenans, new Response.Listener<String>() {
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
        SlingleVolleyRequestQueue.getInstance(FabuHD.this).addToRequestQueue(spr);
    }
    public void dopost(){
        Button send=(Button)findViewById(R.id.fabu004_bt);
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
        StringPostRequest spr=new StringPostRequest(URLMannager.AddrestaurantPackages, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("res_id",map.get(autext.getText().toString()).toString());
        spr.putValue("title",title.getText().toString());
        spr.putValue("detail",content.getText().toString());
        spr.putValue("day",data.getText().toString()+" "+time.getText().toString());
        SlingleVolleyRequestQueue.getInstance(FabuHD.this).addToRequestQueue(spr);
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
