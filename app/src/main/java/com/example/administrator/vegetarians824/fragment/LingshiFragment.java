package com.example.administrator.vegetarians824.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.JKlsdetial;
import com.example.administrator.vegetarians824.dongdong.TieziDetial;
import com.example.administrator.vegetarians824.entry.Lingshi;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LingshiFragment extends Fragment {

    private int count;//第几次加载
    private int totalpage;//总页数
    private List<Lingshi> list;//帖子列表
    private PullToRefreshListView prl;
    private LSAdapter adapter;
    private boolean isup;
    private Date date;
    public LingshiFragment() {
        // Required empty public constructor
        date=new Date();
    }
    private String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_lingshi, container, false);
        Bundle bundle=getArguments();
        type=bundle.getString("type");
        //Toast.makeText(getContext(),type,Toast.LENGTH_SHORT).show();
        count=1;
        isup=false;
        prl=(PullToRefreshListView) v.findViewById(R.id.lingshi_prl);
        list=new ArrayList<>();
        initPRL();
        return v;
    }
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

        //设置加载动画
        prl.setMode(PullToRefreshBase.Mode.BOTH);

        getData("http://www.isuhuo.com/plainliving/androidapi/List/product_list/type/"+type+"p/"+count+"/t/20");


        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate2);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list=new ArrayList<>();
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
                getData("http://www.isuhuo.com/plainliving/androidapi/List/product_list/type/"+type+ "/p/" + count + "/t/20");
            }
            else{
                Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public void getData(String s){
        StringRequest request=new StringRequest(s, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                adapter= new LSAdapter(list,getContext());
                prl.setAdapter(adapter);
                if(isup){
                    prl.getRefreshableView().setSelection(count*10-11);
                    isup=false;
                }
                if(list.size()>0){
                    TextView tv=new TextView(getContext());
                    tv.setText("已经全部加载完毕");
                    tv.setTextSize(12);
                    tv.setTextColor(0xffa0a0a0);
                    ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                    tv.setLayoutParams(params);
                    tv.setGravity(Gravity.CENTER);
                    prl.getRefreshableView().addFooterView(tv);
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }

    public void myJson(String s){
        try {
            JSONObject js1=new JSONObject(s);
            JSONObject js2=js1.getJSONObject("Result");
            JSONObject js3=js2.getJSONObject("product");
            String st=js3.getString("totalpage");
            totalpage=Integer.valueOf(st).intValue();
            if(totalpage==1){
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
            JSONArray ja=js3.getJSONArray("list");
            for(int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                Lingshi ls=new Lingshi();
                ls.setId(jo.getString("id"));
                ls.setTitle(jo.getString("title"));
                ls.setContent(jo.getString("content"));
                ls.setFormat(jo.getString("format"));
                ls.setSale_price(jo.getString("sale_price"));
                ls.setLink(jo.getString("link"));
                ls.setUnit_price(jo.getString("unit_price"));
                ls.setImg_url_1(jo.getString("img_url_1"));
                ls.setType(jo.getString("type"));
                ls.setCreate_time(jo.getString("create_time"));
                ls.setUid(jo.getString("uid"));
                ls.setAddress(jo.getString("address"));
                list.add(ls);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class LSAdapter extends BaseAdapter {
        private List<Lingshi> mydata;
        private Context context;
        public LSAdapter(List<Lingshi> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            int c=mydata.size();
            if(c%2==0){
                return c/2;
            }else {
                return c/2+1;
            }
            //return mydata.size();
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=LayoutInflater.from(context).inflate(R.layout.lingshi_item,null);
            final Lingshi ls1=mydata.get(i*2);

            TextView tv1=(TextView)view.findViewById(R.id.lingshi_item_tv1);
            tv1.setText(ls1.getTitle());
            ImageView ima1=(ImageView)view.findViewById(R.id.lingshi_item_ima1);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+ls1.getImg_url_1(),ima1,options);
            TextView p1=(TextView)view.findViewById(R.id.lingshi_item_price1);
            p1.setText(ls1.getSale_price());
            LinearLayout l1=(LinearLayout)view.findViewById(R.id.lingshi_item_l1);
            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getContext(), JKlsdetial.class);
                    intent.putExtra("url",ls1.getLink());
                    intent.putExtra("title",ls1.getTitle());
                    getActivity().startActivity(intent);
                }
            });

            if((i*2+2)<=mydata.size()){
                FrameLayout fram=(FrameLayout) view.findViewById(R.id.lingshi_item_fram2);
                fram.setVisibility(View.VISIBLE);
                final Lingshi ls2=mydata.get(i*2+1);
                TextView tv2=(TextView)view.findViewById(R.id.lingshi_item_tv2);
                tv2.setText(ls2.getTitle());
                ImageView ima2=(ImageView)view.findViewById(R.id.lingshi_item_ima2);
                loader.displayImage(URLMannager.Imag_URL+""+ls2.getImg_url_1(),ima2,options);
                TextView p2=(TextView)view.findViewById(R.id.lingshi_item_price2);
                p2.setText(ls2.getSale_price());
                LinearLayout l2=(LinearLayout)view.findViewById(R.id.lingshi_item_l2);
                l2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getContext(), JKlsdetial.class);
                        intent.putExtra("url",ls2.getLink());
                        intent.putExtra("title",ls2.getTitle());
                        getActivity().startActivity(intent);
                    }
                });
            }

            return view;
        }
    }
}
