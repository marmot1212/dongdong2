package com.example.administrator.vegetarians824.article;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.NewGridView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tb.emoji.Emoji;
import com.tb.emoji.EmojiUtil;
import com.tb.emoji.FaceFragment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ArticleDetial extends AppCompatActivity {
    private PullToRefreshListView prl;
    private List<View> view_list;
    private List<Pinglun> pl_list;
    String id;
    String type;
    PopupWindow popupWindow,popWindow;
    String actitle,accontent,acpic;
    private int count;//第几次加载
    private int totalpage;//总页数
    private Date date;
    private boolean emojiShow=false;
    EditText input;
    FrameLayout fram;
    ListViewForScrollView listView_pl;
    FrameLayout fram_emoji;
    TextView ti,ti2;
    LinearLayout inputParent;
    FaceFragment faceFragment;
    LinearLayout inputline;
    InputMethodManager inputManager;
    boolean is1,is2;
    private ImageView colima,zanima;
    private TextView count1,count2,count3,count4;
    private CommentAdapter2 commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detial);
        StatusBarUtil.setColorNoTranslucent(this,0x00aff0);
        prl=(PullToRefreshListView)findViewById(R.id.prl3);
        fram=(FrameLayout)findViewById(R.id.tiezi3_fram);
        count=1;
        date=new Date();
        view_list=new ArrayList<>();
        pl_list=new ArrayList<>();
        inputline=(LinearLayout)findViewById(R.id.comment_inputline) ;
        fram_emoji=(FrameLayout)findViewById(R.id.emoji_fram);
        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.emoji_fram,faceFragment).commit();
        Intent intent=getIntent();
        id=intent.getStringExtra("tid");
        initoperate();
        initPRL();
    }
    public void initPRL(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);
        // 设置上拉刷新文本
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);

        //设置加载动画

        if(BaseApplication.getApp().getUser()!=null&&BaseApplication.app.getUser().islogin()) {
            getData(URLMannager.TieZi + id + "/p/" + count + "/t/100"+"/uid/"+BaseApplication.app.getUser().getId());
        }else {
            getData(URLMannager.TieZi + id + "/p/" + count + "/t/100");
        }
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.article_detial_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        is1=false;
        is2=false;
        colima=(ImageView)findViewById(R.id.tiezi_collectima);
        zanima=(ImageView)findViewById(R.id.tiezi_zanima);
        count1=(TextView)findViewById(R.id.tiezi_zan_count);
        count2=(TextView)findViewById(R.id.tiezi_collect_count);
        count3=(TextView)findViewById(R.id.tiezi_comment_count);
        count4=(TextView)findViewById(R.id.tiezi_share_count);
        LinearLayout collect=(LinearLayout) findViewById(R.id.tiezi_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    if(is1){
                        colima.setImageResource(R.mipmap.icon_collect2);
                        is1=false;
                        doPost_cancel();
                        int x=Integer.valueOf(count2.getText().toString())-1;
                        count2.setText(x+"");
                    }else {
                        colima.setImageResource(R.mipmap.icon_collect1);
                        is1=true;
                        doPost();
                        int x=Integer.valueOf(count2.getText().toString())+1;
                        count2.setText(x+"");
                    }
                }else{
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        LinearLayout zan=(LinearLayout)findViewById(R.id.tiezi_zan);
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()) {
                    if (is2) {
                        zanima.setImageResource(R.mipmap.icon_zan2);
                        is2 = false;
                        doPost2_cancel();
                        int x=Integer.valueOf(count1.getText().toString())-1;
                        count1.setText(x+"");
                    } else {
                        zanima.setImageResource(R.mipmap.icon_zan1);
                        is2 = true;
                        doPost2();
                        int x=Integer.valueOf(count1.getText().toString())+1;
                        count1.setText(x+"");
                    }
                }else {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }

            }
        });
        LinearLayout ppinglun=(LinearLayout) findViewById(R.id.tiezi_comment);
        ppinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    getPopwindow();
                    //listenerSoftInput();
                }else {
                    Intent intent=new Intent(ArticleDetial.this,Login.class);
                    startActivity(intent);
                }
            }
        });
        LinearLayout share=(LinearLayout)findViewById(R.id.tiezi_share);
        initPopuptWindow();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundAlpha(0.5f);
                popupWindow.showAtLocation(prl, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

    }
    public void getData(String url){
        Log.d("=========url",url);
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
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
                                final MyImageView ima = new MyImageView(getBaseContext(), null);
                                ima.setType(1);
                                ima.setBorderRadius(6);
                                ima.setScaleType(ImageView.ScaleType.FIT_XY);
                                com.nostra13.universalimageloader.core.ImageLoader loader = ImageLoaderUtils.getInstance(getBaseContext());
                                DisplayImageOptions options = ImageLoaderUtils.getOpt();
                                loader.displayImage(URLMannager.Imag_URL + "" + pic, ima, options);
                                ima.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getPicPop(ima.getDrawable(),prl);
                                    }
                                });
                                view_list.add(ima);
                            }
                            if (type.equals("2")) {
                                String content = jo.getString("content");
                                TextView tv = new TextView(getBaseContext());
                                tv.setText(content);
                                tv.setTextColor(Color.BLACK);
                                tv.setTextSize(16);
                                tv.setLineSpacing(0,(float)1.5);
                                try {
                                    EmojiUtil.handlerEmojiText(tv, tv.getText().toString(), getBaseContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                view_list.add(tv);
                            }
                        }
                    }


                    actitle=js3.getString("title");
                    String time=js3.getString("create_time");
                    String ss=js3.getString("content");
                    if(ss.length()>20){
                        accontent=ss.substring(0,21)+"...";
                    }else {
                        accontent=ss;
                    }
                    if(!js3.isNull("img_url_1"))
                        acpic=js3.getString("img_url_1");

                    //评论信息
                    if(!js2.getString("commentCount").equals("0"))
                    {
                        JSONObject jsc = js2.getJSONObject("commentCountList");
                        String st = jsc.getString("totalpage");
                        totalpage = Integer.valueOf(st).intValue();
                        if (totalpage == 1) {
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        JSONArray jac = jsc.getJSONArray("list");
                        for (int i = 0; i < jac.length(); i++) {
                            JSONObject jo = jac.getJSONObject(i);
                            Pinglun pl = new Pinglun();
                            pl.setUid(jo.getString("uid"));
                            pl.setId(jo.getString("id"));
                            pl.setContent(jo.getString("content"));
                            pl.setUsername(jo.getString("username"));
                            pl.setCreate_time_text(jo.getString("create_time_text"));
                            pl.setUser_head_img_th(jo.getString("user_head_img"));
                            pl.setLv(jo.getString("lv"));
                            pl.setZancount(jo.getString("comment_laund_count"));
                            if(jo.getString("comment_status").equals("1")){
                                pl.setZanstatus(true);
                            }else {
                                pl.setZanstatus(false);
                            }

                            pl_list.add(pl);
                        }

                    }else {
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    //添加布局控件
                    prl.setAdapter(new PRLAdapter(view_list));
                    //用户信息
                     ti=new TextView(getBaseContext());
                    ti.setText(actitle);
                    ti.setTextColor(Color.BLACK);
                    ti.setTextSize(20);
                     ti2=new TextView(getBaseContext());
                    ti2.setText(time);
                    ti2.setTextColor(0xffa2a2a2);
                    //ti2.setHeight(60);
                    prl.getRefreshableView().addHeaderView(ti);
                    prl.getRefreshableView().addHeaderView(ti2);


                    //底部菜单
                    String col=js3.getString("gather_status");
                    String zan=js3.getString("laud_status");
                    if(col.equals("0")){
                        colima.setImageResource(R.mipmap.icon_collect2);
                        is1=false;
                    }else if(col.equals("1")){
                        colima.setImageResource(R.mipmap.icon_collect1);
                        is1=true;
                    }
                    if(zan.equals("0")){
                        zanima.setImageResource(R.mipmap.icon_zan2);
                        is2=false;
                    }else if(zan.equals("1")){
                        zanima.setImageResource(R.mipmap.icon_zan1);
                        is2=true;
                    }
                    count1.setText(js3.getString("laud_count"));
                    count2.setText(js3.getString("gather_count"));
                    count3.setText(js2.getString("commentCount"));
                    count4.setText(js3.getString("share_count"));

                    //评论列表
                    if (pl_list.size() > 0) {
                        listView_pl = new ListViewForScrollView(getBaseContext());
                        commentAdapter=new CommentAdapter2(pl_list, getBaseContext());
                        listView_pl.setAdapter(commentAdapter);
                        TextView tall = new TextView(getBaseContext());
                        tall.setText("全部回复");
                        tall.setTextColor(0xff000000);
                        tall.setTextSize(12);
                        AbsListView.LayoutParams params0=new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,40);
                        tall.setLayoutParams(params0);
                        listView_pl.addHeaderView(tall);
                        TextView tv=new TextView(getBaseContext());
                        tv.setText("已经全部加载完毕");
                        tv.setTextSize(12);
                        tv.setTextColor(0xffa0a0a0);
                        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                        tv.setLayoutParams(params);
                        tv.setGravity(Gravity.CENTER);
                        listView_pl.addFooterView(tv);
                        prl.getRefreshableView().addFooterView(listView_pl);

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
    public void getPicPop(Drawable drawable, View view){
        View popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.image_pop,null);
        ImageView ima=(ImageView) popView.findViewById(R.id.image_show);
        ima.setImageDrawable(drawable);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.width=Width;
        params.height=Width;
        ima.setLayoutParams(params);
        ima.requestLayout();
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
                UMImage image = new UMImage(ArticleDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(ArticleDetial.this)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                addShare(id);
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
                UMImage image = new UMImage(ArticleDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(ArticleDetial.this)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                addShare(id);
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
                UMImage image = new UMImage(ArticleDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(ArticleDetial.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                addShare(id);
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
                UMImage image = new UMImage(ArticleDetial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(ArticleDetial.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                addShare(id);
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
                UMShareAPI mShareAPI = UMShareAPI.get(ArticleDetial.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(ArticleDetial.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        addShare(id);
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
                UMImage image = new UMImage(ArticleDetial.this,URLMannager.Imag_URL+acpic);

                new ShareAction(ArticleDetial.this)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                addShare(id);
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
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ArticleDetial.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ArticleDetial.this.getWindow().setAttributes(lp);
    }
    public class CommentAdapter2 extends BaseAdapter{
        private List<Pinglun> mydata;
        private Context context;
        public CommentAdapter2(List<Pinglun> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View v= LayoutInflater.from(context).inflate(R.layout.comment_item2,null);
            ImageView ima=(ImageView)v.findViewById(R.id.article_comment_ima);
            TextView c=(TextView)v.findViewById(R.id.article_comment_content);
            TextView n=(TextView)v.findViewById(R.id.article_comment_title);
            TextView t=(TextView)v.findViewById(R.id.article_comment_time);
            ImageView lv=(ImageView)v.findViewById(R.id.article_comment_lv);
            c.setText(mydata.get(i).getContent());
            try {
                EmojiUtil.handlerEmojiText(c, c.getText().toString(), getBaseContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            n.setText(mydata.get(i).getUsername());
            t.setText(mydata.get(i).getCreate_time_text());
            switch (mydata.get(i).getLv()){
                case "1":lv.setImageResource(R.mipmap.lv1);break;
                case "2":lv.setImageResource(R.mipmap.lv2);break;
                case "3":lv.setImageResource(R.mipmap.lv3);break;
                default:break;
            }
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getUser_head_img_th(),ima,options);
            ima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ArticleDetial.this,UserDetial.class);
                    intent.putExtra("uid",mydata.get(i).getUid());
                    if(BaseApplication.app.getUser().islogin()){
                        intent.putExtra("id",BaseApplication.app.getUser().getId());
                    }else {
                        intent.putExtra("id","");
                    }
                    ArticleDetial.this.startActivity(intent);
                }
            });

            LinearLayout zan=(LinearLayout) v.findViewById(R.id.article_comment_zan);
            TextView zancount=(TextView)v.findViewById(R.id.article_comment_zancount);
            zancount.setText(mydata.get(i).getZancount());
            final ImageView zanima=(ImageView)v.findViewById(R.id.article_comment_zanima);
            if(mydata.get(i).isZanstatus()){
                zanima.setImageResource(R.mipmap.icon_zan1);
            }else {
                zanima.setImageResource(R.mipmap.icon_zan2);
            }
            zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                        boolean status = mydata.get(i).isZanstatus();
                        int count = Integer.valueOf(mydata.get(i).getZancount());
                        mydata.get(i).setZanstatus(!status);
                        if (status) {
                            mydata.get(i).setZancount((count - 1) + "");
                            zanima.setImageResource(R.mipmap.icon_zan2);
                        } else {
                            mydata.get(i).setZancount((count + 1) + "");
                            zanima.setImageResource(R.mipmap.icon_zan1);
                        }
                        doCommentPost(mydata.get(i).getId());
                        commentAdapter.notifyDataSetChanged();
                    }else {
                        startActivity(new Intent(getBaseContext(),Login.class));
                    }
                }
            });

            return v;
        }
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
            prl.onRefreshComplete();
            prl.getRefreshableView().removeAllViewsInLayout();
            prl.getRefreshableView().removeFooterView(listView_pl);
            prl.getRefreshableView().removeHeaderView(ti);
            prl.getRefreshableView().removeHeaderView(ti2);
            view_list=new ArrayList<>();
            pl_list=new ArrayList<>();
            initPRL();
        }
    }
    public void getPopwindow(){
        inputline.setVisibility(View.VISIBLE);
        input=(EditText) findViewById(R.id.comment_input);
        inputParent=(LinearLayout)findViewById(R.id.comment_input_parent);
        inputManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(input, 0);
        inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        Button bt=(Button) findViewById(R.id.comment_send);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPosts();
                //popWindow.dismiss();
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                }
                inputline.setVisibility(View.INVISIBLE);
            }
        });
        ImageView icon=(ImageView)findViewById(R.id.emoji_icon);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiShow=true;
                inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                ViewGroup.LayoutParams params=fram_emoji.getLayoutParams();
                params.height=210*2;
                fram_emoji.setLayoutParams(params);
                fram_emoji.requestLayout();
                inputParent.setFocusable(true);
                inputParent.setFocusableInTouchMode(true);
                inputParent.requestFocus();
            }
        });
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b&&emojiShow){
                    ViewGroup.LayoutParams params=fram_emoji.getLayoutParams();
                    params.height=0;
                    fram_emoji.setLayoutParams(params);
                    fram_emoji.requestLayout();
                    input.requestFocus();
                    emojiShow=false;

                }
            }
        });
        faceFragment.setListener(new FaceFragment.OnEmojiClickListener() {
            @Override
            public void onEmojiDelete() {
                String text = input.getText().toString();
                if (text.isEmpty()) {
                    return;
                }
                if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                    int index = text.lastIndexOf("[");
                    if (index == -1) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        input.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        try {
                            EmojiUtil.handlerEmojiText2(input, input.getText().toString(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    input.getText().delete(index, text.length());
                    try {
                        EmojiUtil.handlerEmojiText2(input, input.getText().toString(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                input.onKeyDown(KeyEvent.KEYCODE_DEL, event);
            }

            @Override
            public void onEmojiClick(Emoji emoji) {

                if (emoji != null) {
                    int index = input.getSelectionStart();
                    Editable editable = input.getEditableText();
                    editable.append(emoji.getContent());
                }
                try {
                    EmojiUtil.handlerEmojiText2(input, input.getText().toString(), getBaseContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        prl.getRefreshableView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_MOVE&&inputline.getVisibility()==View.VISIBLE) {

                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                    inputline.setVisibility(View.INVISIBLE);
                    if(emojiShow){
                        ViewGroup.LayoutParams params=fram_emoji.getLayoutParams();
                        params.height=0;
                        fram_emoji.setLayoutParams(params);
                        fram_emoji.requestLayout();
                        input.requestFocus();
                        emojiShow=false;
                    }
                }

                return false;
            }
        });
    }
    public void doPosts(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/addcomment", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getBaseContext(),"发布成功",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        spr.putValue("mess_id", id);
        spr.putValue("type_mess_id",type);
        spr.putValue("content",input.getText().toString());
        SlingleVolleyRequestQueue.getInstance(ArticleDetial.this).addToRequestQueue(spr);
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
        spr.putValue("type_mess_id","4");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
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
        spr.putValue("type_mess_id","4");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
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
        spr.putValue("type_mess_id","4");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
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
        spr.putValue("type_mess_id","4");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void doCommentPost(String id){
        StringPostRequest spr=new StringPostRequest(URLMannager.CommentZan, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("id",id);
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void addShare(String id){
        StringPostRequest spr=new StringPostRequest(URLMannager.AddShare, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("id",id);
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
}
