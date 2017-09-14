package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Shuguo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JKPaihangDetial extends AppCompatActivity {
    private String id,title;
    private List<Shuguo> list;
    private  TextView content;
    private String type;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkpaihang_detial);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list=new ArrayList<>();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        title=intent.getStringExtra("title");
        initoperate();
        getdata();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.paihangD_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv=(TextView)findViewById(R.id.paihangD_title);
        tv.setText(title);

        content=(TextView)findViewById(R.id.paihangD_text);
    }
    public void getdata(){
        Log.d("=============url",URLMannager.Paihang + id);
        StringRequest request=new StringRequest(URLMannager.Paihang + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    type=js2.getString("type");
                    tv.setText(js2.getString("title"));
                    if(type.equals("2")){
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Shuguo sg=new Shuguo();
                            sg.setTitle(jo.getString("title"));
                            sg.setAbout(jo.getString("about"));
                            sg.setPic(jo.getString("pic"));
                            sg.setId(jo.getString("pid"));
                            list.add(sg);
                        }
                        ListView listView=(ListView) findViewById(R.id.paihangD_list);
                        listView.setAdapter(new PaihangAdapter(list,JKPaihangDetial.this));

                    }

                    if(type.equals("1")){
                        String ss=js2.getString("content");
                        content.setText(ss);
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
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public class PaihangAdapter extends BaseAdapter{
        private  List<Shuguo> mydata;
        private Context context;
        public PaihangAdapter(List<Shuguo> mydata,Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.paihang_items,null);
            ImageView ima=(ImageView)view.findViewById(R.id.paihang_item_imag);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),ima,options);
            TextView t=(TextView)view.findViewById(R.id.paihang_item_name);
            t.setText(mydata.get(i).getTitle());
            TextView t2=(TextView)view.findViewById(R.id.paihang_item_content);
            t2.setText(mydata.get(i).getAbout());
            TextView rank=(TextView)view.findViewById(R.id.paihang_item_rank);
            rank.setText(""+(i+1));
            rank.setBackgroundColor(0xc0a0a0a0);
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,JKshuguoDetial.class);
                    intent.putExtra("id",mydata.get(x).getId());
                    context.startActivity(intent);
                }
            });

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
