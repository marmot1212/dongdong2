package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

public class CantingJiucuo extends AppCompatActivity {
    private String id,name,content;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting_jiucuo);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        name=intent.getStringExtra("name");
       initView();
    }
    public void initView(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.jiucuo_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView nametv=(TextView)findViewById(R.id.jiucuo_name);
        nametv.setText(name);
        et=(EditText) findViewById(R.id.jiucuo_edit);
        Button bt=(Button)findViewById(R.id.jiucuo_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content=et.getText().toString();
                if(BaseApplication.app.getUser().islogin()){
                    if(!et.getText().toString().equals("")){
                        content=et.getText().toString();
                        dopost();
                    }
                }
                else {
                    Intent intent=new Intent(CantingJiucuo.this, Login.class);
                    CantingJiucuo.this.startActivity(intent);
                }
            }
        });
    }
    public void dopost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.RestaurantError, new Response.Listener<String>() {
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
        spr.putValue("res_id",id);
        SlingleVolleyRequestQueue.getInstance(CantingJiucuo.this).addToRequestQueue(spr);
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
