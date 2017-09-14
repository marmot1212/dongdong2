package com.example.administrator.vegetarians824.fabu;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.fragment.MapFragment;
import com.example.administrator.vegetarians824.fragment.SuFragment;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class FabuShHd extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    TextView sh,hd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_sh_hd);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu00_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }
    public void initView(){
        sh=(TextView)findViewById(R.id.fabu_sh);
        hd=(TextView)findViewById(R.id.fabu_hd);
        fm=getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fabu_frag,new FragmentShanghu());
        ft.commit();
        sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sh.setTextColor(0xff00aff0);
                sh.setBackgroundColor(0xffffffff);
                hd.setTextColor(0xffffffff);
                hd.setBackgroundResource(R.drawable.button_bg);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fabu_frag,new FragmentShanghu());
                ft.commit();
            }
        });
        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hd.setTextColor(0xff00aff0);
                hd.setBackgroundColor(0xffffffff);
                sh.setTextColor(0xffffffff);
                sh.setBackgroundResource(R.drawable.button_bg);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fabu_frag,new FragmentHuodong());
                ft.commit();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
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
