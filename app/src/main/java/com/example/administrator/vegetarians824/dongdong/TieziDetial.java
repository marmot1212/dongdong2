package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.MyImageView;
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

public class TieziDetial extends AppCompatActivity {
    private PullToRefreshListView prl;
    private List<View> view_list;
    String id;
    boolean is1,is2;
    private ImageView colima,zanima;
    String type;
    String actitle,accontent,acpic;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiezi_detial);
        StatusBarUtil.setColorNoTranslucent(this,0x00aff0);
        prl=(PullToRefreshListView)findViewById(R.id.prl);
        view_list=new ArrayList<>();
        is1=false;
        is2=false;
        colima=(ImageView)findViewById(R.id.tiezi_collectima);
        zanima=(ImageView)findViewById(R.id.tiezi_zanima);
        Intent intent=getIntent();
        id=intent.getStringExtra("tid");
        initoperate();
        getData();


    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.tiezi_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayout collect=(LinearLayout) findViewById(R.id.tiezi_collect);
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
                    Intent intent = new Intent(TieziDetial.this, Login.class);
                    TieziDetial.this.startActivity(intent);
                }
            }
        });
        LinearLayout zan=(LinearLayout)findViewById(R.id.tiezi_zan);
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
                    Intent intent = new Intent(TieziDetial.this, Login.class);
                    TieziDetial.this.startActivity(intent);
                }

            }
        });

        TextView ppinglun=(TextView)findViewById(R.id.tiezi_pinglun);
        ppinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    Intent intent=new Intent(TieziDetial.this,Comment.class);
                    intent.putExtra("type",type);
                    intent.putExtra("id",id);
                    TieziDetial.this.startActivity(intent);
                }else {
                    Intent intent = new Intent(TieziDetial.this, Login.class);
                    TieziDetial.this.startActivity(intent);
                }
            }
        });
        LinearLayout share=(LinearLayout)findViewById(R.id.tiezi_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    public void getData(){
        String url="";
        if(BaseApplication.app.getUser()!=null){
            url=URLMannager.TieZi+id+"/uid/"+BaseApplication.app.getUser().getId();
        }else {
            url=URLMannager.TieZi;
        }
        Log.d("==============url",url);
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONObject js3=js2.getJSONObject("detail");
                    type=js3.getString("type");
                    //prl数据
                        JSONArray ja = js3.getJSONArray("contents");
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
                                tv.setLineSpacing(0, (float) 1.5);
                                view_list.add(tv);
                            }
                        }


                    actitle=js3.getString("title");
                    String ss=js3.getString("content");
                    if(ss.length()>20){
                    accontent=ss.substring(0,21)+"...";
                    }else {
                        accontent=ss;
                    }
                    if(!js3.isNull("img_url_1"))
                        acpic=js3.getString("img_url_1");
                    String time=js3.getString("create_time");
                    prl.setAdapter(new PRLAdapter(view_list));

                    TextView ti=new TextView(getBaseContext());
                    ti.setText(actitle);
                    ti.setTextColor(Color.BLACK);
                    ti.setTextSize(20);
                    TextView ti2=new TextView(getBaseContext());
                    ti2.setText(time);
                    ti2.setTextColor(0xffa2a2a2);
                    //ti2.setHeight(60);
                    prl.getRefreshableView().addHeaderView(ti);
                    prl.getRefreshableView().addHeaderView(ti2);

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
                        TextView tnum=(TextView)findViewById(R.id.tiezi_pinglunnum);
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
                            imas.setImageResource(R.drawable.cc_touxiang);
                            com.nostra13.universalimageloader.core.ImageLoader loader2 = ImageLoaderUtils.getInstance(TieziDetial.this);
                            DisplayImageOptions options2 = ImageLoaderUtils.getOpt();
                            loader2.displayImage(URLMannager.Imag_URL + "" + jo.getString("user_head_img"), imas, options2);
                            final String id=jo.getString("id");
                            imas.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(TieziDetial.this,UserDetial.class);
                                    intent.putExtra("uid",id);
                                    if(BaseApplication.app.getUser().islogin()){
                                        intent.putExtra("id",BaseApplication.app.getUser().getId());
                                    }else {
                                        intent.putExtra("id","");
                                    }
                                    TieziDetial.this.startActivity(intent);
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
    public class PRLAdapter extends BaseAdapter{
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
            SlingleVolleyRequestQueue.getInstance(TieziDetial.this).addToRequestQueue(spr);
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
        SlingleVolleyRequestQueue.getInstance(TieziDetial.this).addToRequestQueue(spr);
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
        SlingleVolleyRequestQueue.getInstance(TieziDetial.this).addToRequestQueue(spr);
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
        SlingleVolleyRequestQueue.getInstance(TieziDetial.this).addToRequestQueue(spr);
    }
    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_bottom, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setFocusable(true);
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
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");

                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(TieziDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(TieziDetial.this)
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
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(TieziDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(TieziDetial.this)
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
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(TieziDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(TieziDetial.this)
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
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(TieziDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(TieziDetial.this)
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
                UMShareAPI mShareAPI = UMShareAPI.get(TieziDetial.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(TieziDetial.this, platform, new UMAuthListener() {
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
                String url=URLMannager.ShareTiezi+id;
                UMImage image = new UMImage(TieziDetial.this,URLMannager.Imag_URL+acpic);

                new ShareAction(TieziDetial.this)
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
        WindowManager.LayoutParams lp = TieziDetial.this.getWindow().getAttributes();
        lp.alpha=bgAlpha; //0.0-1.0
        TieziDetial.this.getWindow().setAttributes(lp);
    }
}
