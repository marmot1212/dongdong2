package com.example.administrator.vegetarians824.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.administrator.vegetarians824.entry.UserRank;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JifenRank extends AppCompatActivity {
    ListView listView;
    List<UserRank> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifen_rank);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        listView=(ListView)findViewById(R.id.ranklist);
        list=new ArrayList<>();
        initop();
        initView();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.jifenrank_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initView(){
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainliving/androidapi/indexs/suRanking", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        UserRank ur=new UserRank();
                        ur.setId(jo.getString("id"));
                        ur.setUsername(jo.getString("username"));
                        ur.setGrowth_index(jo.getString("growth_index"));
                        ur.setUser_head_img(jo.getString("user_head_img"));
                        if(!jo.isNull("lv"))
                            ur.setLv(jo.getString("lv"));
                        list.add(ur);
                    }
                    listView.setAdapter(new RankAdapter(list,getBaseContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JifenRank.this).addToRequestQueue(request);
    }

    public class RankAdapter extends BaseAdapter{
        private List<UserRank> mydate;
        private Context context;

        public RankAdapter(List<UserRank> mydate,Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.jinfen_rank_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.rank_num);
            tv1.setText(i+1+"");
            TextView tv2=(TextView)view.findViewById(R.id.rank_name);
            tv2.setText(mydate.get(i).getUsername());
            TextView tv3=(TextView)view.findViewById(R.id.rank_score);
            tv3.setText(Float.valueOf(mydate.get(i).getGrowth_index())+"");
            ImageView head=(ImageView)view.findViewById(R.id.rank_ima);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydate.get(i).getUser_head_img(),head,options);
            ImageView lv=(ImageView)view.findViewById(R.id.rank_grade);
            switch(mydate.get(i).getLv()){
                case "1":lv.setImageResource(R.mipmap.lv1);break;
                case "2":lv.setImageResource(R.mipmap.lv2);break;
                case "3":lv.setImageResource(R.mipmap.lv3);break;
                default:break;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(JifenRank.this,UserDetial.class);
                    intent.putExtra("uid",mydate.get(i).getId());
                    if(BaseApplication.app.getUser().islogin()){
                        intent.putExtra("id",BaseApplication.app.getUser().getId());
                    }else {
                        intent.putExtra("id","");
                    }
                    JifenRank.this.startActivity(intent);
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
