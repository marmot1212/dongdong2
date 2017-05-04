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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Shuguo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.NewGridView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JKshuguo extends AppCompatActivity {
    private List<View> list_view;
    private ListView listView;
    private List<Shuguo> list_month;
    private LvAdapter adp;
    int month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkshuguo);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list_view=new ArrayList<>();
        listView=(ListView)findViewById(R.id.shuguo_list);
        Calendar c = Calendar.getInstance();
        month=c.get(Calendar.MONTH);
        initoperate();
        getData();


    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.shuguo_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView paihang=(TextView)findViewById(R.id.shuguo_paihang);
        paihang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(JKshuguo.this,JKshuguoPH.class);
                JKshuguo.this.startActivity(intent);
            }
        });
    }

    public void getData(){
        StringRequest request=new StringRequest(URLMannager.ShuGuo_List, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                myJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JKshuguo.this).addToRequestQueue(request);
    }


    public void myJson(String s){
        try {
            JSONObject js1=new JSONObject(s);
            JSONArray ja=js1.getJSONArray("Result");
            for(int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                String month=jo.getString("m");
                JSONArray jaa=jo.getJSONArray("list");
                list_month=new ArrayList<>();
                for(int j=0;j<jaa.length();j++){
                    Shuguo sg=new Shuguo();
                    JSONObject joo=jaa.getJSONObject(j);
                    sg.setId(joo.getString("id"));
                    sg.setTitle(joo.getString("title"));
                    sg.setType(joo.getString("type"));
                    sg.setPic(joo.getString("pic"));
                    list_month.add(sg);
                }
                View v=getLayoutInflater().inflate(R.layout.shuguo_month,null);
                TextView textView=(TextView) v.findViewById(R.id.shuguo_month);
                NewGridView gv=(NewGridView) v.findViewById(R.id.shuguo_grid);

                gv.setAdapter(new GdAdapter(list_month,JKshuguo.this));
                textView.setText(month+"月");
                list_view.add(v);
            }

            adp=new LvAdapter(list_view,JKshuguo.this);
            listView.setAdapter(adp);

            listView.setSelection(month);
            adp.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GdAdapter extends BaseAdapter{
        private List<Shuguo> mydata;
        private Context context;
        public GdAdapter(List<Shuguo> mydata, Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.shuguo_item,null);
            ImageView imag=(ImageView) view.findViewById(R.id.shuguo_pic);
            TextView tv=(TextView)view.findViewById(R.id.shuguo_name);
            tv.setText(mydata.get(i).getTitle());
            if(mydata.get(i).getType().equals("2")){
                tv.setBackgroundColor(0xff00aff0);
            }
            else
                if(mydata.get(i).getType().equals("3")){
                    tv.setBackgroundColor(0xffFF4081);
                }
                else
                    if(mydata.get(i).getType().equals("4")){
                        tv.setBackgroundColor(0xffff00ff);
                    }
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),imag,options);
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

    public class LvAdapter extends BaseAdapter{
        private List<View> mydata;
        private Context context;
        public  LvAdapter(List<View> mydata, Context context){
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
            view=mydata.get(i);
            return view;
        }
    }

}
