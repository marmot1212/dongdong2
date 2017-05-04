package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.fragment.MapFragment;
import com.example.administrator.vegetarians824.mannager.URLMannager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapSearch extends AppCompatActivity {
    private ListView listView;
    private List<String> ll;
    public Map map_search;
    private List<CantingInfo> list_ct;
    private EditText et;
    private String city;
    LinearLayout hd,jd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        listView=(ListView)findViewById(R.id.mapsearch_list);
        et=(EditText)findViewById(R.id.mapsearch_et);
        Intent intent=getIntent();
        city=intent.getStringExtra("city");
        if(intent.hasExtra("tex")&&(!intent.getStringExtra("tex").isEmpty())){
            et.setText(intent.getStringExtra("tex"));
            et.setSelection(intent.getStringExtra("tex").length());
        }
        InputMethodManager inputManager =(InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(et, 0);
        inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);

         hd=(LinearLayout)findViewById(R.id.huodong_jump);
         jd=(LinearLayout)findViewById(R.id.jiudian_jump);
        list_ct=new ArrayList<>();
        initView();
        initop();
        initList();
    }
    public void initView(){
        StringRequest request=new StringRequest(URLMannager.MiShiSearch+city, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("picinfo");
                    JSONObject jo=ja.getJSONObject(0);
                    final String tid=jo.getString("tid");
                    String pic=jo.getString("pic");
                    ImageView ima=(ImageView) findViewById(R.id.mapsearch_ima);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+pic,ima,options);
                    ima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(MapSearch.this, TieziDetial.class);
                            intent.putExtra("tid",tid);
                            MapSearch.this.startActivity(intent);
                        }
                    });
                    String content=jo.getString("title");
                    TextView ct=(TextView)findViewById(R.id.mapsearch_content);
                    ct.setText(content);
                    String hotelnum=js2.getString("city_hotel");
                    if(hotelnum.equals("1")){
                        jd.setVisibility(View.INVISIBLE);
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
        SlingleVolleyRequestQueue.getInstance(MapSearch.this).addToRequestQueue(request);
    }
    public void initop(){
        TextView fanhui=(TextView)findViewById(R.id.mapsearch_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapSearch.this,HuodongDiqu.class);
                intent.putExtra("province",city);
                MapSearch.this.startActivity(intent);
            }
        });

        jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapSearch.this,Jiudian.class);
                intent.putExtra("city",city);
                MapSearch.this.startActivity(intent);
            }
        });
    }
    public void initList(){
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
                    searchRequest(editable.toString());
                }

            }
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i== KeyEvent.KEYCODE_ENTER){
                        Intent intent = new Intent(MapSearch.this, MapFragment.class);
                        intent.putExtra("keyword", et.getText().toString());
                        setResult(12, intent);
                        finish();// 关闭此界面
                    return true;
                }
                return false;
            }
        });

        FrameLayout fram=(FrameLayout)findViewById(R.id.mapsearch_fram);
        fram.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }
    //触摸退键盘

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    public void searchRequest(String s){
        list_ct=new ArrayList<>();
        StringBuffer url=new StringBuffer();
        try {
            url.append(URLMannager.MiShiSearchKeyword).append(URLEncoder.encode(s, "utf-8")).append("/city/").append(URLEncoder.encode(city, "utf-8"));
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        }
        StringRequest request=new StringRequest(url.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject js1=new JSONObject(s);
                        JSONArray ja=js1.getJSONArray("Result");
                        for (int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            CantingInfo ct=new CantingInfo();
                            ct.setTitle(jo.getString("title"));
                            ct.setId(jo.getString("id"));
                            ct.setType(jo.getString("type"));
                            list_ct.add(ct);
                        }

                        if(list_ct.size()>0){
                            SearchAdapter sa=new SearchAdapter(list_ct,getBaseContext());
                            listView.setAdapter(sa);
                            sa.notifyDataSetChanged();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();// 关闭此页面
        }

        return false;
    }

    public class SearchAdapter extends BaseAdapter{
        private List<CantingInfo> mydata;
        private Context context;
        public SearchAdapter(List<CantingInfo> mydata,Context context){
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

                view = LayoutInflater.from(context).inflate(R.layout.search_map_item, null);
                TextView tv= (TextView) view.findViewById(R.id.search_map_tv);
                tv.setText(mydata.get(i).getTitle());
                ImageView ima = (ImageView) view.findViewById(R.id.search_map_ima);
                if (mydata.get(i).getType().equals("6")) {
                    ima.setImageResource(R.drawable.lenovo2);
                }
                final int x=i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id= mydata.get(x).getId();
                        Intent intent=new Intent(getBaseContext(),CantingDetail.class);
                        intent.putExtra("item_id",id);
                        MapSearch.this.startActivity(intent);
                    }
                });
            return view;
        }
    }
}
