package com.example.administrator.vegetarians824.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.entry.FabuInfo;
import com.example.administrator.vegetarians824.entry.FabuInfo2;
import com.example.administrator.vegetarians824.entry.SellerInfo;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.fabu.FabuCaipuUpdate;
import com.example.administrator.vegetarians824.fabu.FabuFD;
import com.example.administrator.vegetarians824.fabu.FabuHD;
import com.example.administrator.vegetarians824.fabu.FabuHDUpdate;
import com.example.administrator.vegetarians824.fabu.FabuShUpdate;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerFragment extends Fragment {


    public SellerFragment() {
        date=new Date();
    }
    private PullToRefreshListView prl;
    private TextView nonetext;
    private List<FabuInfo> list;
    private List<FabuInfo2> list2;
    private FabuInfoAdapter adapter;
    private FabuInfoAdapter2 adapter2;
    private Date date;
    private int count;//第几次加载
    private boolean isup;
    private int totalpage=1;//总页数
    private String type;
    TextView tvfoot;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_seller, container, false);
        prl=(PullToRefreshListView)v.findViewById(R.id.seller_prl);
        nonetext=(TextView)v.findViewById(R.id.seller_none);

        count=1;
        isup=false;

        //顶部tab选中状态 0 店铺 1 菜谱 2 活动

        return v;
    }
    //需要维护的店铺信息展示
    public void updateDP(View v){
        type="1";
        initPRL();
    }

    //需要维护的菜谱信息展示
    public void updateCP(View v){
        //添加蓝条
        type="2";
        LinearLayout addline=(LinearLayout) v.findViewById(R.id.update_addline);
        addline.setVisibility(View.VISIBLE);
        TextView addtext=(TextView)v.findViewById(R.id.update_addtext);
        addtext.setText("添加菜谱");
        addline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), FabuFD.class);
                startActivity(intent);
            }
        });
        initPRL();
    }
    //需要维护的活动信息展示
    public void updateHD(View v){
        //添加蓝条
        type="4";
        LinearLayout addline=(LinearLayout) v.findViewById(R.id.update_addline);
        addline.setVisibility(View.VISIBLE);
        TextView addtext=(TextView)v.findViewById(R.id.update_addtext);
        addtext.setText("添加活动");
        addline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), FabuHD.class);
                startActivity(intent);
            }
        });
        initPRL();
    }

    //加载prlist数据
    public void initPRL(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);
        // 设置上拉刷新文本
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);

        getdate();
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate2);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list=new ArrayList<>();
                list2=new ArrayList<>();
                count=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate3);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                date=new Date();
                isup=true;
                new DataRefresh().execute();
            }
        });
    }
    //请求数据
    public void getdate(){
        if(type!="4") {
            StringPostRequest spr = new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/mysharetenans_list", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject js1 = new JSONObject(s);
                        if (js1.isNull("Result")) {
                            nonetext.setVisibility(View.VISIBLE);
                        }
                        JSONObject js2 = js1.getJSONObject("Result");
                        if (js2.getJSONArray("list").length() == 0) {
                            nonetext.setVisibility(View.VISIBLE);
                        }
                        if (!js2.isNull("totalpage")) {
                            String st = js2.getString("totalpage");
                            totalpage = Integer.valueOf(st).intValue();
                        }
                        if (totalpage == 1) {
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        JSONArray ja = js2.getJSONArray("list");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            FabuInfo f = new FabuInfo();
                            f.setId(jo.getString("id"));
                            if (!jo.isNull("list_id"))
                                f.setList_id(jo.getString("list_id"));
                            f.setTitle(jo.getString("title"));
                            f.setCreate_time(jo.getString("create_time"));
                            f.setType_mess_id(jo.getString("type_mess_id"));
                            if (!jo.isNull("mess_id"))
                                f.setMess_id(jo.getString("mess_id"));
                            list.add(f);
                        }
                        adapter = new FabuInfoAdapter(list, getContext());
                        prl.setAdapter(adapter);
                        if (list.size() > 0 && prl.getRefreshableView().getFooterViewsCount() <= 1) {
                            TextView tv = new TextView(getContext());
                            tv.setText("已经全部加载完毕");
                            tv.setTextSize(12);
                            tv.setTextColor(0xffa0a0a0);
                            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                            tv.setLayoutParams(params);
                            tv.setGravity(Gravity.CENTER);
                            prl.getRefreshableView().addFooterView(tv);
                        }
                        if (isup) {
                            prl.getRefreshableView().setSelection(count * 10 - 19);
                            isup = false;
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            spr.putValue("uid", BaseApplication.app.getUser().getId());
            spr.putValue("type", type);
            spr.putValue("p", String.valueOf(count).toString());
            spr.putValue("t", "10");
            SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
        }else {
            StringPostRequest spr = new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/UserCenter/get_restaurantPackages", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject js1 = new JSONObject(s);
                        if (js1.isNull("Result")) {
                            nonetext.setVisibility(View.VISIBLE);
                        }else {
                        JSONArray ja = js1.getJSONArray("Result");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            FabuInfo2 f = new FabuInfo2();
                            f.setId(jo.getString("id"));
                            f.setDetail(jo.getString("detail"));
                            f.setFinish_time(jo.getString("finish_time"));
                            f.setRes_id(jo.getString("res_id"));
                            f.setTitle(jo.getString("title"));
                            f.setRestaurant_title(jo.getString("restaurant_title"));
                            list2.add(f);
                        }
                        }
                        adapter2 = new FabuInfoAdapter2(list2, getContext());
                        prl.setAdapter(adapter2);
                        if (list2.size() > 0 && prl.getRefreshableView().getFooterViewsCount() <= 1) {
                            tvfoot = new TextView(getContext());
                            tvfoot.setText("已经全部加载完毕");
                            tvfoot.setTextSize(12);
                            tvfoot.setTextColor(0xffa0a0a0);
                            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                            tvfoot.setLayoutParams(params);
                            tvfoot.setGravity(Gravity.CENTER);
                            prl.getRefreshableView().addFooterView(tvfoot);
                        }
                        if (isup) {
                            prl.getRefreshableView().setSelection(count * 10 - 19);
                            isup = false;
                        }
                        adapter2.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                }
            });
            spr.putValue("uid", BaseApplication.app.getUser().getId());
            SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
        }
    }
    //下拉上拉刷新
    public class DataRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //给系统2秒时间用来做出反应
            SystemClock.sleep(2000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prl.onRefreshComplete();
            count++;
            if(count<=totalpage){
                getdate();
            }
            else{
                Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public class FabuInfoAdapter extends BaseAdapter{
        private List<FabuInfo> mydate;
        private Context context;
        public FabuInfoAdapter(List<FabuInfo> mydate,Context context){
            this.mydate=mydate;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydate.size();
        }

        @Override
        public Object getItem(int i) {
            return mydate.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=LayoutInflater.from(context).inflate(R.layout.my_fabu_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.my_fabu_title);
            TextView tv2=(TextView) view.findViewById(R.id.my_fabu_time);
            tv1.setText(mydate.get(i).getTitle());
            tv2.setText(mydate.get(i).getCreate_time());
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (type){
                        case "1":
                            Intent intent = new Intent(getContext(), FabuShUpdate.class);
                            intent.putExtra("item_id", mydate.get(x).getId());
                            getActivity().startActivity(intent);
                            break;
                        case "4":
                            //活动
                            Intent intent4=new Intent(getContext(), FabuHDUpdate.class);
                            intent4.putExtra("id",mydate.get(x).getId());
                            getActivity().startActivity(intent4);
                            break;
                        case "2":
                            //菜谱
                            Intent intent6=new Intent(getContext(), FabuCaipuUpdate.class);
                            intent6.putExtra("id",mydate.get(x).getId());
                            getActivity().startActivity(intent6);
                            break;
                    }
                }
            });

            return view;
        }
    }

    public class FabuInfoAdapter2 extends BaseAdapter{
        private List<FabuInfo2> mydate;
        private Context context;
        public FabuInfoAdapter2(List<FabuInfo2> mydate,Context context){
            this.mydate=mydate;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydate.size();
        }

        @Override
        public Object getItem(int i) {
            return mydate.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view=LayoutInflater.from(context).inflate(R.layout.my_fabu_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.my_fabu_title);
            TextView tv2=(TextView) view.findViewById(R.id.my_fabu_time);
            tv1.setText(mydate.get(i).getTitle());
            tv2.setText(mydate.get(i).getDetail());
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), FabuHDUpdate.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("huodong", mydate.get(x));
                    intent.putExtras(mBundle);
                    getActivity().startActivity(intent);
                }
            });

            return view;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nonetext.setVisibility(View.GONE);
        list=new ArrayList<>();
        list2=new ArrayList<>();
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(tvfoot);
        }
        Bundle bundle=getArguments();
        String what=bundle.getString("what");
        switch (what){
            case "0":updateDP(getView());break;
            case "1":updateCP(getView());break;
            case "2":updateHD(getView());break;
            default:break;
        }
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
