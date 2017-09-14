package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.UserFabu;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AWord2 extends AppCompatActivity {
    private String id,name;
    private String veg,egg,pic;
    private TextView word;
    private LinearLayout pop;
    private PopupWindow popupWindow;
    private ImageView danchun;
    private View head;
    private PullToRefreshListView prl;
    private List<UserFabu> list;
    private String totalpage;
    private com.nostra13.universalimageloader.core.ImageLoader loader;
    private DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aword2);
        list=new ArrayList<>();
        totalpage="";
        head=getLayoutInflater().inflate(R.layout.aword_head,null);
        prl=(PullToRefreshListView)findViewById(R.id.aword2_prl);
        Intent intent=getIntent();
        pop=(LinearLayout)findViewById(R.id.word2_pop);
        danchun=(ImageView)findViewById(R.id.danchun);
        name=intent.getStringExtra("cname");
        id=intent.getStringExtra("cid");


        initoperate();
        HttpRequest();

    }
    public  void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.word2_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView country=(TextView) findViewById(R.id.aword2_name);
        country.setText(name);

    }
    public void HttpRequest(){
        StringRequest request=new StringRequest(URLMannager.Country_Detial + id+"/p/1/t/100", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    pic=js2.getString("pic");
                    JSONObject js3=js2.getJSONObject("language");
                    veg=js3.getString("veg");
                    egg=js3.getString("egg");
                    ImageView imageView=(ImageView)head.findViewById(R.id.word2_image);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(AWord2.this);
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+pic,imageView,options);
                    StatusBarUtil.setTranslucentForImageView(AWord2.this,imageView);
                    word=(TextView)head.findViewById(R.id.word2_word);
                    word.setText(egg);
                    pop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            getPopwindow();
                            LinearLayout lin = (LinearLayout) findViewById(R.id.tools3);
                            popupWindow.showAsDropDown(lin);

                                //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0,150);

                        }
                    });

                    prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
                    prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
                    prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据");
                    // 设置上拉刷新文本
                    prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
                    prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
                    prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据");
                    //设置加载动画
                    prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                    prl.getRefreshableView().addHeaderView(head,null,false);

                    if(!js2.isNull("country_posts")){
                        LinearLayout ima=(LinearLayout) head.findViewById(R.id.aword_up);
                        ima.setVisibility(View.VISIBLE);
                        JSONObject js4=js2.getJSONObject("country_posts");
                        totalpage=js4.getString("totalpage");
                        JSONArray jaa=js4.getJSONArray("list");
                        for(int i=0;i<jaa.length();i++) {
                            UserFabu uf = new UserFabu();
                            JSONObject joo = jaa.getJSONObject(i);
                            uf.setId(joo.getString("id"));
                            uf.setUid(joo.getString("uid"));
                            uf.setTitle(joo.getString("title"));
                            uf.setContent(joo.getString("content"));
                            uf.setUname(joo.getString("u_username"));
                            uf.setUhead(joo.getString("u_user_head_img"));
                            uf.setCreate_time_text(joo.getString("create_time_text"));
                            uf.setCommentcount(joo.getString("comment_count"));
                            if (!joo.isNull("img_url_th_1")) {
                                uf.setImg_url_1(joo.getString("img_url_th_1"));
                            }
                            if (!joo.isNull("img_url_th_2")) {
                                uf.setImg_url_2(joo.getString("img_url_th_2"));
                            }
                            if (!joo.isNull("img_url_th_3")) {
                                uf.setImg_url_3(joo.getString("img_url_th_3"));
                            }
                            if (!joo.isNull("img_url_th_4")) {
                                uf.setImg_url_4(joo.getString("img_url_th_4"));
                            }
                            if (!joo.isNull("img_url_th_5")) {
                                uf.setImg_url_5(joo.getString("img_url_th_5"));
                            }
                            if (!joo.isNull("img_url_th_6")) {
                                uf.setImg_url_6(joo.getString("img_url_th_6"));
                            }
                            list.add(uf);
                        }

                    }else {
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    if(totalpage.equals("1")){
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    prl.setAdapter(new AwordAdapter(list,getBaseContext()));
                    if(list.size()>0){
                        TextView tv=new TextView(getBaseContext());
                        tv.setText("已经全部加载完毕");
                        tv.setTextSize(12);
                        tv.setTextColor(0xffa0a0a0);
                        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                        tv.setLayoutParams(params);
                        tv.setGravity(Gravity.CENTER);
                        prl.getRefreshableView().addFooterView(tv);
                    }
                    /*
                    prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                        @Override
                        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                        }

                        @Override
                        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                        }
                    });
                    */
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
    public void getPopwindow(){
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_word, null,
                    false);
            popupWindow_view.setFocusable(true);
            // 创建PopupWindow实例
            popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(new BitmapDrawable());

            // 设置动画效果
            popupWindow.setAnimationStyle(R.style.AnimationFade);

            popupWindow.setOutsideTouchable(true);

            popupWindow.setFocusable(true);
            // 点击其他地方消失
            popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    return false;
                }
            });

            //popWindow内容跳转
            TextView chunsu=(TextView) popupWindow_view.findViewById(R.id.word_chunsu);
            chunsu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    word.setText(veg);
                    danchun.setImageResource(R.mipmap.trip_dannaisu);
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
            TextView dannai=(TextView)popupWindow_view.findViewById(R.id.word2_dannai);
            dannai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    word.setText(egg);
                    danchun.setImageResource(R.mipmap.trip_chusnsu);
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            });
        }


    }

    public class AwordAdapter extends BaseAdapter{
        private List<UserFabu> mydata;
        private Context context;
        public AwordAdapter(List<UserFabu> mydata,Context context){
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
            view = LayoutInflater.from(context).inflate(R.layout.aword_item,null);
            MyImageView uhead=(MyImageView)view.findViewById(R.id.aword_item_head);
            loader= ImageLoaderUtils.getInstance(context);
            options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getUhead(),uhead,options);
            TextView tv1=(TextView)view.findViewById(R.id.aword_item_title) ;
            TextView tv2=(TextView)view.findViewById(R.id.aword_item_uname) ;
            TextView tv3=(TextView)view.findViewById(R.id.aword_item_time) ;
            TextView tv4=(TextView)view.findViewById(R.id.aword_item_content);
            tv1.setText(mydata.get(i).getTitle());
            tv2.setText(mydata.get(i).getUname());
            tv3.setText(mydata.get(i).getCreate_time_text());
            tv4.setText(mydata.get(i).getContent());
            LinearLayout l=(LinearLayout) view.findViewById(R.id.aword_line);
            LinearLayout l2=(LinearLayout) view.findViewById(R.id.aword_line2);
            if(mydata.get(i).getImg_url_1()==null){
                l.removeAllViews();

            }else if(mydata.get(i).getImg_url_4()==null){
                l.removeView(l2);
            }else {
                if(mydata.get(i).getImg_url_1()!=null){
                    MyImageView im1=(MyImageView) view.findViewById(R.id.aword_ima1);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_1(),im1,options);
                }
                if(mydata.get(i).getImg_url_2()!=null){
                    MyImageView im2=(MyImageView) view.findViewById(R.id.aword_ima2);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_2(),im2,options);
                }
                if(mydata.get(i).getImg_url_3()!=null){
                    MyImageView im3=(MyImageView) view.findViewById(R.id.aword_ima3);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_3(),im3,options);
                }
                if(mydata.get(i).getImg_url_4()!=null){
                    MyImageView im4=(MyImageView) view.findViewById(R.id.aword_ima4);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_4(),im4,options);
                }
                if(mydata.get(i).getImg_url_5()!=null){
                    MyImageView im5=(MyImageView) view.findViewById(R.id.aword_ima5);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_5(),im5,options);
                }
                if(mydata.get(i).getImg_url_6()!=null){
                    MyImageView im6=(MyImageView) view.findViewById(R.id.aword_ima6);
                    loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getImg_url_6(),im6,options);
                }
            }
            final  int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(AWord2.this,Tiezi2Detial.class);
                    intent.putExtra("tid",mydata.get(x).getId());
                    AWord2.this.startActivity(intent);
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
