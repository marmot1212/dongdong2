package com.example.administrator.vegetarians824;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chen.wakehao.easybottomtab.EasyBottomTab;
import com.example.administrator.vegetarians824.dongdong.JKsearch;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.fabu.FabuFD;
import com.example.administrator.vegetarians824.fabu.FabuShangHu;
import com.example.administrator.vegetarians824.homePage.FourthFragment;
import com.example.administrator.vegetarians824.homePage.OneFragment;
import com.example.administrator.vegetarians824.homePage.SecondFragment;
import com.example.administrator.vegetarians824.homePage.ThirdFragment;
import com.example.administrator.vegetarians824.homePage.ToolBoxFragment;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.myView.CyclePager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.search.SearchRestaurant;
import com.example.administrator.vegetarians824.util.CheckPermission;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.wlf.filedownloader.FileDownloader;

public class HomePage extends AppCompatActivity {
    private EasyBottomTab easyBottomTab;
    private LinearLayout top;
    private LinearLayout searchline;
    private EditText et;
    private TextView tv;
    private FrameLayout add;
    private ImageView map;
    public AMapLocationClient mLocationClient=null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        easyBottomTab = (EasyBottomTab) findViewById(R.id.ezTab);
        top=(LinearLayout)findViewById(R.id.home_topline);

        searchline=(LinearLayout)findViewById(R.id.home_searchline);
        et=(EditText) findViewById(R.id.home_et);
        tv=(TextView) findViewById(R.id.home_tv);
        et.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        map=(ImageView)findViewById(R.id.home_map);
        map.setVisibility(View.VISIBLE);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MapActivity.class));
            }
        });
        searchline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), SearchRestaurant.class);
                startActivity(intent);
            }
        });
        add=(FrameLayout)findViewById(R.id.home_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                    startActivity(new Intent(getBaseContext(), FabuShangHu.class));
                }else {
                    startActivity(new Intent(getBaseContext(),Login.class));
                }
            }
        });

        easyBottomTab.setBackgroundColor(0xffffffff);
        easyBottomTab
                .addItem(R.mipmap.dibu_res1,R.mipmap.dibu_res2,"素食铺",new OneFragment())
                .addItem(R.mipmap.dibu_food1,R.mipmap.dibu_food2,"素菜谱",new SecondFragment())
                //点击不变色变换图案
                .addItem(R.mipmap.dibu_more1,R.mipmap.dibu_more2,"工具箱",new ToolBoxFragment())
                .addItem(R.mipmap.dibu_more1,R.mipmap.dibu_more2,"工具箱",new ThirdFragment())
                .addItem(R.mipmap.dibu_user1,R.mipmap.dibu_user2,"我的",new FourthFragment())
                //三种模式 该模式点击隐藏字体
                .setChangeMode(EasyBottomTab.ChangeMode.MATERIAL)
               //.setChangeMode(EasyBottomTab.ChangeMode.MATERIAL)
                .setSelectedColor(0xffa0a0a0)
               //.setUnSelectedColor(Color.YELLOW)
                //初始化完毕后调用
                .finishInit();
        easyBottomTab.autoInitFragment(this,R.id.ezFragment,savedInstanceState);
        easyBottomTab.addSwitchListener(new EasyBottomTab.SwitchListener() {
            @Override
            public boolean isBeforeSwitch(int selectedPosition) {
                //返回值false表示直接切换页面 不做判断
                switch (selectedPosition){
                    case 0:
                        //cyclePager.init("1");
                        et.setVisibility(View.GONE);
                        tv.setVisibility(View.VISIBLE);
                        searchline.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                                    startActivity(new Intent(getBaseContext(), FabuShangHu.class));
                                }else {
                                    startActivity(new Intent(getBaseContext(),Login.class));
                                }
                            }
                        });
                        searchline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getBaseContext(), SearchRestaurant.class);
                                startActivity(intent);
                            }
                        });
                        top.setVisibility(View.VISIBLE);
                        map.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        //cyclePager.init("2");
                        et.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.GONE);
                        searchline.setVisibility(View.VISIBLE);
                        searchline.setClickable(false);
                        add.setVisibility(View.VISIBLE);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                                    startActivity(new Intent(getBaseContext(), FabuFD.class));
                                }else {
                                    startActivity(new Intent(getBaseContext(),Login.class));
                                }
                            }
                        });


                        top.setVisibility(View.VISIBLE);
                        map.setVisibility(View.GONE);
                        break;
                    case 2:
                        //cyclePager.init("4");
                        searchline.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        top.setVisibility(View.VISIBLE);
                        map.setVisibility(View.GONE);
                        break;
                    case 3:
                        if(BaseApplication.app.getUser()==null||!BaseApplication.app.getUser().islogin()) {
                            Intent intent=new Intent(HomePage.this, Login.class);
                            intent.putExtra("code",13);
                            startActivityForResult(intent,13);
                            return true;
                        }else {
                            top.setVisibility(View.GONE);
                        }
                        map.setVisibility(View.GONE);
                        break;
                }

                return false;
            }
        });
        getGps();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK)return;

        //登陆完成回调该方法
        easyBottomTab.beforeVerifiedOk(3);
        top.setVisibility(View.GONE);
    }

    public void getGps(){
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        BaseApplication.app.setMyLociation(new MyLociation(String
                                .valueOf(amapLocation.getLongitude()), String
                                .valueOf(amapLocation.getLatitude()), String
                                .valueOf(amapLocation.getCity()),String.valueOf(amapLocation.getAddress()))
                        );

                    }
                }
            }
        });

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        mLocationOption.setOnceLocation(true);

        if(CheckPermission.requestLocaltionPermission(HomePage.this)){
            //启动定位
            mLocationClient.startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 198) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.startLocation();
            } else {
                Toast.makeText(getBaseContext(), "请手动获取定位权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // pause all downloads
         FileDownloader.pauseAll();
        // unregisterDownloadStatusListener
        //FileDownloader.unregisterDownloadStatusListener(mDownloadFileListAdapter);
    }
}
