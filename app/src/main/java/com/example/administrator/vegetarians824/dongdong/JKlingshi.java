package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.example.administrator.vegetarians824.CaptureActivity;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.SubjectFragmentAdapter;
import com.example.administrator.vegetarians824.fabu.FabuCP;
import com.example.administrator.vegetarians824.fragment.LingshiFragment;
import com.example.administrator.vegetarians824.fragment.SubjectFragment;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mine.Aerweima;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JKlingshi extends AppCompatActivity {
    private PagerSlidingTabStrip pagerSliding;
    private ViewPager viewPager;
    private List<String> list_title;
    private List<Fragment>  list_fragemrnt;
    private SubjectFragmentAdapter adapter;
    private final static int SCANNIN_GREQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jklingshi);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        pagerSliding = (PagerSlidingTabStrip) findViewById(R.id.lingshi_pst);
        viewPager = (ViewPager) findViewById(R.id.lingshi_vp);
        initop();
        initVP();
        pagerSliding.setViewPager(viewPager);
        pagerSliding.setTextSize(28);
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.lingshi_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView ewm=(ImageView)findViewById(R.id.erweima);
        ewm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JKlingshi.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                JKlingshi.this.startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        ImageView fabu=(ImageView)findViewById(R.id.add_6);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    Intent intent=new Intent(JKlingshi.this, FabuCP.class);
                    JKlingshi.this.startActivity(intent);
                }else {
                    Intent intent=new Intent(JKlingshi.this, Login.class);
                    JKlingshi.this.startActivity(intent);
                }
            }
        });
    }
    public void initVP(){
        String dp[] = {"素食零食","素食食材"};
        list_title = new ArrayList<>();
        list_title = Arrays.asList(dp);
        list_fragemrnt = new ArrayList<>();
        //零食
        Fragment fragment = new LingshiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type","2");
        fragment.setArguments(bundle);
        list_fragemrnt.add(fragment);
        //食材
        Fragment fragment2 = new LingshiFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type","1");
        fragment2.setArguments(bundle2);
        list_fragemrnt.add(fragment2);

        adapter = new SubjectFragmentAdapter(getSupportFragmentManager(),list_fragemrnt,list_title);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
                    String r=bundle.getString("result");
                    Intent intent=new Intent(JKlingshi.this,Aerweima.class);
                    intent.putExtra("result",r);
                    JKlingshi.this.startActivity(intent);
                }
                break;
        }
    }
}
