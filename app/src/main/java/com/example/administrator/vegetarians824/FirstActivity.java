package com.example.administrator.vegetarians824;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.fabu.FabuFD;
import com.example.administrator.vegetarians824.fabu.FabuShHd;
import com.example.administrator.vegetarians824.fragment.MapFragment;
import com.example.administrator.vegetarians824.fragment.SuFragment;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

/*
*第一次进入的首页
*
*/
public class FirstActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    TextView map,su;
    ImageView touxiang;
    FrameLayout add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initView();
        versionAch();
    }

    //初始化视图
    public void initView(){
        touxiang=(ImageView) findViewById(R.id.first_touxiang);
        add=(FrameLayout)findViewById(R.id.add_00);
        su=(TextView)findViewById(R.id.first_su);
        map=(TextView)findViewById(R.id.first_map);
        fm=getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.home_frag,new SuFragment());
        ft.commit();
        //发布菜谱
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                    Intent intent=new Intent(FirstActivity.this, FabuFD.class);
                    FirstActivity.this.startActivity(intent);
                }else {
                    Intent intent=new Intent(FirstActivity.this, Login.class);
                    FirstActivity.this.startActivity(intent);
                }
            }
        });
        //素生活按钮
        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                su.setTextColor(0xff00aff0);
                su.setBackgroundColor(0xffffffff);
                map.setTextColor(0xffffffff);
                map.setBackgroundResource(R.drawable.button_bg);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.home_frag,new SuFragment());
                ft.commit();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                            Intent intent=new Intent(FirstActivity.this, FabuFD.class);
                            FirstActivity.this.startActivity(intent);
                        }else {
                            Intent intent=new Intent(FirstActivity.this, Login.class);
                            FirstActivity.this.startActivity(intent);
                        }
                    }
                });
            }
        });
        //素食地图按钮
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setTextColor(0xff00aff0);
                map.setBackgroundColor(0xffffffff);
                su.setTextColor(0xffffffff);
                su.setBackgroundResource(R.drawable.button_bg);
                fm=getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.home_frag,new MapFragment());
                ft.commit();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                            Intent intent=new Intent(FirstActivity.this, FabuShHd.class);
                            FirstActivity.this.startActivity(intent);
                        }else {
                            Intent intent=new Intent(FirstActivity.this, Login.class);
                            FirstActivity.this.startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
            httpRequest();
        }

        //重置用户信息
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
            com.nostra13.universalimageloader.core.ImageLoader loader = ImageLoaderUtils.getInstance(getBaseContext());
            DisplayImageOptions options = ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL + "" + BaseApplication.app.getUser().getPic(), touxiang, options);
        }else {
            touxiang.setImageResource(R.drawable.cc_touxiang);
        }
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                    Intent intent=new Intent(FirstActivity.this, UserCenter.class);
                    FirstActivity.this.startActivity(intent);
                }else {
                    Intent intent=new Intent(FirstActivity.this, Login.class);
                    FirstActivity.this.startActivity(intent);
                }
            }
        });
    }
    //版本检验
    public void versionAch(){
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //获取当前版本号
        String version = packInfo.versionName;
        //服务器获取最新版本号
        StringRequest request=new StringRequest("https://isuhuo.com/plainliving/androidapi/Indexs/confirmation_apk/apk_num/"+version, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    String code=js1.getString("Result");
                    if(code.equals("1")){
                        Toast.makeText(getBaseContext(),"您当前版本不是最新版本哦",Toast.LENGTH_SHORT).show();
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
        SlingleVolleyRequestQueue.getInstance(FirstActivity.this).addToRequestQueue(request);
    }
    //首页请求网络更新用户信息
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
}
