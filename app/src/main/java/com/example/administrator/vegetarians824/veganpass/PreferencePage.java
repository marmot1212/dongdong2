package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.SharePreferencesUtils;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PreferencePage extends AppCompatActivity {
    private static final String TAG = "PreferencePage";
    /**
     * mTitle 标题，需要设置cn或者en
     * mBtn1, mBtn2, mBtn3, 3个素食身份的按钮分别是纯素、净素、蛋奶素
     * mDescription1, mDescription2, mDescription3 素食护照的内容
     * mTip1, mTip2, mTip3 单击可查看三个素食身份的解释，然后加载相应的布局——暂时去掉
     */


    @Bind(R.id.welcome_title)
    TextView mTitle;
    @Bind(R.id.welcome_bt1)
    TextView mBtn1;
    @Bind(R.id.welcome_tip1)
    ImageView mTip1;
    @Bind(R.id.setting_des1)
    TextView mDescription1;
    @Bind(R.id.welcome_bt2)
    TextView mBtn2;
    @Bind(R.id.welcome_tip2)
    ImageView mTip2;
    @Bind(R.id.setting_des2)
    TextView mDescription2;
    @Bind(R.id.welcome_bt3)
    TextView mBtn3;
    @Bind(R.id.welcome_tip3)
    ImageView mTip3;
    @Bind(R.id.setting_des3)
    TextView mDescription3;
    @Bind(R.id.welcome_btnext)
    Button m_btn_next;
    @Bind(R.id.welcome_btpre)
    Button m_btn_pre;

    private String type = "";
    private SharedPreferences pre;
    private String stringLanguageType;

    // 解释说明文字的下拉菜单
//    private PopupWindow popWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_page);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this);

        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        //第一次登陆
        if (pre.getBoolean("isfirst", true)) {
            String id = "0" + CreatePhoneID.getRandomString(27);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("phoneid", id);
            editor.apply();//提交数据
            // TODO phoneid的作用
            SharePreferencesUtils.getInstance().setPhoneId(id);

        }
        stringLanguageType = pre.getString("languagetype", "");
//        initView();
        initLanguageType();

    }

    public void initView() {

        m_btn_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        m_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        /**
         * 素食身份解释说明按钮的单击事件，已注释相关代码
         */
//        tip1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //initPopWindow(1);
//                //popWindow.showAsDropDown(tip1);
//            }
//        });
//        tip2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                initPopWindow(2);
//                //popWindow.showAsDropDown(tip2);
//            }
//        });
//        tip3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                initPopWindow(3);
//                //popWindow.showAsDropDown(tip3);
//            }
//        });

    }

    /**
     * 根据stringLanguageType设置语言类型
     */
    private void initLanguageType() {
        if (stringLanguageType.equals("cn")) {
            mTitle.setText("请选择您的偏好");
            mBtn1.setText("纯素");
            mBtn2.setText("净素");
            mBtn3.setText("蛋奶素");
            m_btn_next.setText("下一步");
            m_btn_pre.setText("上一步");
            mDescription1.setText("我是一名纯素者，我不吃任何肉类、蛋奶、海鲜、蜂蜜以及任何动物制品。");
            mDescription2.setText("我是一名素食者，我不吃任何肉类、蛋奶、海鲜和任何动物制品，同时也不吃葱、蒜、洋葱、韭菜。");
            mDescription3.setText("我是一名素食者，我不吃任何肉类、海鲜等，但我吃鸡蛋、奶类、蜂蜜。");
        } else {
            mTitle.setText("Choose your diet");
            mBtn1.setText("Vegan");
            mBtn2.setText("Vuddhist Vegan");
            mBtn3.setText("Ovo-Lacto Vegetarian");
            m_btn_next.setText("Next");
            m_btn_pre.setText("Previous");
            mDescription1.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee.");
            mDescription2.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee. Including egg, diary product and bee. Especially, I don’t eat food containing Chinese onions, garlic, Chinese chives, rocambole, and onions.");
            mDescription3.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, not including eggs and bee.");
        }
    }

    //button点击事件
    public void click(View view) {
        clearbutton(mBtn1, mBtn2, mBtn3);
        // 设置触发单击事件TextView的字体颜色 和控件背景
        ((TextView) view).setTextColor(0xffffffff);
        ((TextView) view).setBackgroundResource(R.drawable.button_bg02);
        type = ((TextView) view).getText().toString(); // 获得单击TextView的文字

        // 显示"下一步"按钮
        m_btn_next.setVisibility(View.VISIBLE);

        // 根据触发事件的按钮，显示相应的素食身份护照信息：mDescription1, mDescription2, mDescription3
        switch (view.getId()) {
            case R.id.welcome_bt1:
                mDescription1.setVisibility(View.VISIBLE);
                break;
            case R.id.welcome_bt2:
                mDescription2.setVisibility(View.VISIBLE);
                break;
            case R.id.welcome_bt3:
                mDescription3.setVisibility(View.VISIBLE);
                break;
        }
    }

    //初始化所有button
    public void clearbutton(View... views) {
        for (View view : views) {
            ((TextView) view).setBackgroundResource(R.drawable.button_bg01);
            ((TextView) view).setTextColor(0xff333333);
        }
        mDescription1.setVisibility(View.GONE);
        mDescription2.setVisibility(View.GONE);
        mDescription3.setVisibility(View.GONE);
    }


//    public void initPopWindow(int x) {
//        View popView = getLayoutInflater().inflate(R.layout.pop_tips, null);
//        TextView tip = (TextView) popView.findViewById(R.id.tip_text);
//        if (stringLanguageType.equals("cn")) {
//            switch (x) {
//                case 1:
//                    tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，包括鸡蛋，奶制品和蜂蜜。");
//                    break;
//                case 2:
//                    tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，包括鸡蛋，奶制品和蜂蜜，特别的，我也不吃含葱、大蒜、韭菜、小蒜、洋葱。");
//                    break;
//                case 3:
//                    tip.setText("我不吃鱼，海鲜，家禽，肉类或任何动物制品，不包括鸡蛋，奶制品和蜂蜜。");
//                    break;
//                default:
//                    break;
//            }
//        } else {
//            switch (x) {
//                case 1:
//                    tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee.");
//                    break;
//                case 2:
//                    tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee. Including egg, diary product and bee. Especially, I don’t eat food containing Chinese onions, garlic, Chinese chives, rocambole, and onions.");
//                    break;
//                case 3:
//                    tip.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, not including eggs and bee.");
//                    break;
//                default:
//                    break;
//            }
//        }
//        popView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (popWindow.isShowing()) {
//                    popWindow.dismiss();
//                }
//                return false;
//            }
//        });
//        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        popWindow.setAnimationStyle(R.anim.alpha);
//        popWindow.setFocusable(true);
//        popWindow.setOutsideTouchable(true);
//        popWindow.setBackgroundDrawable(new ColorDrawable());
//    }


    @OnClick({R.id.welcome_btnext, R.id.welcome_btpre})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.welcome_btnext:
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("type", type);
                editor.apply();//提交数据

                SharePreferencesUtils.getInstance().setVegetarianType(type);
                // 跳转PassportHome界面, 同时finish() 以前的Activity
                MFGT.gotoPassportHome(PreferencePage.this);
                MFGT.finish(this);
                MFGT.finish(CheckLanguage.instance);
//                finish();
//                CheckLanguage.instance.finish();
                break;
            case R.id.welcome_btpre:
                MFGT.finish(PreferencePage.this);
                break;
        }
    }
}
