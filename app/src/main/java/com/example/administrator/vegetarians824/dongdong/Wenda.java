package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.fabu.FabuTZ;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.EllipsizingTextView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Wenda extends AppCompatActivity {
    private int count;//第几次加载
    private int totalpage;//总页数
    private List<Tiezi> list;//帖子列表
    private List<Tiezi> searchlist;
    private PullToRefreshListView prl;
    private WDAdapter adapter;
    private boolean isup;
    private Date date;
    private ListView listView;
    private EditText et;
    TextView tvfoot;
    View head;
    TextView tvf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wenda);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        date=new Date();
        count=1;
        isup=false;
        prl=(PullToRefreshListView)findViewById(R.id.wenda_prl);
        listView=(ListView)findViewById(R.id.wenda_list);
        list=new ArrayList<>();
        searchlist=new ArrayList<>();
        et=(EditText)findViewById(R.id.wenda_et);
        initop();
        //initPRL();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.wenda_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FrameLayout fabu=(FrameLayout)findViewById(R.id.add_tz);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    Intent intent=new Intent(Wenda.this, FabuTZ.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(Wenda.this, Login.class);
                    startActivity(intent);
                }
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
    public void initPRL(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);
        // 设置上拉刷新文本
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);

        //设置image

        head=getLayoutInflater().inflate(R.layout.wenda_head,null);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Wenda.this,JKtiaozhan.class);
                Wenda.this.startActivity(intent);
            }
        });
        prl.getRefreshableView().addHeaderView(head);


        getData(URLMannager.FaXian2+"p/"+count+"/t/20");


        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate2);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list=new ArrayList<Tiezi>();
                count=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate3);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                date=new Date();
                isup=true;
                new DataRefresh().execute();

            }
        });
    }

    public class DataRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            //给系统2秒时间用来做出反应
            SystemClock.sleep(2000);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prl.onRefreshComplete();
            count++;
            if(count<=totalpage){
                getData(URLMannager.FaXian2+ "p/" + count + "/t/20");
            }
            else{
                Toast.makeText(getBaseContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public void getData(String s){
        StringRequest request=new StringRequest(s, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                adapter= new WDAdapter(list,getBaseContext());
                prl.setAdapter(adapter);
                if(list.size()>0&&prl.getRefreshableView().getFooterViewsCount()==0){
                    tvfoot=new TextView(getBaseContext());
                    tvfoot.setText("已经全部加载完毕");
                    tvfoot.setTextSize(12);
                    tvfoot.setTextColor(0xffa0a0a0);
                    ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                    tvfoot.setLayoutParams(params);
                    tvfoot.setGravity(Gravity.CENTER);
                    prl.getRefreshableView().addFooterView(tvfoot);
                }
                if(isup){
                    prl.getRefreshableView().setSelection(count*10-11);
                    isup=false;
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }

    public void myJson(String s){
        try {
            JSONObject js1=new JSONObject(s);
            JSONObject js2=js1.getJSONObject("Result");
            String st=js2.getString("totalpage");
            totalpage=Integer.valueOf(st).intValue();
            if(totalpage==1){
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
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
                list.add(tz);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            view=LayoutInflater.from(context).inflate(R.layout.wenda_item,null);
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
                    Intent intent=new Intent(Wenda.this,Tiezi3Detial.class);
                    intent.putExtra("tid",mydata.get(x).getId());
                    Wenda.this.startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        list=new ArrayList<>();
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(tvfoot);
        }
        if(prl.getRefreshableView().getHeaderViewsCount()>0){
            prl.getRefreshableView().removeHeaderView(head);
        }
        searchlist=new ArrayList<>();
        initPRL();
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
}
