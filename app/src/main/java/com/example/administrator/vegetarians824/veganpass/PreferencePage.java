package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;


public class PreferencePage extends AppCompatActivity {
    private Button btnext,btpre;
    private TextView  bt1,bt2,bt3;
    private ImageView tip1,tip2,tip3;
    private TextView title;
    private String type="";
    private SharedPreferences pre;
    private String languagetype;
    private PopupWindow popWindow;
    private TextView des1,des2,des3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_page);
        StatusBarUtil.setTranslucent(this);

        pre=getSharedPreferences("data", Context.MODE_PRIVATE);
        //第一次登陆
        if(pre.getBoolean("isfirst",true)){
            String id="0"+CreatePhoneID.getRandomString(27);
            SharedPreferences.Editor editor=pre.edit();
            editor.putString("phoneid",id);
            editor.apply();//提交数据
        }
        languagetype=pre.getString("languagetype","");
        initView();

    }

    public void initView(){
        bt1=(TextView) findViewById(R.id.welcome_bt1);
        bt2=(TextView) findViewById(R.id.welcome_bt2);
        bt3=(TextView) findViewById(R.id.welcome_bt3);
        tip1=(ImageView)findViewById(R.id.welcome_tip1);
        tip2=(ImageView)findViewById(R.id.welcome_tip2);
        tip3=(ImageView)findViewById(R.id.welcome_tip3);
        btnext=(Button)findViewById(R.id.welcome_btnext);
        btpre=(Button)findViewById(R.id.welcome_btpre);
        title=(TextView)findViewById(R.id.welcome_title);
        des1=(TextView)findViewById(R.id.setting_des1);
        des2=(TextView)findViewById(R.id.setting_des2);
        des3=(TextView)findViewById(R.id.setting_des3);
        if(languagetype.equals("cn")){
            title.setText("请选择您的偏好");
            bt1.setText("纯素");
            bt2.setText("净素");
            bt3.setText("蛋奶素");
            btnext.setText("下一步");
            btpre.setText("上一步");
            des1.setText("我是一名纯素者，我不吃任何肉类、蛋奶、海鲜、蜂蜜以及任何动物制品。");
            des2.setText("我是一名素食者，我不吃任何肉类、蛋奶、海鲜和任何动物制品，同时也不吃葱、蒜、洋葱、韭菜。");
            des3.setText("我是一名素食者，我不吃任何肉类、海鲜等，但我吃鸡蛋、奶类、蜂蜜。");
        }else {
            title.setText("Choose your diet");
            bt1.setText("Vegan");
            bt2.setText("Vuddhist Vegan");
            bt3.setText("Ovo-Lacto Vegetarian");
            btnext.setText("Next");
            btpre.setText("Previous");
            des1.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee.");
            des2.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee. Including egg, diary product and bee. Especially, I don’t eat food containing Chinese onions, garlic, Chinese chives, rocambole, and onions.");
            des3.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, not including eggs and bee.");
        }
        btpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("type",type);
                editor.apply();//提交数据
                // 跳转PassportHome界面
                MFGT.gotoPassportHome(PreferencePage.this);
                finish();
                CheckLanguage.instance.finish();
            }
        });
        tip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPopWindow(1);
                //popWindow.showAsDropDown(tip1);
            }
        });
        tip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(2);
                //popWindow.showAsDropDown(tip2);
            }
        });
        tip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(3);
                //popWindow.showAsDropDown(tip3);
            }
        });

    }

    //button点击事件
    public void click(View view){
        clearbutton(bt1,bt2,bt3);
        ((TextView)view).setTextColor(0xffffffff);
        ((TextView)view).setBackgroundResource(R.drawable.button_bg02);
        type=((TextView)view).getText().toString();
        btnext.setVisibility(View.VISIBLE);
        switch (view.getId()){
            case R.id.welcome_bt1:des1.setVisibility(View.VISIBLE);break;
            case R.id.welcome_bt2:des2.setVisibility(View.VISIBLE);break;
            case R.id.welcome_bt3:des3.setVisibility(View.VISIBLE);break;
        }
    }

    //初始化所有button
    public void clearbutton(View...views){
        for(View view:views){
            ((TextView)view).setBackgroundResource(R.drawable.button_bg01);
            ((TextView)view).setTextColor(0xff333333);
        }
        des1.setVisibility(View.GONE);
        des2.setVisibility(View.GONE);
        des3.setVisibility(View.GONE);
    }


    public void initPopWindow(int x) {
        View popView = getLayoutInflater().inflate(R.layout.pop_tips, null);
        TextView tip=(TextView) popView.findViewById(R.id.tip_text);
        if(languagetype.equals("cn")){
            switch (x){
                case 1:tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，包括鸡蛋，奶制品和蜂蜜。");break;
                case 2:tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，包括鸡蛋，奶制品和蜂蜜，特别的，我也不吃含葱、大蒜、韭菜、小蒜、洋葱。");break;
                case 3:tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，不包括鸡蛋，奶制品和蜂蜜。");break;
                default:break;
            }
        }else {
            switch (x){
                case 1:tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee.");break;
                case 2:tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee. Including egg, diary product and bee. Especially, I don’t eat food containing Chinese onions, garlic, Chinese chives, rocambole, and onions.");break;
                case 3:tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, not including eggs and bee.");break;
                default:break;
            }
        }
        popView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popWindow.isShowing()) {
                    popWindow.dismiss();
                }
                return false;
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.anim.alpha);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
    }


}
