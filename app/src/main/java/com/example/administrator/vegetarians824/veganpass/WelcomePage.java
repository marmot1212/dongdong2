package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;


public class WelcomePage extends AppCompatActivity {
    private Button bt1,bt2,bt3,btnext;
    private FrameLayout layout,mypage;
    private String type="";
    private SharedPreferences pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        StatusBarUtil.setTranslucent(this);
        pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        //第一次登陆
        if(pre.getBoolean("isfirst",true)){
            String id="0"+CreatePhoneID.getRandomString(27);
            SharedPreferences.Editor editor=pre.edit();
            editor.putString("phoneid",id);
            editor.apply();//提交数据
        }
        initView();
        initoperate();

    }

    public void initView(){
        bt1=(Button)findViewById(R.id.welcome_bt1);
        bt2=(Button)findViewById(R.id.welcome_bt2);
        bt3=(Button)findViewById(R.id.welcome_bt3);
        btnext=(Button)findViewById(R.id.welcome_btnext);
        layout=(FrameLayout)findViewById(R.id.welcome_select);
        mypage=(FrameLayout)findViewById(R.id.welcome_mypage);
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("type",type);
                editor.apply();//提交数据
                Intent intent=new Intent(getBaseContext(),VPHomePage.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //加载引导页
    public void initoperate(){

        if(pre.getString("type","").equals("")){
            layout.setVisibility(View.VISIBLE);
        }else {
            Intent intent=new Intent(getBaseContext(),VPHomePage.class);
            startActivity(intent);
            finish();
        }

    }
    //button点击事件
    public void click(View view){
        clearbutton(bt1,bt2,bt3);
        Drawable drawable=getResources().getDrawable(R.drawable.select);
        if(drawable!=null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        ((Button)view).setCompoundDrawables(null,null, drawable,null);
        type=((Button)view).getText().toString();
        btnext.setVisibility(View.VISIBLE);
    }
    //初始化所有button
    public void clearbutton(View...views){
        for(View view:views){
            ((Button)view).setCompoundDrawables(null,null,null,null);
        }
    }

}
