package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JKshuguoDetialMore extends AppCompatActivity {
    private String url;
    private LinearLayout fanhui;
    private GridView grid;
    private List<Caidan> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkshuguo_detial_more);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        url=getIntent().getStringExtra("url");
        list=new ArrayList<>();
        initView();
        getData();
    }
    public void initView(){
        fanhui=(LinearLayout)findViewById(R.id.shuguo_caipu_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        grid=(GridView)findViewById(R.id.shuguo_caipu_grid);
    }
    public void getData(){
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONObject js3 = js2.getJSONObject("dish");
                    JSONArray ja = js3.getJSONArray("list");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        Caidan caidan=new Caidan();
                        caidan.setId(jo.getString("id"));
                        caidan.setPic(jo.getString("pic"));
                        caidan.setTitle(jo.getString("title"));
                        caidan.setContent(jo.getString("content"));
                        list.add(caidan);
                    }
                    grid.setAdapter(new ShuguoMoreAdapter(list,getBaseContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    public class ShuguoMoreAdapter extends BaseAdapter{
        private List<Caidan> mydata;
        private Context context;
        public ShuguoMoreAdapter(List<Caidan> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
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
            view= LayoutInflater.from(context).inflate(R.layout.caipu_relative_item, null);
            TextView tv1 = (TextView) view.findViewById(R.id.relative_item_title);
            TextView tv2 = (TextView)view.findViewById(R.id.relative_item_content);
            tv1.setText(mydata.get(i).getTitle());
            tv2.setText(mydata.get(i).getContent());
            MyImageView ima= (MyImageView) view.findViewById(R.id.relative_item_ima);
            com.nostra13.universalimageloader.core.ImageLoader loaders = ImageLoaderUtils.getInstance(context);
            DisplayImageOptions optionss = ImageLoaderUtils.getOpt();
            loaders.displayImage(URLMannager.Imag_URL + "" + mydata.get(i).getPic(), ima, optionss);
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(context, CaipuDetail.class);
                    intent1.putExtra("id",mydata.get(x).getId());
                    startActivity(intent1);
                }
            });
            return view;
        }
    }
}
