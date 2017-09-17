package com.example.administrator.vegetarians824.veganpass;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myView.UserDefineScrollView;
import com.example.administrator.vegetarians824.util.ConnectionNetUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PassportHome extends AppCompatActivity {
    @Bind(R.id.favour_leader)
    ImageView m_favour_leader;
    private FrameLayout content;
    /**
     * dialog工具类
     */
    private LoadingDialog loadingDialog;//加载显示框

    private FrameLayout bartop;
    private LinearLayout barbottom;
    //    private LinearLayout barcenter;
    private Long up = 0L, down = 0L;
    private boolean netflag = false;
    private String type;
    private LinearLayout localfailed;
    private SlidingMenu slidingMenu;
    private SharedPreferences pre;
    private ListView listview;
    private LinearLayout listview2;
    private String mycountry, mylanguage;//选中的国家
    private TextView mycountrytv;//国家
    private CountryAdapter adapter;
    private CoutryData data;
    private TextView title, contents;
    private PopupWindow popWindow, popFavour;
    private int[] id;//语言资源索引数组
    private int language_num = 0;//当前语言编号
    private GestureDetector gestureDetector;
    private List<Country> list, list_en;
    private List<Letters> list2;
    private List<TextView> tvlist;
    /**
     * 小手提示图标，
     * tip1: 快速单击，顶部菜单
     * tip2: 滑动，右侧划出
     */
    private ImageView tip1, tip2;
    //    private SeekBar seekbar; // 字体大小调节工具，进度条模式
    private float x, y;
    private FrameLayout rootview; // activity_passport_home.xml整个布局
    private ImageButton floats;
    private TextView favour;
    public static boolean hasedit = false;
    private String language_id;
    private String code = "";
    private TextView title1, title2, title3, preference1, preference2, preference3;
    private LocationManager locationManager;
    private String locationProvider;
    private String en_ch;
//    private ImageView favour_leader;
    private boolean nopreference = true;
    private TextView jumpurl;
    private String url = "";
    private CustomToast customToast; // Toast信息：定位成功
    private CustomToast2 customToast2; // Toast提示信息：国家：xx 信息已翻译至当地语言

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止屏幕休眠或锁屏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_passport_home);
        ButterKnife.bind(this);
        StatusBarUtil.setColorDiff(this, 0x8e000000);

        initData();

        initView();
        initMenu();
        if (en_ch.equals("cn")) {
            loadingDialog.setMessage("连接网络...");
        } else {
            loadingDialog.setMessage("loading...");
        }
        loadingDialog.show();
        new CheckInternet().execute();
        //location();

    }

    public void initView() {
        if (en_ch.equals("cn")) {
            m_favour_leader.setImageResource(R.drawable.leader_cn);
        } else {
//            favour_leader.setImageResource(R.drawable.leader_en);
        }
        rootview = (FrameLayout) findViewById(R.id.frameLayout_port_home);
        content = (FrameLayout) findViewById(R.id.frame_passport_content);
        localfailed = (LinearLayout) findViewById(R.id.home_locate_failed);
        mycountrytv = (TextView) findViewById(R.id.home_country);
        title = (TextView) findViewById(R.id.home_title);
        contents = (TextView) findViewById(R.id.home_contents);
        bartop = (FrameLayout) findViewById(R.id.passport_menubar_top);
        barbottom = (LinearLayout) findViewById(R.id.home_barbottom);
//        barcenter = (LinearLayout) findViewById(R.id.home_barcenter);
        loadingDialog = new LoadingDialog(this);
        tip1 = (ImageView) findViewById(R.id.tips_click);
        tip2 = (ImageView) findViewById(R.id.tips_move);
        if (en_ch.equals("en")) {
//            tip1.setImageResource(R.mipmap.click2);
//            tip2.setImageResource(R.mipmap.slide2);
        }
//        seekbar = (SeekBar) findViewById(R.id.home_seekbar);
        floats = (ImageButton) findViewById(R.id.home_floats);
        favour = (TextView) findViewById(R.id.home_favour);
        if (en_ch.equals("en")) {
            favour.setText("Setting");
        } else {
            favour.setText("设置");
        }
        if (!pre.getBoolean("isfirstedit", true)) {
            floats.setVisibility(View.VISIBLE);
        }
        jumpurl = (TextView) findViewById(R.id.jump_url);

        initPopFavour();
        //设置国家
        LinearLayout setcountry = (LinearLayout) findViewById(R.id.home_setcountry);
        setcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customToast.cancel();

                if (!slidingMenu.isMenuShowing()) {
                    slidingMenu.showMenu();
                }
                //Intent intent=new Intent(getBaseContext(),CountryList.class);
                // startActivity(intent);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", "enter");
                StatService.onEvent(getBaseContext(), "国家列表", "press", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "国家列表", "press");
                StatService.onEventEnd(getBaseContext(), "国家列表", "press");

            }
        });

        TextView feedback = (TextView) findViewById(R.id.home_fedback);
        if (en_ch.equals("en")) {
            feedback.setText("Feedback");
        } else {
            feedback.setText("反馈");
        }
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customToast.cancel();
                Intent intent = new Intent(getBaseContext(), FeedBack.class);
                intent.putExtra("country", mycountry);
                intent.putExtra("language", mylanguage);
                startActivity(intent);
            }
        });


        if (pre.getBoolean("isfirst", true)) {
            tip1.setVisibility(View.VISIBLE);
        }

        //长按消失 显示
        UserDefineScrollView layout = (UserDefineScrollView) findViewById(R.id.home_layout);


        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Date dt = new Date();
                    down = dt.getTime();
                } else {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        Date dt = new Date();
                        up = dt.getTime();
                    }
                }
                if ((up - down > 0) && (up - down < 80)) {
                    if (bartop.getVisibility() == View.VISIBLE) {

                        rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                        bartop.setVisibility(View.INVISIBLE);
                        // barbottom.setVisibility(View.INVISIBLE);
                        floats.setVisibility(View.INVISIBLE);
//                        barcenter.setVisibility(View.INVISIBLE);
                        Animation mAlphaAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha);
//                        barcenter.startAnimation(mAlphaAnimation);

                    } else {
                        rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        bartop.setVisibility(View.VISIBLE);
                        //barbottom.setVisibility(View.VISIBLE);
                        if (!pre.getBoolean("isfirstedit", true) && (!nopreference)) {
                            floats.setVisibility(View.VISIBLE);
                        }
//                        barcenter.setVisibility(View.VISIBLE);
                    }
                    slidingMenu.showContent();

                }

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (tip1.getVisibility() == View.VISIBLE) {
                        tip1.setVisibility(View.INVISIBLE);
                        tip2.setVisibility(View.VISIBLE);
                    } else {
                        if (tip1.getVisibility() == View.INVISIBLE && tip2.getVisibility() == View.VISIBLE) {
                            tip2.setVisibility(View.INVISIBLE);

                            SharedPreferences.Editor editor = pre.edit();
                            editor.putBoolean("isfirst", false);
                            editor.apply();//提交数据
                        }
                    }
                }
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }

        });


        x = title.getTextSize();
        y = contents.getTextSize();
        Log.d("===============xx", x + "  " + y);
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                Log.d("===============xxx", (x + progress) + "  " + (y + progress));
//                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, x + progress);
//                contents.setTextSize(TypedValue.COMPLEX_UNIT_PX, y + progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        floats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customToast.cancel();

                backgroundAlpha(0.5f);
                popFavour.showAtLocation(content, Gravity.BOTTOM, 0, 0);
                initpreference();
                getLanguageId();
            }
        });

        favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hasedit = true;
                customToast.cancel();
                Intent intent = new Intent(getBaseContext(), Setting.class);
                startActivity(intent);
                //startBeizerAnimation();
            }
        });
    }

    //获取本地存储的数据 初始化数据类
    public void initData() {
        // 初始化数据类
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        tvlist = new ArrayList<>();
        customToast = new CustomToast();
        customToast2 = new CustomToast2();

        // 实例化用户手势检测工具类：GestureDetector实例gestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() < -80) {
                    customToast.cancel();
                    Intent intent = new Intent(getBaseContext(), FavourList.class);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(PassportHome.this);
                    startActivity(intent, activityOptionsCompat.toBundle());
                    return true;
                }
                /*
                else {

                    if(e2.getRawX()-e1.getRawX()>80){
                        Intent intent=new Intent(getBaseContext(),LoadMusic.class);
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomePage.this);
                        HomePage.this.startActivity(intent,activityOptionsCompat.toBundle());
                    }

                }
                */

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        type = pre.getString("type", "");
        code = pre.getString("phoneid", "");
        en_ch = pre.getString("languagetype", "");
        data = new CoutryData();
        for (int i = 0; i < data.getCountryName().length; i++) {
            Country c = new Country();
            c.setName(data.getCountryName()[i]);
            c.setEnglish_name(data.getCountryEnName()[i]);
            list.add(c);
        }
        String ss[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Y", "Z"};
        String sss[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        if (en_ch.equals("en")) {
            Collections.sort(list, new LetterComparator());
        }
        if (en_ch.equals("cn")) {
            for (int i = 0; i < ss.length; i++) {
                Letters l = new Letters();
                l.setName(ss[i]);
                l.setIschoose(false);
                list2.add(l);
            }
        } else {
            for (int i = 0; i < sss.length; i++) {
                Letters l = new Letters();
                l.setName(sss[i]);
                l.setIschoose(false);
                list2.add(l);
            }
        }
        //list2.get(0).setIschoose(true);

    }

    public class CheckInternet extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //1.5秒检查网络
            netflag = ConnectionNetUtils.isConnectionNet(getBaseContext());
            SystemClock.sleep(1500);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //有网络
            if (netflag) {
                loadingDialog.dismiss();
                location();
            } else {
                //无网络
                loadingDialog.dismiss();
                if (en_ch.equals("cn")) {
                    Toast.makeText(getBaseContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Disconnect", Toast.LENGTH_SHORT).show();
                }
                loadfail();
            }
        }
    }

    public void location() {
        if (en_ch.equals("cn")) {
            loadingDialog.setMessage("定位中...");
        } else {
            loadingDialog.setMessage("positioning...");
        }
        loadingDialog.show();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
            }
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else {
                if (en_ch.equals("cn")) {
                    Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "no localtion service", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                loadfail();
                return;
            }

            loadingDialog.dismiss();
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                if (en_ch.equals("cn")) {
                    customToast.showToast(getBaseContext(), "定位成功");
                } else {
                    customToast.showToast(getBaseContext(), "Success");
                }
                getCountry(location);

            } else {
                if (en_ch.equals("cn")) {
                    Toast.makeText(this, "获取不到地理位置", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "has not found location", Toast.LENGTH_SHORT).show();
                }
                loadfail();
            }
        }


    }

    public void getCountry(Location location) {
        StringRequest request = new StringRequest("http://apis.map.qq.com/ws/geocoder/v1/?location=" + location.getLatitude() + "," + location.getLongitude() + "&key=KRIBZ-EGVHU-IEFV5-2SFQT-LAPJK-RDFEM", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("status").equals("0")) {
                        JSONObject js2 = js1.getJSONObject("result");
                        JSONObject js3 = js2.getJSONObject("address_component");
                        String country = js3.getString("nation");
                        if (!country.equals("")) {
                            mycountry = country;
                            SharedPreferences.Editor editor = pre.edit();
                            editor.putString("country", mycountry);
                            editor.apply();//提交数据
                            load();
                            if (en_ch.equals("cn")) {
                                customToast2.showToast(getBaseContext(), "国家: " + mycountry, "信息已翻译至当地语言");
                            } else {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getName().equals(mycountry)) {
                                        String enname = list.get(i).getEnglish_name();
                                        customToast2.showToast(getBaseContext(), "Country: " + enname, "The information translated into local language");
                                    }
                                }
                            }
                            if (country.equals("中国")) {
                                initJump();
                            }
                        } else {
                            if (en_ch.equals("cn")) {
                                Toast.makeText(getBaseContext(), "无法解析所在国家", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "The nation can't be found", Toast.LENGTH_SHORT).show();
                            }
                            loadfail();
                        }
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
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }

    public void load() {
        content.setVisibility(View.VISIBLE);
        if (en_ch.equals("cn")) {
            mycountrytv.setText(mycountry);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(mycountry)) {
                    mycountrytv.setText(list.get(i).getEnglish_name());
                }
            }
        }
        if (barbottom.getChildCount() > 0) {
            barbottom.removeAllViews();
        }
        getLanguage(mycountry);
        loadContent(language_num);
    }

    /**
     *
     */
    public void loadfail() {
        localfailed.setVisibility(View.VISIBLE);
        Button bt1 = (Button) findViewById(R.id.home_op1);
        Button bt2 = (Button) findViewById(R.id.home_op2);
        if (en_ch.equals("cn")) {
            bt1.setText("重新获取");
            bt2.setText("手动获取");
        } else {
            bt1.setText("resume");
            bt2.setText("manual");
        }
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (en_ch.equals("cn")) {
                    loadingDialog.setMessage("连接网络...");
                } else {
                    loadingDialog.setMessage("Loading...");
                }
                loadingDialog.show();
                new CheckInternet().execute();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingMenu.showMenu();
            }
        });

    }

    public void initMenu() {
        slidingMenu = new SlidingMenu(getBaseContext());
        slidingMenu.setMode(SlidingMenu.RIGHT);//左侧边栏
        //slidingMenu.setBehindWidth(450);//侧边栏出来宽度
        slidingMenu.setBehindOffset(120);
        View view = getLayoutInflater().inflate(R.layout.vertical_menu, null);
        TextView hot1 = (TextView) view.findViewById(R.id.hot_tv1);
        TextView hot2 = (TextView) view.findViewById(R.id.hot_tv2);
        TextView hot3 = (TextView) view.findViewById(R.id.hot_tv3);
        TextView hot4 = (TextView) view.findViewById(R.id.hot_tv4);
        TextView hot5 = (TextView) view.findViewById(R.id.hot_tv5);
        TextView hot6 = (TextView) view.findViewById(R.id.hot_tv6);
        TextView hottitle = (TextView) view.findViewById(R.id.hottitle);
        if (en_ch.equals("cn")) {
            hottitle.setText("热门语言");
            hot1.setText("英语");
            hot2.setText("法语");
            hot3.setText("西班牙语");
            hot4.setText("德语");
            hot5.setText("日语");
            hot6.setText("韩语");
        } else {
            hottitle.setText("Popular Language");
            hot1.setText("English");
            hot2.setText("French");
            hot3.setText("Spanish");
            hot4.setText("German");
            hot5.setText("Japanese");
            hot6.setText("Korean");
        }
        hot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "英国";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "United Kingdom", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "英国", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        hot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "法国";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "France", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "法国", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        hot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "西班牙";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "Spain", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "西班牙", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        hot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "德国";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "Germany", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "德国", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        hot5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "日本";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "Japan", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "日本", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        hot6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mycountry = "韩国";
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + "South Korea", "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + "韩国", "信息已翻译至当地语言");
                }
                jumpurl.setVisibility(View.INVISIBLE);
            }
        });
        listview = (ListView) view.findViewById(R.id.countrylist);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mycountry = list.get(i).getName();
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();
                if (en_ch.equals("en")) {
                    customToast2.showToast(getBaseContext(), "Country: " + list.get(i).getEnglish_name(), "The information translated into local language");
                } else {
                    customToast2.showToast(getBaseContext(), "国家: " + list.get(i).getName(), "信息已翻译至当地语言");
                }
                if (!mycountry.equals("中国")) {
                    jumpurl.setVisibility(View.INVISIBLE);
                } else {
                    //jumpurl.setVisibility(View.VISIBLE);
                    initJump();
                }
                //保存记录
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("country", list.get(i).getName());
                editor.apply();//提交数据

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", mycountry);
                StatService.onEvent(getBaseContext(), "国家选择", "press3", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "国家选择", "press3");
                StatService.onEventEnd(getBaseContext(), "国家选择", "press3");
            }
        });


        listview2 = (LinearLayout) view.findViewById(R.id.letterlist);
        for (int i = 0; i < list2.size(); i++) {
            View convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.adapter_letter_item, null);
            final TextView tv = (TextView) convertView.findViewById(R.id.letter_item);
            tv.setText(list2.get(i).getName());
            if (i == 0) {
                tv.setTextColor(0xffff0000);
            }
            final int x = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (en_ch.equals("cn")) {
                        String s[][] = data.getCountryNameA2Z();
                        String cc = s[x][0];
                        for (int z = 0; z < list.size(); z++) {
                            if (cc.equals(list.get(z).getName())) {
                                listview.setSelection(z);
                            }
                        }

                        for (int o = 0; o < tvlist.size(); o++) {
                            tvlist.get(o).setTextColor(0xffffffff);
                        }
                        tv.setTextColor(0xffff0000);
                    } else {
                        for (int z = 0; z < list.size(); z++) {
                            String fisrtletter = list.get(z).getEnglish_name().substring(0, 1);
                            if (tv.getText().toString().equals(fisrtletter)) {
                                listview.setSelection(z);
                                break;
                            }
                        }

                        for (int o = 0; o < tvlist.size(); o++) {
                            tvlist.get(o).setTextColor(0xffffffff);
                        }
                        tv.setTextColor(0xffff0000);
                    }
                }
            });

            tvlist.add(tv);
            listview2.addView(convertView);
        }

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (en_ch.equals("cn")) {
                    String s[][] = data.getCountryNameA2Z();
                    String country = list.get(firstVisibleItem).getName();
                    char l = 'A';
                    for (int i = 0; i < s.length; i++) {
                        for (int y = 0; y < s[i].length; y++) {
                            if (country.equals(s[i][y]) && (firstVisibleItem + visibleItemCount) != totalItemCount) {
                                l = (char) (l + i);
                                for (int o = 0; o < tvlist.size(); o++) {
                                    tvlist.get(o).setTextColor(0xffffffff);
                                }

                                tvlist.get(i).setTextColor(0xffff0000);

                            }
                        }
                    }
                } else {
                    String fisrst = list.get(firstVisibleItem).getEnglish_name().substring(0, 1);
                    for (int i = 0; i < tvlist.size(); i++) {
                        if (fisrst.equals(tvlist.get(i).getText().toString())) {
                            for (int o = 0; o < tvlist.size(); o++) {
                                tvlist.get(o).setTextColor(0xffffffff);
                            }
                            tvlist.get(i).setTextColor(0xffff0000);
                        }
                    }
                }

            }
        });

        getCountryList();
        slidingMenu.setMenu(view);
        slidingMenu.setFadeDegree(0.35f);//设置滑动时的渐变成度
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT, true);//侧边栏执行开始
        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                bartop.setVisibility(View.VISIBLE);
                barbottom.setVisibility(View.VISIBLE);

            }
        });
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                bartop.setVisibility(View.INVISIBLE);
                barbottom.setVisibility(View.INVISIBLE);
            }
        });
        slidingMenu.setSlidingEnabled(false);

    }

    public void getCountryList() {
        adapter = new CountryAdapter(list, PassportHome.this, en_ch);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void getLanguage(String c) {
        int flag = 0;
        //查询国家在数组的位置
        for (int i = 0; i < data.getCountryName().length; i++) {
            if (data.getCountryName()[i].equals(c)) {
                flag = i;
            }
        }
        //匹配语言数组 数组长度=这个国家有几种语言
        id = data.getCountryLanguage()[flag];

        for (int x = 0; x < id.length; x++) {
            View view = getLayoutInflater().inflate(R.layout.language_item, null);
            TextView tv = (TextView) view.findViewById(R.id.language_item_tv);
            final String language_name = getResources().getResourceEntryName(id[x]);
            final String english_name = getResources().getStringArray(id[x])[6];
            if (x == language_num) {
                tv.setTextColor(0xff3e3e3e);
                mylanguage = language_name;
            }
            tv.setText(english_name);
            final int flags = x;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    language_num = flags;
                    if (en_ch.equals("cn")) {
                        customToast.showToast(getBaseContext(), language_name);
                    } else {
                        customToast.showToast(getBaseContext(), english_name);
                    }
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("event", language_name);
                    StatService.onEvent(getBaseContext(), "语种选择", "press5", 1, hashMap);
                    StatService.onEventStart(getBaseContext(), "语种选择", "press5");
                    StatService.onEventEnd(getBaseContext(), "语种选择", "press5");
                    load();
                }
            });
            if (id.length > 1) {
                barbottom.addView(view);
            }
        }

    }

    public void loadContent(int choose_language) {
        String[] content = getResources().getStringArray(id[choose_language]);
        switch (type) {
            case "纯素":
                if (!content[0].equals("")) {
                    title.setText(content[0]);
                    contents.setText(content[1]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
            case "蛋奶素":
                if (!content[2].equals("")) {
                    title.setText(content[2]);
                    contents.setText(content[3]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
            case "净素":
                if (!content[4].equals("")) {
                    title.setText(content[4]);
                    contents.setText(content[5]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
            case "Vegan":
                if (!content[0].equals("")) {
                    title.setText(content[0]);
                    contents.setText(content[1]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
            case "Ovo-Lacto Vegetarian":
                if (!content[2].equals("")) {
                    title.setText(content[2]);
                    contents.setText(content[3]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
            case "Vuddhist Vegan":
                if (!content[4].equals("")) {
                    title.setText(content[4]);
                    contents.setText(content[5]);
                } else {
                    title.setText("暂无该语种信息");
                    contents.setText("");
                }
                break;
        }
    }

    public void initPopFavour() {
        View popView = getLayoutInflater().inflate(R.layout.pop_favour, null);
        title1 = (TextView) popView.findViewById(R.id.title1);
        title2 = (TextView) popView.findViewById(R.id.title2);
        title3 = (TextView) popView.findViewById(R.id.title3);
        preference1 = (TextView) popView.findViewById(R.id.preference1);
        preference2 = (TextView) popView.findViewById(R.id.preference2);
        preference3 = (TextView) popView.findViewById(R.id.preference3);
        popFavour = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popFavour.setAnimationStyle(R.style.mypopwindow_anim_style2);
        popFavour.setFocusable(true);
        popFavour.setOutsideTouchable(true);
        popFavour.setBackgroundDrawable(new ColorDrawable());
        popFavour.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bartop.getVisibility() == View.INVISIBLE) {
            rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        StatService.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        customToast.cancel();
        customToast2.cancel();
        loadingDialog.cancel();
        StatService.onPause(this);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void startBeizerAnimation() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        LoveView love = new LoveView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(60, 60);
        love.setLayoutParams(params);
        love.requestLayout();
        love.setImageResource(R.mipmap.love);
        rootView.addView(love);
        int startposition[] = new int[2];
        favour.getLocationInWindow(startposition);
        Point startpoint = new Point(startposition[0], startposition[1]);
        int endposition[] = new int[2];
        floats.getLocationInWindow(endposition);
        Point endpoint = new Point(endposition[0], endposition[1]);

        love.setStartPosition(startpoint);
        love.setEndPosition(endpoint);
        love.startBeizerAnimation();

    }

    public void getLanguageId() {
        StringPostRequest spr = new StringPostRequest(URLManager.Language, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("status").equals("success")) {
                        JSONArray ja = js1.getJSONArray("data");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            if (jo.getString("name").equals(mylanguage)) {
                                language_id = jo.getString("id");
                            }
                        }

                    }
                    Log.d("==========laid", language_id);
                    getLanguagePreference();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("country", mycountry);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void getLanguagePreference() {
        StringPostRequest spr = new StringPostRequest(URLManager.GetLanguagePreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("============la", s);
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("status").equals("success")) {
                        JSONArray ja = js1.getJSONArray("data");
                        JSONObject jo1 = ja.getJSONObject(0);
                        title1.setText("【" + jo1.getString("title") + "】");
                        StringBuffer sb1 = new StringBuffer();
                        JSONArray ja1 = jo1.getJSONArray("list");
                        for (int i = 0; i < ja1.length(); i++) {
                            sb1.append(ja1.getString(i)).append(" ");
                        }
                        preference1.setText(sb1.toString());
                        if (ja1.length() == 0) {
                            title1.setText("");
                        }
                        JSONObject jo2 = ja.getJSONObject(1);
                        title2.setText("【" + jo2.getString("title") + "】");
                        StringBuffer sb2 = new StringBuffer();
                        JSONArray ja2 = jo2.getJSONArray("list");
                        for (int i = 0; i < ja2.length(); i++) {
                            sb2.append(ja2.getString(i)).append(" ");
                        }
                        preference2.setText(sb2.toString());
                        if (ja2.length() == 0) {
                            title2.setText("");
                        }
                        JSONObject jo3 = ja.getJSONObject(2);
                        title3.setText("【" + jo3.getString("title") + "】");
                        StringBuffer sb3 = new StringBuffer();
                        JSONArray ja3 = jo3.getJSONArray("list");
                        for (int i = 0; i < ja3.length(); i++) {
                            sb3.append(ja3.getString(i)).append(" ");
                        }
                        preference3.setText(sb3.toString());
                        if (ja3.length() == 0) {
                            title3.setText("");
                        }
                        if ((ja1.length() > 0) || (ja2.length() > 0) || (ja3.length() > 0)) {
                            nopreference = false;
                        }
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
        spr.putValue("code", code);
        spr.putValue("language_id", language_id);
        spr.putValue("languages", en_ch);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void initpreference() {
        title1.setText("");
        title2.setText("");
        title3.setText("");
        preference1.setText("");
        preference2.setText("");
        preference3.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            loadingDialog.dismiss();
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    loadfail();
                    Toast.makeText(getBaseContext(), "请手动获取定位权限", Toast.LENGTH_SHORT).show();
                } else {
                    Location location = locationManager.getLastKnownLocation(locationProvider);
                    if (location != null) {
                        customToast.showToast(getBaseContext(), "定位成功");
                        getCountry(location);
                    } else {
                        loadfail();
                    }
                }
            } else {
                loadfail();
                Toast.makeText(getBaseContext(), "请手动获取定位权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    static class LetterComparator implements Comparator {
        public int compare(Object object1, Object object2) {// 实现接口中的方法
            Country p1 = (Country) object1; // 强制转换
            Country p2 = (Country) object2;
            return new String(p1.getEnglish_name()).compareTo(p2.getEnglish_name());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("========restart", "aa");
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        tvlist = new ArrayList<>();
        initData();
        initView();
        initMenu();
        load();
        getLanguageId();
        if ((hasedit && pre.getBoolean("isfirstedit", true))) {

            m_favour_leader.setVisibility(View.VISIBLE);
            m_favour_leader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_favour_leader.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startBeizerAnimation();
                            hasedit = false;
                            floats.setVisibility(View.VISIBLE);
                        }
                    }, 600);
                }
            });

            SharedPreferences.Editor editor = pre.edit();
            editor.putBoolean("isfirstedit", false);
            editor.apply();//提交数据
        }

    }

    public void initJump() {
        jumpurl.setVisibility(View.VISIBLE);
        if (en_ch.equals("cn")) {
            jumpurl.setText("发现素食的美好（附近素食铺）>>");
            url = "https://www.isuhuo.com/plainliving/wxapi/index/index/languages/cn";

        } else {
            jumpurl.setText("To find the beauty of vegetarian diet (vegetarian shop nearby).");
            url = "https://www.isuhuo.com/plainliving/wxapi/index/index/languages/en";
        }
        jumpurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }
}
