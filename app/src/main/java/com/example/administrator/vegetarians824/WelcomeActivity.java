package com.example.administrator.vegetarians824;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ConnectionNetUtils;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class WelcomeActivity extends AppCompatActivity {
    private LinearLayout mLayout;
    private Animation mAlphaAnimation;
    private android.support.v7.app.AlertDialog.Builder builder;
    public AMapLocationClient mLocationClient=null;
    public AMapLocationClientOption mLocationOption = null;
    Boolean isHaveNet;
    private ProgressDialog progressDialog;
    public AMapLocationListener mLocationListener=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StatusBarUtil.setTranslucent(WelcomeActivity.this);
        //进来先定个位，激活gps
        mLocationListener=new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        BaseApplication.app.setMyLociation(new MyLociation(String
                                .valueOf(amapLocation.getLongitude()), String
                                .valueOf(amapLocation.getLatitude()), String
                                .valueOf(amapLocation.getCity()),String.valueOf(amapLocation.getAddress()))
                        );

                    }
                }
            }
        };
        initViews();
        gps();
        initDatas();
        initOpers();
        getUser();
        //创建消息推送
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"p4FCm8tu8MqD4BLDuHvuwRX4");
    }
    //创建用户信息，用户如果第一次使用初始化用户信息，不是第一次则SharedPreferences加载存在本地的用户信息
    public void getUser(){
        SharedPreferences pre=getSharedPreferences("shared", Context.MODE_PRIVATE);
        if((pre!=null)&&!pre.getString("id","").equals("")&&pre.getBoolean("islog",false)){
            User user=new User();
            user.setId(pre.getString("id",""));
            user.setName(pre.getString("username",""));
            user.setPwd(pre.getString("password",""));
            user.setMobile(pre.getString("mobile",""));
            user.setPic(pre.getString("user_head_img",""));
            user.setStatus(pre.getString("status",""));
            user.setProvince(pre.getString("province",""));
            user.setCity(pre.getString("city",""));
            user.setSex(pre.getString("sex",""));
            user.setIntro(pre.getString("intro",""));
            user.setJifen(pre.getString("jifen",""));
            user.setIslogin(pre.getBoolean("islog",false));
            user.setType(pre.getString("type",""));
            user.setTenans(pre.getString("tenans",""));
            BaseApplication.app.setUser(user);
        }
        else{
            BaseApplication.getApp().setUser(new User());
            BaseApplication.getApp().getUser().initUser();
        }
    }
    public void initViews() {
        // TODO 获取布局id
        mLayout = (LinearLayout) findViewById(R.id.welcome);
    }
    public void initDatas() {
        // TODO 获取在资源文件夹定义的动画渐变的一个方法
        mAlphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
    }
    public void initOpers(){
        mLayout.setAnimation(mAlphaAnimation);
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                 isHaveNet = ConnectionNetUtils.isConnectionNet(WelcomeActivity.this);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                    if(isHaveNet) {
                        Intent intent = new Intent(WelcomeActivity.this, FirstActivity.class);
                        WelcomeActivity.this.startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                else{
                        Toast.makeText(getBaseContext(),"网络异常",Toast.LENGTH_SHORT).show();
                        popDialog();
                    }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画执行
            }
        });
    }
    public void gps(){
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        mLocationOption.setOnceLocation(true);
        //启动定位
        mLocationClient.startLocation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
    }
    //网络状态提升
    private void popDialog() {
        builder = new android.support.v7.app.AlertDialog.Builder(WelcomeActivity.this);
        builder.setTitle("设置网络").setMessage("当前没有网络请您设置网络")
                .setCancelable(false)// 设置网络是否可以来接
                .setPositiveButton("确定设置网络", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO 跳转到设置网络的Activity
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO 如果不为空表示刚刚设置了网络
        if (builder != null) {
            progressDialog = new ProgressDialog(this);

            // TODO 在Android中实现异步操作的一种方式
            AsyncTask<Void, String, Void> task = new AsyncTask<Void, String, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.show();
                    progressDialog.setMessage("获取网络\n\r.");
                }

                // TODO 子线程中做耗时操作
                @Override
                protected Void doInBackground(Void... params) {
                    // TODO 持续判断网络
                    for (int i = 1; i < 21; i++) {
                        isHaveNet = ConnectionNetUtils
                                .isConnectionNet(WelcomeActivity.this);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isHaveNet) {// TODO 这里是有网的状态
                            i = 21;
                        }

                        // TODO 通知主线程
                        switch (i % 6) {
                            case 1:
                                publishProgress("获取网络状态\n\r.");
                                break;

                            case 2:
                                publishProgress("获取网络状态\n\r..");
                                break;
                            case 3:
                                publishProgress("获取网络状态\n\r...");
                                break;
                            case 4:
                                publishProgress("获取网络状态\n\r....");
                                break;
                            case 5:
                                publishProgress("获取网络状态\n\r.....");
                                break;

                        }
                    }
                    return null;
                }

                // TODO 实时更新UI
                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    progressDialog.setMessage(values[0]);

                }

                // TODO 退出时如果有网则退出进度匡 如果没网继续进行获取网络状态
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    if (isHaveNet) {
                        progressDialog.dismiss();
                        Intent intent = new Intent(WelcomeActivity.this, FirstActivity.class);
                        WelcomeActivity.this.startActivity(intent);
                        WelcomeActivity.this.finish();
                    } else {
                        popDialog();
                    }
                    progressDialog.dismiss();// TODO 取消进度对话框
                }
            };
            task.execute();// 执行中

        }

    }
}
