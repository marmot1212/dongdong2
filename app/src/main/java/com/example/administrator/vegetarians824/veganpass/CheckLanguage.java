package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.SharePreferencesUtils;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CheckLanguage extends AppCompatActivity {
    private static final String TAG = "CheckLanguage";
    @Bind(R.id.lead_bt1)
    TextView mBtn1;
    @Bind(R.id.lead_bt2)
    TextView mBtn2;
    @Bind(R.id.mainlayout)
    FrameLayout mFrame;


    //    private TextView bt1, bt2;
    private SharedPreferences pre;
    public static CheckLanguage instance;
//    private FrameLayout mainlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_language);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucent(this);
        instance = this;
        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        //第一次登陆
        if (pre.getBoolean("isfirst", true)) {
            mFrame.setVisibility(View.VISIBLE);
            String uid = "0" + CreatePhoneID.getRandomString(27);
            SharedPreferences.Editor editor = pre.edit();
            editor.putString("phoneid", uid);
            editor.apply();//提交数据

            SharePreferencesUtils.getInstance().setPhoneId(uid);
            Log.e(TAG, "自定义SharePreferenceUtils工具类， uid = "+uid);


        } else {
            MFGT.gotoPassportHome(this);
            finish();
        }
    }


    /**
     * 按钮"中文""英文"的单击响应事件
     * checked按钮为选中状态，unChecked为未选中状态
     * @param checked       单击的按钮（实际上是一个TestView）
     * @param unChecked     未单击的按钮
     * @param languageType  语言信息，保存到SharePreference：en英语，cn中文
     */
    private void setListener(final TextView checked, final TextView unChecked, final String languageType) {
        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked.setTextColor(0xffffffff);
                checked.setBackgroundResource(R.drawable.button_bg02);
                unChecked.setTextColor(0xff333333);
                unChecked.setBackgroundResource(R.drawable.button_bg01);

                // 保存到SharePreference
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("languagetype", languageType);
                editor.apply();

                SharePreferencesUtils.getInstance().setLanguageType(languageType);
                MFGT.gotoPreferencePage(CheckLanguage.this);
            }
        });
    }

    @OnClick({R.id.lead_bt1, R.id.lead_bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lead_bt1:
                setListener(mBtn1, mBtn2, "en");
                break;
            case R.id.lead_bt2:
                setListener(mBtn2, mBtn1, "cn");
                break;
        }
    }
}
