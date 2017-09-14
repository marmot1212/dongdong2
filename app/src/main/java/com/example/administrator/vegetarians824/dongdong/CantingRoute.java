package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CantingRoute extends AppCompatActivity {

    private MapView mMapView;
    private AMap aMap;
    private LatLonPoint end,start;
    private LinearLayout daohang;
    private Double longitude,latitude;
    private PopupWindow popWindow;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting_route);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        mMapView = (MapView) findViewById(R.id.canting_route);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        daohang=(LinearLayout)findViewById(R.id.daohang);
        daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startNaviGao();
                getPopwindow();
            }
        });
        Intent intent=getIntent();
        longitude=intent.getDoubleExtra("longitude",0);
        latitude=intent.getDoubleExtra("latitude",0);
        name=intent.getStringExtra("name");
        end=new LatLonPoint(latitude,longitude);
        Double startlongitude=Double.valueOf(BaseApplication.app.getMyLociation().getLongitude());
        Double startlatitude=Double.valueOf(BaseApplication.app.getMyLociation().getLatitude());
        start=new LatLonPoint(startlatitude,startlongitude);
        makeRoute(start,end);
    }

    public void makeRoute(LatLonPoint mStartPoint, LatLonPoint mEndPoint){
        RouteSearch mRouteSearch=new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult result, int i) {
                aMap.clear();
                final DrivePath drivePath = result.getPaths().get(0);
                DrivingRouteOverlay drivingRouteOverlay=new DrivingRouteOverlay(getBaseContext(),aMap, drivePath,
                        result.getStartPos(),
                        result.getTargetPos());
                drivingRouteOverlay.setNodeIconVisibility(false);
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }
        });
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo,  RouteSearch.DrivingDefault, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
        StatService.onPause(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }


    //验证各种导航地图是否安装
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public void getPopwindow(){
        View popView = getLayoutInflater().inflate(R.layout.pop_route,null);
        TextView baidu=(TextView)popView.findViewById(R.id.map_baidu);
        TextView gaode=(TextView)popView.findViewById(R.id.map_gaode);
        TextView google=(TextView)popView.findViewById(R.id.map_google);
        baidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNaviBaiddu();
            }
        });
        gaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNaviGao();
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNaviGoogle();
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 获取光标
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(daohang, Gravity.BOTTOM, 0, 0);

    }

    public void startNaviGao() {
        if (isAvilible(getBaseContext(), "com.autonavi.minimap")) {
            try {
                //sourceApplication
                Intent intent = Intent.getIntent("androidamap://navi?sourceApplication= &poiname= &lat=" + latitude + "&lon=" + longitude + "&dev=0");
                startActivity(intent);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), "您尚未安装高德地图或地图版本过低", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNaviBaiddu() {
        if (isAvilible(getBaseContext(), "com.baidu.BaiduMap")) {
            try {
                Intent intent = Intent.getIntent("intent://map/direction?" +
                        "destination=name:"+name+        //终点
                        "&mode=driving&" +          //导航路线方式
                        "region= " +           //
                        "&src= #Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent); //启动调用
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getBaseContext(), "您尚未安装百度地图或地图版本过低", Toast.LENGTH_SHORT).show();
        }
    }

    public void startNaviGoogle() {
        if (isAvilible(getBaseContext(), "com.google.android.apps.maps")) {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else {
            Toast.makeText(getBaseContext(), "您尚未安装谷歌地图或地图版本过低", Toast.LENGTH_SHORT).show();
        }
    }

}
