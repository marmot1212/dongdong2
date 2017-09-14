package com.example.administrator.vegetarians824.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.MapActivity;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.Subway;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchRestaurant extends AppCompatActivity {
    private EditText et;
    private ListView listView;
    private TextView cancel;
    private List<CantingInfo> list_search;
    private String keywords="";
    private LoadingDialog loadingDialog;
    private int flags=0;// 1 联想结果 2 搜索结果
    private String order="";
    private String food_type="";
    private String distance="";
    private String longitude,latitude;
    private LinearLayout line1,line2,line3,line4;
    private TextView food_type_tv,ordertv,near_type_tv;
    private PopupWindow pop1,pop2,pop3;
    private ListView list1,list2;
    private MySelectAdapter adapter,adapter2;
    private List<Subway> listsubway;
    private List<Subway> liststation;
    private LinearLayout searchbar;
    private TextView none;
    private String subway_status="2";
    private TextView foot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurant);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        loadingDialog=new LoadingDialog(this);
        if(BaseApplication.app.getMyLociation()!=null){
            longitude=BaseApplication.app.getMyLociation().getLongitude();
            latitude=BaseApplication.app.getMyLociation().getLatitude();
            foot = new TextView(getBaseContext());
            foot.setTextSize(12);
            foot.setTextColor(0xffa0a0a0);
            AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
            foot.setLayoutParams(params2);
            foot.setGravity(Gravity.CENTER);
            initPop1();
            initPop2();
            initPop3();
            order="distance,asc";
            initView();
        }

    }
    public void initView(){
        none=(TextView)findViewById(R.id.search_restaurant_none);
        et=(EditText)findViewById(R.id.search_restaurant_et);
        listView=(ListView)findViewById(R.id.search_restaurant_list);
        cancel=(TextView)findViewById(R.id.search_restaurant_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                distance="";
                longitude=BaseApplication.app.getMyLociation().getLongitude();
                latitude=BaseApplication.app.getMyLociation().getLatitude();

                if(!s.toString().equals("")){
                    keywords=s.toString();
                    //1 联想 2 结果
                    flags=1;
                    initSearchList();
                }else {
                    flags=1;
                    searchbar.setVisibility(View.GONE);
                    list_search=new ArrayList<>();
                    listView.setAdapter(new SearchResultAdapter(list_search,getBaseContext()));
                }
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    loadingDialog.show();
                    flags=2;
                    initSearchList();
                    return true;
                }
                return false;
            }
        });

        line1=(LinearLayout) findViewById(R.id.home_module1_menu1);
        line2=(LinearLayout) findViewById(R.id.home_module1_menu2);
        line3=(LinearLayout) findViewById(R.id.home_module1_menu3);
        line4=(LinearLayout) findViewById(R.id.home_module1_menu4);

        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop1.showAsDropDown(line1);
            }
        });
        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop2.showAsDropDown(line2);
            }
        });
        line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop3.showAsDropDown(line3);
            }
        });
        line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MapActivity.class));
            }
        });
        food_type_tv=(TextView)findViewById(R.id.food_type_tv);
        ordertv=(TextView)findViewById(R.id.order_tv);
        near_type_tv=(TextView)findViewById(R.id.near_type_tv);
        searchbar=(LinearLayout)findViewById(R.id.search_result_bar);
    }

    public void initSearchList(){
        none.setVisibility(View.GONE);
        //flags 1 联想 2 结果
        if(flags==1){
            searchbar.setVisibility(View.GONE);
        }else {
            searchbar.setVisibility(View.VISIBLE);
        }
        list_search=new ArrayList<>();
        StringPostRequest spr=new StringPostRequest(URLMannager.GetRestList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                if(listView.getFooterViewsCount()>0){
                    listView.removeFooterView(foot);
                }
                Log.d("============list",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja1=js2.getJSONArray("subway_list");
                        JSONArray ja2=js2.getJSONArray("restaurant_list");
                        if(ja2.length()==0&&ja1.length()==0){
                            none.setVisibility(View.VISIBLE);
                        }
                        for(int i=0;i<ja1.length();i++){
                            JSONObject jo1=ja1.getJSONObject(i);
                            CantingInfo sr=new CantingInfo();
                            sr.setId(jo1.getString("id"));
                            sr.setTitle(jo1.getString("name"));
                            sr.setDistance(jo1.getString("distance"));
                            sr.setLatitude(jo1.getString("latitude"));
                            sr.setLongitude(jo1.getString("longitude"));
                            sr.setType("2");
                            sr.setSubway_status(subway_status);
                            list_search.add(sr);
                        }
                        for(int i=0;i<ja2.length();i++){
                            JSONObject jo2=ja2.getJSONObject(i);
                            CantingInfo sr=new CantingInfo();
                            sr.setId(jo2.getString("id"));
                            sr.setTitle(jo2.getString("title"));
                            sr.setDistance(jo2.getString("distance"));
                            sr.setUnit_pric(jo2.getString("unit_price"));
                            sr.setImg_url_th_1(jo2.getString("img_url"));
                            sr.setParking(jo2.getString("parking_status"));
                            sr.setContent(jo2.getString("content"));
                            sr.setType("1");
                            sr.setSubway_status(subway_status);
                            list_search.add(sr);
                        }
                        if(flags==1) {
                            listView.setAdapter(new SearchResultAdapter(list_search, getBaseContext()));
                        }else {
                            listView.setAdapter(new SearchResultAdapter2(list_search, getBaseContext()));
                            if(list_search.size()>0) {
                                foot.setText("已经全部加载完毕");
                                listView.addFooterView(foot);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                Toast.makeText(getBaseContext(),"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
        spr.putValue("keywords",keywords);
        spr.putValue("longitudes", BaseApplication.app.getMyLociation().getLongitude());
        spr.putValue("latitudes", BaseApplication.app.getMyLociation().getLatitude());
        spr.putValue("city",BaseApplication.app.getMyLociation().getMyCity());
        spr.putValue("order",order);
        spr.putValue("food_type",food_type);
        spr.putValue("distance",distance);
        spr.putValue("longitude",longitude);
        spr.putValue("latitude",latitude);
        //Log.d("==========value",longitude+" "+latitude+" "+BaseApplication.app.getMyLociation().getMyCity()+" "+keywords+" "+distance);
        if(subway_status.equals("1")) {
            spr.putValue("subway_status", subway_status);
        }
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public class SearchResultAdapter extends BaseAdapter{
        private List<CantingInfo> mydata;
        private Context context;
        public SearchResultAdapter( List<CantingInfo> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(context).inflate(R.layout.search_item01,null);
            TextView tv1=(TextView)convertView.findViewById(R.id.search_item_tv1);
            TextView tv2=(TextView)convertView.findViewById(R.id.search_item_tv2);
            if(mydata.get(position).getType().equals("2")){
                tv1.setText(mydata.get(position).getTitle()+"（地铁站）");
            }else {
                tv1.setText(mydata.get(position).getTitle());
            }
            tv2.setText("距您"+mydata.get(position).getDistance()+"米");
            final int x=position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mydata.get(x).getType().equals("1")){
                        Intent intent=new Intent(getBaseContext(), CantingDetail.class);
                        intent.putExtra("item_id",mydata.get(x).getId());
                        startActivity(intent);
                    }else {
                        keywords="";
                        longitude=mydata.get(x).getLongitude();
                        latitude=mydata.get(x).getLatitude();
                        loadingDialog.show();
                        flags=2;
                        initSearchList();
                    }
                }
            });
            return convertView;
        }
    }

    public class SearchResultAdapter2 extends BaseAdapter{
        private List<CantingInfo> mydata;
        private Context context;
        public SearchResultAdapter2( List<CantingInfo> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.canting_item2, null);
            TextView title=(TextView)convertView.findViewById(R.id.canting_item2_title);
            title.setText(mydata.get(position).getTitle());
            final int x=position;
            if(mydata.get(position).getType().equals("1")){
                ImageView imageView=(ImageView) convertView.findViewById(R.id.canting_item2_imageView);
                com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
                DisplayImageOptions options=ImageLoaderUtils.getOpt();
                loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getImg_url_th_1(),imageView,options);
                TextView parking=(TextView)convertView.findViewById(R.id.canting_item2_park);
                /*
                if(mydata.get(position).getParking().equals("1")){
                    parking.setText("有停车位");
                }else {
                    if(mydata.get(position).getParking().equals("2")){
                        parking.setText("无停车位");
                    }
                }
                */
                parking.setText(mydata.get(position).getContent());

                TextView distance=(TextView)convertView.findViewById(R.id.canting_item2_local);
                if(mydata.get(position).getSubway_status().equals("1")) {
                    distance.setText("距地铁站" + mydata.get(position).getDistance() + "米");
                }else {
                    distance.setText("距您" + mydata.get(position).getDistance() + "米");
                }
                TextView price=(TextView)convertView.findViewById(R.id.canting_item2_price);
                price.setText("¥"+mydata.get(position).getUnit_pric());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getBaseContext(), CantingDetail.class);
                        intent.putExtra("item_id",mydata.get(x).getId());
                        startActivity(intent);
                    }
                });
            }else {
                TextView price=(TextView)convertView.findViewById(R.id.canting_item2_price);
                price.setText("点击查看周边素餐厅");
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keywords="";
                        longitude=mydata.get(x).getLongitude();
                        latitude=mydata.get(x).getLatitude();
                        loadingDialog.show();
                        flags=2;
                        initSearchList();
                    }
                });
                ImageView imageView=(ImageView) convertView.findViewById(R.id.canting_item2_imageView);
            }
            return convertView;
        }
    }

    public void initPop1(){
        View popView1 =LayoutInflater.from(getBaseContext()).inflate(R.layout.pop_select01,null);
        final TextView tv1=(TextView)popView1.findViewById(R.id.home_module1_tv1);
        final TextView tv2=(TextView)popView1.findViewById(R.id.home_module1_tv2);
        final View bar1=popView1.findViewById(R.id.home_module1_bar1);
        final View bar2=popView1.findViewById(R.id.home_module1_bar2);
        list1=(ListView)popView1.findViewById(R.id.home_module1_list1);
        list2=(ListView)popView1.findViewById(R.id.home_module1_list2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(0xffff5e5e);
                bar1.setVisibility(View.VISIBLE);
                tv2.setTextColor(0xff3e3e3e);
                bar2.setVisibility(View.INVISIBLE);
                getNearly();
                adapter2=new MySelectAdapter(new ArrayList<Subway>(),getBaseContext());
                list2.setAdapter(adapter2);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setTextColor(0xffff5e5e);
                bar2.setVisibility(View.VISIBLE);
                tv1.setTextColor(0xff3e3e3e);
                bar1.setVisibility(View.INVISIBLE);
                loadingDialog.show();
                getSubway();
            }
        });

        getNearly();
        pop1= new PopupWindow(popView1, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pop1.setFocusable(true);
        pop1.setOutsideTouchable(true);
        pop1.setBackgroundDrawable(new ColorDrawable());
    }
    public void initPop2(){
        View popView2 =LayoutInflater.from(getBaseContext()).inflate(R.layout.pop_select02,null);
        TextView type0=(TextView)popView2.findViewById(R.id.food_type_00);
        TextView type1=(TextView)popView2.findViewById(R.id.food_type_01);
        TextView type2=(TextView)popView2.findViewById(R.id.food_type_02);
        TextView type3=(TextView)popView2.findViewById(R.id.food_type_03);
        type0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type="";
                food_type_tv.setText("全部");
                flags=2;
                initSearchList();
                pop2.dismiss();
            }
        });
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type="1";
                food_type_tv.setText("纯素");
                flags=2;
                initSearchList();
                pop2.dismiss();
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type="2";
                food_type_tv.setText("蛋奶素");
                flags=2;
                initSearchList();
                pop2.dismiss();
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type="3";
                food_type_tv.setText("净素");
                flags=2;
                initSearchList();
                pop2.dismiss();
            }
        });
        pop2= new PopupWindow(popView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop2.setFocusable(true);
        pop2.setOutsideTouchable(true);
        pop2.setBackgroundDrawable(new ColorDrawable());
    }
    public void initPop3(){
        View popView3 =LayoutInflater.from(getBaseContext()).inflate(R.layout.pop_select03,null);
        TextView order1=(TextView)popView3.findViewById(R.id.order_tv_01);
        TextView order2=(TextView)popView3.findViewById(R.id.order_tv_02);
        TextView order3=(TextView)popView3.findViewById(R.id.order_tv_03);
        order1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order="distance,asc";
                ordertv.setText("距离优先");
                flags=2;
                initSearchList();
                pop3.dismiss();
            }
        });
        order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order="unit_price,asc";
                ordertv.setText("均价从低到高");
                flags=2;
                initSearchList();
                pop3.dismiss();
            }
        });
        order3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order="unit_price,desc";
                ordertv.setText("均价从高到低");
                flags=2;
                initSearchList();
                pop3.dismiss();
            }
        });
        pop3= new PopupWindow(popView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop3.setFocusable(true);
        pop3.setOutsideTouchable(true);
        pop3.setBackgroundDrawable(new ColorDrawable());
    }
    public void getSubway(){
        listsubway=new ArrayList<>();
        StringPostRequest spr=new StringPostRequest(URLMannager.GetSubway, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("==========subway",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONArray ja=js1.getJSONArray("Result");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Subway subway=new Subway();
                            subway.setId(jo.getString("id"));
                            subway.setName(jo.getString("name"));
                            listsubway.add(subway);
                        }
                        listsubway.get(0).setIschoose(true);
                        adapter=new MySelectAdapter(listsubway,getBaseContext());
                        list1.setAdapter(adapter);
                        getSubwayStation(listsubway.get(0).getId());
                        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for(int i=0;i<listsubway.size();i++){
                                    listsubway.get(i).setIschoose(false);
                                }
                                listsubway.get(position).setIschoose(true);
                                adapter.notifyDataSetChanged();
                                getSubwayStation(listsubway.get(position).getId());
                            }
                        });

                    }else {
                        Subway subway=new Subway();
                        subway.setName(js1.getString("Message"));
                        listsubway.add(subway);
                        adapter=new MySelectAdapter(listsubway,getBaseContext());
                        list1.setAdapter(adapter);
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
        spr.putValue("city",BaseApplication.app.getMyLociation().getMyCity());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
    public void getNearly(){
        listsubway=new ArrayList<>();
        Subway subway;
        subway=new Subway();
        subway.setName("附近（智能范围）");
        listsubway.add(subway);
        subway=new Subway();
        subway.setName("500米");
        listsubway.add(subway);
        subway=new Subway();
        subway.setName("1000米");
        listsubway.add(subway);
        subway=new Subway();
        subway.setName("2000米");
        listsubway.add(subway);
        subway=new Subway();
        subway.setName("5000米");
        listsubway.add(subway);
        adapter=new MySelectAdapter(listsubway,getBaseContext());
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:distance="";break;
                    case 1:distance="500";break;
                    case 2:distance="1000";break;
                    case 3:distance="2000";break;
                    case 4:distance="5000";break;
                    default:break;
                }
                pop1.dismiss();
                //subway_status="";
                //longitude=BaseApplication.app.getMyLociation().getLongitude();
                //latitude=BaseApplication.app.getMyLociation().getLatitude();
                flags=2;
                initSearchList();
                if(position==0){
                    near_type_tv.setText("附近");
                }else {
                    near_type_tv.setText(listsubway.get(position).getName());
                }
                for(int i=0;i<listsubway.size();i++){
                    listsubway.get(i).setIschoose(false);
                }
                listsubway.get(position).setIschoose(true);
            }
        });
    }
    public void getSubwayStation(String id){
        liststation=new ArrayList<>();
        StringPostRequest spr=new StringPostRequest(URLMannager.GetSubwayStation, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONArray ja=js1.getJSONArray("Result");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Subway sw=new Subway();
                            sw.setId(jo.getString("id"));
                            sw.setName(jo.getString("name"));
                            sw.setLongitude(jo.getString("longitude"));
                            sw.setLatitude(jo.getString("latitude"));
                            liststation.add(sw);
                        }
                        adapter2=new MySelectAdapter(liststation,getBaseContext());
                        list2.setAdapter(adapter2);
                        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                longitude=liststation.get(position).getLongitude();
                                latitude=liststation.get(position).getLatitude();
                                pop1.dismiss();
                                flags=2;
                                subway_status="1";
                                distance="";
                                initSearchList();
                                near_type_tv.setText(liststation.get(position).getName());
                                for(int i=0;i<liststation.size();i++){
                                    liststation.get(i).setIschoose(false);
                                }
                                liststation.get(position).setIschoose(true);
                                adapter2.notifyDataSetChanged();
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
        spr.putValue("subway_id",id);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
    public class MySelectAdapter extends BaseAdapter{
        private List<Subway> mydata;
        private Context context;
        public MySelectAdapter(List<Subway> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_select1_item,null);
            FrameLayout fram=(FrameLayout)convertView.findViewById(R.id.select1_fram);
            TextView tv=(TextView)convertView.findViewById(R.id.select1_tv);
            tv.setText(mydata.get(position).getName());
            if(mydata.get(position).ischoose()){
                fram.setBackgroundColor(0xffffffff);
                tv.setTextColor(0xffff5e5e);
            }
            return convertView;
        }
    }

}
