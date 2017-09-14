package com.example.administrator.vegetarians824.veganpass;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;


public class EditFavour extends AppCompatActivity {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TextView tv1,tv2,tv3;
    private FrameLayout fanhui;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_favour);
        StatusBarUtil.setColorDiff(this,0x8e000000);
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.editfavour_content,new MyPreference());
        ft.commit();
        initView();
    }
    public void initView(){
        fanhui=(FrameLayout)findViewById(R.id.editfavour_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv1=(TextView)findViewById(R.id.editfavour_tv1);
        tv2=(TextView)findViewById(R.id.editfavour_tv2);
        tv3=(TextView)findViewById(R.id.editfavour_tv3);
        //偏好
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv1.setBackgroundResource(R.drawable.button_bg01);
                tv1.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyPreference());
                ft.commit();
            }
        });
        //忌口
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv2.setBackgroundResource(R.drawable.button_bg01);
                tv2.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyAvoid());
                ft.commit();
            }
        });
        //过敏
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv3.setBackgroundResource(R.drawable.button_bg01);
                tv3.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyAllergy());
                ft.commit();
            }
        });

    }
    public void initTextView(){
        tv1.setBackground(null);
        tv1.setTextColor(0xffffffff);
        tv2.setBackground(null);
        tv2.setTextColor(0xffffffff);
        tv3.setBackground(null);
        tv3.setTextColor(0xffffffff);
    }
}
