package com.example.administrator.vegetarians824.fabu;

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
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

public class FabuCP extends AppCompatActivity {
    EditText et1,et2;
    String title,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_cp);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        et1=(EditText)findViewById(R.id.fabu02_et1) ;
        et2=(EditText)findViewById(R.id.fabu02_et2) ;
        initop();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu02_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button bt=(Button)findViewById(R.id.fabu02_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et1.getText().toString();
                link=et2.getText().toString();
                if((title.length()<2))
                {
                    Toast.makeText(getBaseContext(),"产品名称至少输入2个字",Toast.LENGTH_SHORT).show();
                }else {
                    if(link.equals("")){
                        Toast.makeText(getBaseContext(),"请输入产品链接",Toast.LENGTH_SHORT).show();
                    }else {
                        doPost();
                    }
                }
            }
        });
    }
    public void doPost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.FabuCPPost, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
;               Toast.makeText(getBaseContext(),"发布成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("title", title);
        spr.putValue("link",link);
        SlingleVolleyRequestQueue.getInstance(FabuCP.this).addToRequestQueue(spr);
    }
}
