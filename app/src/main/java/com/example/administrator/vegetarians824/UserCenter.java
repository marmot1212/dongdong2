package com.example.administrator.vegetarians824;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.dongdong.MyCalender;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.JifenRank;
import com.example.administrator.vegetarians824.mine.MyAdmin;
import com.example.administrator.vegetarians824.mine.MyCode;
import com.example.administrator.vegetarians824.mine.MyCollect;
import com.example.administrator.vegetarians824.mine.MyCollect2;
import com.example.administrator.vegetarians824.mine.MyDianping;
import com.example.administrator.vegetarians824.mine.MyDuihuan;
import com.example.administrator.vegetarians824.mine.MyEdit;
import com.example.administrator.vegetarians824.mine.MyFabu;
import com.example.administrator.vegetarians824.mine.MyFankui;
import com.example.administrator.vegetarians824.mine.MySetting;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class UserCenter extends AppCompatActivity {
    RoundImageView rpic;
    private User user;
    PopupWindow popWindow;
    private static String maxbackid="";
    public static UserCenter instance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        instance=this;
        user= BaseApplication.app.getUser();
        if(user!=null)
            getInfo();
        initop();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.usercenter_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout rili=(LinearLayout)findViewById(R.id.line_rili);
        rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserCenter.this, MyCalender.class);
                UserCenter.this.startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        //判断是否用户是否登录，是，加载用户信息
        rpic=(RoundImageView)findViewById(R.id.my_pic);
        TextView name=(TextView)findViewById(R.id.my_name);
        if(user.islogin()){
            if(user.getPic()!=null){
                com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
                DisplayImageOptions options=ImageLoaderUtils.getOpt();
                loader.displayImage(URLMannager.Imag_URL+""+user.getPic(),rpic,options);
            }
            if(user.getName()!=null){
                name.setText(user.getName());
            }
        }
        else
        {
            rpic.setImageResource(R.drawable.cc_touxiang);
            name.setText("");
        }
        rpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable=rpic.getDrawable();
                getPicPop(drawable,view);
            }
        });


        //设置
        ImageView setting=(ImageView) findViewById(R.id.my_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()){
                    Intent intent = new Intent(getBaseContext(), MySetting.class);
                    UserCenter.this.startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });

        //编辑

        ImageView edit = (ImageView) findViewById(R.id.my_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getBaseContext(), MyEdit.class);
                    UserCenter.this.startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });


        //积分
        if(user.islogin()) {
            TextView jifen = (TextView) findViewById(R.id.my_jifen);
            jifen.setText(Float.valueOf(user.getJifen())+"");
            View jindu = findViewById(R.id.my_jindu);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(user.getJifen()));
            jindu.setLayoutParams(param);
            jindu.requestLayout();
            View jindu2 = findViewById(R.id.my_jindu22);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(user.getJifen()));
            jindu2.setLayoutParams(param2);
            jindu2.requestLayout();
        }

        //积分排名
        LinearLayout paiming=(LinearLayout)findViewById(R.id.my_paiming);
        paiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), JifenRank.class);
                UserCenter.this.startActivity(intent);
            }
        });

        //下部列表
        //我的发布
        LinearLayout fabu=(LinearLayout)findViewById(R.id.my_fabu);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.islogin()) {
                    Intent intent = new Intent(getBaseContext(), MyFabu.class);
                    UserCenter.this.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });
        //我的点评
        LinearLayout dianping=(LinearLayout)findViewById(R.id.my_dianping);
        dianping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.islogin()) {
                    Intent intent = new Intent(getBaseContext(), MyDianping.class);
                    UserCenter.this.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });
        //我的收藏
        LinearLayout collect=(LinearLayout)findViewById(R.id.my_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.islogin()) {
                    Intent intent = new Intent(getBaseContext(), MyCollect2.class);
                    UserCenter.this.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });
        //我的反馈
        LinearLayout fankui=(LinearLayout)findViewById(R.id.my_fankui);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.islogin()){
                    Intent intent=new Intent(getBaseContext(), MyFankui.class);
                    UserCenter.this.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });
        //我的优惠码
        LinearLayout code=(LinearLayout)findViewById(R.id.my_code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.islogin()){
                    Intent intent=new Intent(getBaseContext(), MyCode.class);
                    UserCenter.this.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    UserCenter.this.startActivity(intent);
                }
            }
        });
        //商家认证
        LinearLayout shangjia=(LinearLayout)findViewById(R.id.my_shangjia);
        shangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(BaseApplication.getApp().getUser().getTenans().equals("1")){
                        Intent intent=new Intent(getBaseContext(), SellerCenter.class);
                        UserCenter.this.startActivity(intent);
                    }else {
                        Intent intent=new Intent(getBaseContext(), MyAdmin.class);
                        intent.putExtra("flag",1);
                        UserCenter.this.startActivity(intent);
                    }

            }
        });

        StatService.onResume(this);
    }
    public void getPicPop(Drawable drawable,View view){
        View popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.image_pop,null);
        ImageView ima=(ImageView) popView.findViewById(R.id.image_show);
        ima.setImageDrawable(drawable);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.width=Width;
        params.height=Width;
        ima.setLayoutParams(params);
        ima.requestLayout();
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
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onStart() {
        super.onStart();
        ;if(BaseApplication.app.getUser().islogin()){
            httpRequest();
        }
        if(user.islogin())
            getInfo2();
    }
    public void httpRequest(){

        StringPostRequest spr=new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    String code=js1.getString("Code");
                    if(code.equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        BaseApplication.app.getUser().setName(js2.getString("username"));
                        BaseApplication.app.getUser().setMobile(js2.getString("mobile"));
                        BaseApplication.app.getUser().setPic(js2.getString("user_head_img"));
                        BaseApplication.app.getUser().setProvince(js2.getString("province"));
                        BaseApplication.app.getUser().setCity(js2.getString("city"));
                        BaseApplication.app.getUser().setSex(js2.getString("sex"));
                        BaseApplication.app.getUser().setIntro(js2.getString("intro"));
                        BaseApplication.app.getUser().setJifen(js2.getString("jifen"));
                        BaseApplication.app.getUser().setTenans(js2.getString("tenans"));
                        SharedPreferences preferences=getBaseContext().getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("username",js2.getString("username"));
                        editor.putString("mobile",js2.getString("mobile"));
                        editor.putString("user_head_img",js2.getString("user_head_img"));
                        editor.putString("province",js2.getString("province"));
                        editor.putString("city",js2.getString("city"));
                        editor.putString("sex",js2.getString("sex"));
                        editor.putString("intro",js2.getString("intro"));
                        editor.putString("jifen",js2.getString("jifen"));
                        editor.putString("tenans",js2.getString("tenans"));
                        editor.commit();//提交数据
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
    public void getInfo(){
        StringPostRequest spr=new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    final String url=js2.getString("exchangelink");
                    if(js2.has("maxbackid")){
                        maxbackid=js2.getString("maxbackid");
                    }
                    TextView duihuan=(TextView) findViewById(R.id.my_duihuan);
                    duihuan.setVisibility(View.VISIBLE);
                    duihuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getBaseContext(), MyDuihuan.class);
                            intent.putExtra("url",url);
                            UserCenter.this.startActivity(intent);
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
    public void getInfo2(){
        StringPostRequest spr=new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    String back=js2.getString("maxbackid");
                    if(!back.equals(maxbackid)){
                        ImageView mark=(ImageView) findViewById(R.id.my_mark);
                        mark.setVisibility(View.VISIBLE);
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
