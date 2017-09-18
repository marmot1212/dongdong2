package com.example.administrator.vegetarians824.homePage.personalProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyContribute extends AppCompatActivity {
    private static final String TAG = "MyContribute";
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_tip1)
    TextView mTvTip1;
    @Bind(R.id.tv_tip2)
    TextView mTvTip2;
    @Bind(R.id.tv_tip3)
    TextView mTvTip3;
    @Bind(R.id.tv_tpye)
    TextView mTvTpye;
    @Bind(R.id.ev_num)
    EditText mEvNum;
    @Bind(R.id.tv_moneyOrPerson)
    TextView mTvMoneyOrPerson;
    @Bind(R.id.tv_countResult)
    TextView mTvCountResult;
    @Bind(R.id.lLayout_count)
    LinearLayout mLLayoutCount;

    @Bind(R.id.fLayout_contribute)
    FrameLayout mFLayoutContribute;
    @Bind(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @Bind(R.id.contribute_toApp)
    LinearLayout mContributeToApp;
    @Bind(R.id.contribute_toTeam)
    LinearLayout mContributeToTeam;
    @Bind(R.id.contribute_toVegetarianism)
    LinearLayout mContributeToVegetarianism;


    private int index = 1;
    private int mMoney1, mMoney2, mMoney3;
    private boolean isMoney; // 打赏APP、团队为true，推广素食为false
    private String mUnit;
    private int mNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contribute);
        ButterKnife.bind(this);
        // 修改状态栏的背景颜色
        StatusBarUtil.setColorDiff(this, 0xff00aff0);

        initView();


    }

    private void initView() {
        mTvHeaderTitle.setText("我的捐助");
    }

    @OnClick({R.id.backArea, R.id.btn_contribute_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArea:
                MFGT.finish(this);
                break;
            case R.id.btn_contribute_confirm:
                initDataForFrame2();
                mFLayoutContribute.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick({R.id.contribute_toApp, R.id.contribute_toTeam, R.id.contribute_toVegetarianism})
    public void onPayChecked(View v) {
        initButtonOfContributeType();
        switch (v.getId()) {
            case R.id.contribute_toApp:
                mContributeToApp.setBackgroundResource(R.drawable.contribute_long_item_checked);
                index = 1;
                break;
            case R.id.contribute_toTeam:
                mContributeToTeam.setBackgroundResource(R.drawable.contribute_long_item_checked);
                index = 2;
                break;
            case R.id.contribute_toVegetarianism:
                mContributeToVegetarianism.setBackgroundResource(R.drawable.contribute_long_item_checked);
                index = 3;
                break;
        }
    }

    private void initButtonOfContributeType() {
        mContributeToApp.setBackgroundResource(R.drawable.contribute_long_item_normal);
        mContributeToTeam.setBackgroundResource(R.drawable.contribute_long_item_normal);
        mContributeToVegetarianism.setBackgroundResource(R.drawable.contribute_long_item_normal);
    }

    /**
     * 下面是确认捐款金额部分的代码
     */

    private void initDataForFrame2() {
        switch (index) {
            case 1:
                mMoney1 = 3;
                mMoney2 = 7;
                mMoney3 = 21;
                isMoney = true;
                break;
            case 2:
                mMoney1 = 10;
                mMoney2 = 50;
                mMoney3 = 100;
                isMoney = true;
                break;
            case 3:
                mMoney1 = 1;
                mMoney2 = 7;
                mMoney3 = 21;
                isMoney = false;
                break;
        }
        initFrame2(isMoney);
    }

    private void initFrame2(boolean isMoney) {
        if (isMoney) {
            // 初始化标题文字
            if (index == 1) {
                mTvTitle.setText("打赏APP");
            } else {
                mTvTitle.setText("赞助团队");
            }
            mUnit = "元";
            mTvTpye.setText("其他金额");
            mTvMoneyOrPerson.setText(mUnit);
            mEvNum.setHint("请输入赞助金额");
        } else {
            mTvTitle.setText("推广素食");
            mUnit = "人";
            mTvTpye.setText("其他人数");
            mTvMoneyOrPerson.setText(mUnit);
            mEvNum.setHint("请输入赞助人数");
            mLLayoutCount.setVisibility(View.VISIBLE);
        }
        mTvTip1.setText(mMoney1 + mUnit);
        mTvTip2.setText(mMoney2 + mUnit);
        mTvTip3.setText(mMoney3 + mUnit);
        // 推广素食 请客 赞助总计
        mTvCountResult.setText("20 元");
    }

    @OnClick({R.id.btn_pay_confirm, R.id.iv_close})
    public void onViewClicked2(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                initViewAndeDataForPay();
                break;
            case R.id.btn_pay_confirm:
                if (mNumber == 0) {
                    Toast.makeText(this, "请选择支付金额", Toast.LENGTH_SHORT).show();
                    break;
                }
                Toast.makeText(this, "即将捐助"+mNumber+"元，多谢！", Toast.LENGTH_LONG).show();
                Log.e(TAG, "固定选项的捐助金额 mNumber = "+ mNumber);
                Log.e(TAG, "固定选项的捐助金额 mNumber = "+ mNumber);
                Log.e(TAG, "固定选项的捐助金额 mNumber = "+ mNumber);
                initViewAndeDataForPay();
                break;
        }

    }

    @OnClick({R.id.tv_tip1, R.id.tv_tip2, R.id.tv_tip3})
    public void onPayClicked(View view) {
        initViewAndeDataForPay();
        switch (view.getId()) {
            case R.id.tv_tip1:
                if (isMoney) {
                    mNumber = mMoney1;
                } else {
                    mNumber = mMoney1*20;
                }
                mTvTip1.setBackgroundResource(R.drawable.contribute_long_item_checked);
                break;
            case R.id.tv_tip2:
                if (isMoney) {
                    mNumber = mMoney2;
                } else {
                    mNumber = mMoney2*20;
                }
                mTvTip2.setBackgroundResource(R.drawable.contribute_long_item_checked);
                break;
            case R.id.tv_tip3:
                if (isMoney) {
                    mNumber = mMoney3;
                } else {
                    mNumber = mMoney3*20;
                }
                mTvTip3.setBackgroundResource(R.drawable.contribute_long_item_checked);
                break;
        }
    }

    private void initViewAndeDataForPay() {
        mTvTip1.setBackgroundResource(R.drawable.menu_item_normal);
        mTvTip2.setBackgroundResource(R.drawable.menu_item_normal);
        mTvTip3.setBackgroundResource(R.drawable.menu_item_normal);

        mFLayoutContribute.setVisibility(View.GONE);
        mLLayoutCount.setVisibility(View.GONE);

        mNumber = 0;
    }
}
