package com.example.administrator.vegetarians824.dongdong;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.YongliaoAdapter;
import com.example.administrator.vegetarians824.adapter.ZuofaAdapter;
import com.example.administrator.vegetarians824.entry.Yongliao;
import com.example.administrator.vegetarians824.entry.Zuofa;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
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

public class CaipuDetail extends AppCompatActivity {

    private List<Yongliao> yl_list;
    private List<Zuofa> zf_list;
    private PopupWindow popupWindow;
    private String content,title,pic,id;
    private String type;
    private String uid;
    private ImageView pic1;
    private ScrollView scrollView;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 是否正在放大
    private Boolean mScaling = false;

    private DisplayMetrics metric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caipu_detail);
        yl_list=new ArrayList<>();
        zf_list=new ArrayList<>();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        scrollView=(ScrollView)findViewById(R.id.caipu_scroll);
        pic1=(ImageView)findViewById(R.id.caipu_pic);
        httpRequest(id);
        initoperate();
        setScroll();
    }

    public void httpRequest(String id){

        StringRequest request=new StringRequest(URLMannager.Caipu_Detail + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    public void myJson(String s){
        try {
            JSONObject js=new JSONObject(s);
            JSONObject js1=js.getJSONObject("Result");
            JSONObject js2=js1.getJSONObject("dish");
            type=js2.getString("type");
            title=js2.getString("title");
            content=js2.getString("content");
            uid=js2.getString("uid");
            String uname=js2.getString("username");
            String time=js2.getString("create_time");
            pic=js2.getString("pic");
            String upic=js2.getString("user_head");
            String reminded="";
            if((!js2.isNull("reminded"))&&(!js2.getString("reminded").equals(""))){
                reminded=js2.getString("reminded");
                TextView tv5=(TextView)findViewById(R.id.caipu_items);
                tv5.setText(reminded);
            }else
                if(js2.getString("reminded").equals("")) {
                LinearLayout ll = (LinearLayout) findViewById(R.id.caipu_line);
                TextView l1 = (TextView) findViewById(R.id.caipu_line1);
                ll.removeView(l1);
            }

            TextView tv1=(TextView)findViewById(R.id.caipu_name);
            tv1.setText(title);
            TextView tv2=(TextView)findViewById(R.id.caipu_content);
            tv2.setText(content);
            TextView tv3=(TextView)findViewById(R.id.caipu_username);
            tv3.setText(uname);
            TextView tv4=(TextView)findViewById(R.id.caipu_time);
            tv4.setText(time);
            ImageView lv=(ImageView)findViewById(R.id.caipu_lv);
            switch (js2.getString("lv")){
                case "1":lv.setImageResource(R.mipmap.lv1);break;
                case "2":lv.setImageResource(R.mipmap.lv2);break;
                case "3":lv.setImageResource(R.mipmap.lv3);break;
                default:break;
            }
            ImageView pic2=(ImageView)findViewById(R.id.caipu_userpic);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(CaipuDetail.this);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+pic,pic1,options);
            loader.displayImage(URLMannager.Imag_URL+""+upic,pic2,options);
            FrameLayout fram=(FrameLayout)findViewById(R.id.caipu_fram);
            StatusBarUtil.setTranslucentForImageView(CaipuDetail.this,fram);

            pic2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(CaipuDetail.this, UserDetial.class);
                    intent.putExtra("uid",uid);
                    CaipuDetail.this.startActivity(intent);
                }
            });
            JSONArray ja1=js2.getJSONArray("dress");
            for(int i=0;i<ja1.length();i++){
                JSONObject jaa=ja1.getJSONObject(i);
                Yongliao yl=new Yongliao();
                yl.setName(jaa.getString("title"));
                yl.setNum(jaa.getString("num"));
                if(jaa.getString("pid")!=null){
                    yl.setPid(jaa.getString("pid"));
                }
                yl_list.add(yl);
            }
            ListViewForScrollView yl_lv=(ListViewForScrollView) findViewById(R.id.caipu_yongliao);
            yl_lv.setAdapter(new YongliaoAdapter(yl_list,CaipuDetail.this));


            JSONArray ja2=js2.getJSONArray("step");
            Log.d("============step",ja2.toString());
            for(int j=0;j<ja2.length();j++){
                JSONObject jaa2=ja2.getJSONObject(j);
                Zuofa zf=new Zuofa();
                zf.setStep(""+(j+1));
                zf.setPic(jaa2.getString("img_url"));
                zf.setDep(jaa2.getString("content"));
                zf.setInfo(jaa2.getString("img_info"));
                zf_list.add(zf);
            }
            ListViewForScrollView zf_lv=(ListViewForScrollView) findViewById(R.id.caipu_zuofa);
            zf_lv.setAdapter(new ZuofaAdapter(zf_list,CaipuDetail.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void initoperate(){
        TextView share=(TextView)findViewById(R.id.caipu_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        TextView pinglun=(TextView)findViewById(R.id.caipu_pinglun);
        pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    Intent intent=new Intent(CaipuDetail.this,Comment.class);
                    intent.putExtra("id",id);
                    intent.putExtra("type",type);
                    CaipuDetail.this.startActivity(intent);
                }else {
                    Intent intent = new Intent(CaipuDetail.this, Login.class);
                    CaipuDetail.this.startActivity(intent);
                }
            }
        });
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.caipu_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");

                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(CaipuDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(CaipuDetail.this)
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
                        .withText(content)
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
                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(CaipuDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(CaipuDetail.this)
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
                        .withText(content)
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
                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(CaipuDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(CaipuDetail.this)
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
                        .withText(content)
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
                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(CaipuDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(CaipuDetail.this)
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
                        .withText(content)
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
                UMShareAPI mShareAPI = UMShareAPI.get(CaipuDetail.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(CaipuDetail.this, platform, new UMAuthListener() {
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
                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(CaipuDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(CaipuDetail.this)
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
                        .withText(content)
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
        WindowManager.LayoutParams lp = CaipuDetail.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        CaipuDetail.this.getWindow().setAttributes(lp);
    }
    public void setScroll(){
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        ViewGroup.LayoutParams lp =pic1.getLayoutParams();
        lp.width = metric.widthPixels;
        lp.height = metric.widthPixels * 9 / 16;
        pic1.setLayoutParams(lp);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({ "ClickableViewAccessibility", "NewApi" })
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = pic1.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //手指离开后恢复图片
                        mScaling = false;
                        replyImage();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!mScaling) {
                            if (scrollView.getScrollY() == 0) {
                                mFirstPosition = event.getY();// 滚动到顶部时记录位置，否则正常返回
                            } else {
                                break;
                            }
                        }
                        int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                        if (distance < 0) { // 当前位置比记录位置要小，正常返回
                            break;
                        }

                        // 处理放大
                        mScaling = true;
                        lp.width = metric.widthPixels + distance;
                        lp.height = (metric.widthPixels + distance) * 9 / 16;
                        pic1.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });
    }

    // 回弹动画 (使用了属性动画)
    @SuppressLint("NewApi")
    public void replyImage() {
        final ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) pic1.getLayoutParams();
        final float w = pic1.getLayoutParams().width;// 图片当前宽度
        final float h = pic1.getLayoutParams().height;// 图片当前高度
        final float newW = metric.widthPixels;// 图片原宽度
        final float newH = metric.widthPixels * 9 / 16;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                pic1.setLayoutParams(lp);
            }
        });
        anim.start();

    }
}
