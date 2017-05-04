package com.example.administrator.vegetarians824.dongdong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.PinglunAdapter;
import com.example.administrator.vegetarians824.entry.CanTingXq;
import com.example.administrator.vegetarians824.entry.IMg_Url_w;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.fabu.FabuShUpdate;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.MyAdmin;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.UserDefineScrollView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CantingDetail extends AppCompatActivity {
    private LinearLayout lin_back;// 返回
    private TextView tv_jiucuo, tv_name, tv_price, tv_address, tv_pinglun,
            tv_share;// 纠错，名称，价格，地址,评论，分享
    private LinearLayout iv_phone;// 打电话
    private ListViewForScrollView list_pinglun;// 评论的ListView
    PinglunAdapter pinglun_Adapter;// 适配器

    private ImageView[] imageViews = null;// 轮播，存放四个点
    private ImageView imageView = null;// 存放轮播图片
    private ViewPager advPager = null;// 轮播ViewPager
    /**
     * AtomicInteger则通过一种线程安全的加减操作接口。 特别适合在多线程下特别适用于高并发访问。
     */
    private AtomicInteger what= new AtomicInteger(0); ;
    private boolean isContinue = true;// 设置标记

    private MyHandler handler;// 解析数据
    public String item_id;// 地图底部点击事件传过来的id
    private PopupWindow popupWindow,popWindow;
    public ImageLoader loader;// ImageLoader工具类
    public DisplayImageOptions options;// ImageLoader方法
    public CanTingXq canTing ;
    public String imageUrl;
    String type;
    LinearLayout group;
    private static boolean isrun=true;
    ImageView imashow;
    LinearLayout prom;
    private TextView jiucuo,renling;
    private String canting_title;
    private android.support.v7.app.AlertDialog.Builder builder;
    private  String renlingflag="";
    private LinearLayout line1,line2,line3,line4;
    private UserDefineScrollView scrollView;
    private LinearLayout dish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting_detail);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        isrun=true;
        canTing=new CanTingXq();
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");

        initViews();// 加载布局
        //initDatas();// 拿到id,请求服务器接口
        //initOpers();// 初始化操作

    }

    public void initViews() {
        lin_back = (LinearLayout) this
                .findViewById(R.id.ditu_xiangqing_lin_back);// 返回

        tv_name = (TextView) this.findViewById(R.id.ditu_xiangqing_tv_name);// 名称
        tv_price = (TextView) this.findViewById(R.id.ditu_xiangqing_tv_price);// 价格
        tv_address = (TextView) this.findViewById(R.id.ditu_xiangqing_tv_dizhi);// 地址

        tv_share = (TextView) this.findViewById(R.id.ditu_xiangqing_tv_share);// 分享
        iv_phone = (LinearLayout) this.findViewById(R.id.ditu_xiangqing_iv_phone);// 打电话
        list_pinglun = (ListViewForScrollView) this
                .findViewById(R.id.ditu_xiangqing_listView);// 评论listView
        group = (LinearLayout) findViewById(R.id.ditu_xiangqing_viewGroup);// 展示小圆点
        jiucuo=(TextView)findViewById(R.id.jiucuotv);
        renling=(TextView)findViewById(R.id.renlingtv);
        scrollView=(UserDefineScrollView)findViewById(R.id.canting_scroll);
        dish=(LinearLayout)findViewById(R.id.ditu_xiangqing_dish);
         prom=(LinearLayout)findViewById(R.id.ditu_xiangqing_prom);
        line1=(LinearLayout)findViewById(R.id.ditu_xiangqing_show_line1);
        line2=(LinearLayout)findViewById(R.id.ditu_xiangqing_show_line2);
        line3=(LinearLayout)findViewById(R.id.ditu_xiangqing_show_line3);
        line4=(LinearLayout)findViewById(R.id.ditu_xiangqing_show_line4);
        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,advPager.getTop());
            }
        });
        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,dish.getTop());
            }
        });
        line3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,list_pinglun.getTop());
            }
        });
        line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.smoothScrollTo(0,prom.getTop());
            }
        });
    }

    public void initDatas() {
        // 拿到地图地步列表点击事件传过来的id
        HttpGet(item_id);
    }

    public void initOpers() {
        // back设置监听
        lin_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
        TextView pinglun=(TextView) findViewById(R.id.canting_pinglun);
        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    Intent intent=new Intent(CantingDetail.this,Comment2.class);
                    intent.putExtra("id",item_id);
                    intent.putExtra("type",type);
                    CantingDetail.this.startActivity(intent);
                }else {
                    Intent intent = new Intent(CantingDetail.this, Login.class);
                    CantingDetail.this.startActivity(intent);
                }
            }
        });


        jiucuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(CantingDetail.this,CantingJiucuo.class);
                    intent.putExtra("id",item_id);
                    intent.putExtra("name",canting_title);
                    CantingDetail.this.startActivity(intent);
            }
        });

    }

    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_bottom, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
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
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
                String title=canTing.getTitle();
                String tex=canTing.getContent();
                String url=URLMannager.ShareRestaurant+canTing.getId();
                UMImage image = new UMImage(CantingDetail.this,URLMannager.Imag_URL+imageUrl);

                        new ShareAction(CantingDetail.this)
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
                        .withText(tex)
                        .withTitle(title)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
                String title=canTing.getTitle();
                String tex=canTing.getContent();
                String url=URLMannager.ShareRestaurant+canTing.getId();
                UMImage image = new UMImage(CantingDetail.this,URLMannager.Imag_URL+imageUrl);

                new ShareAction(CantingDetail.this)
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
                        .withText(tex)
                        .withTitle(title)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String title=canTing.getTitle();
                String tex=canTing.getContent();
                String url=URLMannager.ShareRestaurant+canTing.getId();
                UMImage image = new UMImage(CantingDetail.this,URLMannager.Imag_URL+imageUrl);

                new ShareAction(CantingDetail.this)
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
                        .withText(tex)
                        .withTitle(title)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String title=canTing.getTitle();
                String tex=canTing.getContent();
                String url=URLMannager.ShareRestaurant+canTing.getId();
                UMImage image = new UMImage(CantingDetail.this,URLMannager.Imag_URL+imageUrl);

                new ShareAction(CantingDetail.this)
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
                        .withText(tex)
                        .withTitle(title)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.REDIRECT_URL="http://www.isuhuo.com";

                UMShareAPI mShareAPI = UMShareAPI.get(CantingDetail.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(CantingDetail.this, platform, new UMAuthListener() {
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

                PlatformConfig.setSinaWeibo("2225421609","835f1b19840f1f8bc90264a90e436321");
                String tex=canTing.getContent();
                String url=URLMannager.ShareRestaurant+canTing.getId();
                UMImage image = new UMImage(CantingDetail.this,URLMannager.Imag_URL+imageUrl);
                new ShareAction(CantingDetail.this)
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
                        .withText(tex)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });





    }

    private void getPopupWindow() {
            initPopuptWindow();
    }
    /**
     * 自定义，加载listView
     */
    public void initListView(List<Pinglun> list_Pinglun) {
        pinglun_Adapter = new PinglunAdapter(CantingDetail.this,
                list_Pinglun,getWindowManager());
        list_pinglun.setAdapter(pinglun_Adapter);// 绑定适配器
        if(list_Pinglun.size()>0&&list_pinglun.getFooterViewsCount()==0){
            TextView tv=new TextView(getBaseContext());
            tv.setText("已经全部加载完毕");
            tv.setTextSize(12);
            tv.setTextColor(0xffa0a0a0);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            list_pinglun.addFooterView(tv);
        }
        list_pinglun.setEnabled(false);// 设置不可滑动

    }
    /**
     * 加载轮播图片方法 img_Url_list,轮播图片的url集合
     */
    private void initViewPager(List<IMg_Url_w> img_Url_list) {

        advPager = (ViewPager) findViewById(R.id.ditu_xiangqing_viewpager);// ViewPager
        group.removeAllViews();
        List<View> advPics = new ArrayList<View>();// 轮播图片View集合
        // ImageLoader工具类
        loader = ImageLoaderUtils.getInstance(CantingDetail.this);
        options = ImageLoaderUtils.getOpt();
        for (int i = 0; i < img_Url_list.size(); i++) {
            final ImageView img_View = new ImageView(this);
            // ImageLoader加载图片，保存到一个ImageView
            //img_View.setType(1);
            //img_View.setBorderRadius(0);
            img_View.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            img_View.setLayoutParams(lLayoutlayoutParams);
            img_View.requestLayout();

            //loader.displayImage(img_Url_list.get(i).getUrl(), img_View, options);
            com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(getBaseContext());
            DisplayImageOptions options2=ImageLoaderUtils.getOpt();
            loader2.displayImage(img_Url_list.get(i).getUrl(),img_View,options2);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int Width = display.getWidth();
            ViewGroup.LayoutParams params=img_View.getLayoutParams();
            double n=(double)Width/params.width;
            params.width=Width;
            params.height=(int)(Width*n);
            img_View.setLayoutParams(params);
            img_View.requestLayout();
            img_View.setScaleType(ImageView.ScaleType.CENTER_CROP);
            final String url=img_Url_list.get(i).getUrl();
            img_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(url,img_View);
                }
            });
            advPics.add(img_View);// 轮播图片View集合
        }

        imageViews = new ImageView[advPics.size()];
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));// 设置远点大小
            imageView.setPadding(5, 5, 5, 5);// 设置远点内边距
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i]
                        .setBackgroundResource(R.drawable.banner_dian_focus);// 蓝色
            } else {
                imageViews[i]
                        .setBackgroundResource(R.drawable.banner_dian_blur);// 灰色
            }
            group.addView(imageViews[i]);// 把每个设置好的点添加进ViewGroup里面
        }

        advPager.setAdapter(new AdvAdapter(advPics));// 适配器
        // 设置监听，主要是设置点点的背景
        advPager.setOnPageChangeListener(new GuidePageChangeListener());
        // 设置手滑动图片的监听事件OnTouchListener
        advPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }

                return false;
            }

        });
        /**
         * 开个线程，播放轮播图片
         */
            if(isrun) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (isContinue) {
                                viewHandler.sendEmptyMessage(what.get());
                                whatOption();
                            }
                        }
                    }
                }).start();
            }

    }

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-4);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
    }

    /**
     * handle设置当前的应该播放的图片
     */
    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    /**
     * 给轮播图设置变化的监听事件
     *
     * @author Administrator
     *
     */
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        // onInterceptTouchEvent

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            what.getAndSet(arg0);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.banner_dian_focus);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.banner_dian_blur);
                }
            }

        }
    }

    /**
     * PagerAdapter
     *
     * @author Administrator
     */
    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }

    /**
     * 网络编程,请求服务器接口
     */
    private void HttpGet(final String id) {
        final String str = URLMannager.Restaurant_Detial;
        handler = new MyHandler();
        new Thread() {
            public void run() {
                try {
                    String str1 = str + "/res_id/" + id + "/p/1/t/1000";
                    System.out.println("链接" + str1);
                    URL url = new URL(str1);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setReadTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(7000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        byte[] bytes = new byte[512];
                        int len = -1;
                        StringBuilder sb = new StringBuilder();
                        while ((len = in.read(bytes)) != -1) {
                            sb.append(new String(bytes, 0, len));
                        }
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = sb.toString();
                        System.out.println("数据" + sb.toString());
                        handler.sendMessage(msg);
                    }
                    //Log.i("log", "click2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();
    }

    /**
     * handle机制,数据解析
     */
    class MyHandler extends Handler {
        CanTingXq mCanTing = null;// 餐厅详情
        List<Pinglun> list_Pinglun = null;// 评论集合
        Pinglun mPinglun = null;// 评论
        IMg_Url_w img_Url_w;// 轮播图url
        List<IMg_Url_w> img_Url_list;// 轮播图url集合

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Json解析数据
            if (msg.what == 2) {
                String strData = (String) msg.obj;
                System.out.println("开始解析:" + strData.toString());
                mCanTing = new CanTingXq();// 新建
                try {
                    JSONObject jsonObj1 = new JSONObject(strData);
                    JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                    renlingflag=jsonObj2.getString("teansReleStatus");
                    if(BaseApplication.getApp().getUser().getTenans().equals("1")&&renlingflag.equals("2")){
                        renling.setVisibility(View.VISIBLE);
                        renling.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                    /*
                    * 传 uid id向服务器检查该是否认领过
                    *
                    * 没认领 跳转
                    * 认领 提示 您已认领
                    * */
                                Intent intent=new Intent(CantingDetail.this,MyAdmin.class);
                                intent.putExtra("flag",2);
                                intent.putExtra("mess_id",item_id);
                                CantingDetail.this.startActivity(intent);

                            }
                        });
                    }

                    JSONArray array1 = jsonObj2.getJSONArray("detail");
                    JSONObject array1_2 = array1.getJSONObject(0);
                    mCanTing.id = array1_2.getString("id");// id
                    type=array1_2.getString("type");
                    mCanTing.title = array1_2.getString("title");// 名字
                    canting_title=array1_2.getString("title");
                    if(!array1_2.isNull("tel"))
                        mCanTing.tel = array1_2.getString("tel");// 电话
                    if(!array1_2.isNull("unit_price"))
                        mCanTing.unit_price = array1_2.getString("unit_price");// 价格
                    mCanTing.longitude = array1_2.getDouble("longitude");// 经度
                    mCanTing.latitude = array1_2.getDouble("latitude");// 维度
                    mCanTing.address = array1_2.getString("address");// 地址
                    mCanTing.content=array1_2.getString("content");
                    if(!array1_2.isNull("img_url_1"))
                        imageUrl=array1_2.getString("img_url_1");
                    //特别推荐
                    if(!array1_2.isNull("newstitle")){
                        line4.setVisibility(View.VISIBLE);
                        prom.setVisibility(View.VISIBLE);
                        TextView prom1=(TextView)findViewById(R.id.ditu_xiangqing_prom1);
                        TextView prom2=(TextView)findViewById(R.id.ditu_xiangqing_prom2);
                        prom1.setText(array1_2.getString("newstitle"));
                        prom2.setText(array1_2.getString("newscontent"));
                    }

                    // 轮播图片的数据处理，图片，最少1张，最多6张
                    img_Url_list = new ArrayList<IMg_Url_w>();
                    for (int k = 1; k < 7; k++) {
                        if (array1_2.isNull("img_url_w_" + k)) {
                            break;
                        } else {
                            img_Url_w = new IMg_Url_w();
                            img_Url_w
                                    .setUrl(URLMannager.Imag_URL
                                            + array1_2.getString("img_url_w_"
                                            + k));// 拼接图片网络地址
                            img_Url_list.add(img_Url_w);// 添加到集合
                        }
                    }
                    mCanTing.setImg_url_w_pic(img_Url_list);// 图片集合
                    /**
                     * 解析餐厅的评论，不确定有几条
                     */
                    list_Pinglun = new ArrayList<Pinglun>();// 新建
                    JSONObject jsonObj4 = jsonObj2.getJSONObject("commentlist");
                    JSONArray array3 = jsonObj4.getJSONArray("list");
                    for (int i = 0; i < array3.length(); i++) {
                        mPinglun = new Pinglun();// 新建
                        JSONObject jsonObj5 = array3.getJSONObject(i);
                        mPinglun.setCreate_time_text(jsonObj5.getString("create_time_text"));
                        mPinglun.setUser_head_img_th(jsonObj5.getString("user_head_img_th"));
                        mPinglun.setContent(jsonObj5.getString("content"));
                        mPinglun.setUsername(jsonObj5.getString("username"));
                        mPinglun.setId(array3.getJSONObject(i).getString("id"));
                        mPinglun.setUid(jsonObj5.getString("uid"));
                        if(!jsonObj5.isNull("img_url_1"))
                            mPinglun.setImg_url_01(jsonObj5.getString("img_url_1"));
                        if(!jsonObj5.isNull("img_url_2"))
                            mPinglun.setImg_url_02(jsonObj5.getString("img_url_2"));
                        if(!jsonObj5.isNull("img_url_3"))
                            mPinglun.setImg_url_03(jsonObj5.getString("img_url_3"));
                        if(!jsonObj5.isNull("img_url_4"))
                            mPinglun.setImg_url_04(jsonObj5.getString("img_url_4"));
                        mPinglun.setLv(jsonObj5.getString("lv"));
                        list_Pinglun.add(mPinglun);// 循环一次，添加一次
                    }
                    //菜谱
                    LinearLayout line=(LinearLayout)findViewById(R.id.relative_addline);
                    line.removeAllViews();
                    if(jsonObj2.has("dish")){
                        JSONObject js4 = jsonObj2.getJSONObject("dish");
                        Log.d("==========dish",js4.toString());
                        if(js4.has("list")){
                            dish.setVisibility(View.VISIBLE);
                            line2.setVisibility(View.VISIBLE);
                        JSONArray jaa = js4.getJSONArray("list");
                        for (int j = 0; j < jaa.length(); j++) {
                            JSONObject joo = jaa.getJSONObject(j);
                            View vv = getLayoutInflater().inflate(R.layout.caipu_relative_item, null);
                            TextView tv1 = (TextView) vv.findViewById(R.id.relative_item_title);
                            TextView tv2 = (TextView) vv.findViewById(R.id.relative_item_content);
                            tv1.setText(joo.getString("title"));
                            tv2.setText(joo.getString("content"));
                            ImageView imas = (ImageView) vv.findViewById(R.id.relative_item_ima);
                            com.nostra13.universalimageloader.core.ImageLoader loaders = ImageLoaderUtils.getInstance(getBaseContext());
                            DisplayImageOptions optionss = ImageLoaderUtils.getOpt();
                            loaders.displayImage(URLMannager.Imag_URL + "" + joo.getString("pic"), imas, optionss);
                            final String cpid = joo.getString("id");
                            vv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent1 = new Intent(getBaseContext(), CaipuDetail.class);
                                    intent1.putExtra("id", cpid);
                                    startActivity(intent1);
                                }
                            });
                            line.addView(vv);
                        }

                        }
                    }
                    canTing=mCanTing;
                } catch (JSONException e) {
                    e.printStackTrace();
                }




				/*
				 * 设置名字，价格，地址
				 */
                tv_name.setText(mCanTing.getTitle());
                tv_price.setText(mCanTing.getUnit_price());
                float distance=0;
                if(BaseApplication.app.getMyLociation()!=null){
                    LatLng latLngs=new LatLng(Double.valueOf(BaseApplication.app.getMyLociation().getLatitude()),Double.valueOf(BaseApplication.app.getMyLociation().getLongitude()));
                    LatLng latLnge=new LatLng(Double.valueOf(mCanTing.getLatitude().toString()),Double.valueOf(mCanTing.getLongitude().toString()));
                    distance = AMapUtils.calculateLineDistance(latLngs,latLnge);
                }
                tv_address.setText("距您"+(int)distance+"米 | "+mCanTing.getAddress());
                // 电话点击事件
                if(mCanTing.getTel()==null){
                   iv_phone.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           getDialog();
                       }
                   });
                }else {
                    iv_phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            AlertDialog.Builder dBuilder = new AlertDialog.Builder(CantingDetail.this);
                            dBuilder.setTitle(mCanTing.getTel());
                            dBuilder.setNegativeButton("呼叫",
                                    new DialogInterface.OnClickListener() {
                                        @SuppressWarnings("static-access")
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // 执行拨号操作
                                            // 1.获取用户输入的号码
                                            String number = mCanTing.getTel();
                                            // 2.执行拨号操作
                                            // 创建一个拨号意图
                                            Intent intent = new Intent();
                                            // 设置要拨打的号码 (URL:统一资源定位符,uri:统一资源标识符)
                                            Uri uri = Uri.parse("tel://" + number); // 设置要操作的路径
                                            // 设置动作,拨号动作
                                            intent.setAction(intent.ACTION_CALL);
                                            intent.setData(uri);
                                            // 跳转到拨号界面
                                            CantingDetail.this.startActivity(intent);
                                        }
                                    })
                                    .setPositiveButton("取消",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {

                                                }
                                            }).create().show();
                        }
                    });
                }// 电话点击事件结束
                // 地址跳转
                tv_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(CantingDetail.this, CantingRoute.class);
                        intent.putExtra("longitude",mCanTing.getLongitude());
                        intent.putExtra("latitude",mCanTing.getLatitude());
                        CantingDetail.this.startActivity(intent);

                    }
                });
            }
            initViewPager(img_Url_list);// 加载轮播图
            initListView(list_Pinglun);// 加载适配器
        }

    }// Handler类结尾

    public void getPicPop(String url,ImageView view){

        View popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.image_pop,null);
        imashow=(ImageView) popView.findViewById(R.id.image_show);
        com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(getBaseContext());
        DisplayImageOptions options2=ImageLoaderUtils.getOpt();
        loader2.displayImage(url,imashow,options2);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = imashow.getLayoutParams();
        double n=(double)(Width/params.width);
        params.width=Width;
        params.height=(int)(params.height*n);
        imashow.setLayoutParams(params);
        imashow.requestLayout();
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

    public void getDialog(){
        builder = new android.support.v7.app.AlertDialog.Builder(CantingDetail.this);
        builder.setMessage("我还没有要到她的手机号")
                .setPositiveButton("朕帮你要", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(CantingDetail.this,CantingJiucuo.class);
                        intent.putExtra("id",item_id);
                        intent.putExtra("name",canting_title);
                        CantingDetail.this.startActivity(intent);
                    }
                }).setNegativeButton("退下", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDatas();// 拿到id,请求服务器接口
        initOpers();// 初始化操作
    }

    @Override
    protected void onPause() {
        super.onPause();
        isrun=false;
    }


}
