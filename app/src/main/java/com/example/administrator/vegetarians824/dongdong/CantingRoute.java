package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class CantingRoute extends AppCompatActivity {

    private MapView mMapView;
    private AMap aMap;
    private LatLonPoint end,start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting_route);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        mMapView = (MapView) findViewById(R.id.canting_route);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();


        Intent intent=getIntent();
        Double longitude=intent.getDoubleExtra("longitude",0);
        Double latitude=intent.getDoubleExtra("latitude",0);
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
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }



}
