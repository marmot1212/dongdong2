package com.example.administrator.vegetarians824.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CantingListAdapter;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.CitySelectScrollView;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.MapSearch;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.LatLng_jw;
import com.example.administrator.vegetarians824.fabu.FabuShHd;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements LocationSource,AMapLocationListener,AMap.OnMarkerClickListener,AMap.OnCameraChangeListener,AMap.OnMapLoadedListener {
    private AMap aMap;
    private MapView mapView;
    public static AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption=null;
    boolean isFirstLoc = true;// 设置一个标记,定位方法中使用
    public static LocationSource.OnLocationChangedListener mListener;
    public LatLng_jw latLng_jw;// 自定义对象，接受经纬度数据
    public List<CantingInfo> list_cantingInfo,list_ct,list_jd,list_hd;
    public static LatLng latLng;// 高德地图的坐标对象
    private MarkerOptions markerOption;// 给地图添加标记（Marker）
    private PopupWindow popupWindow;// 弹窗
    private ListView listView;//底部list显示餐厅信息
    private boolean listIsShow=false;
    private TextView city;
    private ImageView find,dingwei;
    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;
    private TextView inputs;
    private LinearLayout line;
    public Map map_search;
    private android.support.v7.app.AlertDialog.Builder dialogbuilder;
    CantingListAdapter adapter;
    private LinearLayout bottomline;
    private LinearLayout rank1,rank2,choose;
    private String ranktype;
    private TextView liechujieguo;
    private ImageView rankima;
    private ImageView rankima0;
    private boolean isup;
    private static Marker amarker=null;
    private String where="";
    private PopupWindow popWindow;
    private int type;
    private View activityRootView;
    private FrameLayout msg;
    FrameLayout msgline;
    ImageView msgima;
    LinearLayout msgclose;
    Boolean ismsgshow;
    TextView msgtext;
    private int windowheight;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_map, container, false);
        line=(LinearLayout) v.findViewById(R.id.ditu_find_lin_liebiao);
        mapView = (MapView) v.findViewById(R.id.mishimap);
        listView=(ListView) v.findViewById(R.id.candan_listview);
        city=(TextView) v.findViewById(R.id.mishi_city);
        inputs=(TextView)v.findViewById(R.id.ditu_edit);
        find=(ImageView)v.findViewById(R.id.mishfind);
        dingwei=(ImageView)v.findViewById(R.id.mishidingwei);
        bottomline=(LinearLayout)v.findViewById(R.id.mishi_bottom);
        rank1=(LinearLayout)v.findViewById(R.id.mishi_rank1);
        rank2=(LinearLayout)v.findViewById(R.id.mishi_rank2);
        choose=(LinearLayout)v.findViewById(R.id.mishi_choose);
        liechujieguo=(TextView)v.findViewById(R.id.ditu_find_tv_liebiao);
        rankima=(ImageView)v.findViewById(R.id.mishi_rankima);
        rankima0=(ImageView)v.findViewById(R.id.mishi_rankima0);
        activityRootView = v.findViewById(R.id.rells);
        msg=(FrameLayout)v.findViewById(R.id.mishimsg);

        msgline=(FrameLayout)v.findViewById(R.id.msg_line);
        msgima=(ImageView)v.findViewById(R.id.msg_ima);
        msgclose=(LinearLayout)v.findViewById(R.id.msg_close);
        msgtext=(TextView)v.findViewById(R.id.msg_text);
        //适配方法，弹出高度为屏幕1/3
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        windowheight=metric.heightPixels/2;
        //确保fragment得到activity,放置切换过快闪退
        if(getActivity()!=null){
            initdata();
            mapView.onCreate(savedInstanceState);// 此方法必须重写
            setMap();
            initOperate();
        }
        return v;
    }
    //初始化数据
    public void initdata(){
        map_search=new HashMap();
        list_cantingInfo=new ArrayList<>();
        list_ct=new ArrayList<>();
        list_jd=new ArrayList<>();
        list_hd=new ArrayList<>();
        ranktype="distance";
        isup=true;
        ismsgshow=true;
        type=0;
    }

    //设置地图
    public void setMap(){
        aMap = mapView.getMap();
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapLoadedListener(this);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置比例尺隐藏
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        //地图点击事件，listview消失，marker未选中状态
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击map退菜单
                if(listIsShow)
                {
                    ViewGroup.LayoutParams params=bottomline.getLayoutParams();
                    params.height=0;
                    bottomline.setLayoutParams(params);
                    bottomline.requestLayout();
                    liechujieguo.setHeight(40);
                    listIsShow=false;
                }
                //点击map marker初始化
                if(amarker!=null){
                    if(where.equals("1")){
                        amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker2));
                    }else if(where.equals("6")){
                        amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker1));
                    }else if(where.equals("2")){
                        amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker4));
                    }
                }

                //inputs.clearFocus();
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               // imm.hideSoftInputFromWindow(mapView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });
    }
    //绑定地图生命周期
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        StatService.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        StatService.onPause(this);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }
    //启动定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener=onLocationChangedListener;
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getContext());
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            if (mLocationOption.isOnceLocationLatest()) {
                mLocationOption.setOnceLocationLatest(true);
                // 设置setOnceLocationLatest(boolean
                // b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
                // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
            }
            mLocationOption.setWifiActiveScan(true);
            mLocationOption.setInterval(3000);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }
    //定位结束
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
    //定位监听
    @Override
    public void onLocationChanged(final AMapLocation amapLocation) {
        String current=null;
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();// 获取纬度
                amapLocation.getLongitude();// 获取经度
                amapLocation.getAccuracy();// 获取精度信息
                amapLocation.getAddress();//
                // 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();// 国家信息
                amapLocation.getProvince();// 省信息
                amapLocation.getCity();// 城市信息
                current = amapLocation.getCity(); // 保存当前城市
                Log.d("======country",current);
                amapLocation.getDistrict();// 城区信息
                amapLocation.getStreet();// 街道信息
                amapLocation.getStreetNum();// 街道门牌号信息
                amapLocation.getCityCode();// 城市编码
                amapLocation.getAdCode();// 地区编码
                amapLocation.getAoiName();// 获取当前定位点的AOI信息
                // 设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            } else {
                if(amapLocation != null && amapLocation.getErrorCode() == 12){
                    Toast.makeText(getContext(),"请手动获取定位权限",Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }else {
                    Toast.makeText(getContext(),"无定位权限",Toast.LENGTH_SHORT).show();
                }
            }
        }
        latLng_jw = new LatLng_jw();// 接受初始化定位的经纬度数据，再传给http方法
        latLng_jw.setLongitude(String.valueOf(amapLocation.getLongitude()));
        latLng_jw.setLatitude(String.valueOf(amapLocation.getLatitude()));
        dingwei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(BaseApplication.app.getMyLociation()!=null&&city.getText().equals(BaseApplication.app.getMyLociation().getMyCity())){
                    //在改城市范围，直接切换回定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude())));
                }else {
                    //不在改城市
                    dialogbuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    dialogbuilder.setMessage("是否切换回定位所在城市？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                // TODO 确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String mcity;
                                    if(BaseApplication.app.getMyLociation().getMyCity()!=null) {
                                        mcity = BaseApplication.app.getMyLociation().getMyCity();

                                    city.setText(mcity);
                                    if(mcity.equals("北京市")||mcity.equals("上海市")||mcity.equals("香港")||mcity.equals("台湾")||mcity.equals("广州市")||mcity.equals("深圳市"))
                                    {
                                        find.setImageResource(R.drawable.ditu_userfind);
                                    }
                                    else{
                                        find.setImageResource(R.drawable.ditu_userfindall);
                                    }
                                    CityQiehuan(mcity,mcity);
                                    aMap.clear();
                                    DingWeiMarker();
                                    line.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        // TODO 取消按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialogbuilder.show();
                }
            }
        });
        city.setText(current);
        if(current.equals("北京市")||current.equals("上海市")||current.equals("香港")||current.equals("台湾")||current.equals("广州市")||current.equals("深圳市"))
        {
            find.setImageResource(R.drawable.ditu_userfind);
            HttpRequest(1,latLng_jw,null,"","");
        }
        else{
            find.setImageResource(R.drawable.ditu_userfindall);
            //HttpRequest(2, latLng_jw, null, city.getText().toString(), null);
        }
        ranktype="distance";


        rank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankima0.setVisibility(View.VISIBLE);
                rankima.setVisibility(View.INVISIBLE);
                ranktype="distance";
                aMap.clear();
                HttpRequest(1,latLng_jw,null,"","");
            }
        });
        isup=true;
        rank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankima.setVisibility(View.VISIBLE);
                rankima0.setVisibility(View.INVISIBLE);
                ranktype="unit_price";
                if(isup){
                    rankima.setImageResource(R.drawable.barrow_2);
                    isup=false;
                    aMap.clear();
                    HttpRequest(1,latLng_jw,null,"","");
                }else {
                    rankima.setImageResource(R.drawable.barrow);
                    isup=true;
                    aMap.clear();
                    HttpRequest(1,latLng_jw,null,"","");
                }
            }
        });
    }
    //marker点击
    @Override
    public boolean onMarkerClick(Marker marker) {

        //aMap.getMapScreenMarkers();
        if(listIsShow)
        {
            ViewGroup.LayoutParams params=bottomline.getLayoutParams();
            params.height=0;
            bottomline.setLayoutParams(params);
            bottomline.requestLayout();
            liechujieguo.setHeight(40);
            listIsShow=false;

        }
        /*
        for (int i = 1; i < aMap.getMapScreenMarkers().size(); i++) {
                    aMap.getMapScreenMarkers()
                    .get(i)
                    .setIcon(
                            BitmapDescriptorFactory
                                    .fromResource(R.drawable.sumarker2));
        }*/
        //还原上一个marker
        if(amarker!=null){
            if(where.equals("1")){
                amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker2));
            }else if(where.equals("6")){
                amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker1));
            }else if(where.equals("2")){
                amarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker4));
            }
            //荤餐厅
            if(where.equals("00")){
                amarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_yezi2));
            }
        }

        //把要修改的marker传给静态marker
        if(marker.getTitle()!=null) {
            amarker = marker;
            int x = Integer.valueOf(marker.getTitle().trim());
            where=list_cantingInfo.get(x).getType();
            //荤餐厅
            if(list_cantingInfo.get(x).getVege_status().equals("2")){
                where="00";
            }
        }else {
            amarker=null;
        }
        // 点击标记后，更换蓝色标记图片，
        if(marker.getTitle()!=null) {
            //marker.hideInfoWindow();
            // Log.d("=========mark",marker.getTitle().trim());
            marker.setIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.sumarker3));

        }

        // 将地图移动到定位点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(marker
                .getPosition().latitude, marker.getPosition().longitude)));

        int i = Integer.valueOf(marker.getTitle().trim());// 拿到自定义的标记
        LinearLayout popLinearLayout=(LinearLayout)getView().findViewById(R.id.ditu_find_lin_liebiao);
        View view=LayoutInflater.from(getContext()).inflate(R.layout.canting_item_pop,null);
        listView.setSelection(i);

        //荤餐厅
        if(marker.getTitle()!=null) {
            if(list_cantingInfo.get(i).getVege_status().equals("2")) {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_yezi));
            }
        }

        //显示详情
        TextView name=(TextView) view.findViewById(R.id.canting_item_namep);
        name.setText(list_cantingInfo.get(i).getTitle());
        //荤餐厅
        if(list_cantingInfo.get(i).getVege_status().equals("2")){
            name.setTextColor(0xffff5e5e);
        }
        TextView jiage=(TextView)view.findViewById(R.id.canting_item_jiagep);
        jiage.setText("¥"+list_cantingInfo.get(i).getUnit_pric());
        TextView juli=(TextView) view.findViewById(R.id.canting_item_julip);
        juli.setText("距您"+list_cantingInfo.get(i).getDistance()+"米");
        TextView content=(TextView)view.findViewById(R.id.canting_item_neirongp);
        content.setText(list_cantingInfo.get(i).getContent());
        //荤餐厅
        if(list_cantingInfo.get(i).getVege_status().equals("2")){
            content.setText("已加入素食餐厅友好餐厅计划（点击发现素食）");
            content.setTextColor(0xff51b30c);
        }
        ImageView imageView=(ImageView) view.findViewById(R.id.canting_item_imageViewp);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getContext());
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+list_cantingInfo.get(i).getImg_url_th_1(),imageView,options);
        ImageView logo=(ImageView) view.findViewById(R.id.canting_item_logop);
        if(list_cantingInfo.get(i).getType().equals("1")){
            logo.setImageResource(R.mipmap.maplistdini);
        }else if(list_cantingInfo.get(i).getType().equals("6")){
            logo.setImageResource(R.mipmap.maplisthot);
        }
        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list_cantingInfo.get(x).getType().equals("1")||list_cantingInfo.get(x).getType().equals("6")){
                    Intent intent=new Intent(getContext(), CantingDetail.class);
                    intent.putExtra("item_id",list_cantingInfo.get(x).getId());
                    getActivity().startActivity(intent);
                }else if(list_cantingInfo.get(x).getType().equals("2")){
                    Intent intent=new Intent(getContext(), HuodongDetail.class);
                    intent.putExtra("id",list_cantingInfo.get(x).getId());
                    getActivity().startActivity(intent);
                }
            }
        });
        LinearLayout other=(LinearLayout) view.findViewById(R.id.canting_item_others);
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(listIsShow)
                {
                    //listView.setVisibility(View.INVISIBLE);
                    ViewGroup.LayoutParams params=bottomline.getLayoutParams();
                    params.height=0;
                    bottomline.setLayoutParams(params);
                    bottomline.requestLayout();
                    liechujieguo.setHeight(40);
                    listIsShow=false;
                }
                else
                {
                    // listView.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params=bottomline.getLayoutParams();
                    params.height=windowheight;
                    bottomline.setLayoutParams(params);
                    bottomline.requestLayout();
                    liechujieguo.setHeight(0);
                    listIsShow=true;
                }
            }
        });
        // 点击，弹出层
        popupWindow = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 点击popupWindow以外的区域,自动消失
        popupWindow.setOutsideTouchable(true);

        // 设置弹出窗体,显示位置
        //popupWindow.showAsDropDown(popLinearLayout);
        popupWindow.showAtLocation(mapView, Gravity.BOTTOM,0,0);

        return true;
    }
    //返回结果处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("=============code","aaa "+requestCode);
        if(listIsShow)
        {
            ViewGroup.LayoutParams params=bottomline.getLayoutParams();
            params.height=0;
            bottomline.setLayoutParams(params);
            bottomline.requestLayout();
            liechujieguo.setHeight(40);
            listIsShow=false;
        }
        //城市列表返回数据
        switch (requestCode) {
            case 11:
                String c;
                if(data!=null)
                {
                    c = data.getStringExtra("city");
                    city.setText(c);
                    CityQiehuan(c, c);
                    showRecommendation(c);
                    if(c.equals("北京市")||c.equals("上海市")||c.equals("香港")||c.equals("台湾")||c.equals("广州市")||c.equals("深圳市")||c.equals("香港特别行政区"))
                    {
                        find.setImageResource(R.drawable.ditu_userfind);
                    }
                    else{
                        find.setImageResource(R.drawable.ditu_userfindall);
                    }
                    aMap.clear();
                    DingWeiMarker();
                    line.setVisibility(View.INVISIBLE);

                }
                break;
            //关键字查询返回数据
            case 12:
                if(data!=null) {
                    inputs.setText(data.getStringExtra("keyword"));
                    if ( (!city.getText().toString().equals("城市"))&&(!data.getStringExtra("keyword").equals("")) ) {
                        final String keyword = data.getStringExtra("keyword");
                        aMap.clear();
                        DingWeiMarker();
                        inputs.clearFocus();
                        ranktype = "distance";
                        HttpRequest(4, null, null, city.getText().toString(), keyword);
                        isup = true;
                        rank1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rankima0.setVisibility(View.VISIBLE);
                                rankima.setVisibility(View.INVISIBLE);
                                ranktype = "distance";
                                HttpRequest(4, null, null, city.getText().toString(), keyword);
                            }
                        });
                        rank2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                rankima.setVisibility(View.VISIBLE);
                                rankima0.setVisibility(View.INVISIBLE);
                                ranktype = "unit_price";
                                if (isup) {
                                    rankima.setImageResource(R.drawable.barrow_2);
                                    isup = false;
                                    HttpRequest(4, null, null, city.getText().toString(), keyword);
                                } else {
                                    rankima.setImageResource(R.drawable.barrow);
                                    isup = true;
                                    HttpRequest(4, null, null, city.getText().toString(), keyword);
                                }
                            }
                        });
                    }
                }else {
                    inputs.setText("");
                }
                break;
        }

    }

    //地图位置移动
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        final LatLng target = cameraPosition.target;// 得到中心点坐标
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=0;

                //是否获取gps
                if (BaseApplication.app.getMyLociation()==null) {
                    Toast.makeText(getContext(),"定位失败！",Toast.LENGTH_SHORT).show();
                } else {
                    //获取到gps
                    GeocodeSearch geocoderSearch = new GeocodeSearch(getContext());
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(target.latitude, target.longitude), 200, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                    geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                        @Override
                        public void onRegeocodeSearched(RegeocodeResult result, int i) {

                            if (i==1000) {
                                String acity="";
                                if(result.getRegeocodeAddress().getCity().isEmpty()){
                                    acity=result.getRegeocodeAddress().getProvince();
                                }else {
                                    acity= result.getRegeocodeAddress().getCity();
                                }
                                Log.d("===========city",acity);
                                switch (acity){
                                    case "香港特別行政區": acity="香港特别行政区";break;
                                    case "澳門特別行政區":acity="澳门特别行政区";break;
                                    case "香港特別行政区": acity="香港特别行政区";break;
                                    case "澳門特別行政区":acity="澳门特别行政区";break;
                                    case "台湾省":acity="台湾";break;
                                    default:break;
                                }
                                Log.d("===========city",acity);
                                //是在本市
                                if(acity.equals("")){
                                    Toast.makeText(getContext(),"暂未找到屏幕所在地区",Toast.LENGTH_SHORT).show();
                                }else {
                                    if (acity.equals(city.getText())) {
                                        if (acity.equals("北京市") || acity.equals("上海市") || acity.equals("香港") || acity.equals("台湾")|| acity.equals("广州市")|| acity.equals("深圳市")|| acity.equals("香港特别行政区")) {
                                            final LatLng_jw my = new LatLng_jw();
                                            my.setLatitude(BaseApplication.app.getMyLociation().getLatitude());
                                            my.setLongitude(BaseApplication.app.getMyLociation().getLongitude());
                                            final LatLng_jw center = new LatLng_jw();
                                            center.setLatitude(target.latitude + "");
                                            center.setLongitude(target.longitude + "");
                                            aMap.clear();
                                            //type=0;
                                            ranktype="distance";
                                            HttpRequest(3, my, center, "", "");
                                            isup=true;
                                            rank1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    rankima0.setVisibility(View.VISIBLE);
                                                    rankima.setVisibility(View.INVISIBLE);
                                                    ranktype="distance";
                                                    aMap.clear();
                                                    HttpRequest(3, my, center, "", "");
                                                }
                                            });
                                            rank2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    rankima.setVisibility(View.VISIBLE);
                                                    rankima0.setVisibility(View.INVISIBLE);
                                                    ranktype="unit_price";
                                                    if(isup) {
                                                        rankima.setImageResource(R.drawable.barrow_2);
                                                        isup=false;
                                                        aMap.clear();
                                                        HttpRequest(3, my, center, "", "");
                                                    }else {
                                                        rankima.setImageResource(R.drawable.barrow);
                                                        isup=true;
                                                        aMap.clear();
                                                        HttpRequest(3, my, center, "", "");
                                                    }
                                                }
                                            });
                                        } else {
                                            final LatLng_jw my2 = new LatLng_jw();
                                            my2.setLatitude(BaseApplication.app.getMyLociation().getLatitude());
                                            my2.setLongitude(BaseApplication.app.getMyLociation().getLongitude());
                                            aMap.clear();
                                            //type=0;
                                            ranktype="distance";
                                            HttpRequest(2, my2, null, city.getText().toString(), null);
                                            isup=true;
                                            rank1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    rankima0.setVisibility(View.VISIBLE);
                                                    rankima.setVisibility(View.INVISIBLE);
                                                    ranktype="distance";
                                                    aMap.clear();
                                                    HttpRequest(2, my2, null, city.getText().toString(), null);
                                                }
                                            });
                                            rank2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    rankima.setVisibility(View.VISIBLE);
                                                    rankima0.setVisibility(View.INVISIBLE);
                                                    ranktype="unit_price";
                                                    if(isup){
                                                        rankima.setImageResource(R.drawable.barrow_2);
                                                        isup=false;
                                                        aMap.clear();
                                                        HttpRequest(2, my2, null, city.getText().toString(), null);
                                                    }else {
                                                        rankima.setImageResource(R.drawable.barrow);
                                                        isup=true;
                                                        aMap.clear();
                                                        HttpRequest(2, my2, null, city.getText().toString(), null);
                                                    }
                                                }
                                            });
                                        }

                                    } else {
                                        getDialog(acity);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onGeocodeSearched(GeocodeResult result, int i) {

                        }
                    });

                }


            }
        });


    }
    //移动结束
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }
    //地图加载结束
    @Override
    public void onMapLoaded() {

    }
    //参数：请求类型 当前经纬 屏幕中心经纬 城市 关键字
    public void HttpRequest(final int type,final LatLng_jw myjw,final LatLng_jw centerjw,final String mcity,final String keyword){
        amarker=null;
        String url="";
        //请求类型
        // 1.定位按经纬查找    2.按城市显示城市所有餐厅
        switch (type){
            //首次进入数据
            case 1:url=URLMannager.Mishi_Canting+"longitude/"
                    + myjw.getLongitude()
                    + "/latitude/"
                    + myjw.getLatitude();
                break;
            //全部
            case 2:StringBuffer sb=new StringBuffer();
                try {
                    sb.append(URLMannager.Mishi_CityCanting).append(URLEncoder.encode(mcity,"utf-8")).append("/longitude/").append(myjw.getLongitude()).append("/latitude/").append(myjw.getLatitude());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url=sb.toString();
                break;
            //在这找
            case 3:url=URLMannager.Mishi_Canting+"longitude/"+centerjw.getLongitude()+"/latitude/"+centerjw.getLatitude()
                    +"/longitudes/"+myjw.getLongitude()+"/latitudes/"+myjw.getLatitude();
                break;
            //关键字搜索
            case 4:StringBuffer sb2=new StringBuffer();
                try {
                    sb2.append(URLMannager.Mishi_Canting).append("keyword/").append(URLEncoder.encode(keyword,"utf-8")).append("/city/").append(URLEncoder.encode(mcity,"utf-8")).append("/longitude/").append(BaseApplication.app.getMyLociation().getLongitude()).append("/latitude/").append(BaseApplication.app.getMyLociation().getLatitude());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url=sb2.toString();

                break;
        }
        url=url+"/order/"+ranktype;

        Log.d("======url",url);
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                jsonArrays(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }); SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);

    }
    //json解析
    public void jsonArrays(String s){
        try {
            Log.d("===============ss",s);
            builder= new LatLngBounds.Builder();
            list_cantingInfo=new ArrayList<>();
            list_jd=new ArrayList<>();
            list_ct=new ArrayList<>();
            list_hd=new ArrayList<>();
            JSONObject jsonObj1 = new JSONObject(s);
            JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
            JSONArray array1 = jsonObj2.getJSONArray("list");
            int y=0;
            for (int i = 0; i < array1.length(); i++) {
                JSONObject array1_2 = array1.getJSONObject(i);
                CantingInfo cantingInfo = new CantingInfo();
                cantingInfo.id = array1_2.getString("id");
                cantingInfo.title = array1_2.getString("title");
                if(array1_2.getString("tel")!=null)
                    cantingInfo.tel = array1_2.getString("tel");
                cantingInfo.content = array1_2.getString("content");
                cantingInfo.unit_pric = array1_2.getString("unit_price");
                cantingInfo.longitude = array1_2.getString("longitude");
                cantingInfo.latitude = array1_2.getString("latitude");
                if(array1_2.getString("img_url_1")!=null)
                    cantingInfo.img_url_1 = array1_2.getString("img_url_1");
                cantingInfo.type = array1_2.getString("type");
                cantingInfo.create_time = array1_2.getString("create_time");
                cantingInfo.uid = array1_2.getString("uid");
                cantingInfo.address = array1_2.getString("address");
                if(array1_2.getString("img_url_th_1")!=null)
                    cantingInfo.img_url_th_1 = array1_2.getString("img_url_th_1");
                if(array1_2.getString("user_head_img")!=null)
                    cantingInfo.user_head_img = array1_2.getString("user_head_img");
                if(array1_2.getString("user_head_img_th")!=null)
                    cantingInfo.user_head_img_th = array1_2.getString("user_head_img_th");
                cantingInfo.username = array1_2.getString("username");
                cantingInfo.distance = array1_2.getString("distance");
                cantingInfo.vege_status=array1_2.getString("vege_status");
                if(type==1&&cantingInfo.getType().equals("1")) {
                    list_cantingInfo.add(cantingInfo);
                    addMarkersToMap(cantingInfo, y);
                    y++;
                }else if(type==2&&cantingInfo.getType().equals("6")){
                    list_cantingInfo.add(cantingInfo);
                    addMarkersToMap(cantingInfo, y);
                    y++;
                }else if(type==3&&cantingInfo.getType().equals("2")){
                    list_cantingInfo.add(cantingInfo);
                    addMarkersToMap(cantingInfo, y);
                    y++;
                }else if(type==0){
                    list_cantingInfo.add(cantingInfo);
                    addMarkersToMap(cantingInfo, i);
                }

                if(cantingInfo.getType().equals("1")){
                    list_ct.add(cantingInfo);
                }else if(cantingInfo.getType().equals("6")){
                    list_jd.add(cantingInfo);
                }else if(cantingInfo.getType().equals("2")){
                    list_hd.add(cantingInfo);
                }

            }
            initList();

            bounds=builder.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            if(list_cantingInfo.size()>0) {
                line.setVisibility(View.VISIBLE);
                msg.setVisibility(View.INVISIBLE);
                showRecommendation(city.getText().toString());
            }


        } catch (JSONException e) {
            if(list_cantingInfo.size()==0){
                Toast.makeText(getContext(), "未找到相关餐厅信息", Toast.LENGTH_SHORT).show();// 定位中心周边没有数据
                line.setVisibility(View.INVISIBLE);
                if(!city.getText().equals("北京市")&&!city.getText().equals("上海市")&&!city.getText().equals("香港")&&!city.getText().equals("台湾")&&!city.getText().equals("广州市")&&!city.getText().equals("深圳市")){
                    msg.setVisibility(View.VISIBLE);
                    msgline.setVisibility(View.VISIBLE);
                    msgtext.setText("暂无本地信息收录，欢迎上传");
                    msgline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(BaseApplication.app.getUser().islogin()){
                                Intent intent=new Intent(getContext(), FabuShHd.class);
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getContext(), Login.class);
                                getActivity().startActivity(intent);
                            }
                        }
                    });
                    ismsgshow=true;
                }
            }

            e.printStackTrace();
        }
    }
    //添加标记
    public void addMarkersToMap(CantingInfo cantingInfo, int id) {

        latLng = new LatLng(Float.valueOf(cantingInfo.getLatitude()),
                Float.valueOf(cantingInfo.getLongitude()));
        markerOption = new MarkerOptions();// 初始化对象
        markerOption.position(latLng);//
        markerOption.draggable(false);// 允许可以自由移动标记，设置为true,默认为false
        markerOption.visible(true);// 标记可见
        markerOption.anchor(1f, 1f);// anchor图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。
        // 自定义图标,使用已经存在的资源创建自定义图标
        switch (cantingInfo.getType()){
            case "1":markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker2));break;
            case "6":markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker1));break;
            case "2":markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.sumarker4));break;
            default:break;
        }

        if(cantingInfo.getVege_status().equals("2")){
            markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_yezi2));
        }

        markerOption.title("" + id);// 给marker添加一个自定义的标记，通过title属性传一个int值
        // 添加标记
        aMap.addMarker(markerOption);

        //把标记存入list
        builder.include(latLng);

    }
    //设置弹出列表
    public void initList(){
        if((!isup)&&(ranktype.equals("unit_price"))){
            Collections.reverse(list_cantingInfo);
        }
        if(list_jd.size()>0&&list_ct.size()>0&&list_hd.size()==0){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            choose.setLayoutParams(params);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPopwindow("ct+jd");
                }
            });
        }else if(list_ct.size()>0&&list_hd.size()>0&&list_jd.size()==0){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            choose.setLayoutParams(params);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPopwindow("ct+hd");
                }
            });
        }else if(list_jd.size()>0&&list_hd.size()>0&&list_ct.size()==0){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            choose.setLayoutParams(params);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPopwindow("jd+hd");
                }
            });
        }else if(list_jd.size()>0&&list_ct.size()>0&&list_hd.size()>0){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            choose.setLayoutParams(params);
            choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPopwindow("ct+jd+hd");
                }
            });
        }else
        {
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,0);
            choose.setLayoutParams(params);
        }
        adapter=new CantingListAdapter(list_cantingInfo,getContext());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listIsShow)
                {
                    //listView.setVisibility(View.INVISIBLE);
                    ViewGroup.LayoutParams params=bottomline.getLayoutParams();
                    params.height=0;
                    bottomline.setLayoutParams(params);
                    bottomline.requestLayout();
                    liechujieguo.setHeight(40);
                    listIsShow=false;
                }
                else
                {
                    // listView.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params=bottomline.getLayoutParams();
                    params.height=windowheight;
                    bottomline.setLayoutParams(params);
                    bottomline.requestLayout();
                    liechujieguo.setHeight(0);
                    listIsShow=true;
                }
            }
        });



    }
    //初始化顶部操作框
    public void initOperate(){

        //城市选择操作
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),CitySelectScrollView.class);
                intent.putExtra("city",city.toString());
                startActivityForResult(intent, 11);
            }
        });

        inputs.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapSearch.class);
                intent.putExtra("city", city.getText().toString());
                intent.putExtra("tex",inputs.getText().toString());
                startActivityForResult(intent,12);
            }
        });
        msgline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    Intent intent=new Intent(getContext(), FabuShHd.class);
                    getActivity().startActivity(intent);
                }else {
                    Intent intent=new Intent(getContext(), Login.class);
                    getActivity().startActivity(intent);
                }
            }
        });
        msgima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismsgshow){
                    msgline.setVisibility(View.INVISIBLE);
                    msgline.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_out));
                    ismsgshow=false;
                }else {
                    msgline.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_right_in));
                    msgline.setVisibility(View.VISIBLE);
                    ismsgshow=true;
                }
            }
        });
        msgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismsgshow){
                    msgline.setVisibility(View.INVISIBLE);
                    msgline.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_out));
                    ismsgshow=false;
                }else {
                    msgline.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_right_in));
                    msgline.setVisibility(View.VISIBLE);
                    ismsgshow=true;
                }
            }
        });

    }
    //城市切换
    public void CityQiehuan(String name, String city) {
        list_cantingInfo=new ArrayList<>();
        type=0;
        msg.setVisibility(View.INVISIBLE);
        //adapter.notifyDataSetChanged();
        GeocodeSearch geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch
                .setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

                    // 逆地理编码回调
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult result,
                                                    int rCode) {
                        if (rCode == 1000) {

                        }
                    }

                    // 地理编码结果回调
                    @Override
                    public void onGeocodeSearched(GeocodeResult result,
                                                  int rCode) {

                        if (rCode == 1000) {
                            if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
                                GeocodeAddress address = result
                                        .getGeocodeAddressList().get(0);// 得到地址
                                aMap.animateCamera(CameraUpdateFactory
                                        .newLatLngZoom(new LatLng(
                                                address.getLatLonPoint()
                                                        .getLatitude(), address
                                                .getLatLonPoint()
                                                .getLongitude()), 10));// 地图自动移动到当前城市

                            }
                        }

                    }
                });
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(name, city);
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求


    }
    //扎定位标记
    public void DingWeiMarker(){
        if(BaseApplication.app.getMyLociation().getLatitude()!=null){
            latLng = new LatLng(Float.valueOf(BaseApplication.app.getMyLociation().getLatitude()),
                    Float.valueOf(BaseApplication.app.getMyLociation().getLongitude()));
            markerOption = new MarkerOptions();// 初始化对象
            markerOption.position(latLng);//
            markerOption.draggable(false);// 允许可以自由移动标记，设置为true,默认为false
            markerOption.visible(true);// 标记可见
            markerOption.anchor(1f, 1f);// anchor图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。
            // 自定义图标,使用已经存在的资源创建自定义图标
            markerOption.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));
            // 添加标记
            aMap.addMarker(markerOption);
            //把标记存入list
            //builder.include(latLng);
        }
    }
    //位置切换弹的提示框
    public void getDialog(final String mcity){
        dialogbuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        dialogbuilder.setMessage("屏幕区域位于"+mcity+"，是否切换到该城市展示更多信息？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        city.setText(mcity);
                        if(mcity.equals("北京市")||mcity.equals("上海市")||mcity.equals("香港")||mcity.equals("台湾")||mcity.equals("广州市")||mcity.equals("深圳市"))
                        {
                            find.setImageResource(R.drawable.ditu_userfind);
                        }
                        else{
                            find.setImageResource(R.drawable.ditu_userfindall);
                        }

                        CityQiehuan(mcity,mcity);
                        line.setVisibility(View.INVISIBLE);
                        aMap.clear();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogbuilder.show();
    }

    public void getPopwindow(String s){
        Log.d("===========tp",s);
        View popView = LayoutInflater.from(getContext()).inflate(R.layout.mishi_choosetype_pop,null);
        TextView ct=(TextView)popView.findViewById(R.id.mishi_choose_ct);
        TextView jd=(TextView)popView.findViewById(R.id.mishi_choose_jd);
        TextView hd=(TextView)popView.findViewById(R.id.mishi_choose_hd);
        switch (s){
            case "ct+jd":
                ViewGroup.LayoutParams params=hd.getLayoutParams();
                params.height=0;
                hd.setLayoutParams(params);
                hd.requestLayout();
                break;
            case "ct+hd":
                ViewGroup.LayoutParams params2=jd.getLayoutParams();
                params2.height=0;
                jd.setLayoutParams(params2);
                jd.requestLayout();
                break;
            case "jd+hd":
                ViewGroup.LayoutParams params3=ct.getLayoutParams();
                params3.height=0;
                ct.setLayoutParams(params3);
                ct.requestLayout();
                break;
            case "ct+jd+hd":break;
        }
        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=1;
                list_cantingInfo=list_ct;
                adapter=new CantingListAdapter(list_cantingInfo,getContext());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                aMap.clear();
                builder= new LatLngBounds.Builder();
                for(int i=0;i<list_cantingInfo.size();i++){
                    addMarkersToMap(list_cantingInfo.get(i),i);
                }
                bounds=builder.build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                popWindow.dismiss();
            }
        });
        jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=2;
                list_cantingInfo=list_jd;
                adapter=new CantingListAdapter(list_cantingInfo,getContext());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                aMap.clear();
                builder= new LatLngBounds.Builder();
                for(int i=0;i<list_cantingInfo.size();i++){
                    addMarkersToMap(list_cantingInfo.get(i),i);
                }
                bounds=builder.build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                popWindow.dismiss();
            }
        });
        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=3;
                list_cantingInfo=list_hd;
                adapter=new CantingListAdapter(list_cantingInfo,getContext());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                aMap.clear();
                builder= new LatLngBounds.Builder();
                for(int i=0;i<list_cantingInfo.size();i++){
                    addMarkersToMap(list_cantingInfo.get(i),i);
                }
                bounds=builder.build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                popWindow.dismiss();
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // 获取光标
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAsDropDown(choose);

    }

    public void showRecommendation(String city){
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainLiving/index.php/androidapi/map/city_restaurant/city/"+city, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("0")){
                        msg.setVisibility(View.INVISIBLE);
                    }else {
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("news");
                        JSONObject jo=ja.getJSONObject(0);
                        final String id=jo.getString("res_id");
                        String title=jo.getString("title");
                        msgtext.setText(title);
                        msg.setVisibility(View.VISIBLE);
                        msgline.setVisibility(View.VISIBLE);
                        ismsgshow=true;
                        msgline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    Intent intent=new Intent(getContext(), CantingDetail.class);
                                    intent.putExtra("item_id",id);
                                    getActivity().startActivity(intent);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }

}
