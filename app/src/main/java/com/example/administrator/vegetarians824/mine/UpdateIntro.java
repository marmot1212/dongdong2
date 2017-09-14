package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateIntro extends AppCompatActivity {
    String intro,id;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_intro);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initoperate();
        updataIntro();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit3_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void updataIntro(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        intro=intent.getStringExtra("intro");
        et=(EditText)findViewById(R.id.edit3_edit);

        if(!intro.equals("null"))
            et.setText(intro);

        Button bt=(Button)findViewById(R.id.edit3_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intro=et.getText().toString();
                doPost();


            }
        });
    }
    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/editintro", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    String code=js.getString("Code");
                    if(code.equals("1")){
                        Toast.makeText(getBaseContext(),js.getString("Message"),Toast.LENGTH_SHORT).show();
                        BaseApplication.app.getUser().setIntro(intro);
                        SharedPreferences preferences=UpdateIntro.this.getSharedPreferences("shared",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("intro",intro);
                        editor.commit();//提交数据
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
        spr.putValue("uid",id);
        spr.putValue("intro",intro);
        SlingleVolleyRequestQueue.getInstance(UpdateIntro.this).addToRequestQueue(spr);
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
