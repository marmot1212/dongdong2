package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.MyListAdapter;
import com.example.administrator.vegetarians824.adapter.VPAdapter;
import com.example.administrator.vegetarians824.entry.DDActivity;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HuodongDiqu extends AppCompatActivity {
    private LinearLayout fanhui;
    private List<DDActivity> start_List;
    private List <DDActivity> started_list;
    private ListView listView;
    private View headview;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong_diqu);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        start_List=new ArrayList();
        started_list=new ArrayList();
        operates();
        listView=(ListView) findViewById(R.id.diqu_listview);
        Intent intent=getIntent();
        String province=intent.getStringExtra("province");
        TextView textView=(TextView) findViewById(R.id.diqu_province);
        textView.setText(province+"活动");

        StringBuffer sb=new StringBuffer();
        try {
            sb.append(URLMannager.Huodong_Diqu)
                    .append(URLEncoder.encode(province,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        loadData(sb.toString());

    }
    public void operates() {
        //返回按钮
        fanhui = (LinearLayout) findViewById(R.id.diqu_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HuodongDiqu.this.finish();
            }
        });
    }
    public void  loadData(String path){

        StringRequest request=new StringRequest(path, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                jsonArray(s);
                headview=getLayoutInflater().from(getBaseContext()).inflate(R.layout.huodong_head,null);
                ViewPagerInit();
                ListInit();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    public void jsonArray(String ja){

        try {
            JSONObject js1 = new JSONObject(ja);
            JSONObject js2 = js1.getJSONObject("Result");
            JSONArray jsa1 = js2.getJSONArray("start");
            for (int i = 0; i < jsa1.length(); i++) {
                DDActivity ddActivity = new DDActivity();
                JSONObject jso = jsa1.getJSONObject(i);
                ddActivity.setActivity_id(jso.getString("id"));
                ddActivity.setActivity_title(jso.getString("title"));
                ddActivity.setActivity_start_time(jso.getString("start_time"));
                ddActivity.setActivity_finish_time(jso.getString("finish_time"));
                ddActivity.setActivity_province(jso.getString("province"));
                ddActivity.setActivity_content(jso.getString("content"));
                ddActivity.setActivity_img_url_1(jso.getString("img_url_1"));
                ddActivity.setActivity_img_url_2(jso.getString("img_url_2"));
                ddActivity.setActivity_type(jso.getString("type"));
                ddActivity.setActivity_img_url_th_1(jso.getString("img_url_th_1"));
                start_List.add(ddActivity);
            }
            if (!js2.isNull("started")){
                JSONArray jsa2 = js2.getJSONArray("started");
                for (int x = 0; x < jsa2.length(); x++) {
                    JSONObject jso2 = jsa2.getJSONObject(x);
                    JSONArray monthlist = jso2.getJSONArray("list");
                    for (int j = 0; j < monthlist.length(); j++) {
                        JSONObject aa = monthlist.getJSONObject(j);
                        DDActivity ddActivity2 = new DDActivity();
                        ddActivity2.setActivity_id(aa.getString("id"));
                        ddActivity2.setActivity_title(aa.getString("title"));
                        ddActivity2.setActivity_start_time(aa.getString("start_time"));
                        ddActivity2.setActivity_finish_time(aa.getString("finish_time"));
                        ddActivity2.setActivity_province(aa.getString("province"));
                        ddActivity2.setActivity_content(aa.getString("content"));
                        ddActivity2.setActivity_img_url_1(aa.getString("img_url_1"));
                        ddActivity2.setActivity_img_url_2(aa.getString("img_url_2"));
                        ddActivity2.setActivity_type(aa.getString("type"));
                        started_list.add(ddActivity2);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ViewPagerInit(){
        final List <View> vp_list=new ArrayList<>();
        viewPager=(ViewPager)headview.findViewById(R.id.huodong_vp);
        for( int i=0;i<start_List.size();i++){
            View v=getLayoutInflater().from(this).inflate(R.layout.list_huodong,null);
            TextView tv1=(TextView) v.findViewById(R.id.huodong_title);
            tv1.setText(start_List.get(i).getActivity_title().toString());
            TextView tv2=(TextView)v.findViewById(R.id.huodong_time);
            tv2.setText(start_List.get(i).getActivity_start_time().toString()+" - "+start_List.get(i).getActivity_finish_time());
            TextView tv3=(TextView)v.findViewById(R.id.huodong_content);
            tv3.setText(start_List.get(i).getActivity_content());
            Button bt=(Button)v.findViewById(R.id.huodong_province);
            bt.setText(start_List.get(i).getActivity_province().toString());
            ImageView imageView=(ImageView) v.findViewById(R.id.huodong_image);
            ImageLoader loader= ImageLoaderUtils.getInstance(HuodongDiqu.this);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+start_List.get(i).getActivity_img_url_2(),imageView,options);

            final int x=i;
            //点击item显示详情
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(HuodongDiqu.this,HuodongDetail.class);
                    intent.putExtra("image",start_List.get(x).getActivity_img_url_1());
                    intent.putExtra("content",start_List.get(x).getActivity_content());
                    intent.putExtra("id",start_List.get(x).getActivity_id());
                    HuodongDiqu.this.startActivity(intent);
                }
            });

            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            vp_list.add(v);
        }
        VPAdapter vpAdapter=new VPAdapter(vp_list);
        viewPager.setAdapter(vpAdapter);
        viewPager.setCurrentItem(0);
        vpAdapter.notifyDataSetChanged();




    }
    //listview初始化
    public void ListInit(){
        Log.d("=============ssa","aa "+started_list.size());
        if(started_list.size()==0){
            Log.d("=============ss","aa");
            LinearLayout jihua=(LinearLayout)headview.findViewById(R.id.jihua_line);
            LinearLayout all=(LinearLayout)headview.findViewById(R.id.all_line);
            all.removeView(jihua);
        }
        listView.addHeaderView(headview);
        listView.setAdapter(new MyListAdapter(started_list,HuodongDiqu.this));
    }
}
