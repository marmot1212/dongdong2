package com.example.administrator.vegetarians824.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.dongdong.Tiezi3Detial;
import com.example.administrator.vegetarians824.dongdong.TieziDetial;
import com.example.administrator.vegetarians824.entry.UserFabu;
import com.example.administrator.vegetarians824.entry.UserRank;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
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

public class UserDetial extends AppCompatActivity {
    String id,uid;
    View vv;
    private int count;//第几次加载
    private int totalpage;//总页数
    private List<UserFabu> list;//帖子列表
    private PullToRefreshListView prl;
    private UserFabuAdapter adapter;
    private boolean isup;
    private boolean iszan;
    TextView zannum;
    ImageView zanima;
    String num;
    PopupWindow popWindow;
    ImageView hima;
     int xx;
    private Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detial);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        uid=intent.getStringExtra("uid");
        vv=getLayoutInflater().inflate(R.layout.user_head,null);
        prl=(PullToRefreshListView) findViewById(R.id.userdetial_prl);
        count=1;
        isup=false;
        iszan=false;
        list=new ArrayList<>();
        date=new Date();
        initHead();
        initprl();
        initop();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.userdetial_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initHead(){
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainliving/Androidapi/List/userProfiles/uid/"+uid+"/id/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("userData");
                    JSONObject js3=ja.getJSONObject(0);
                    //头像
                    String pic=js3.getString("user_head_img");
                    hima=(ImageView) vv.findViewById(R.id.userdetial_headima);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(UserDetial.this);
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+pic,hima,options);
                    hima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getPicPop(hima.getDrawable(),"");
                        }
                    });
                    //赞
                    num=js3.getString("laud_count");

                     zannum=(TextView)vv.findViewById(R.id.userdetial_zannum);
                    zanima=(ImageView) vv.findViewById(R.id.userdetial_zan);

                    zannum.setText(num);
                    String zan=js3.getString("laud_status");
                    if(zan.equals("0")){
                        iszan=false;
                    }else {
                        iszan=true;
                        zanima.setImageResource(R.drawable.zanon2);
                    }
                    xx=Integer.valueOf(num);
                    zanima.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(BaseApplication.app.getUser().islogin()){
                                if(iszan){
                                    zanima.setImageResource(R.drawable.zanoff2);
                                    iszan=false;
                                    xx=xx-1;
                                    zannum.setText(xx+"");
                                    cancelzan();
                                }else {
                                    zanima.setImageResource(R.drawable.zanon2);
                                    iszan=true;
                                    xx=xx+1;
                                    zannum.setText(xx+"");
                                    addzan();
                                }
                            }
                            else {
                                Intent intent=new Intent(UserDetial.this, Login.class);
                                UserDetial.this.startActivity(intent);
                            }
                        }
                    });

                    //名字
                    String name=js3.getString("username");
                    TextView namet=(TextView)vv.findViewById(R.id.userdetial_name);
                    namet.setText(name);
                    String grade=js3.getString("lv");
                    ImageView lv=(ImageView)vv.findViewById(R.id.userdetial_grade);
                    switch (grade){
                        case "1":lv.setImageResource(R.mipmap.lv1);break;
                        case "2":lv.setImageResource(R.mipmap.lv2);break;
                        case "3":lv.setImageResource(R.mipmap.lv3);break;
                        default:break;
                    }
                    //城市
                    String city=js3.getString("city");
                    TextView cityt=(TextView)vv.findViewById(R.id.userdetial_city);
                    if(!city.equals("null"))
                        cityt.setText(city);
                    //个人说明
                    String intro=js3.getString("intro");
                    TextView introt=(TextView)vv.findViewById(R.id.userdetial_intro);
                    if(!intro.equals("null"))
                        introt.setText(intro);
                    //赞的人
                    LinearLayout zanadd=(LinearLayout)vv.findViewById(R.id.zan_addline);
                    JSONArray jaa=js3.getJSONArray("laudlist");
                    for(int i=0;i<jaa.length();i++){
                        JSONObject jo=jaa.getJSONObject(i);
                        RoundImageView imas=new RoundImageView(getBaseContext());
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(60,60);
                        params.setMargins(15,0,0,0);
                        imas.setLayoutParams(params);
                        com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(UserDetial.this);
                        DisplayImageOptions options2=ImageLoaderUtils.getOpt();
                        loader2.displayImage(URLMannager.Imag_URL+""+jo.getString("user_head_img"),imas,options2);
                        final String id=jo.getString("id");
                        imas.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(UserDetial.this,UserDetial.class);
                                intent.putExtra("uid",id);
                                if(BaseApplication.app.getUser().islogin()){
                                    intent.putExtra("id",BaseApplication.app.getUser().getId());
                                }else {
                                    intent.putExtra("id","");
                                }
                                UserDetial.this.startActivity(intent);
                            }
                        });
                        zanadd.addView(imas);
                    }
                    //积分
                    String jf=js3.getString("jifen");
                    TextView jifen = (TextView) vv.findViewById(R.id.myuser_jifen);
                    jifen.setText(jf);
                    View jindu = vv.findViewById(R.id.myuser_jindu);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(jf));
                    jindu.setLayoutParams(param);
                    jindu.requestLayout();
                    View jindu2 = vv.findViewById(R.id.my_jindu2);
                    LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(jf));
                    jindu2.setLayoutParams(param2);
                    jindu2.requestLayout();

                    LinearLayout jfline=(LinearLayout)vv.findViewById(R.id.my_paiming);
                    jfline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Intent intent=new Intent(UserDetial.this,JifenRank.class);
                                UserDetial.this.startActivity(intent);
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
        SlingleVolleyRequestQueue.getInstance(UserDetial.this).addToRequestQueue(request);
    }

    public void initprl(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("放开刷新...");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);

        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        prl.getRefreshableView().addHeaderView(vv);

        getData();
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list=new ArrayList<UserFabu>();
                count=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
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
                getData();
            }
            else{
                Toast.makeText(getBaseContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public void getData(){
        String url="";
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
            url="http://www.isuhuo.com/plainliving/Androidapi/List/userProfiles/uid/"+uid+"/id/"+id+ "/p/" + count + "/t/10";
        }else {
            url="http://www.isuhuo.com/plainliving/Androidapi/List/userProfiles/uid/"+uid+"/p/" + count + "/t/10";
        }
        Log.d("================lis",url);
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                adapter= new UserFabuAdapter(list,getBaseContext());
                prl.setAdapter(adapter);
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
            JSONObject js3=js2.getJSONObject("listData");
            String st=js3.getString("totalpage");
            totalpage=Integer.valueOf(st);
            JSONArray ja=js3.getJSONArray("list");
            for(int i=0;i<ja.length();i++){
                JSONObject jo=ja.getJSONObject(i);
                UserFabu ub=new UserFabu();
                ub.setId(jo.getString("id"));
                ub.setContent(jo.getString("content"));
                ub.setCreate_time(jo.getString("create_time"));
                ub.setCreate_time_text(jo.getString("create_time_text"));
                ub.setTitle(jo.getString("title"));
                ub.setStype(jo.getString("stype"));
                ub.setUid(jo.getString("uid"));
                if(!jo.isNull("img_url_1")){
                    ub.setImg_url_1(jo.getString("img_url_1"));
                }
                list.add(ub);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class  UserFabuAdapter extends BaseAdapter{
        private List<UserFabu> mydate;
        private Context context;
        public UserFabuAdapter(List<UserFabu> mydate,Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.userfabu_item,null);
            final LinearLayout line=(LinearLayout)view.findViewById(R.id.userfabu_line);
            TextView t1=(TextView) view.findViewById(R.id.userfabu_title);
            TextView t2=(TextView) view.findViewById(R.id.userfabu_content);
            TextView t3=(TextView) view.findViewById(R.id.userfabu_time);
            t1.setText(mydate.get(i).getTitle());
            t2.setText(mydate.get(i).getContent());
            t3.setText(mydate.get(i).getCreate_time_text());
            final ImageView ima=(ImageView)view.findViewById(R.id.userfabu_ima);
            if(mydate.get(i).getImg_url_1()!=null){


                com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(context);
                DisplayImageOptions options2=ImageLoaderUtils.getOpt();
                loader2.displayImage(URLMannager.Imag_URL+""+mydate.get(i).getImg_url_1(),ima,options2);

                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                int Width = display.getWidth();
                ViewGroup.LayoutParams params=ima.getLayoutParams();
                double n=(double)Width/params.width;
                params.width=Width;
                params.height=(int)(Width*n);
                ima.setLayoutParams(params);
                ima.requestLayout();
                ima.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
                ima.setLayoutParams(lLayoutlayoutParams);
                ima.requestLayout();

                ima.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getPicPop(null,mydate.get(i).getImg_url_1());
                    }
                });
            }else {
                line.removeView(ima);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   switch (mydate.get(i).getStype()){
                       case "7":
                           Intent intent=new Intent(UserDetial.this,CaipuDetail.class);
                           intent.putExtra("id",mydate.get(i).getId());
                           UserDetial.this.startActivity(intent);
                           break;
                       case "1":
                           Intent intent2=new Intent(UserDetial.this, CantingDetail.class);
                           intent2.putExtra("item_id",mydate.get(i).getId());
                           UserDetial.this.startActivity(intent2);
                           break;
                       case "4":
                           Intent intent3=new Intent(UserDetial.this, HuodongDetail.class);
                           intent3.putExtra("id",mydate.get(i).getId());
                           UserDetial.this.startActivity(intent3);
                           break;
                       case "3":
                           Intent intent4=new Intent(UserDetial.this, Tiezi3Detial.class);
                           intent4.putExtra("tid",mydate.get(i).getId());
                           UserDetial.this.startActivity(intent4);
                           break;
                   }
                }
            });
            return view;
        }
    }

    public void addzan(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/addUserlaud", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("cover_id",uid);
        SlingleVolleyRequestQueue.getInstance(UserDetial.this).addToRequestQueue(spr);
    }
    public void cancelzan(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/cancelUserlaud", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("cover_id",uid);
        SlingleVolleyRequestQueue.getInstance(UserDetial.this).addToRequestQueue(spr);
    }

    public void getPicPop(Drawable drawable,String url){
        View popView =LayoutInflater.from(getBaseContext()).inflate(R.layout.image_pop,null);
        ImageView ima=(ImageView) popView.findViewById(R.id.image_show);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.width=Width;
        params.height=Width;
        ima.setLayoutParams(params);
        ima.requestLayout();
        if(url.equals("")){
            ima.setImageDrawable(drawable);
        }else {
            com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(getBaseContext());
            DisplayImageOptions options2=ImageLoaderUtils.getOpt();
            loader2.displayImage(URLMannager.Imag_URL+""+url,ima,options2);
        }
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setAnimationStyle(R.anim.alpha);
        // 获取光标
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(hima, Gravity.CENTER, 0, 0);

    }

}
