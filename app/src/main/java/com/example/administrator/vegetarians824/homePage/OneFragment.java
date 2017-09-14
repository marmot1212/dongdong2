package com.example.administrator.vegetarians824.homePage;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter;
import com.example.administrator.vegetarians824.adapter.CantingListAdapter2;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.Subway;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.CyclePager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {

    @Bind(R.id.nearby_list)
    PullToRefreshListView mPullToRefresh_nearbyList;
    @Bind(R.id.fab2)
    FloatingActionButton mFab;

    private List<CantingInfo> list_info;
    private LinearLayout line1, line2, line3, line4;
//    private PullToRefreshListView list;
    private LoadingDialog loadingDialog;
    private ListView list1, list2;
    private List<Subway> listsubway;
    private List<Subway> liststation;
    private PopupWindow pop1, pop2, pop3, pop4;
    private String longitude, latitude;
    private MySelectAdapter adapter, adapter2;
    private TextView foot;
    private String order = "";
    private String food_type = "";
    private String distance = "";
    private String vege_status = "";
    private TextView food_type_tv, ordertv, near_type_tv, vege_status_tv;
    private String subway_status = "2";
    private LinearLayout qiu;
    private List<Caidan> listcandan;

    public OneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);
        initListener();


        loadingDialog = new LoadingDialog(getActivity());
        if (BaseApplication.app.getMyLociation() != null && getContext() != null) {
            longitude = BaseApplication.app.getMyLociation().getLongitude();
            latitude = BaseApplication.app.getMyLociation().getLatitude();
            foot = new TextView(getContext());
            foot.setTextSize(12);
            foot.setTextColor(0xffa0a0a0);
            AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
            foot.setLayoutParams(params2);
            foot.setGravity(Gravity.CENTER);
            initView();
            initPop1();
            initPop2();
            initPop3();
            initPop4();
            order = "distance,asc";
            initdata();
        }

        ButterKnife.bind(this, v);
        return v;
    }

    private void initListener() {
        /**
         * 跳转地图
         */
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoMapActivity(getActivity());
// 删除               startActivity(new Intent(getContext(), MapActivity.class));
            }
        });

        mPullToRefresh_nearbyList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new DataRefresh().execute();
            }
        });
    }

    public void initView() {
        View head = LayoutInflater.from(getContext()).inflate(R.layout.head_home1, null);
        qiu = (LinearLayout) head.findViewById(R.id.shipu_qiu);
        line1 = (LinearLayout) head.findViewById(R.id.home_module1_menu1);
        line2 = (LinearLayout) head.findViewById(R.id.home_module1_menu2);
        line3 = (LinearLayout) head.findViewById(R.id.home_module1_menu3);
        line4 = (LinearLayout) head.findViewById(R.id.home_module1_menu4);

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
                pop4.showAsDropDown(line3);
            }
        });
        food_type_tv = (TextView) head.findViewById(R.id.food_type_tv);
        ordertv = (TextView) head.findViewById(R.id.order_tv);
        near_type_tv = (TextView) head.findViewById(R.id.near_type_tv);
        vege_status_tv = (TextView) head.findViewById(R.id.vegatype_tv);
        LinearLayout group = (LinearLayout) head.findViewById(R.id.ditu_xiangqing_viewGroup2);// 展示小圆点
        ViewPager advPager = (ViewPager) head.findViewById(R.id.ditu_xiangqing_viewpager2);// ViewPager
        CyclePager cyclePager = new CyclePager(advPager, group, getContext());
        cyclePager.init("1");
        mPullToRefresh_nearbyList.getRefreshableView().addHeaderView(head);
    }

    public void initdata() {
        qiu.setVisibility(View.GONE);
        loadingDialog.show();
        if (mPullToRefresh_nearbyList.getRefreshableView().getFooterViewsCount() > 0) {
            mPullToRefresh_nearbyList.getRefreshableView().removeFooterView(foot);
        }
        mPullToRefresh_nearbyList.setMode(PullToRefreshBase.Mode.BOTH);
        list_info = new ArrayList<>();
        listcandan = new ArrayList<>();
        StringPostRequest spr = new StringPostRequest(URLMannager.GetRestList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("==========s", s);
                    JSONObject jsonObj1 = new JSONObject(s);
                    if (jsonObj1.getString("Code").equals("1")) {
                        //请求成功
                        JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                        JSONArray array1 = jsonObj2.getJSONArray("restaurant_list");
                        if (array1.length() > 0) {
                            for (int i = 0; i < array1.length(); i++) {
                                JSONObject array1_2 = array1.getJSONObject(i);
                                CantingInfo cantingInfo = new CantingInfo();
                                cantingInfo.setImg_url_th_1(array1_2.getString("img_url"));
                                cantingInfo.setId(array1_2.getString("id"));
                                cantingInfo.setTitle(array1_2.getString("title"));
                                cantingInfo.setUnit_pric(array1_2.getString("unit_price"));
                                cantingInfo.setParking(array1_2.getString("parking_status"));
                                cantingInfo.setDistance(array1_2.getString("distance"));
                                cantingInfo.setSubway_status(subway_status);
                                cantingInfo.setContent(array1_2.getString("content"));
                                cantingInfo.setVege_status(array1_2.getString("vege_status"));
                                if (array1_2.has("vege_lv")) {
                                    cantingInfo.setVege_lv(array1_2.getString("vege_lv"));
                                }
                                list_info.add(cantingInfo);
                            }
                            mPullToRefresh_nearbyList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            foot.setText("已经全部加载完毕");
                            mPullToRefresh_nearbyList.getRefreshableView().addFooterView(foot);
                            mPullToRefresh_nearbyList.setAdapter(new CantingListAdapter2(list_info, getContext()));
                        } else {
                            //foot.setText("找不到商户信息");
                            //list.getRefreshableView().addFooterView(foot);
                            qiu.setVisibility(View.VISIBLE);
                            JSONArray ja2 = jsonObj2.getJSONArray("like_list");
                            for (int i = 0; i < ja2.length(); i++) {
                                JSONObject jo = ja2.getJSONObject(i);
                                Caidan cd = new Caidan();
                                cd.setId(jo.getString("id"));
                                cd.setTitle(jo.getString("title"));
                                cd.setContent(jo.getString("content"));
                                cd.setPic(jo.getString("img_url"));
                                cd.setType(jo.getString("video_status"));
                                listcandan.add(cd);
                            }
                            mPullToRefresh_nearbyList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            foot.setText("已经全部加载完毕");
                            mPullToRefresh_nearbyList.getRefreshableView().addFooterView(foot);
                            mPullToRefresh_nearbyList.setAdapter(new CaidanAdapter(listcandan, getActivity()));
                        }

                        /*
                        if(jsonObj2.has("like_list")){
                            if(list.getRefreshableView().getFooterViewsCount()>0){
                                list.getRefreshableView().removeFooterView(foot);
                            }
                            qiu.setVisibility(View.VISIBLE);
                            JSONArray ja2=jsonObj2.getJSONArray("like_list");
                            for(int i=0;i<ja2.length();i++){
                                JSONObject array1_2 = ja2.getJSONObject(i);
                                CantingInfo cantingInfo = new CantingInfo();
                                cantingInfo.setImg_url_th_1(array1_2.getString("img_url"));
                                cantingInfo.setId(array1_2.getString("id"));
                                cantingInfo.setTitle(array1_2.getString("title"));
                                cantingInfo.setUnit_pric(array1_2.getString("unit_price"));
                                cantingInfo.setParking(array1_2.getString("parking_status"));
                                cantingInfo.setDistance("");
                                cantingInfo.setSubway_status(subway_status);
                                cantingInfo.setContent(array1_2.getString("content"));
                                list_info.add(cantingInfo);
                            }
                        }
                        */

                    }
                    loadingDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.dismiss();
                foot.setText("网络错误");
                mPullToRefresh_nearbyList.getRefreshableView().addFooterView(foot);
            }
        });
        spr.putValue("longitude", longitude);
        spr.putValue("latitude", latitude);
        spr.putValue("longitudes", BaseApplication.app.getMyLociation().getLongitude());
        spr.putValue("latitudes", BaseApplication.app.getMyLociation().getLatitude());
        spr.putValue("city", BaseApplication.app.getMyLociation().getMyCity());
        spr.putValue("order", order);
        spr.putValue("food_type", food_type);
        spr.putValue("distance", distance);
        spr.putValue("subway_status", subway_status);
        spr.putValue("vege_status", vege_status);
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void initPop1() {
        View popView1 = LayoutInflater.from(getContext()).inflate(R.layout.pop_select01, null);
        final TextView tv1 = (TextView) popView1.findViewById(R.id.home_module1_tv1);
        final TextView tv2 = (TextView) popView1.findViewById(R.id.home_module1_tv2);
        final View bar1 = popView1.findViewById(R.id.home_module1_bar1);
        final View bar2 = popView1.findViewById(R.id.home_module1_bar2);
        list1 = (ListView) popView1.findViewById(R.id.home_module1_list1);
        list2 = (ListView) popView1.findViewById(R.id.home_module1_list2);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setTextColor(0xffff5e5e);
                bar1.setVisibility(View.VISIBLE);
                tv2.setTextColor(0xff3e3e3e);
                bar2.setVisibility(View.INVISIBLE);
                getNearly();
                adapter2 = new MySelectAdapter(new ArrayList<Subway>(), getContext());
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
        pop1 = new PopupWindow(popView1, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        pop1.setFocusable(true);
        pop1.setOutsideTouchable(true);
        pop1.setBackgroundDrawable(new ColorDrawable());
    }

    public void initPop2() {
        View popView2 = LayoutInflater.from(getContext()).inflate(R.layout.pop_select02, null);
        TextView type0 = (TextView) popView2.findViewById(R.id.food_type_00);
        TextView type1 = (TextView) popView2.findViewById(R.id.food_type_01);
        TextView type2 = (TextView) popView2.findViewById(R.id.food_type_02);
        TextView type3 = (TextView) popView2.findViewById(R.id.food_type_03);
        type0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type = "";
                food_type_tv.setText("全部");
                initdata();
                pop2.dismiss();
            }
        });
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type = "1";
                food_type_tv.setText("纯素");
                initdata();
                pop2.dismiss();
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type = "2";
                food_type_tv.setText("蛋奶素");
                initdata();
                pop2.dismiss();
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_type = "3";
                food_type_tv.setText("净素");
                initdata();
                pop2.dismiss();
            }
        });
        pop2 = new PopupWindow(popView2, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop2.setFocusable(true);
        pop2.setOutsideTouchable(true);
        pop2.setBackgroundDrawable(new ColorDrawable());
    }

    public void initPop3() {
        View popView3 = LayoutInflater.from(getContext()).inflate(R.layout.pop_select03, null);
        TextView order1 = (TextView) popView3.findViewById(R.id.order_tv_01);
        TextView order2 = (TextView) popView3.findViewById(R.id.order_tv_02);
        TextView order3 = (TextView) popView3.findViewById(R.id.order_tv_03);
        order1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = "distance,asc";
                ordertv.setText("距离优先");
                initdata();
                pop3.dismiss();
            }
        });
        order2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = "unit_price,asc";
                ordertv.setText("均价从低到高");
                initdata();
                pop3.dismiss();
            }
        });
        order3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = "unit_price,desc";
                ordertv.setText("均价从高到低");
                initdata();
                pop3.dismiss();
            }
        });
        pop3 = new PopupWindow(popView3, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop3.setFocusable(true);
        pop3.setOutsideTouchable(true);
        pop3.setBackgroundDrawable(new ColorDrawable());
    }

    public void initPop4() {
        View popView4 = LayoutInflater.from(getContext()).inflate(R.layout.pop_select05, null);
        TextView type1 = (TextView) popView4.findViewById(R.id.vegantype_tv_01);
        TextView type2 = (TextView) popView4.findViewById(R.id.vegantype_tv_02);
        TextView type3 = (TextView) popView4.findViewById(R.id.vegantype_tv_03);
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vege_status = "";
                vege_status_tv.setText("全部");
                initdata();
                pop4.dismiss();
            }
        });
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vege_status = "2";
                vege_status_tv.setText("素食友好餐厅");
                initdata();
                pop4.dismiss();
            }
        });
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vege_status = "1";
                vege_status_tv.setText("素餐厅");
                initdata();
                pop4.dismiss();
            }
        });
        pop4 = new PopupWindow(popView4, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        pop4.setFocusable(true);
        pop4.setOutsideTouchable(true);
        pop4.setBackgroundDrawable(new ColorDrawable());
    }

    public void getSubway() {
        listsubway = new ArrayList<>();
        StringPostRequest spr = new StringPostRequest(URLMannager.GetSubway, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("==========subway", s);
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("Code").equals("1")) {
                        JSONArray ja = js1.getJSONArray("Result");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            Subway subway = new Subway();
                            subway.setId(jo.getString("id"));
                            subway.setName(jo.getString("name"));
                            listsubway.add(subway);
                        }
                        listsubway.get(0).setIschoose(true);
                        adapter = new MySelectAdapter(listsubway, getContext());
                        list1.setAdapter(adapter);
                        getSubwayStation(listsubway.get(0).getId());
                        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (int i = 0; i < listsubway.size(); i++) {
                                    listsubway.get(i).setIschoose(false);
                                }
                                listsubway.get(position).setIschoose(true);
                                adapter.notifyDataSetChanged();
                                getSubwayStation(listsubway.get(position).getId());
                            }
                        });

                    } else {
                        Subway subway = new Subway();
                        subway.setName(js1.getString("Message"));
                        listsubway.add(subway);
                        adapter = new MySelectAdapter(listsubway, getContext());
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
        spr.putValue("city", BaseApplication.app.getMyLociation().getMyCity());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void getNearly() {
        listsubway = new ArrayList<>();
        Subway subway;
        subway = new Subway();
        subway.setName("附近（智能范围）");
        listsubway.add(subway);
        subway = new Subway();
        subway.setName("500米");
        listsubway.add(subway);
        subway = new Subway();
        subway.setName("1000米");
        listsubway.add(subway);
        subway = new Subway();
        subway.setName("2000米");
        listsubway.add(subway);
        subway = new Subway();
        subway.setName("5000米");
        listsubway.add(subway);
        adapter = new MySelectAdapter(listsubway, getContext());
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        distance = "";
                        break;
                    case 1:
                        distance = "500";
                        break;
                    case 2:
                        distance = "1000";
                        break;
                    case 3:
                        distance = "2000";
                        break;
                    case 4:
                        distance = "5000";
                        break;
                    default:
                        break;
                }
                pop1.dismiss();
                //subway_status="";
                //longitude=BaseApplication.app.getMyLociation().getLongitude();
                //latitude=BaseApplication.app.getMyLociation().getLatitude();
                initdata();
                if (position == 0) {
                    near_type_tv.setText("附近");
                } else {
                    near_type_tv.setText(listsubway.get(position).getName());
                }
                for (int i = 0; i < listsubway.size(); i++) {
                    listsubway.get(i).setIschoose(false);
                }
                listsubway.get(position).setIschoose(true);
            }
        });
    }

    public void getSubwayStation(String id) {
        liststation = new ArrayList<>();
        StringPostRequest spr = new StringPostRequest(URLMannager.GetSubwayStation, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("Code").equals("1")) {
                        JSONArray ja = js1.getJSONArray("Result");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            Subway sw = new Subway();
                            sw.setId(jo.getString("id"));
                            sw.setName(jo.getString("name"));
                            sw.setLongitude(jo.getString("longitude"));
                            sw.setLatitude(jo.getString("latitude"));
                            liststation.add(sw);
                        }
                        adapter2 = new MySelectAdapter(liststation, getContext());
                        list2.setAdapter(adapter2);
                        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                longitude = liststation.get(position).getLongitude();
                                latitude = liststation.get(position).getLatitude();
                                pop1.dismiss();
                                subway_status = "1";
                                distance = "";
                                initdata();
                                near_type_tv.setText(liststation.get(position).getName());
                                for (int i = 0; i < liststation.size(); i++) {
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
        spr.putValue("subway_id", id);
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class MySelectAdapter extends BaseAdapter {
        private List<Subway> mydata;
        private Context context;

        public MySelectAdapter(List<Subway> mydata, Context context) {
            this.mydata = mydata;
            this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select1_item, null);
            FrameLayout fram = (FrameLayout) convertView.findViewById(R.id.select1_fram);
            TextView tv = (TextView) convertView.findViewById(R.id.select1_tv);
            tv.setText(mydata.get(position).getName());
            if (mydata.get(position).ischoose()) {
                fram.setBackgroundColor(0xffffffff);
                tv.setTextColor(0xffff5e5e);
            }
            return convertView;
        }
    }

    public class DataRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //给系统2秒时间用来做出反应
            SystemClock.sleep(1500);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mPullToRefresh_nearbyList.onRefreshComplete();
            initView();
            initdata();
        }
    }

}
