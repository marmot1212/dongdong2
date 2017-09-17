package com.example.administrator.vegetarians824.homePage.toolbox.nutridata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.MFGT;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by scorpion on 2017/9/17.
 */

public class NutriData extends AppCompatActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_data);
        ButterKnife.bind(this);
//        StatusBarUtil.setColorDiff(this,0xff00aff0);
    }

    @OnClick({R.id.backArea, R.id.btn_nutuiData})
    public void initListener(View v) {
        switch (v.getId()) {
            case R.id.backArea:
                MFGT.finish(NutriData.this);
                break;
            case R.id.btn_nutuiData:
                MFGT.gotoNutriQuery(this);
                break;
        }
    }

    @OnClick({R.id.protein, R.id.carbohydrate, R.id.shanShiXianWei, R.id.zhiLei, R.id.weiShengSu, R.id.kuangWuZhi, R.id.water})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.protein:
                MFGT.gotoNutriDataDetails(this, "what", "蛋白质");
                break;
            case R.id.carbohydrate:
                MFGT.gotoNutriDataDetails(this, "what", "碳水化合物");
                break;
            case R.id.shanShiXianWei:
                MFGT.gotoNutriDataDetails(this, "what", "膳食纤维");
                break;
            case R.id.zhiLei:
                MFGT.gotoNutriDataDetails(this, "what", "脂类");
                break;
            case R.id.weiShengSu:
                MFGT.gotoNutriDataDetails(this, "what", "维生素");
                break;
            case R.id.kuangWuZhi:
                MFGT.gotoNutriDataDetails(this, "what", "矿物质");
                break;
            case R.id.water:
                MFGT.gotoNutriDataDetails(this, "what", "水");
                break;
        }
    }
}
