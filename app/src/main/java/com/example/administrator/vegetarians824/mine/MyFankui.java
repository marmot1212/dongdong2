package com.example.administrator.vegetarians824.mine;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.entry.FabuInfo;
import com.example.administrator.vegetarians824.entry.FanKui;
import com.example.administrator.vegetarians824.fankui.Fankui;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFankui extends AppCompatActivity {
    private ListView prl;
    private List<FanKui> list;
    int p,totalpage;
    private FabuInfoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fankui);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        prl=(ListView) findViewById(R.id.subfrag_list4);
        list=new ArrayList<>();
        initop();
        getdate();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout) findViewById(R.id.my_fankui_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout fankui=(LinearLayout) findViewById(R.id.my_fankui_fankui);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyFankui.this, Fankui.class);
                MyFankui.this.startActivity(intent);
            }
        });
    }
    public void getdate(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/feed_back", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    Log.d("=======",s);
                    if(js1.getString("Result").equals("")){
                        TextView tv=(TextView)findViewById(R.id.noinfo3);
                        tv.setText("当前无反馈信息");
                    }
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("fankui");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        FanKui f=new FanKui();
                        f.setId(jo.getString("id"));
                        f.setCreate_time(jo.getString("create_time"));
                        f.setContent(jo.getString("content"));
                        JSONObject jaa=jo.getJSONObject("back");
                        f.setBack_id(jaa.getString("back_id"));
                        f.setBack_content(jaa.getString("back_content"));
                        f.setBack_create_time(jaa.getString("back_create_time"));
                        list.add(f);
                    }
                    adapter=new FabuInfoAdapter(list,getBaseContext());
                    prl.setAdapter(adapter);
                    if(list.size()>0){
                        TextView tv=new TextView(getBaseContext());
                        tv.setText("已经全部加载完毕");
                        tv.setTextSize(12);
                        tv.setTextColor(0xffa0a0a0);
                        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                        tv.setLayoutParams(params);
                        tv.setGravity(Gravity.CENTER);
                        prl.addFooterView(tv);
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
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public class FabuInfoAdapter extends BaseAdapter {
        private List<FanKui> mydate;
        private Context context;
        public FabuInfoAdapter(List<FanKui> mydate,Context context){
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(context).inflate(R.layout.fankui_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.fankui_title);
            TextView tv2=(TextView) view.findViewById(R.id.fankui_back);
            tv1.setText(mydate.get(i).getContent());
            if(!mydate.get(i).getBack_content().equals("null"))
                tv2.setText(mydate.get(i).getBack_content());
            return view;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
