package com.example.administrator.vegetarians824.mine;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.UserCenter;
import com.example.administrator.vegetarians824.fankui.Fankui;
import com.example.administrator.vegetarians824.fankui.Thanks;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class MySetting extends AppCompatActivity {
    PopupWindow popupWindow;
    String version=null;
    TextView ddversion;
    private android.support.v7.app.AlertDialog.Builder builder;
    private SharedPreferences pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Color1SwitchStyle);
        setContentView(R.layout.activity_my_setting);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        // 获取packagemanager的实例
        pre=getSharedPreferences("shared", Context.MODE_PRIVATE);
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packInfo.versionName;
        initop();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.my_set_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FrameLayout fankui=(FrameLayout) findViewById(R.id.set_fankui);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MySetting.this, Fankui.class);
                MySetting.this.startActivity(intent);
            }
        });
        FrameLayout huancu=(FrameLayout) findViewById(R.id.set_huancun);
        huancu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFilesByDirectory(getBaseContext().getCacheDir());
                Toast.makeText(getBaseContext(),"清除缓存成功",Toast.LENGTH_SHORT).show();

            }
        });
        FrameLayout ganxie=(FrameLayout) findViewById(R.id.set_ganxie);
        ganxie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MySetting.this, Thanks.class);
                MySetting.this.startActivity(intent);
            }
        });
        FrameLayout updata=(FrameLayout)findViewById(R.id.set_updata);
        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versionAch();
            }
        });
        //推送开关
        Switch swit=(Switch)findViewById(R.id.set_switch);
        if(!JPushInterface.isPushStopped(getApplicationContext())){
            swit.setChecked(true);
        }else {
            swit.setChecked(false);
        }
        swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(getBaseContext(),"开启成功",Toast.LENGTH_SHORT).show();
                    //PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"p4FCm8tu8MqD4BLDuHvuwRX4");
                    JPushInterface.resumePush(getApplicationContext());
                }else {
                    Toast.makeText(getBaseContext(),"关闭成功",Toast.LENGTH_SHORT).show();
                    //PushManager.stopWork(getApplicationContext());
                    JPushInterface.stopPush(getApplicationContext());
                }
            }
        });
        //2g 3g,4g wifi播放下载开关
        boolean play_gprs=pre.getBoolean("play_gprs",false);
        boolean download_gprs=pre.getBoolean("download_gprs",false);
        boolean autoplay_wifi=pre.getBoolean("autoplay_wifi",true);
        Switch swit1=(Switch)findViewById(R.id.set_switch_play234g);
        Switch swit2=(Switch)findViewById(R.id.set_switch_download234g);
        Switch swit3=(Switch)findViewById(R.id.set_switch_autowifi);
        swit1.setChecked(play_gprs);
        swit2.setChecked(download_gprs);
        swit3.setChecked(autoplay_wifi);
        swit1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=pre.edit();
                editor.putBoolean("play_gprs",isChecked);
                editor.apply();//提交数据
            }
        });
        swit2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=pre.edit();
                editor.putBoolean("download_gprs",isChecked);
                editor.apply();//提交数据
            }
        });
        swit3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor=pre.edit();
                editor.putBoolean("autoplay_wifi",isChecked);
                editor.apply();//提交数据
            }
        });

        ImageView share=(ImageView)findViewById(R.id.share_dongdong);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        ddversion=(TextView) findViewById(R.id.set_vesion);
        ddversion.setText("咚咚 V"+version+"  2015-2016 素活项目组");
    }
    private  void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_bottom, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        // 点击其他地方消失
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        LinearLayout weixin=(LinearLayout) popupWindow_view.findViewById(R.id.share_weixin);
        LinearLayout pengyouquan=(LinearLayout)popupWindow_view.findViewById(R.id.share_pengyouquan);
        LinearLayout qq=(LinearLayout)popupWindow_view.findViewById(R.id.share_qq);
        LinearLayout zone=(LinearLayout)popupWindow_view.findViewById(R.id.share_zone);
        LinearLayout weibo=(LinearLayout)popupWindow_view.findViewById(R.id.share_weibo);

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url="http://www.isuhuo.com/mportal.htm";
                UMImage image = new UMImage(MySetting.this,R.drawable.logo120);

                new ShareAction(MySetting.this)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("素食，从来不应该是一件让人望而生畏的事。它只是生活方式的一种选择，是一种与众不同的状态，代表着个人的情趣与喜好，就像每个人有自己偏爱的色彩、音乐形式一样，习惯素食的你，应该感到骄傲和自在。")
                        .withTitle("咚咚")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url="http://www.isuhuo.com/mportal.htm";
                UMImage image = new UMImage(MySetting.this,R.drawable.logo120);
                new ShareAction(MySetting.this)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("素食，从来不应该是一件让人望而生畏的事。它只是生活方式的一种选择，是一种与众不同的状态，代表着个人的情趣与喜好，就像每个人有自己偏爱的色彩、音乐形式一样，习惯素食的你，应该感到骄傲和自在。")
                        .withTitle("咚咚")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url="http://www.isuhuo.com/mportal.htm";
                UMImage image = new UMImage(MySetting.this,R.drawable.logo120);
                new ShareAction(MySetting.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("素食，从来不应该是一件让人望而生畏的事。它只是生活方式的一种选择，是一种与众不同的状态，代表着个人的情趣与喜好，就像每个人有自己偏爱的色彩、音乐形式一样，习惯素食的你，应该感到骄傲和自在。")
                        .withTitle("咚咚")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url="http://www.isuhuo.com/mportal.htm";
                UMImage image = new UMImage(MySetting.this,R.drawable.logo120);
                new ShareAction(MySetting.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("素食，从来不应该是一件让人望而生畏的事。它只是生活方式的一种选择，是一种与众不同的状态，代表着个人的情趣与喜好，就像每个人有自己偏爱的色彩、音乐形式一样，习惯素食的你，应该感到骄傲和自在。")
                        .withTitle("咚咚")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.REDIRECT_URL="http://www.isuhuo.com/mportal.htm";
                UMShareAPI mShareAPI = UMShareAPI.get(MySetting.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(MySetting.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });


                String url="http://www.isuhuo.com/mportal.htm";
                UMImage image = new UMImage(MySetting.this,R.drawable.logo120);

                new ShareAction(MySetting.this)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("素食，从来不应该是一件让人望而生畏的事。它只是生活方式的一种选择，是一种与众不同的状态，代表着个人的情趣与喜好，就像每个人有自己偏爱的色彩、音乐形式一样，习惯素食的你，应该感到骄傲和自在。")
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });





    }

    private void getPopupWindow() {
        initPopuptWindow();
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = MySetting.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        MySetting.this.getWindow().setAttributes(lp);
    }
    public void versionAch(){
        // Log.d("===============vesion",version);
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainliving/androidapi/Indexs/confirmation_apk/apk_num/"+version, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    String code=js1.getString("Result");
                    if(code.equals("1")){
                        getDialog();
                    }else {
                        Toast.makeText(getBaseContext(),"您当前已是最新版本",Toast.LENGTH_SHORT).show();
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
        SlingleVolleyRequestQueue.getInstance(MySetting.this).addToRequestQueue(request);
    }
    public void getDialog(){
        builder = new android.support.v7.app.AlertDialog.Builder(MySetting.this);
        builder.setTitle("咚咚").setMessage("发现咚咚新版本")
                .setPositiveButton("前往更新", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //跳系统默认浏览器下载
                        // Uri  uri = Uri.parse("http://www.isuhuo.com/suhuo/download");
                        // Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
                        //MySetting.this.startActivity(intent);

                        //自动执行系统下载
                        DownloadManager downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                        //下载链接，android官网下载地址
                        String apkUrl = "http://www.isuhuo.com/suhuo/download";
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                        request.setDestinationInExternalPublicDir("ddownload", "dongdong.apk");
                       // long downloadId=downloadManager.enqueue(request);

                    }
                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
