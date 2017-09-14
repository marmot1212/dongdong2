package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class JKsearch extends AppCompatActivity {
    private List<String> list_item;
    private ListView listView;
    private List<Caidan> list_caidan;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jksearch);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list_item=new ArrayList<>();
        listView=(ListView)findViewById(R.id.jksearch_list);
        list_caidan=new ArrayList<>();
        initop();
        initView();
    }
    public void initop(){
        TextView fanhui=(TextView)findViewById(R.id.jksearch_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

                if(editable.toString().equals("")){
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                    try {
                        httpRequest(URLMannager.DishSreachKeyword+URLEncoder.encode(editable.toString(),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        FrameLayout fram=(FrameLayout)findViewById(R.id.jksearch_fram);
        fram.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }
    public void initView(){
        StringRequest request=new StringRequest(URLMannager.DishSearch, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("picinfo");
                    JSONObject jo=ja.getJSONObject(0);
                    final String tid=jo.getString("tid");
                    String pic=jo.getString("pic");
                    ImageView ima=(ImageView) findViewById(R.id.jksearch_ima);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+pic,ima,options);
                    ima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(JKsearch.this, TieziDetial.class);
                            intent.putExtra("tid",tid);
                            JKsearch.this.startActivity(intent);
                        }
                    });
                    String content=jo.getString("title");
                    TextView ct=(TextView)findViewById(R.id.jksearch_content);
                    ct.setText(content);

                    JSONArray ja2=js2.getJSONArray("type");
                    for(int i=0;i<ja2.length();i++){
                        list_item.add(ja2.getString(i));
                    }

                    GridView gv=(GridView)findViewById(R.id.jksearch_grid);
                    gv.setAdapter(new SearchGridAdapter(list_item,getBaseContext()));
                    TextView chun=(TextView)findViewById(R.id.jksearch_chun);
                    TextView dan=(TextView)findViewById(R.id.jksearch_dan);
                    chun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listView.setVisibility(View.VISIBLE);
                            try {
                                httpRequest(URLMannager.Caidan_URL+"/type/"+ URLEncoder.encode("纯素","utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listView.setVisibility(View.VISIBLE);
                            try {
                                httpRequest(URLMannager.Caidan_URL+"/type/"+URLEncoder.encode("蛋奶素","utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JKsearch.this).addToRequestQueue(request);
    }
    public class SearchGridAdapter extends BaseAdapter{
        private List<String> mydata;
        private Context context;

        public SearchGridAdapter(List<String> mydata,Context context){
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            TextView tv=new TextView(context);
            tv.setText(mydata.get(i));
            tv.setTextColor(0xff000000);
            tv.setHeight(80);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(14);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listView.setVisibility(View.VISIBLE);
                    try {
                        httpRequest(URLMannager.Caidan_URL+"/type/"+ URLEncoder.encode(mydata.get(i),"utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            return tv;
        }
    }
    public void httpRequest(String url){
        list_caidan=new ArrayList<>();
        StringRequest request2=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject jsonObj1 = new JSONObject(s);
                    JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                    JSONArray array1 = jsonObj2.getJSONArray("list");
                    for (int i = 0; i < array1.length(); i++) {
                        Caidan mCaidanBean = new Caidan();
                        JSONObject array1_2 = array1.getJSONObject(i);
                        mCaidanBean.id = array1_2.getString("id");
                        mCaidanBean.title = array1_2.getString("title");
                        mCaidanBean.content = array1_2.getString("content");
                        mCaidanBean.pic = array1_2.getString("pic");
                        if(!array1_2.isNull("stype")){
                            mCaidanBean.type=array1_2.getString("stype");
                        }else {
                            mCaidanBean.type="";
                        }
                        list_caidan.add(mCaidanBean);
                    }

                   listView.setAdapter(new CaidanAdapter(list_caidan,JKsearch.this));

                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(),"找不到结果",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JKsearch.this).addToRequestQueue(request2);
    }

    //触摸退键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
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
