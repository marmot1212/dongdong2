package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.SharePrefrenceUtils;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;


public class CheckLanguage extends AppCompatActivity {
    private static final String TAG = "CheckLanguage";
    private TextView bt1,bt2;
    private SharedPreferences pre;
    public static CheckLanguage instance;
    private FrameLayout mainlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_language);
        StatusBarUtil.setTranslucent(this);
        instance=this;
        mainlayout=(FrameLayout) findViewById(R.id.mainlayout);
        pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        //第一次登陆
        if(pre.getBoolean("isfirst",true)){
            mainlayout.setVisibility(View.VISIBLE);
            String id="0"+ CreatePhoneID.getRandomString(27);
            SharedPreferences.Editor editor=pre.edit();
            editor.putString("phoneid",id);
            editor.apply();//提交数据
        }else {
            // TODO 跳转界面
            MFGT.gotoPassportHome(this);
            finish();
        }

        initView();
    }
    public void initView(){
        bt1=(TextView)findViewById(R.id.lead_bt1);
        bt2=(TextView)findViewById(R.id.lead_bt2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setTextColor(0xffffffff);
                bt1.setBackgroundResource(R.drawable.button_bg02);
                bt2.setBackgroundResource(R.drawable.button_bg01);
                bt2.setTextColor(0xff333333);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("languagetype","en");
                editor.apply();//提交数据
                startActivity(new Intent(getBaseContext(),PreferencePage.class));
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt2.setTextColor(0xffffffff);
                bt2.setBackgroundResource(R.drawable.button_bg02);
                bt1.setBackgroundResource(R.drawable.button_bg01);
                bt1.setTextColor(0xff333333);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("languagetype","cn");
                editor.apply();//提交数据
                startActivity(new Intent(getBaseContext(),PreferencePage.class));
            }
        });
    }
}
