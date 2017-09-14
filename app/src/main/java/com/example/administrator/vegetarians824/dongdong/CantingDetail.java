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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanFriendlyAdapter;
import com.example.administrator.vegetarians824.adapter.PinglunAdapter;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.CanTingXq;
import com.example.administrator.vegetarians824.entry.IMg_Url_w;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.entry.RestaurantTag;
import com.example.administrator.vegetarians824.fabu.FabuShUpdate;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.MyAdmin;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.NewGridView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myView.UserDefineScrollView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.DipPX;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tb.emoji.EmojiUtil;
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

import java.io.IOException;
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
    public String item_id,vege_status="1";// 地图底部点击事件传过来的id
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
    private ListView list_friendly,list_danmu;
    private TextView menu1,menu2,menu3;
    private DanmuAdapter adapter_danmu;
    private TagGridAdapter adapter_tag;
    private NewGridView gridView;
    private LinearLayout lv;
    private TextView viewcount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canting_detail);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        isrun=true;
        canTing=new CanTingXq();
        Intent intent = getIntent();
        item_id = intent.getStringExtra("item_id");
        if(intent.hasExtra("vege_status")){
            vege_status=intent.getStringExtra("vege_status");
        }
        initViews();// 加载布局
        initNewView();
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
        gridView=(NewGridView)findViewById(R.id.ditu_xiangqing_grid);
        lv=(LinearLayout)findViewById(R.id.ditu_xiangqing_lv);
        viewcount=(TextView)findViewById(R.id.ditu_xiangqing_viewcount);
    }

    public void initNewView(){
        menu1=(TextView)findViewById(R.id.canting_detail_menu1);
        if(vege_status.equals("2")){
            menu1.setVisibility(View.VISIBLE);
        }
        menu2=(TextView)findViewById(R.id.canting_detail_menu2);
        menu3=(TextView)findViewById(R.id.canting_detail_menu3);
        list_friendly=(ListView)findViewById(R.id.canting_detail_friendlylist);
        list_danmu=(ListView)findViewById(R.id.canting_detail_danmu);
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_friendly.setVisibility(View.VISIBLE);
                menu1.setBackgroundResource(R.drawable.button_bg_red2);
                menu1.setTextColor(0xffffffff);
                menu2.setBackgroundColor(0xffffffff);
                menu2.setTextColor(0xff8e8e8e);
                menu3.setBackgroundColor(0xffffffff);
                menu3.setTextColor(0xff8e8e8e);
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_friendly.setVisibility(View.GONE);
                menu2.setBackgroundResource(R.drawable.button_bg_red2);
                menu2.setTextColor(0xffffffff);
                menu1.setBackgroundColor(0xffffffff);
                menu1.setTextColor(0xff8e8e8e);
                menu3.setBackgroundColor(0xffffffff);
                menu3.setTextColor(0xff8e8e8e);
                scrollView.smoothScrollTo(0,advPager.getTop());
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_friendly.setVisibility(View.GONE);
                menu3.setBackgroundResource(R.drawable.button_bg_red2);
                menu3.setTextColor(0xffffffff);
                menu2.setBackgroundColor(0xffffffff);
                menu2.setTextColor(0xff8e8e8e);
                menu1.setBackgroundColor(0xffffffff);
                menu1.setTextColor(0xff8e8e8e);
                scrollView.smoothScrollTo(0,list_pinglun.getTop());
            }
        });
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
            AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            list_pinglun.addFooterView(tv);

            TextView tv2=new TextView(getBaseContext());
            tv2.setText("评价");
            tv2.setTextSize(14);
            tv2.setBackgroundColor(0xffffffff);
            tv2.setTextColor(0xff8e8e8e);
            AbsListView.LayoutParams params2=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            tv2.setLayoutParams(params2);
            tv2.setGravity(Gravity.CENTER);
            list_pinglun.addHeaderView(tv2);
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
                    if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
                        str1=str1+"/uid/"+BaseApplication.app.getUser().getId();
                    }
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
        List<RestaurantTag> list_tag;
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
                    viewcount.setText(array1_2.getString("view")+"次回眸");
                    mCanTing.id = array1_2.getString("id");// id
                    type=array1_2.getString("type");
                    initDanmu();
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
                    //标签 营业时间 停车位
                    if(array1_2.has("food_type")){
                        ImageView tag=(ImageView)findViewById(R.id.ditu_xiangqing_tv_tag);
                        tag.setVisibility(View.VISIBLE);
                        switch (array1_2.getString("food_type")){
                            case "1":tag.setImageResource(R.drawable.su1);break;
                            case "2":tag.setImageResource(R.drawable.su3);break;
                            case "3":tag.setImageResource(R.drawable.su2);break;
                            default:break;
                        }
                    }
                    if(array1_2.has("parking_status")||array1_2.has("open_times")){
                        LinearLayout line=(LinearLayout)findViewById(R.id.ditu_xiangqing_line);
                        line.setVisibility(View.VISIBLE);
                        if(array1_2.has("open_times")){
                            LinearLayout line1=(LinearLayout)findViewById(R.id.ditu_xiangqing_line1);
                            line1.setVisibility(View.VISIBLE);
                            TextView time=(TextView)findViewById(R.id.ditu_xiangqing_tv_time);
                            time.setText(array1_2.getString("open_times"));
                        }
                        if(array1_2.has("parking_status")){
                            if(!array1_2.getString("parking_status").equals("0")){
                                LinearLayout line2=(LinearLayout)findViewById(R.id.ditu_xiangqing_line2);
                                line2.setVisibility(View.VISIBLE);
                                TextView park=(TextView)findViewById(R.id.ditu_xiangqing_tv_park);
                                if(array1_2.getString("parking_status").equals("1")){
                                    park.setText("有");
                                }else {
                                    park.setText("无");
                                }
                            }
                        }
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

                    //素食友好菜单
                    if(vege_status.equals("2")){
                        List<Caidan> list_caidan=new ArrayList<>();
                        JSONArray ja=jsonObj2.getJSONArray("friend_list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Caidan cd=new Caidan();
                            cd.setTitle(jo.getString("title"));
                            cd.setPic(jo.getString("img_url"));
                            cd.setContent(jo.getString("content"));
                            cd.setPrice(jo.getString("price"));
                            cd.setId(jo.getString("id"));
                            list_caidan.add(cd);
                        }
                        list_friendly.setAdapter(new CaidanFriendlyAdapter(list_caidan,CantingDetail.this));
                    }
                    //标签
                    list_tag=new ArrayList<>();
                    if(jsonObj2.has("friend_real")){
                        JSONArray jaa=jsonObj2.getJSONArray("friend_real");
                        for(int i=0;i<jaa.length();i++){
                            JSONObject joo=jaa.getJSONObject(i);
                            RestaurantTag rt=new RestaurantTag();
                            rt.setId(joo.getString("id"));
                            rt.setTitle(joo.getString("title"));
                            rt.setCount(joo.getString("count"));
                            rt.setStatus(joo.getString("status"));
                            rt.setClick_status(joo.getString("click_status"));
                            list_tag.add(rt);
                        }
                        adapter_tag=new TagGridAdapter(list_tag,CantingDetail.this);
                        gridView.setAdapter(adapter_tag);
                    }
                    //lv
                    if(jsonObj2.has("vege_lv")){
                        int lvnum=Integer.valueOf(jsonObj2.getString("vege_lv"));
                        for(int i=0;i<lvnum;i++){
                            ImageView ima=new ImageView(getBaseContext());
                            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(DipPX.dip2px(getBaseContext(),16),DipPX.dip2px(getBaseContext(),16));
                            ima.setLayoutParams(params);
                            ima.setImageResource(R.mipmap.yezi);
                            lv.addView(ima);
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
                if(mCanTing.getUnit_price().equals("0")||mCanTing.getUnit_price().equals("99999999")){
                    LinearLayout priceline=(LinearLayout)findViewById(R.id.ditu_xiangqing_tv_priceline);
                    priceline.setVisibility(View.GONE);
                }else {
                    tv_price.setText(mCanTing.getUnit_price());
                }
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
                        intent.putExtra("name",tv_address.getText().toString());
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

    public void backgroundAlpha(float bgAlpha) {
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

        HttpGet(item_id);// 拿到id,请求服务器接口
        initOpers();// 初始化操作
        //initDanmu();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isrun=false;
        StatService.onPause(this);
    }

    public void initDanmu(){
        String url=URLMannager.CommentList +type+ "/mess_id/" + item_id + "/p/1/t/3";
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
            url=url+"/uid/"+BaseApplication.app.getUser().getId();
        }
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    List<Pinglun> danmuku=new ArrayList<>();
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Pinglun pl=new Pinglun();
                            pl.setId(jo.getString("id"));
                            pl.setUser_head_img_th(jo.getString("user_head_img"));
                            pl.setUsername(jo.getString("username"));
                            pl.setContent(jo.getString("content"));
                            pl.setZancount(jo.getString("comment_laund_count"));
                            pl.setZanstatus(jo.getString("comment_status").equals("1"));
                            danmuku.add(pl);
                        }
                        adapter_danmu=new DanmuAdapter(danmuku,getBaseContext());
                        list_danmu.setAdapter(adapter_danmu);
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

    public class DanmuAdapter extends BaseAdapter{
        private List<Pinglun> mydata;
        private Context context;
        public DanmuAdapter(List<Pinglun> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_danmu,null);
            final int x=position;
            RoundImageView ima=(RoundImageView)convertView.findViewById(R.id.danmu_ima);
            TextView name=(TextView)convertView.findViewById(R.id.danmu_name);
            TextView content=(TextView)convertView.findViewById(R.id.danmu_content);
            TextView zancount=(TextView)convertView.findViewById(R.id.danmu_zancount);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getUser_head_img_th(),ima,options);
            name.setText(mydata.get(position).getUsername());
            content.setText(mydata.get(position).getContent());
            zancount.setText(mydata.get(position).getZancount());
            try {
                EmojiUtil.handlerEmojiText3(content, content.getText().toString(), context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LinearLayout zan=(LinearLayout)convertView.findViewById(R.id.danmu_zan);
            ImageView zanicon=(ImageView)convertView.findViewById(R.id.danmu_zanicon);
            if(mydata.get(position).isZanstatus()){
                zanicon.setImageResource(R.mipmap.icon_zan1);
            }else {
                zanicon.setImageResource(R.mipmap.icon_zan2);
            }
            zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean flag=mydata.get(x).isZanstatus();
                    if(flag){
                        int count=Integer.valueOf(mydata.get(x).getZancount());
                        mydata.get(x).setZancount((count-1)+"");
                    }else {
                        int count=Integer.valueOf(mydata.get(x).getZancount());
                        mydata.get(x).setZancount((count+1)+"");
                    }
                    mydata.get(x).setZanstatus(!flag);
                    adapter_danmu.notifyDataSetChanged();
                    doCommentLand(mydata.get(x).getId());
                }
            });

            return convertView;
        }
    }

    public void doCommentLand(String id){
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
            StringPostRequest spr = new StringPostRequest(URLMannager.CommentZan, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            spr.putValue("id", id);
            spr.putValue("uid", BaseApplication.app.getUser().getId());
            SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
        }else {
            startActivity(new Intent(this,Login.class));
        }
    }

    public class TagGridAdapter extends BaseAdapter{
        private List<RestaurantTag> mydata;
        private Context context;
        public TagGridAdapter( List<RestaurantTag> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int position) {
            return mydata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView=LayoutInflater.from(context).inflate(R.layout.adapter_tag_item,null);
            final int x=position;
            TextView tv=(TextView)convertView.findViewById(R.id.restaurant_tag_tv);
            LinearLayout bg=(LinearLayout)convertView.findViewById(R.id.restaurant_tag_line);
            tv.setText(mydata.get(position).getTitle()+"\t\t"+mydata.get(position).getCount());
            if(mydata.get(position).getClick_status().equals("2")){
                bg.setBackgroundResource(R.drawable.button_bg_gray2);
                tv.setTextColor(0xff8e8e8e);
            }else {
                bg.setBackgroundResource(R.drawable.button_bg);
                tv.setTextColor(0xff00aff0);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mydata.get(x).getClick_status().equals("2")){
                        mydata.get(x).setClick_status("1");
                        int count=Integer.valueOf(mydata.get(x).getCount());
                        mydata.get(x).setCount((count+1)+"");
                    }else {
                        mydata.get(x).setClick_status("2");
                        int count=Integer.valueOf(mydata.get(x).getCount());
                        mydata.get(x).setCount((count-1)+"");
                    }
                    adapter_tag.notifyDataSetChanged();
                    doTagClick(mydata.get(x).getId());
                }
            });
            return convertView;
        }
    }

    public void doTagClick(String id){
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
            StringPostRequest spr = new StringPostRequest(URLMannager.TagClick, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            spr.putValue("uid",BaseApplication.app.getUser().getId());
            spr.putValue("rest_id",item_id);
            spr.putValue("real_id",id);
            SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
        }else {
            startActivity(new Intent(this,Login.class));
        }
    }


}
