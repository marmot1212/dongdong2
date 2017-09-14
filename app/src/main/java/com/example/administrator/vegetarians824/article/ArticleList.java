package com.example.administrator.vegetarians824.article;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.ArticleListAdapter;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.CyclePager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleList extends AppCompatActivity {
    private LinearLayout group;
    private ViewPager advPager;
    private PullToRefreshListView prl;
    private int count=1;//第几次加载
    private int totalpage=1;//总页数
    private boolean isup=false;
    private TextView foot;
    private List<Tiezi> list;
    private ArticleListAdapter adapter;
    private LoadingDialog loadingDialog;
    private LinearLayout searchline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        group = (LinearLayout)findViewById(R.id.ditu_xiangqing_viewGroup2);// 展示小圆点
        advPager = (ViewPager)findViewById(R.id.ditu_xiangqing_viewpager2);// ViewPager
        CyclePager c=new CyclePager(advPager,group,ArticleList.this);
        c.init("3");
        loadingDialog=new LoadingDialog(ArticleList.this);
        loadingDialog.show();
        initView();
    }
    public void initView(){
        foot = new TextView(getBaseContext());
        foot.setTextSize(12);
        foot.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
        foot.setLayoutParams(params2);
        foot.setGravity(Gravity.CENTER);
        foot.setText("已经全部加载完毕");
        prl=(PullToRefreshListView)findViewById(R.id.article_prl);
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list=new ArrayList<>();
                count=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isup=true;
                new DataRefresh().execute();
            }
        });
        searchline=(LinearLayout)findViewById(R.id.article_searchline);
        searchline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),ArticleSearch.class));
            }
        });

    }
    public void getList(){
        prl.setMode(PullToRefreshBase.Mode.BOTH);
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(foot);
        }
        StringRequest request=new StringRequest(URLMannager.FaXian + "p/"+count+"/t/10", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("===============faxian",s);
                try {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        totalpage=Integer.valueOf(js2.getString("totalpage"));
                        if(count==totalpage){
                            prl.getRefreshableView().addFooterView(foot);
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Tiezi tz=new Tiezi();
                            tz.setId(jo.getString("id"));
                            tz.setTitle(jo.getString("title"));
                            tz.setPic(jo.getString("img_url_th_1"));
                            tz.setContent(jo.getString("content"));
                            tz.setCreate_time_text(jo.getString("create_time_text"));
                            list.add(tz);
                        }
                    }else {
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                    }

                    adapter=new ArticleListAdapter(list,ArticleList.this);
                    prl.setAdapter(adapter);
                    if(isup){
                        prl.getRefreshableView().setSelection(count*10-12);
                        isup=false;
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
            prl.onRefreshComplete();
            count++;
            if(count<=totalpage){
                getList();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        list=new ArrayList<>();
        count=1;
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(foot);
        }
        getList();
    }



}
