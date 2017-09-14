package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.BitmapUtils;
import com.example.administrator.vegetarians824.entry.Country;
import com.example.administrator.vegetarians824.entry.Language;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.entry.TripInfo;
import com.example.administrator.vegetarians824.fabu.FabuTZ;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.HttpUtil;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Trip extends AppCompatActivity implements AMap.OnMarkerClickListener,AMap.InfoWindowAdapter,AMap.OnInfoWindowClickListener,LocationSource,AMap.OnCameraChangeListener{
    private AMap aMap;
    private MapView mapView;
    private List<Country> countryList;
    private List<Language> languageList;
    private List<TripInfo> tripInfoList;
    private MarkerOptions markerOption;
   // private List<Bitmap> b;
    private Mhander handler;
    private List<Tiezi> tzlist;
    public static AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption=null;
   // private ScrollView scrolls;
    public static LocationSource.OnLocationChangedListener mListener;
    public int counter=0;
    private TextView lb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        countryList = new ArrayList<>();
        languageList = new ArrayList<>();
        tripInfoList = new ArrayList<>();
        tzlist=new ArrayList<>();
        mapView = (MapView) findViewById(R.id.trip_map);
        //scrolls=(ScrollView)findViewById(R.id.scrolls);
        lb=(TextView)findViewById(R.id.trip_lunbo);
        mapView.onCreate(savedInstanceState);
        initmap();
        initoperate();
        HttpRequest();
        initButton();

    }

    public void initoperate() {
        LinearLayout fanhui = (LinearLayout) findViewById(R.id.trip_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout fabu=(LinearLayout)findViewById(R.id.fabu_add4);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    Intent intent=new Intent(Trip.this, FabuTZ.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(Trip.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    //初始化地图
    public void initmap() {


        aMap = mapView.getMap();
        /*
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                scrolls.requestDisallowInterceptTouchEvent(true);
                mapView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });
        */
        aMap.setOnCameraChangeListener(this);
        aMap.setLocationSource(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(4));
        aMap.setOnInfoWindowClickListener(this);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        StatService.onResume(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        StatService.onPause(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    //从网络获取数据，国家信息，地图下面列表
    public void HttpRequest() {
        StringRequest request = new StringRequest(URLMannager.Trip_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    //解析json
    public void myJson(String s) {
        try {
            JSONObject js1 = new JSONObject(s);
            JSONObject js2 = js1.getJSONObject("Result");
            JSONArray ja1 = js2.getJSONArray("country");
            //解析国家
            for (int x = 0; x < ja1.length(); x++) {
                JSONObject cy = ja1.getJSONObject(x);
                Country country = new Country();
                country.setCid(cy.getString("cid"));
                country.setName(cy.getString("name"));
                country.setLongitude(cy.getString("longitude"));
                country.setLatitude(cy.getString("latitude"));
                country.setIco(cy.getString("ico"));
                countryList.add(country);
            }
            //解析地图下面语言
            JSONArray ja2 = js2.getJSONArray("language");
            for (int y = 0; y < ja2.length(); y++) {
                JSONObject l = ja2.getJSONObject(y);
                Language language = new Language();
                language.setTitle(l.getString("title"));
                language.setLid(l.getString("lid"));
                languageList.add(language);
            }
            //解析下面酒店列表
            JSONArray ja3 = js2.getJSONArray("info");
            for (int z = 0; z < ja3.length(); z++) {
                JSONObject info = ja3.getJSONObject(z);
                TripInfo tripInfo = new TripInfo();
                tripInfo.setId(info.getString("id"));
                tripInfo.setTitle(info.getString("title"));
                tripInfo.setContent(info.getString("content"));
                tripInfo.setType(info.getString("type"));
                // Log.d("==========uid",info.getInt("uid")+"");
                tripInfo.setImg_url_1(info.getString("img_url_1"));
                tripInfo.setTel(info.getString("tel"));
                tripInfo.setUnit_price(info.getString("unit_price"));
                tripInfo.setLongitude(info.getString("longitude"));
                tripInfo.setLatitude(info.getString("latitude"));
                tripInfo.setCreate_time(info.getString("create_time"));
                tripInfoList.add(tripInfo);
            }
            //帖子轮播
            JSONArray ja4=js2.getJSONArray("posts");
            for(int i=0;i<ja4.length();i++){
                JSONObject posts=ja4.getJSONObject(i);
                Tiezi tz=new Tiezi();
                tz.setTitle(posts.getString("title"));
                tz.setId(posts.getString("id"));
                tzlist.add(tz);
            }

            addMarker();
            //initList();
            setLunbo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //获取mark图片
    public void addMarker() {
        //b=new ArrayList<>();
        handler = new Mhander();
        //开一个新线程从网络加载国家logo图片
        Thread t = new Thread() {
            public void run() {
                for (int i = 0; i < countryList.size(); i++) {
                    Country c = countryList.get(i);
                    BitmapDrawable bg = (BitmapDrawable) HttpUtil.doGetImag(URLMannager.Imag_URL + c.getIco());
                    Bitmap bm=new BitmapUtils().zoomImage(bg.getBitmap(),50,50);
                    //b.add(bm);
                    //拿一个mark丢给handle一个
                    Message msg = new Message();
                    msg.what = 2;
                    msg.arg1=i;
                    msg.obj = bm;
                    handler.sendMessage(msg);// handle交给管理者处理
                }
                //全部把mark丢给handle
                //Message msg = new Message();
                //msg.what = 2;
                //msg.obj = b;
                //handler.sendMessage(msg);// handle交给管理者处理
            }
        };
        t.start();

    }

    //mark点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    //自定义nark弹窗
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.info_window, null);
        TextView c=(TextView) infoWindow.findViewById(R.id.marker_country);
        c.setText(marker.getSnippet());
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(R.layout.info_window, null);
        TextView c=(TextView) infoContent.findViewById(R.id.marker_country);
        c.setText(marker.getSnippet());
        return infoContent;
    }

    //弹窗点击事件
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent=new Intent(Trip.this,AWord2.class);
        intent.putExtra("cid",marker.getTitle());
        intent.putExtra("cname",marker.getSnippet());
        Trip.this.startActivity(intent);
    }
    //定位开始
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener=onLocationChangedListener;
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            //设置定位监听
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    BaseApplication.app.setMyLociation(new MyLociation(String
                            .valueOf(amapLocation.getLongitude()), String
                            .valueOf(amapLocation.getLatitude()), String
                            .valueOf(amapLocation.getCity())));
                }
            });
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

    //地图移动
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //scrolls.requestDisallowInterceptTouchEvent(true);
        mapView.getParent().requestDisallowInterceptTouchEvent(true);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    //handler对mrak扎点
    class Mhander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                   int i=msg.arg1;
                    Country c = countryList.get(i);
                    LatLng latLng = new LatLng(Float.valueOf(c.getLatitude()),
                            Float.valueOf(c.getLongitude()));
                    markerOption = new MarkerOptions();// 初始化对象
                    markerOption.position(latLng);//
                    markerOption.draggable(false);// 允许可以自由移动标记，设置为true,默认为false
                    markerOption.visible(true);// 标记可见
                    markerOption.anchor(0.5f, 0.5f);// anchor图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。自定义图标,使用已经存在的资源创建自定义图标
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap((Bitmap)msg.obj));
                    markerOption.title(c.getCid());
                    markerOption.snippet(c.getName());
                    aMap.addMarker(markerOption);

                    //及时回收bitmap,防止占用过多内存
                    if(((Bitmap) msg.obj).isRecycled()==false)
                        ((Bitmap) msg.obj).recycle();

            }
        }
    }

    //底部列表
    /*
    public void initList(){
        ListView  listView=(ListView) findViewById(R.id.trip_list);
        listView.setAdapter(new TripListAdapter(tripInfoList,Trip.this));

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        View view=getLayoutInflater().inflate(R.layout.trip_item,null);
        view.measure(0,0);
        params.height=view.getMeasuredHeight()*listView.getAdapter().getCount();
        listView.setLayoutParams(params);
        listView.requestLayout();

    }*/

    //语言选择
    public void initButton(){
        Button b1=(Button) findViewById(R.id.trip_b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",1+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b2=(Button) findViewById(R.id.trip_b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",2+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b3=(Button) findViewById(R.id.trip_b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",3+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b4=(Button) findViewById(R.id.trip_b4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",4+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b5=(Button) findViewById(R.id.trip_b5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",5+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b6=(Button) findViewById(R.id.trip_b6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",6+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b7=(Button) findViewById(R.id.trip_b7);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",7+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b8=(Button) findViewById(R.id.trip_b8);
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",8+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b9=(Button) findViewById(R.id.trip_b9);
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",9+"");
                Trip.this.startActivity(intent);
            }
        });
        Button b10=(Button) findViewById(R.id.trip_b10);
        b10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Trip.this,AWord.class);
                intent.putExtra("id",10+"");
                Trip.this.startActivity(intent);
            }
        });
    }

    public void setLunbo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    viewHandler.sendEmptyMessage(counter);
                    counter++;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            lb.setText(tzlist.get(counter%(tzlist.size())).getTitle());
            lb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Trip.this,TieziDetial.class);
                    intent.putExtra("tid",tzlist.get(counter%(tzlist.size())).getId());
                    Trip.this.startActivity(intent);
                }
            });
            super.handleMessage(msg);
        }

    };



}