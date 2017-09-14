package com.example.administrator.vegetarians824.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.JKshuguoDetial;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CaipuGet extends AppCompatActivity {
    private TextView cancel,admit;
    private EditText et;
    private ListView listView;
    private List<Caidan> list_caidan;
    private String keyword="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipu_get);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initView();
    }
    public void initView(){
        cancel=(TextView)findViewById(R.id.caipu_get_cancel);
        admit=(TextView)findViewById(R.id.caipu_get_submit);
        et=(EditText)findViewById(R.id.caipu_get_et);
        listView=(ListView)findViewById(R.id.caipu_get_listview);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        admit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et.getText().toString().equals("")){
                    dopost();
                }else {
                    Toast.makeText(getBaseContext(),"请输入菜品名称",Toast.LENGTH_SHORT).show();
                }
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    keyword=s.toString();
                    initData2();
                }else {
                    list_caidan=new ArrayList<Caidan>();
                    listView.setAdapter(new CaipuAdapter02(list_caidan,CaipuGet.this));
                }
            }
        });
    }
    public void initData2(){
        list_caidan=new ArrayList<>();
        StringRequest request=new StringRequest(URLMannager.Caidan_URL+"/keyword/"+keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        if(js2.has("food")){
                            JSONArray jaa=js2.getJSONArray("food");
                            for(int i=0;i<jaa.length();i++){
                                JSONObject joo=jaa.getJSONObject(i);
                                Caidan cd=new Caidan();
                                cd.setTitle(joo.getString("title"));
                                cd.setId(joo.getString("id"));
                                cd.setDress(joo.getString("about"));
                                cd.setContent(joo.getString("content"));
                                cd.setType(joo.getString("stype"));
                                cd.setPic(joo.getString("pic"));
                                list_caidan.add(cd);
                            }
                        }
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Caidan cd=new Caidan();
                            cd.setId(jo.getString("id"));
                            cd.setTitle(jo.getString("title"));
                            cd.setType(jo.getString("video_status"));
                            cd.setDress(jo.getString("dress"));
                            cd.setPic(jo.getString("pic"));
                            cd.setUsername(jo.getString("username"));
                            cd.setViewcount(jo.getString("view"));
                            cd.setGathercount(jo.getString("gather_count"));
                            list_caidan.add(cd);
                        }
                        listView.setAdapter(new CaipuAdapter02(list_caidan,CaipuGet.this));
                    }else {

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
    public class CaipuAdapter02 extends BaseAdapter {
        private List<Caidan> mydata;
        private Context context;
        public CaipuAdapter02(List<Caidan> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_caipu_item02,null);
            TextView title=(TextView) convertView.findViewById(R.id.caipu_item02_title);
            TextView dress=(TextView) convertView.findViewById(R.id.caipu_item02_dress);
            TextView username=(TextView) convertView.findViewById(R.id.caipu_item02_username);
            TextView view=(TextView) convertView.findViewById(R.id.caipu_item02_view);
            MyImageView ima=(MyImageView)convertView.findViewById(R.id.caipu_item02_ima);
            ImageView play=(ImageView)convertView.findViewById(R.id.caipu_item02_play);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getPic(),ima,options);
            title.setText(mydata.get(position).getTitle());
            final int x=position;
            if(!mydata.get(position).getType().equals("9")) {
                dress.setText(mydata.get(position).getDress());
                username.setText(mydata.get(position).getUsername());
                view.setText(mydata.get(position).getViewcount() + "浏览\t" + mydata.get(position).getGathercount() + "收藏");
                if(mydata.get(position).getType().equals("2")) {
                    play.setVisibility(View.VISIBLE);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, VideoDetail.class);
                            intent.putExtra("id", mydata.get(x).getId());
                            context.startActivity(intent);
                        }
                    });
                }else {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CaipuDetail.class);
                            intent.putExtra("id", mydata.get(x).getId());
                            context.startActivity(intent);
                        }
                    });
                }
            }else {
                dress.setText(mydata.get(position).getContent());
                username.setTextColor(0xff51b30c);
                username.setText(mydata.get(position).getDress());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, JKshuguoDetial.class);
                        intent.putExtra("id", mydata.get(x).getId());
                        context.startActivity(intent);
                    }
                });
            }
            return convertView;
        }
    }
    public void dopost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.DishRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("===========ss",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        Toast.makeText(getBaseContext(),"发布成功",Toast.LENGTH_SHORT).show();
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
        spr.putValue("content",et.getText().toString());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

}
