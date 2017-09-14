package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.NewGridView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tiezi2Detial extends AppCompatActivity {
    private PullToRefreshListView prl;
    private List<View> view_list;
    private List<String> ima_list;
    String id;
    boolean is1,is2;
    private ImageView colima,zanima;
    String type;
    private NewGridView gridView;
    PopupWindow popupWindow,popWindow;
    String actitle,accontent,acpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiezi2_detial);
        StatusBarUtil.setColorNoTranslucent(this,0x00aff0);
        prl=(PullToRefreshListView)findViewById(R.id.prl2);
        view_list=new ArrayList<>();
        ima_list=new ArrayList<>();
        is1=false;
        is2=false;
        colima=(ImageView)findViewById(R.id.tiezi2_collectima);
        zanima=(ImageView)findViewById(R.id.tiezi2_zanima);
        Intent intent=getIntent();
        id=intent.getStringExtra("tid");
        initoperate();
        getData();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.tiezi2_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout collect=(LinearLayout) findViewById(R.id.tiezi2_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    if(is1){
                        colima.setImageResource(R.drawable.favoriteoff);
                        is1=false;
                        doPost_cancel();
                    }else {
                        colima.setImageResource(R.drawable.favoriteon);
                        is1=true;
                        doPost();
                    }
                }else{
                    Intent intent = new Intent(Tiezi2Detial.this, Login.class);
                    Tiezi2Detial.this.startActivity(intent);
                }
            }
        });
        LinearLayout zan=(LinearLayout)findViewById(R.id.tiezi2_zan);
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    if (is2) {
                        zanima.setImageResource(R.drawable.zanoff2);
                        is2 = false;
                        doPost2_cancel();
                    } else {
                        zanima.setImageResource(R.drawable.zanon2);
                        is2 = true;
                        doPost2();
                    }
                }else {
                    Intent intent = new Intent(Tiezi2Detial.this, Login.class);
                    Tiezi2Detial.this.startActivity(intent);
                }

            }
        });

        TextView ppinglun=(TextView)findViewById(R.id.tiezi2_pinglun);
        ppinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    Intent intent=new Intent(Tiezi2Detial.this,Comment.class);
                    intent.putExtra("type",type);
                    intent.putExtra("id",id);
                    Tiezi2Detial.this.startActivity(intent);
                }else {
                    Intent intent = new Intent(Tiezi2Detial.this, Login.class);
                    Tiezi2Detial.this.startActivity(intent);
                }
            }
        });
        LinearLayout share=(LinearLayout)findViewById(R.id.tiezi2_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    public void getData(){
        StringRequest request=new StringRequest(URLMannager.TieZi + id+"/uid/"+BaseApplication.app.getUser().getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONObject js3=js2.getJSONObject("detail");
                    type=js3.getString("type");
                    //图文混排
                    if(!js3.isNull("contents")) {
                        JSONArray ja=js3.getJSONArray("contents");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            String type = jo.getString("type");
                            if (type.equals("1")) {
                                String pic = jo.getString("content");
                                //ImageView ima=new ImageView(getBaseContext());
                                MyImageView ima = new MyImageView(getBaseContext(), null);
                                ima.setType(1);
                                ima.setBorderRadius(6);
                                ima.setScaleType(ImageView.ScaleType.FIT_XY);
                                com.nostra13.universalimageloader.core.ImageLoader loader = ImageLoaderUtils.getInstance(getBaseContext());
                                DisplayImageOptions options = ImageLoaderUtils.getOpt();
                                loader.displayImage(URLMannager.Imag_URL + "" + pic, ima, options);
                                view_list.add(ima);
                            }
                            if (type.equals("2")) {
                                String content = jo.getString("content");
                                TextView tv = new TextView(getBaseContext());
                                tv.setText(content);
                                tv.setTextColor(Color.BLACK);
                                tv.setTextSize(16);
                                tv.setLineSpacing(0,(float)1.5);
                                view_list.add(tv);
                            }
                        }
                    }
                    //非图文混排
                    else
                    {
                        String content = js3.getString("content");
                        TextView tv = new TextView(getBaseContext());
                        tv.setText(content);
                        tv.setTextColor(Color.BLACK);
                        view_list.add(tv);
                        for(int i=1;i<=6;i++){
                            if(!js3.isNull("img_url_"+i)){
                                ima_list.add(js3.getString("img_url_"+i));
                            }
                        }
                        Log.d("=============size",ima_list.size()+"");
                        getGrid();
                    }

                    actitle=js3.getString("title");
                    String ss=js3.getString("content");
                    if(ss.length()>20){
                        accontent=ss.substring(0,21)+"...";
                    }else {
                        accontent=ss;
                    }
                    if(!js3.isNull("img_url_1")){
                        acpic=js3.getString("img_url_1");
                    }else {
                        acpic=js3.getString("user_head_img");
                    }
                    String time=js3.getString("create_time");
                    String uname=js3.getString("username");
                    String ima=js3.getString("user_head_img");
                    String grade=js3.getString("lv");
                    prl.setAdapter(new PRLAdapter(view_list));
                    View v= getLayoutInflater().inflate(R.layout.tiezi_head,null);
                    ImageView im=(ImageView) v.findViewById(R.id.tiezi_head_ima);
                    TextView tv1=(TextView) v.findViewById(R.id.tiezi_head_title);
                    TextView tv2=(TextView) v.findViewById(R.id.tiezi_head_uname);
                    TextView tv3=(TextView) v.findViewById(R.id.tiezi_head_time);
                    ImageView lv=(ImageView)v.findViewById(R.id.tiezi_head_lv);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+ima,im,options);
                    tv1.setText(actitle);
                    tv2.setText(uname);
                    tv3.setText(time);
                    switch (grade){
                        case "1":lv.setImageResource(R.mipmap.lv1);break;
                        case "2":lv.setImageResource(R.mipmap.lv2);break;
                        case "3":lv.setImageResource(R.mipmap.lv3);break;
                        default:break;
                    }
                    prl.getRefreshableView().addHeaderView(v);
                    if(ima_list.size()>0)
                        prl.getRefreshableView().addFooterView(gridView);
                    //底部菜单
                    String col=js3.getString("gather_status");
                    String zan=js3.getString("laud_status");
                    if(col.equals("0")){
                        colima.setImageResource(R.drawable.favoriteoff);
                    }else if(col.equals("1")){
                        colima.setImageResource(R.drawable.favoriteon);
                    }
                    if(zan.equals("0")){
                        zanima.setImageResource(R.drawable.zanoff2);
                    }else if(zan.equals("1")){
                        zanima.setImageResource(R.drawable.zanon2);
                    }
                    String num=js2.getString("commentCount");
                    if(!num.equals("0")){
                        TextView tnum=(TextView)findViewById(R.id.tiezi2_pinglunnum);
                        tnum.setText(num);
                    }

                    //赞的人
                    if(!js3.isNull("laudlist")) {
                        JSONArray jaa = js3.getJSONArray("laudlist");
                        View vv = getLayoutInflater().inflate(R.layout.tiezi_zan_line, null);
                        LinearLayout zanadd = (LinearLayout) vv.findViewById(R.id.zantz_addline);
                        TextView zannum = (TextView) vv.findViewById(R.id.zantz_num);
                        zannum.setText(jaa.length() + "");
                        for (int i = 0; i < jaa.length(); i++) {
                            JSONObject jo = jaa.getJSONObject(i);
                            RoundImageView imas = new RoundImageView(getBaseContext());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60, 60);
                            params.setMargins(15, 0, 0, 0);
                            imas.setLayoutParams(params);
                            com.nostra13.universalimageloader.core.ImageLoader loader2 = ImageLoaderUtils.getInstance(Tiezi2Detial.this);
                            DisplayImageOptions options2 = ImageLoaderUtils.getOpt();
                            loader2.displayImage(URLMannager.Imag_URL + "" + jo.getString("user_head_img"), imas, options2);
                            final String id=jo.getString("id");
                            imas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(Tiezi2Detial.this,UserDetial.class);
                                    intent.putExtra("uid",id);
                                    if(BaseApplication.app.getUser().islogin()){
                                        intent.putExtra("id",BaseApplication.app.getUser().getId());
                                    }else {
                                        intent.putExtra("id","");
                                    }
                                    Tiezi2Detial.this.startActivity(intent);
                                }
                            });
                            zanadd.addView(imas);
                        }
                        prl.getRefreshableView().addFooterView(vv);
                    }
                    prl.getRefreshableView().setDividerHeight(30);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    public class PRLAdapter extends BaseAdapter {
        private List<View> mydata;
        public PRLAdapter(List<View> mydata){
            this.mydata=mydata;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=mydata.get(i);
            return view;
        }
    }

    public void doPost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.GatherAdd, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("mess_id",id);
        spr.putValue("type_mess_id","3");
        SlingleVolleyRequestQueue.getInstance(Tiezi2Detial.this).addToRequestQueue(spr);
    }
    public void doPost_cancel(){
        StringPostRequest spr=new StringPostRequest(URLMannager.GatherCancel, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("mess_id",id);
        spr.putValue("type_mess_id","3");
        SlingleVolleyRequestQueue.getInstance(Tiezi2Detial.this).addToRequestQueue(spr);
    }

    public void doPost2(){
        StringPostRequest spr=new StringPostRequest(URLMannager.LandAdd, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("p_id",id);
        spr.putValue("type_mess_id","3");
        SlingleVolleyRequestQueue.getInstance(Tiezi2Detial.this).addToRequestQueue(spr);
    }
    public void doPost2_cancel(){
        StringPostRequest spr=new StringPostRequest(URLMannager.LandCancel, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("p_id",id);
        spr.putValue("type_mess_id","3");
        SlingleVolleyRequestQueue.getInstance(Tiezi2Detial.this).addToRequestQueue(spr);
    }

    public void getGrid(){
        if(ima_list.size()>0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            // 取得屏幕的宽度和高度
            WindowManager windowManager =getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int Width = display.getWidth();
            int Height = display.getHeight();

            gridView = new NewGridView(this);
            switch (ima_list.size()){
                case 1:
                    gridView.setNumColumns(1);
                    break;
                case 2:
                    gridView.setNumColumns(2);
                    break;
                case 3:
                    gridView.setNumColumns(3);
                    break;
                case 4:
                    gridView.setNumColumns(2);
                    break;
                case 5:
                    gridView.setNumColumns(3);
                    break;
                case 6:
                    gridView.setNumColumns(3);
                    break;

            }
            gridView.setColumnWidth(40);
            gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            if (Width == 720 && Height == 1280) {
                gridView.setColumnWidth(40);
            }
            gridView.setGravity(Gravity.CENTER_VERTICAL);
            gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
            // 去除gridView边框
            gridView.setVerticalSpacing(1);
            gridView.setHorizontalSpacing(1);
            gridView.setLayoutParams(params);
            gridView.setAdapter(new TZGridAdapter(ima_list,getBaseContext()));
           // prl.getRefreshableView().addFooterView(gridView);
        }
    }

    public class TZGridAdapter extends BaseAdapter{
        private List<String> myima;
        private Context context;
        public TZGridAdapter(List<String> myima,Context context){
            this.myima=myima;
            this.context=context;
        }
        @Override
        public int getCount() {
            return myima.size();
        }

        @Override
        public Object getItem(int i) {
            return myima.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            int x=gridView.getNumColumns();
            final ImageView iv=new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+myima.get(i),iv,options);
            if(x==2){
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(300,300);
                params.setMargins(20,0,0,0);
                iv.setLayoutParams(params);
            }else if(x==3){
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(200,200);
                params.setMargins(20,0,0,0);
                iv.setLayoutParams(params);
            }
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(iv.getDrawable(),"",view);
                }
            });
            return iv;
        }
    }
    public void getPicPop(Drawable drawable, String url,View view){
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
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

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


                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(Tiezi2Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi2Detial.this)
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
                        .withText(accontent)
                        .withTitle(actitle)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(Tiezi2Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi2Detial.this)
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
                        .withText(accontent)
                        .withTitle(actitle)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(Tiezi2Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi2Detial.this)
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
                        .withText(accontent)
                        .withTitle(actitle)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(Tiezi2Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi2Detial.this)
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
                        .withText(accontent)
                        .withTitle(actitle)
                        .withTargetUrl(url)
                        .withMedia(image)
                        .share();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.REDIRECT_URL="http://www.isuhuo.com";
                UMShareAPI mShareAPI = UMShareAPI.get(Tiezi2Detial.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(Tiezi2Detial.this, platform, new UMAuthListener() {
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


                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(Tiezi2Detial.this,URLMannager.Imag_URL+acpic);

                new ShareAction(Tiezi2Detial.this)
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
                        .withText(accontent)
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
        WindowManager.LayoutParams lp = Tiezi2Detial.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        Tiezi2Detial.this.getWindow().setAttributes(lp);
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
