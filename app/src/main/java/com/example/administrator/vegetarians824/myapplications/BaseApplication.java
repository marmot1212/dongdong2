package com.example.administrator.vegetarians824.myapplications;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.HotCity;
import com.example.administrator.vegetarians824.entry.MyLociation;
import com.example.administrator.vegetarians824.entry.Province;
import com.example.administrator.vegetarians824.entry.ProvinceCity;
import com.example.administrator.vegetarians824.entry.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016-09-06.
 */
public class BaseApplication extends MultiDexApplication {

    // 拿到application上下文
    public static BaseApplication app;

    // 保存我的位置
    public MyLociation myLociation;

    public User user;

    public String acity;

    public String getAcity() {
        return acity;
    }

    public void setAcity(String acity) {
        this.acity = acity;
    }

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
        initFileDownloader();
        Fresco.initialize(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
        PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
        PlatformConfig.setSinaWeibo("2225421609","835f1b19840f1f8bc90264a90e436321");
        UMShareAPI.get(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BaseApplication() {
        super();
    }

    public static BaseApplication getApp() {
        return app;
    }

    public static void setApp(BaseApplication app) {
        BaseApplication.app = app;
    }


    public MyLociation getMyLociation() {
        return myLociation;
    }

    public void setMyLociation(MyLociation myLociation) {
        this.myLociation = myLociation;
    }

    private void initFileDownloader() {

        // 1.create FileDownloadConfiguration.Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

        // 2.config FileDownloadConfiguration.Builder
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "dongdongDownload"); // config the download path
        // builder.configFileDownloadDir("/storage/sdcard1/FileDownloader");

        // allow 3 download tasks at the same time
        builder.configDownloadTaskSize(3);

        // config retry download times when failed
        builder.configRetryDownloadTimes(5);

        // enable debug mode
        //builder.configDebugMode(true);

        // config connect timeout
        builder.configConnectTimeout(25000); // 25s

        // 3.init FileDownloader with the configuration
        FileDownloadConfiguration configuration = builder.build(); // build FileDownloadConfiguration with the builder
        FileDownloader.init(configuration);
    }

    // release FileDownloader
    private void releaseFileDownloader() {
        FileDownloader.release();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();

        // release FileDownloader
        releaseFileDownloader();
    }
}
