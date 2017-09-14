package com.example.administrator.vegetarians824.fankui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.FanKui;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Fankui extends AppCompatActivity {
    String content;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fankui);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        et=(EditText)findViewById(R.id.fankui_edit);
        initop();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fankui_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button fankui=(Button)findViewById(R.id.fankui_bt);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    if(!et.getText().toString().equals("")){
                        content=et.getText().toString();
                        dopost();
                    }
                }
                else {
                    Intent intent=new Intent(Fankui.this, Login.class);
                    Fankui.this.startActivity(intent);
                }
            }
        });
    }
    public void dopost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.FankuiPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getBaseContext(),"提交成功，我们会尽快处理！",Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("content",content);
        SlingleVolleyRequestQueue.getInstance(Fankui.this).addToRequestQueue(spr);
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
