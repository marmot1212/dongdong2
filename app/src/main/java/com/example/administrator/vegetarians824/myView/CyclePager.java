package com.example.administrator.vegetarians824.myView;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.article.ArticleDetial;
import com.example.administrator.vegetarians824.dongdong.TieziDetial;
import com.example.administrator.vegetarians824.entry.HeadImag;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017-06-22.
 */
public class CyclePager {
    private LinearLayout group;
    private ViewPager advPager;
    private List<View> advPics;
    private ImageView[] imageViews= null;
    private ImageView imageView = null;
    private AtomicInteger what= new AtomicInteger(0); ;
    private boolean isContinue = true;// 设置标记
    private List<HeadImag> list_imag;
    private Context context;
    private MyThraead thread;
    public CyclePager(ViewPager vp, LinearLayout group, Context context){
        this.advPager=vp;
        this.group=group;
        this.context=context;
    }

    public void init(String sign){
        if(thread!=null&&thread.isAlive()){
            thread.stopThread(true);
        }
        list_imag=new ArrayList<>();
        StringPostRequest request=new StringPostRequest(URLMannager.Lunbo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObj1 = new JSONObject(s);
                    if(jsonObj1.getString("Code").equals("1")) {

                        JSONArray imaja = jsonObj1.getJSONArray("Result");
                        for (int x = 0; x < imaja.length(); x++) {
                            JSONObject imajo = imaja.getJSONObject(x);
                            HeadImag headImag = new HeadImag();
                            headImag.setId(imajo.getString("tid"));
                            headImag.setPic(imajo.getString("img_url"));
                            list_imag.add(headImag);
                        }

                        group.removeAllViews();
                        advPics = new ArrayList<View>();// 轮播图片View集合
                        ImageLoader loader = ImageLoaderUtils.getInstance(context);
                        DisplayImageOptions options = ImageLoaderUtils.getOpt();
                        for (int i = 0; i < list_imag.size(); i++) {
                            if (context != null) {
                                ImageView img_View = new ImageView(context);
                                img_View.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                img_View.setLayoutParams(lLayoutlayoutParams);
                                img_View.requestLayout();
                                loader.displayImage(URLMannager.Imag_URL + list_imag.get(i).getPic(), img_View, options);
                                final int x = i;
                                img_View.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, ArticleDetial.class);
                                        intent.putExtra("tid", list_imag.get(x).getId());
                                        context.startActivity(intent);
                                    }
                                });
                                advPics.add(img_View);// 轮播图片View集合
                            }
                        }

                        imageViews = new ImageView[advPics.size()];
                        for (int i = 0; i < advPics.size(); i++) {
                            imageView = new ImageView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                            params.setMargins(5, 0, 5, 0);
                            imageView.setLayoutParams(params);// 设置远点大小
                            imageViews[i] = imageView;
                            if (i == 0) {
                                imageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);// 蓝色
                            } else {
                                imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);// 灰色
                            }
                            group.addView(imageViews[i]);// 把每个设置好的点添加进ViewGroup里面
                        }
                        advPager.setAdapter(new AdvAdapter(advPics));// 适配器

                        //advPager.setOffscreenPageLimit(3);
                        advPager.setPageMargin(20);
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
                            thread=new MyThraead();
                            thread.start();

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
        request.putValue("sign",sign);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(request);
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
    private final Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            advPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };
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
                imageViews[arg0].setBackgroundResource(R.drawable.banner_dian_focus);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
                }
            }

        }
    }
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

    public class MyThraead extends Thread{
        private boolean  _run  = true;
        public void stopThread(boolean run){
            this._run=!run;
        }

        @Override
        public void run() {
            super.run();
            while (_run) {
                if (isContinue) {
                    viewHandler.sendEmptyMessage(what.get());
                    whatOption();
                }
            }
        }
    }

}
