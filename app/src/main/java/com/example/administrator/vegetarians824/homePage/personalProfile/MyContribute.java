package com.example.administrator.vegetarians824.homePage.personalProfile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyContribute extends AppCompatActivity {

    @Bind(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @Bind(R.id.contribute_toApp)
    LinearLayout mContributeToApp;
    @Bind(R.id.contribute_toTeam)
    LinearLayout mContributeToTeam;
    @Bind(R.id.contribute_toVegetarianism)
    LinearLayout mContributeToVegetarianism;

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
                break;
        }
    }

    @OnClick({R.id.contribute_toApp, R.id.contribute_toTeam, R.id.contribute_toVegetarianism})
    public void onPayChecked(View v) {
        initPayButton();
        switch (v.getId()) {
            case R.id.contribute_toApp:
                mContributeToApp.setBackgroundResource(R.drawable.contribute_item_checked);
                break;
            case R.id.contribute_toTeam:
                mContributeToTeam.setBackgroundResource(R.drawable.contribute_item_checked);
                break;
            case R.id.contribute_toVegetarianism:
                mContributeToVegetarianism.setBackgroundResource(R.drawable.contribute_item_checked);
                break;
        }
    }

    private void initPayButton() {
        mContributeToApp.setBackgroundResource(R.drawable.contribute_item_normal);
        mContributeToTeam.setBackgroundResource(R.drawable.contribute_item_normal);
        mContributeToVegetarianism.setBackgroundResource(R.drawable.contribute_item_normal);
    }

}
