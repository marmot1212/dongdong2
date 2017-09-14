package com.example.administrator.vegetarians824.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter2;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.JKshuguoDetial;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
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

public class VideoSearch extends AppCompatActivity {
    private EditText et;
    private TextView fanhui;
    private String keyword;
    private List<Caidan> list_caidan;
    private ListView listView;
    private ScrollView noresult;
    private Button qiu;
    private ListViewForScrollView likelistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_search);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initView();
    }
    public void initView(){
        fanhui=(TextView)findViewById(R.id.jksearch_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView=(ListView)findViewById(R.id.jksearch_listview);
        et=(EditText)findViewById(R.id.jksearch_et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    keyword = editable.toString();
                    initData();
                }else{
                    list_caidan=new ArrayList<>();
                    listView.setAdapter(new CaipuAdapter01(list_caidan,VideoSearch.this));
                }

            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    initData2();
                    return true;
                }
                return false;
            }
        });
        noresult=(ScrollView)findViewById(R.id.video_search_noresult);
        qiu=(Button)findViewById(R.id.bt_qiu);
        qiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                    startActivity(new Intent(getBaseContext(), CaipuGet.class));
                }else {
                    startActivity(new Intent(getBaseContext(), Login.class));
                }
            }
        });
        likelistview=(ListViewForScrollView)findViewById(R.id.caipu_qiu_like);
    }

    public void initData(){
        noresult.setVisibility(View.GONE);
        list_caidan=new ArrayList<>();
        StringRequest request=new StringRequest(URLMannager.DishSreachKeyword+keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Caidan cd=new Caidan();
                            cd.setId(jo.getString("id"));
                            cd.setTitle(jo.getString("title"));
                            list_caidan.add(cd);
                        }
                        listView.setAdapter(new CaipuAdapter01(list_caidan,VideoSearch.this));

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
    public void initData2(){
        noresult.setVisibility(View.GONE);
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
                        if(list_caidan.size()>0){
                            listView.setAdapter(new CaipuAdapter02(list_caidan,VideoSearch.this));
                        }else {
                            noresult.setVisibility(View.VISIBLE);
                            List <Caidan>likelist=new ArrayList();
                            JSONArray ja2=js2.getJSONArray("like_dish");
                            for (int i = 0; i < ja2.length(); i++) {
                                Caidan mCaidanBean = new Caidan();
                                JSONObject array1_2 = ja2.getJSONObject(i);
                                mCaidanBean.id = array1_2.getString("id");
                                mCaidanBean.title = array1_2.getString("title");
                                mCaidanBean.content = array1_2.getString("content");
                                mCaidanBean.pic = array1_2.getString("pic");
                                if (!array1_2.isNull("stype")) {
                                    mCaidanBean.type = array1_2.getString("stype");
                                } else {
                                    mCaidanBean.type = "";
                                }
                                likelist.add(mCaidanBean);
                            }
                            likelistview.setAdapter(new CaidanAdapter2(likelist, VideoSearch.this, getWindowManager()));

                        }


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
    public class CaipuAdapter01 extends BaseAdapter{
        private List<Caidan> mydata;
        private Context context;
        public CaipuAdapter01(List<Caidan> mydata,Context context){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.adapter_caipu_item01,null);
            TextView tv=(TextView) convertView.findViewById(R.id.caipu_item01_tv);
            tv.setText(mydata.get(position).getTitle());
            final int x=position;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CaipuDetail.class);
                    intent.putExtra("id", mydata.get(x).getId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }
    public class CaipuAdapter02 extends BaseAdapter{
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
}
