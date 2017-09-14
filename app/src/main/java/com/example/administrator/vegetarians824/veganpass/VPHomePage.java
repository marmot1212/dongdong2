package com.example.administrator.vegetarians824.veganpass;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
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
import android.util.TypedValue;
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
import android.widget.SeekBar;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VPHomePage extends AppCompatActivity {
    private FrameLayout content;
    private LoadingDialog loadingDialog;//加载显示框
    private FrameLayout bartop;
    private LinearLayout barbottom;
    private LinearLayout barcenter;
    private Long up = 0L, down = 0L;
    private boolean netflag = false;
    private String type;
    private LinearLayout localfailed;
    private SlidingMenu slidingMenu;
    private SharedPreferences pre;
    private ListView listview;
    private LinearLayout listview2;
    private String mycountry, mylanguage;//选中的国家
    private TextView mycountrytv, typetv;//国家 素食类型
    private List<Country> list;
    private CountryAdapter adapter;
    private TextView local;//定位所在国家
    private CoutryData data;
    private TextView title, contents;
    private PopupWindow popWindow, popFavour;
    private int[] id;//语言资源索引数组
    private int language_num = 0;//当前语言编号
    private GestureDetector gestureDetector;
    private List<Letters> list2;
    private List<TextView> tvlist;
    private ImageView tip1, tip2;
    private SeekBar seekbar;
    private float x, y;
    private FrameLayout rootview;
    private ImageButton floats;
    private TextView favour;
    private static boolean hasedit = false;
    private String language_id;
    private String code = "";
    private TextView title1, title2, title3, preference1, preference2, preference3;

    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止屏幕休眠或锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_home_page);
        StatusBarUtil.setColorDiff(this, 0x8e000000);
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        tvlist = new ArrayList<>();
        initData();
        initView();
        initMenu();
        loadingDialog.setMessage("连接网络...");
        loadingDialog.show();
        new CheckInternet().execute();
        //location();

    }

    public void initView() {
        rootview = (FrameLayout) findViewById(R.id.home_rootview);
        content = (FrameLayout) findViewById(R.id.home_content);
        localfailed = (LinearLayout) findViewById(R.id.home_localfailed);
        mycountrytv = (TextView) findViewById(R.id.home_country);
        typetv = (TextView) findViewById(R.id.home_type);
        title = (TextView) findViewById(R.id.home_title);
        contents = (TextView) findViewById(R.id.home_contents);
        bartop = (FrameLayout) findViewById(R.id.home_bartop);
        barbottom = (LinearLayout) findViewById(R.id.home_barbottom);
        barcenter = (LinearLayout) findViewById(R.id.home_barcenter);
        loadingDialog = new LoadingDialog(this);
        tip1 = (ImageView) findViewById(R.id.tips_click);
        tip2 = (ImageView) findViewById(R.id.tips_move);
        seekbar = (SeekBar) findViewById(R.id.home_seekbar);
        floats = (ImageButton) findViewById(R.id.home_floats);
        favour = (TextView) findViewById(R.id.home_favour);

        if (!pre.getBoolean("isfirstedit", true)) {
            floats.setVisibility(View.VISIBLE);
        }
        initPopWindow();
        initPopFavour();
        //设置国家
        LinearLayout setcountry = (LinearLayout) findViewById(R.id.home_setcountry);
        setcountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomToast.cancel();

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
        //设置类型
        LinearLayout settype = (LinearLayout) findViewById(R.id.home_settype);
        settype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //bartop.setVisibility(View.INVISIBLE);
                // barbottom.setVisibility(View.INVISIBLE);

                CustomToast.cancel();
                if (slidingMenu.isMenuShowing()) {
                    slidingMenu.toggle();
                }
                popWindow.showAtLocation(content, Gravity.RIGHT, 0, 0);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", "enter");
                StatService.onEvent(getBaseContext(), "类型选择", "press2", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "类型选择", "press2");
                StatService.onEventEnd(getBaseContext(), "类型选择", "press2");
            }
        });

        TextView feedback = (TextView) findViewById(R.id.home_fedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomToast.cancel();
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
                        barcenter.setVisibility(View.INVISIBLE);
                        Animation mAlphaAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha);
                        barcenter.startAnimation(mAlphaAnimation);

                    } else {
                        rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        bartop.setVisibility(View.VISIBLE);
                        //barbottom.setVisibility(View.VISIBLE);
                        floats.setVisibility(View.VISIBLE);
                        barcenter.setVisibility(View.VISIBLE);
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
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("===============xxx", (x + progress) + "  " + (y + progress));
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX, x + progress);
                contents.setTextSize(TypedValue.COMPLEX_UNIT_PX, y + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        floats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.cancel();

                if (slidingMenu.isMenuShowing()) {
                    slidingMenu.toggle();
                }
                backgroundAlpha(0.5f);
                popFavour.showAtLocation(content, Gravity.START, 0, 0);
                initpreference();
                getLanguageId();
            }
        });

        favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                hasedit = true;
                CustomToast.cancel();
                Intent intent = new Intent(getBaseContext(), EditFavour.class);
                startActivity(intent);
                //startBeizerAnimation();
            }
        });
    }

    //获取本地存储的数据 初始化数据类
    public void initData() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getRawX() - e1.getRawX() < -80) {
                    CustomToast.cancel();
                    Intent intent = new Intent(getBaseContext(), FavourList.class);
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(VPHomePage.this);
                    startActivity(intent, activityOptionsCompat.toBundle());
                    return true;
                }
                /*
                else {

                    if(e2.getRawX()-e1.getRawX()>80){
                        Intent intent=new Intent(getBaseContext(),LoadMusic.class);
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(VPHomePage.this);
                        VPHomePage.this.startActivity(intent,activityOptionsCompat.toBundle());
                    }

                }
                */

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        type = pre.getString("type", "");
        code = pre.getString("phoneid", "");
        data = new CoutryData();
        for (int i = 0; i < data.getCountryName().length; i++) {
            Country c = new Country();
            c.setName(data.getCountryName()[i]);
            //c.setEnglish_name(data.getCountryENameA2Z()[x][i]);
            list.add(c);
        }
        String ss[] = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "X", "Y", "Z"};
        for (int i = 0; i < ss.length; i++) {
            Letters l = new Letters();
            l.setName(ss[i]);
            l.setIschoose(false);
            list2.add(l);
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
                Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_SHORT).show();
                loadfail();
            }
        }
    }

    public void location() {
        loadingDialog.setMessage("定位中...");
        loadingDialog.show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
            }
        } else {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                loadingDialog.dismiss();
                CustomToast.showToast(getBaseContext(), "定位成功");
                getCountry(location);
            } else {
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
                            local.setText(mycountry);
                            SharedPreferences.Editor editor = pre.edit();
                            editor.putString("country", mycountry);
                            editor.apply();//提交数据
                            load();
                        } else {
                            Toast.makeText(getBaseContext(), "无法解析所在国家", Toast.LENGTH_SHORT).show();
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
        mycountrytv.setText(mycountry);
        typetv.setText(type);
        if (barbottom.getChildCount() > 0) {
            barbottom.removeAllViews();
        }
        getLanguage(mycountry);
        loadContent(language_num);
    }

    public void loadfail() {
        localfailed.setVisibility(View.VISIBLE);
        Button bt1 = (Button) findViewById(R.id.home_op1);
        Button bt2 = (Button) findViewById(R.id.home_op2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.setMessage("连接网络...");
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
        View view = getLayoutInflater().inflate(R.layout.menu, null);
        local = (TextView) view.findViewById(R.id.localtv);
        local.setText(pre.getString("country", ""));
        listview = (ListView) view.findViewById(R.id.countrylist);
        local = (TextView) view.findViewById(R.id.localtv);
        //letter=(TextView) view.findViewById(R.id.letter);
        local.setText(pre.getString("country", ""));
        LinearLayout localline = (LinearLayout) view.findViewById(R.id.localline);
        localline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mycountry = local.getText().toString();
                localfailed.setVisibility(View.INVISIBLE);
                load();
                slidingMenu.showContent();
            }
        });
        listview = (ListView) view.findViewById(R.id.countrylist);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mycountry = list.get(i).getName();
                local.setText(mycountry);
                localfailed.setVisibility(View.INVISIBLE);
                language_num = 0;
                load();
                slidingMenu.showContent();

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
                Log.d("==========sel", "ss" + firstVisibleItem + " " + visibleItemCount + " " + totalItemCount);
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

            }
        });

        getCountryList(0);


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

    public void getCountryList(int x) {
        adapter = new CountryAdapter(list, VPHomePage.this);
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
            String english_name = getResources().getStringArray(id[x])[6];
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
                    CustomToast.showToast(getBaseContext(), "切换至" + language_name);
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
        }
    }

    public void initPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.pop_style, null);
        final Button bt1 = (Button) popView.findViewById(R.id.style_bt1);
        final Button bt2 = (Button) popView.findViewById(R.id.style_bt2);
        final Button bt3 = (Button) popView.findViewById(R.id.style_bt3);
        Drawable drawable = getResources().getDrawable(R.drawable.select);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        if (bt1.getText().toString().equals(type)) {
            bt1.setCompoundDrawables(null, null, drawable, null);
        }
        if (bt2.getText().toString().equals(type)) {
            bt2.setCompoundDrawables(null, null, drawable, null);
        }
        if (bt3.getText().toString().equals(type)) {
            bt3.setCompoundDrawables(null, null, drawable, null);
        }
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearbutton(bt1, bt2, bt3);
                Drawable drawable = getResources().getDrawable(R.drawable.select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                }
                bt1.setCompoundDrawables(null, null, drawable, null);
                type = bt1.getText().toString();
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("type", type);
                editor.apply();//提交数据
                load();
                popWindow.dismiss();
                CustomToast.showToast(getBaseContext(), "我爱【" + type + "】");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", type);
                StatService.onEvent(getBaseContext(), "用户类型", "press4", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "用户类型", "press4");
                StatService.onEventEnd(getBaseContext(), "用户类型", "press4");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearbutton(bt1, bt2, bt3);
                Drawable drawable = getResources().getDrawable(R.drawable.select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                }
                bt2.setCompoundDrawables(null, null, drawable, null);
                type = bt2.getText().toString();
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("type", type);
                editor.apply();//提交数据
                load();
                popWindow.dismiss();
                CustomToast.showToast(getBaseContext(), "我爱【" + type + "】");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", type);
                StatService.onEvent(getBaseContext(), "用户类型", "press4", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "用户类型", "press4");
                StatService.onEventEnd(getBaseContext(), "用户类型", "press4");
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearbutton(bt1, bt2, bt3);
                Drawable drawable = getResources().getDrawable(R.drawable.select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                }
                bt3.setCompoundDrawables(null, null, drawable, null);
                type = bt3.getText().toString();
                SharedPreferences.Editor editor = pre.edit();
                editor.putString("type", type);
                editor.apply();//提交数据
                load();
                popWindow.dismiss();
                CustomToast.showToast(getBaseContext(), "我爱【" + type + "】");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", type);
                StatService.onEvent(getBaseContext(), "用户类型", "press4", 1, hashMap);
                StatService.onEventStart(getBaseContext(), "用户类型", "press4");
                StatService.onEventEnd(getBaseContext(), "用户类型", "press4");
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style3);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());

    }

    public void initPopFavour() {
        View popView = getLayoutInflater().inflate(R.layout.pop_favour, null);
        title1 = (TextView) popView.findViewById(R.id.title1);
        title2 = (TextView) popView.findViewById(R.id.title2);
        title3 = (TextView) popView.findViewById(R.id.title3);
        preference1 = (TextView) popView.findViewById(R.id.preference1);
        preference2 = (TextView) popView.findViewById(R.id.preference2);
        preference3 = (TextView) popView.findViewById(R.id.preference3);
        popFavour = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.MATCH_PARENT);
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

    public void clearbutton(View... views) {
        for (View view : views) {
            ((Button) view).setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bartop.getVisibility() == View.INVISIBLE) {
            rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            rootview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        if (hasedit && pre.getBoolean("isfirstedit", true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startBeizerAnimation();
                    hasedit = false;
                    floats.setVisibility(View.VISIBLE);
                }
            }, 600);

            SharedPreferences.Editor editor = pre.edit();
            editor.putBoolean("isfirstedit", false);
            editor.apply();//提交数据

        }

        StatService.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
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

                        JSONObject jo2 = ja.getJSONObject(1);
                        title2.setText("【" + jo2.getString("title") + "】");
                        StringBuffer sb2 = new StringBuffer();
                        JSONArray ja2 = jo2.getJSONArray("list");
                        for (int i = 0; i < ja2.length(); i++) {
                            sb2.append(ja2.getString(i)).append(" ");
                        }
                        preference2.setText(sb2.toString());

                        JSONObject jo3 = ja.getJSONObject(2);
                        title3.setText("【" + jo3.getString("title") + "】");
                        StringBuffer sb3 = new StringBuffer();
                        JSONArray ja3 = jo3.getJSONArray("list");
                        for (int i = 0; i < ja3.length(); i++) {
                            sb3.append(ja3.getString(i)).append(" ");
                        }
                        preference3.setText(sb3.toString());

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
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if(location!=null){
                    loadingDialog.dismiss();
                    CustomToast.showToast(getBaseContext(),"定位成功");
                    getCountry(location);
                }else {
                    loadingDialog.dismiss();
                    loadfail();
                }
            } else {
                loadingDialog.dismiss();
                loadfail();
                Toast.makeText(getBaseContext(), "请手动获取定位权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

}
