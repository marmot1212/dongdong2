package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.EllipsizingTextView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WendaSearch extends AppCompatActivity {
    private ListView listView;
    private EditText et;
    private FrameLayout fanhui;
    private List<Tiezi> searchlist;
    TextView tvf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenda_search);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        searchlist=new ArrayList<>();
        initView();
    }
    public void initView(){
        listView=(ListView)findViewById(R.id.wenda_list);
        et=(EditText)findViewById(R.id.wenda_et);
        fanhui=(FrameLayout)findViewById(R.id.add_tz_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().equals("")){
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                    try {
                        httpRequest(URLMannager.FaXian2+"p/1/t/100/keyword/"+ URLEncoder.encode(editable.toString(),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void httpRequest(String url){
        if(listView.getFooterViewsCount()>0)
            listView.removeFooterView(tvf);
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                searchlist=new ArrayList<>();
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("list");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        Tiezi tz=new Tiezi();
                        tz.setId(jo.getString("id"));
                        tz.setTitle(jo.getString("title"));
                        tz.setContent(jo.getString("content"));
                        tz.setCreate_time_text(jo.getString("create_time_text"));
                        if(!jo.isNull("img_url_1"))
                            tz.setIma1(jo.getString("img_url_1"));
                        if(!jo.isNull("img_url_2"))
                            tz.setIma2(jo.getString("img_url_2"));
                        if(!jo.isNull("img_url_3"))
                            tz.setIma3(jo.getString("img_url_3"));
                        if(!jo.isNull("img_url_4"))
                            tz.setIma4(jo.getString("img_url_4"));
                        if(!jo.isNull("img_url_5"))
                            tz.setIma5(jo.getString("img_url_5"));
                        if(!jo.isNull("img_url_6"))
                            tz.setIma6(jo.getString("img_url_6"));
                        searchlist.add(tz);
                    }
                    listView.setAdapter(new WDAdapter(searchlist,getBaseContext()));
                    if(searchlist.size()>0){
                        tvf=new TextView(getBaseContext());
                        tvf.setText("已经全部加载完毕");
                        tvf.setTextSize(12);
                        tvf.setTextColor(0xffa0a0a0);
                        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                        tvf.setLayoutParams(params);
                        tvf.setGravity(Gravity.CENTER);
                        listView.addFooterView(tvf);
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

    public class WDAdapter extends BaseAdapter {
        private List<ImageView> imalist;
        private List<Tiezi> mydata;
        private Context context;
        public WDAdapter(List<Tiezi> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
            imalist=new ArrayList<>();
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
            view= LayoutInflater.from(context).inflate(R.layout.wenda_item,null);
            EllipsizingTextView title_tv=(EllipsizingTextView)view.findViewById(R.id.wenda_title);
            EllipsizingTextView content_tv=(EllipsizingTextView)view.findViewById(R.id.wenda_content);
            TextView time=(TextView)view.findViewById(R.id.wenda_time);
            title_tv.setText(mydata.get(i).getTitle());
            content_tv.setText(mydata.get(i).getContent());
            time.setText(mydata.get(i).getCreate_time_text());
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(WendaSearch.this,Tiezi3Detial.class);
                    intent.putExtra("tid",mydata.get(x).getId());
                    WendaSearch.this.startActivity(intent);
                }
            });
            LinearLayout add=(LinearLayout)view.findViewById(R.id.ima_addline);
            com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(getBaseContext());
            DisplayImageOptions options2=ImageLoaderUtils.getOpt();
            if(mydata.get(i).getIma1()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma1(),imas,options2);
                add.addView(imas);
            }
            if(mydata.get(i).getIma2()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma2(),imas,options2);
                add.addView(imas);
            }
            if(mydata.get(i).getIma3()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma3(),imas,options2);
                add.addView(imas);
            }
            if(mydata.get(i).getIma4()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma4(),imas,options2);
                add.addView(imas);
            }
            if(mydata.get(i).getIma5()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma5(),imas,options2);
                add.addView(imas);
            }
            if(mydata.get(i).getIma6()!=null){
                MyImageView imas=new MyImageView(getBaseContext(),null);
                imas.setType(1);
                imas.setBorderRadius(3);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(15,0,0,0);
                imas.setLayoutParams(params);
                loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getIma6(),imas,options2);
                add.addView(imas);
            }
            return view;
        }
    }
}
