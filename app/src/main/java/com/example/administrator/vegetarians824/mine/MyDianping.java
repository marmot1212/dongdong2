package com.example.administrator.vegetarians824.mine;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.SubjectFragmentAdapter;
import com.example.administrator.vegetarians824.fragment.SubjectFragment;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyDianping extends AppCompatActivity {
    private PagerSlidingTabStrip pagerSliding;
    private ViewPager viewPager;
    private List<String> list_title;
    private List<Fragment>  list_fragemrnt;
    private SubjectFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dianping);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        pagerSliding = (PagerSlidingTabStrip) findViewById(R.id.pst02);
        viewPager = (ViewPager) findViewById(R.id.my_dp_vp);
        initop();
        initVP();
        pagerSliding.setViewPager(viewPager);
        pagerSliding.setTextSize(28);
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.my_dp_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initVP(){
        String dp[] = {"餐馆","酒店","帖子","菜谱"};
        list_title = new ArrayList<>();
        list_title = Arrays.asList(dp);
        list_fragemrnt = new ArrayList<>();
        for (int i = 0;i <list_title.size();i++){
            Fragment fragment = new SubjectFragment();
            Bundle bundle = new Bundle();
            bundle.putString("switch",list_title.get(i));
            bundle.putString("which","my_commentlist");
            fragment.setArguments(bundle);
            list_fragemrnt.add(fragment);
        }
        adapter = new SubjectFragmentAdapter(getSupportFragmentManager(),list_fragemrnt,list_title);
        viewPager.setAdapter(adapter);
    }
}
