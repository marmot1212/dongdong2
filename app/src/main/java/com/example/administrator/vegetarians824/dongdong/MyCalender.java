package com.example.administrator.vegetarians824.dongdong;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.fragment.Nongli;
import com.example.administrator.vegetarians824.fragment.SubjectFragment;
import com.example.administrator.vegetarians824.fragment.Zangli;
import com.example.administrator.vegetarians824.myinterface.SetTime;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyCalender extends AppCompatActivity{
    TextView nong,zang;
    FrameLayout fram;
    FragmentManager fm;
    FragmentTransaction ft;
    FrameLayout cal_fram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calender);
        StatusBarUtil.setColorDiff(this,0xff00aff0);

        cal_fram=(FrameLayout)findViewById(R.id.cal_fram);
        initop();
        initView();

    }
    public void initView(){
        nong=(TextView)findViewById(R.id.calendar_nong);
        zang=(TextView)findViewById(R.id.calendar_zang);
        fm=getSupportFragmentManager();
        ft = fm.beginTransaction();
        Nongli nl=new Nongli();
        ft.replace(R.id.calendar_frag,nl);
        ft.commit();

        nong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusBarUtil.setColorDiff(MyCalender.this,0xff00aff0);
                cal_fram.setBackgroundColor(0xff00aff0);
                nong.setTextColor(0xff00aff0);
                zang.setTextColor(0xffffffff);
                nong.setBackgroundColor(0xffffffff);
                zang.setBackgroundResource(R.drawable.button_bg);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                Nongli nl=new Nongli();
                ft.replace(R.id.calendar_frag,nl);
                ft.commit();
            }
        });
        zang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatusBarUtil.setColorDiff(MyCalender.this,0xffff0000);
                cal_fram.setBackgroundColor(0xffff0000);
                nong.setTextColor(0xffffffff);
                zang.setTextColor(0xffff0000);
                zang.setBackgroundColor(0xffffffff);
                nong.setBackgroundResource(R.drawable.button_bg_red);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                Zangli zl=new Zangli();
                ft.replace(R.id.calendar_frag,zl);
                ft.commit();
            }
        });
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.calender_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
