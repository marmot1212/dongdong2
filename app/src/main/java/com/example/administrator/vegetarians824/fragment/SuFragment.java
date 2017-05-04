package com.example.administrator.vegetarians824.fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter2;
import com.example.administrator.vegetarians824.dongdong.JKfujin;
import com.example.administrator.vegetarians824.dongdong.JKlingshi;
import com.example.administrator.vegetarians824.dongdong.JKsearch;
import com.example.administrator.vegetarians824.dongdong.JKshuguo;
import com.example.administrator.vegetarians824.dongdong.JKtiaozhan;
import com.example.administrator.vegetarians824.dongdong.JKyuansu;
import com.example.administrator.vegetarians824.dongdong.TieziDetial;
import com.example.administrator.vegetarians824.dongdong.Wenda;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.HeadImag;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuFragment extends Fragment {

    private List<Caidan> list_caidan;
    private List<HeadImag> list_imag;
    private PullToRefreshListView listView;
    private TextView et;
    private LinearLayout yuansu,fujin,shuguo,tiaozhan,lingshi;
    View vv;
    int count=1;
    int total=0;
    boolean isup;
    CaidanAdapter2 adapter;
    private Date date;
    private LinearLayout group;
    private ViewPager advPager;
    private ImageView[] imageViews= null;
    private ImageView imageView = null;
    private AtomicInteger what= new AtomicInteger(0); ;
    private boolean isContinue = true;// 设置标记
    private String aword;
    private TextView aword_tv;
    private boolean starttTouch=true;
    private float  starty=0,fheight=0;
    public SuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_su, container, false);
        listView=(PullToRefreshListView) v.findViewById(R.id.jiankang_list);
        vv=LayoutInflater.from(getContext()).inflate(R.layout.prl_head,null);
        list_imag=new ArrayList<>();
        list_caidan=new ArrayList<>();
        isup=false;
        date=new Date();
        group = (LinearLayout)vv.findViewById(R.id.ditu_xiangqing_viewGroup2);// 展示小圆点
        advPager = (ViewPager)vv.findViewById(R.id.ditu_xiangqing_viewpager2);// ViewPager
        aword_tv=(TextView) getActivity().findViewById(R.id.atext);
        //确保fragment得到activity,放置切换过快闪退
        if(getActivity()!=null){
        initHead();
        initoperate();
        initView();
        }
        return v;
    }
    public void initHead(){
        StringRequest request=new StringRequest(URLMannager.Caidan_URL+"/p/"+"1"+"/t/10", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObj1 = new JSONObject(s);
                    JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                    JSONArray imaja=jsonObj2.getJSONArray("picinfo");
                    for(int x=0;x< imaja.length();x++){
                        JSONObject imajo=imaja.getJSONObject(x);
                        HeadImag headImag=new HeadImag();
                        headImag.setId(imajo.getString("tid"));
                        headImag.setPic(imajo.getString("pic"));
                        list_imag.add(headImag);
                    }

                    initvp(list_imag);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }

    public void initoperate(){


        et=(TextView)vv.findViewById(R.id.caipu_input);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),JKsearch.class);
                getActivity().startActivity(intent);
            }
        });


        yuansu=(LinearLayout)vv.findViewById(R.id.jiankang_yuansu);
        yuansu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("event", "jump");
                StatService.onEvent(getContext(), "元素", "press", 1, hashMap);
                StatService.onEventStart(getContext(), "元素", "press");
                Intent intent=new Intent(getActivity(),JKyuansu.class);
                getActivity().startActivity(intent);

                StatService.onEventEnd(getContext(), "元素", "press");

            }
        });
        fujin=(LinearLayout)vv.findViewById(R.id.jiankang_fujin);
        fujin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),JKfujin.class);
                getActivity().startActivity(intent);
            }
        });
        shuguo=(LinearLayout)vv.findViewById(R.id.jiankang_shuguo);
        shuguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),JKshuguo.class);
                getActivity().startActivity(intent);
            }
        });

        tiaozhan=(LinearLayout)vv.findViewById(R.id.jiankang_tiaozhan);
        tiaozhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Wenda.class);
                getActivity().startActivity(intent);
            }
        });
        lingshi=(LinearLayout)vv.findViewById(R.id.jiankang_lingshi);
        lingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),JKlingshi.class);
                getActivity().startActivity(intent);
            }
        });
    }



    public void initData(){
        StringRequest request=new StringRequest(URLMannager.Caidan_URL+"/p/"+count+"/t/10", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                if(getActivity()!=null) {
                    adapter = new CaidanAdapter2(list_caidan, getContext(), getActivity().getWindowManager());
                    listView.setAdapter(adapter);
                    if (isup) {
                        listView.getRefreshableView().setSelection(count * 10 - 10);
                        isup = false;
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }

    public void myJson(String s){
        try {
            JSONObject jsonObj1 = new JSONObject(s);
            JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
            aword=jsonObj2.getString("tips");
            aword_tv.setText(aword);
            if(!jsonObj2.isNull("totalpage")){
                total=Integer.valueOf(jsonObj2.getString("totalpage")).intValue();
                if(total==1){
                    listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
            JSONArray array1 = jsonObj2.getJSONArray("list");
            for (int i = 0; i < array1.length(); i++) {
                Caidan mCaidanBean = new Caidan();
                JSONObject array1_2 = array1.getJSONObject(i);
                mCaidanBean.id = array1_2.getString("id");
                mCaidanBean.title = array1_2.getString("title");
                mCaidanBean.content = array1_2.getString("content");
                mCaidanBean.pic = array1_2.getString("pic");
                if(!array1_2.isNull("stype")){
                    mCaidanBean.type=array1_2.getString("stype");
                }else {
                    mCaidanBean.type="";
                }
                list_caidan.add(mCaidanBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initView(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开刷新...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);

        listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        listView.getRefreshableView().addHeaderView(vv);
        //下拉拉出提示语
        listView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {
            @Override
            public void onPullEvent(PullToRefreshBase<ListView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {

                if(state== PullToRefreshBase.State.RESET){
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
                    aword_tv.setLayoutParams(params);
                    aword_tv.requestLayout();
                    starttTouch=true;
                }else if(state== PullToRefreshBase.State.RELEASE_TO_REFRESH)
                {

                    listView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent ev) {

                            if(starttTouch){
                                starty=ev.getY();
                                starttTouch=false;
                            }
                            float height=ev.getY()-starty;
                            if(height>100&&height<180&&height>fheight){
                                Log.d("===========h",""+((int)height-100));
                                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)height-100);
                                aword_tv.setLayoutParams(params);
                                aword_tv.requestLayout();
                            }
                            fheight=height;
                            return false;
                        }
                    });

                }

            }
        });

        initData();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list_caidan=new ArrayList<Caidan>();
                count=0;
                listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
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
            listView.onRefreshComplete();
            count++;
            if(count<=total){
                initData();
            }
            else{
                Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public void initvp(List<HeadImag> list_imag){
        Log.d("===========size",list_imag.size()+"");
        group.removeAllViews();
        List<View> advPics = new ArrayList<View>();// 轮播图片View集合
        // ImageLoader工具类

        for (int i = 0; i < list_imag.size(); i++) {
            if(getActivity()!=null) {
                com.nostra13.universalimageloader.core.ImageLoader loader = ImageLoaderUtils.getInstance(getContext());
                DisplayImageOptions options = ImageLoaderUtils.getOpt();
                View v=LayoutInflater.from(getContext()).inflate(R.layout.viewpaper_view,null);
                HorizontalScrollView hscroll=(HorizontalScrollView)v.findViewById(R.id.h_scroll);
                hscroll.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                MyImageView imal=(MyImageView)v.findViewById(R.id.vpima_left);
                MyImageView imac=(MyImageView)v.findViewById(R.id.vpima_center);
                MyImageView imar=(MyImageView)v.findViewById(R.id.vpima_right);
                if(i==0){
                    if(list_imag.contains(list_imag.get(list_imag.size()-1))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(list_imag.size()-1).getPic(), imal,options);
                    }
                    if(list_imag.contains(list_imag.get(0))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(0).getPic(), imac,options);
                    }
                    if(list_imag.contains(list_imag.get(1))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(1).getPic(), imar,options);
                    }
                }else if(i==list_imag.size()-1){
                    if(list_imag.contains(list_imag.get(list_imag.size()-2))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(list_imag.size()-2).getPic(), imal,options);
                    }
                    if(list_imag.contains(list_imag.get(list_imag.size()-1))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(list_imag.size()-1).getPic(), imac,options);
                    }
                    if(list_imag.contains(list_imag.get(0))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(0).getPic(), imar,options);
                    }
                }else {
                    if(list_imag.contains(list_imag.get(i-1))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(i-1).getPic(), imal,options);
                    }
                    if(list_imag.contains(list_imag.get(i))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(i).getPic(), imac,options);
                    }
                    if(list_imag.contains(list_imag.get(i+1))){
                        loader.displayImage(URLMannager.Imag_URL + list_imag.get(i+1).getPic(), imar,options);
                    }
                }

                final String id = list_imag.get(i).getId();
                imac.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), TieziDetial.class);
                        intent.putExtra("tid", id);
                        getActivity().startActivity(intent);
                    }
                });

                advPics.add(v);// 轮播图片View集合
            }
        }

        imageViews = new ImageView[advPics.size()];
        for (int i = 0; i < advPics.size(); i++) {
            imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(20,20);
            params.setMargins(5,0,5,0);
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

        advPager.setOffscreenPageLimit(3);
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
                imageViews[arg0].setBackgroundResource(R.drawable.banner_dian_focus);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
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


}
