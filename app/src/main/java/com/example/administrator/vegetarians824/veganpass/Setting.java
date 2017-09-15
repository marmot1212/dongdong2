package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;


public class Setting extends AppCompatActivity {
    private String en_cn;
    private FrameLayout fanhui;
    private TextView tv1,tv2;
    private SharedPreferences pre;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TextView bartitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setColorDiff(this,0x8e000000);
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.setting_fram,new FragmentPreference());
        ft.commit();
        initView();
    }
    public void initView(){
        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        en_cn=pre.getString("languagetype","");
        fanhui=(FrameLayout) findViewById(R.id.setting_fanhui);
        bartitle=(TextView)findViewById(R.id.setting_bartitle);
        if(en_cn.equals("en")){
            bartitle.setText("Preference");
        }else {
            bartitle.setText("偏好");
        }
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv1=(TextView)findViewById(R.id.setting_tv1);
        tv2=(TextView)findViewById(R.id.setting_tv2);
        if(en_cn.equals("cn")){
            tv1.setText("偏好设置");
            tv2.setText("其他设置");
        }else {
            tv1.setText("Preference");
            tv2.setText("Setting");
        }
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setBackgroundColor(0xffc0c0c0);
                tv1.setTextColor(0xffffffff);
                tv2.setBackgroundColor(0xffffffff);
                tv2.setTextColor(0xff8e8e8e);
                ft=fm.beginTransaction();
                ft.replace(R.id.setting_fram,new FragmentPreference());
                ft.commit();
                if(en_cn.equals("en")){
                    bartitle.setText("Preference");
                }else {
                    bartitle.setText("偏好");
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setBackgroundColor(0xffc0c0c0);
                tv2.setTextColor(0xffffffff);
                tv1.setBackgroundColor(0xffffffff);
                tv1.setTextColor(0xff8e8e8e);
                ft=fm.beginTransaction();
                ft.replace(R.id.setting_fram,new FragmentSetting());
                ft.commit();
                if(en_cn.equals("en")){
                    bartitle.setText("Setting");
                }else {
                    bartitle.setText("设置");
                }
            }
        });
    }
}
