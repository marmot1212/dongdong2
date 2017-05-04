package com.example.administrator.vegetarians824.fankui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.SpecialUser;
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
import java.util.List;

public class Thanks extends AppCompatActivity {
    NewGridView gv1,gv2,gv3;
    List<SpecialUser> list1,list2,list3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list1=new ArrayList<>();
        list2=new ArrayList<>();
        list3=new ArrayList<>();
        gv1=(NewGridView)findViewById(R.id.thanks_gv1);
        gv2=(NewGridView)findViewById(R.id.thanks_gv2);
        gv3=(NewGridView)findViewById(R.id.thanks_gv3);
        initop();
        getdate();
    }
    public void initop(){
        final LinearLayout fanhui=(LinearLayout)findViewById(R.id.thanks_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getdate(){
        StringRequest request=new StringRequest(URLMannager.ThanksList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja1=js2.getJSONArray("teamuser");
                    JSONArray ja2=js2.getJSONArray("bestuser");
                    JSONArray ja3=js2.getJSONArray("special");

                    for(int x=0;x<ja1.length();x++){
                        JSONObject jo=ja1.getJSONObject(x);
                        SpecialUser su=new SpecialUser();
                        su.setId(jo.getString("id"));
                        su.setUid(jo.getString("uid"));
                        su.setGroup(jo.getString("group"));
                        su.setUsername(jo.getString("username"));
                        su.setUser_head_img(jo.getString("user_head_img"));
                        list1.add(su);
                    }
                    for(int y=0;y<ja2.length();y++){
                        JSONObject jo=ja2.getJSONObject(y);
                        SpecialUser su=new SpecialUser();
                        su.setId(jo.getString("id"));
                        su.setUid(jo.getString("uid"));
                        su.setGroup(jo.getString("group"));
                        su.setUsername(jo.getString("username"));
                        su.setUser_head_img(jo.getString("user_head_img"));
                        list2.add(su);
                    }
                    for(int z=0;z<ja3.length();z++){
                        JSONObject jo=ja3.getJSONObject(z);
                        SpecialUser su=new SpecialUser();
                        su.setId(jo.getString("id"));
                        su.setUid(jo.getString("uid"));
                        su.setGroup(jo.getString("group"));
                        su.setUsername(jo.getString("username"));
                        su.setUser_head_img(jo.getString("user_head_img"));
                        list3.add(su);
                    }
                    gv1.setAdapter(new ThanksAdapter(list1,getBaseContext()));
                    gv2.setAdapter(new ThanksAdapter(list2,getBaseContext()));
                    gv3.setAdapter(new ThanksAdapter(list3,getBaseContext()));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(Thanks.this).addToRequestQueue(request);
    }

    public class ThanksAdapter extends BaseAdapter{
        private List<SpecialUser> mydate;
        private Context context;
        public ThanksAdapter(List<SpecialUser> mydate,Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.thanks_item,null);
            ImageView head=(ImageView)view.findViewById(R.id.thanks_head);
            TextView name=(TextView)view.findViewById(R.id.thanks_name);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydate.get(i).getUser_head_img(),head,options);
            name.setText(mydate.get(i).getUsername());
            return view;
        }
    }
}
