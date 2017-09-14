package com.example.administrator.vegetarians824;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.adapter.SubjectFragmentAdapter;
import com.example.administrator.vegetarians824.fragment.SellerFragment;
import com.example.administrator.vegetarians824.fragment.SubjectFragment;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
* 商户系统
* */
public class SellerCenter extends AppCompatActivity {
    private PagerSlidingTabStrip pagerSliding;
    private ViewPager viewPager;
    private List<String> list_title;
    private List<Fragment>  list_fragemrnt;
    private SubjectFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_center);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        pagerSliding = (PagerSlidingTabStrip) findViewById(R.id.update_pst);
        viewPager = (ViewPager) findViewById(R.id.update_vp);
        initop();
        initVP();
        pagerSliding.setViewPager(viewPager);
        pagerSliding.setTextSize(28);
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.update_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //往PagerSlidingTabStrip里扔fragment
    public void initVP(){
        String dp[] = {"店铺维护","菜谱维护","活动维护"};
        list_title = new ArrayList<>();
        list_title = Arrays.asList(dp);
        list_fragemrnt = new ArrayList<>();
        for (int i = 0;i <list_title.size();i++){
            Fragment fragment = new SellerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("what",i+"");
            fragment.setArguments(bundle);
            list_fragemrnt.add(fragment);
        }
        adapter = new SubjectFragmentAdapter(getSupportFragmentManager(),list_fragemrnt,list_title);
        viewPager.setAdapter(adapter);
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
