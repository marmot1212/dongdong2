package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateName extends AppCompatActivity {
    String name,id;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initoperate();
        updataName();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit2_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void updataName(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
        et=(EditText)findViewById(R.id.edit2_edit);
        et.setHint(name);

        Button bt=(Button)findViewById(R.id.edit2_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=et.getText().toString();
                doPost();
            }
        });
    }

    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/update_username", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    String code=js.getString("Code");
                    if(code.equals("1")){
                        Toast.makeText(getBaseContext(),js.getString("Message"),Toast.LENGTH_SHORT).show();
                        BaseApplication.app.getUser().setName(name);
                        SharedPreferences preferences=UpdateName.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("username",name);
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
        spr.putValue("username",name);
        SlingleVolleyRequestQueue.getInstance(UpdateName.this).addToRequestQueue(spr);
    }
}
