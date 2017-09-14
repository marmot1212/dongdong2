package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CommentAdapter;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.NewGridView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
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

public class Tiezi3Detial extends AppCompatActivity {
    private PullToRefreshListView prl;
    private List<View> view_list;
    private List<String> ima_list;
    private List<Pinglun> pl_list;
    String id;
    boolean is1,is2;
    private ImageView colima,zanima;
    String type;
    private NewGridView gridView;
    PopupWindow popupWindow,popWindow;
    String actitle,accontent,acpic;
    private int count;//第几次加载
    private int totalpage;//总页数
    private boolean isup;
    private Date date;
    private boolean emojiShow=false;
    EditText input;
    FrameLayout fram;
    ListViewForScrollView listView_pl;
    FrameLayout fram_emoji;
    View user_head;
    LinearLayout inputParent;
    FaceFragment faceFragment;
    LinearLayout inputline;
    InputMethodManager inputManager;
    EditText eee;
    private boolean startlistern=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiezi3_detial);
        StatusBarUtil.setColorNoTranslucent(this,0x00aff0);
        prl=(PullToRefreshListView)findViewById(R.id.prl3);

        fram=(FrameLayout)findViewById(R.id.tiezi3_fram);
        count=1;
        isup=false;
        date=new Date();
        view_list=new ArrayList<>();
        ima_list=new ArrayList<>();
        pl_list=new ArrayList<>();
        inputline=(LinearLayout)findViewById(R.id.comment_inputline) ;
        fram_emoji=(FrameLayout)findViewById(R.id.emoji_fram);
        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.emoji_fram,faceFragment).commit();
        //eee=(EditText)findViewById(R.id.comment_input2);

        Intent intent=getIntent();
        id=intent.getStringExtra("tid");
        initoperate();
        initPRL(false);
    }

    public void initPRL(boolean isrefresh){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);
        // 设置上拉刷新文本
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);

        //设置加载动画


        getData(URLMannager.TieZi+id+"/p/"+count+"/t/10",isrefresh);

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
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.tiezi3_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView ppinglun=(TextView)findViewById(R.id.tiezi3_pinglun);
        ppinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseApplication.app.getUser().islogin()){
                    getPopwindow();
                    //listenerSoftInput();
                }else {
                    Intent intent=new Intent(Tiezi3Detial.this,Login.class);
                    Tiezi3Detial.this.startActivity(intent);
                }
            }
        });
        LinearLayout share=(LinearLayout)findViewById(R.id.tiezi3_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow();
                popupWindow.showAtLocation(prl, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    public void getData(String url, final boolean isrefresh){
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONObject js3=js2.getJSONObject("detail");
                    Log.d("========detail",js3.toString());
                    type=js3.getString("type");
                    //图文混排
                    if(!js3.isNull("contents")) {
                        JSONArray ja=js3.getJSONArray("contents");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            String type = jo.getString("type");
                            if (type.equals("1")) {
                                String pic = jo.getString("content");
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
                                try {
                                    EmojiUtil.handlerEmojiText(tv, tv.getText().toString(), getBaseContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                        try {
                            EmojiUtil.handlerEmojiText(tv, tv.getText().toString(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        view_list.add(tv);
                        for(int i=1;i<=6;i++){
                            if(!js3.isNull("img_url_"+i)){
                                ima_list.add(js3.getString("img_url_"+i));
                            }
                        }
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
                    final String uid=js3.getString("uid");

                    //用户信息
                    user_head= getLayoutInflater().inflate(R.layout.tiezi_head,null);
                    ImageView im=(ImageView) user_head.findViewById(R.id.tiezi_head_ima);
                    TextView tv1=(TextView) user_head.findViewById(R.id.tiezi_head_title);
                    TextView tv2=(TextView) user_head.findViewById(R.id.tiezi_head_uname);
                    TextView tv3=(TextView) user_head.findViewById(R.id.tiezi_head_time);
                    ImageView lv=(ImageView)user_head.findViewById(R.id.tiezi_head_lv);
                    com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+ima,im,options);
                    im.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(Tiezi3Detial.this,UserDetial.class);
                            intent.putExtra("uid",uid);
                            if(BaseApplication.app.getUser().islogin()){
                                intent.putExtra("id",BaseApplication.app.getUser().getId());
                            }else {
                                intent.putExtra("id","");
                            }
                            Tiezi3Detial.this.startActivity(intent);
                        }
                    });

                    tv1.setText(actitle);
                    tv2.setText(uname);
                    tv3.setText(time);
                    switch (grade){
                        case "1":lv.setImageResource(R.mipmap.lv1);break;
                        case "2":lv.setImageResource(R.mipmap.lv2);break;
                        case "3":lv.setImageResource(R.mipmap.lv3);break;
                        default:break;
                    }

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
                            pl_list.add(pl);
                        }

                    }
                        //添加布局控件
                        prl.setAdapter(new PRLAdapter(view_list));
                        //用户信息
                        prl.getRefreshableView().addHeaderView(user_head);
                        //帖子图片
                        if (ima_list.size() > 0)
                            prl.getRefreshableView().addFooterView(gridView);
                        //评论列表
                        if (pl_list.size() > 0) {
                            listView_pl = new ListViewForScrollView(getBaseContext());
                            listView_pl.setAdapter(new CommentAdapter(pl_list, Tiezi3Detial.this));
                            TextView tall = new TextView(getBaseContext());
                            tall.setText("全部回复");
                            tall.setTextColor(0xff000000);
                            tall.setTextSize(12);
                            ViewGroup.LayoutParams params0=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,40);
                            tall.setLayoutParams(params0);
                            listView_pl.addHeaderView(tall);
                            TextView tv=new TextView(getBaseContext());
                            tv.setText("已经全部加载完毕");
                            tv.setTextSize(12);
                            tv.setTextColor(0xffa0a0a0);
                            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
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
    public void getGrid(){
        if(ima_list.size()>0){
            GridView.LayoutParams params = new GridView.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(300,300);
                iv.setLayoutParams(params);
            }else if(x==3){
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(200,200);
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
    public void getPicPop(Drawable drawable, String url, View view){
        View popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.image_pop,null);
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
                UMImage image = new UMImage(Tiezi3Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi3Detial.this)
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
                UMImage image = new UMImage(Tiezi3Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi3Detial.this)
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
                UMImage image = new UMImage(Tiezi3Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi3Detial.this)
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
                UMImage image = new UMImage(Tiezi3Detial.this,URLMannager.Imag_URL+acpic);
                new ShareAction(Tiezi3Detial.this)
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
                UMShareAPI mShareAPI = UMShareAPI.get(Tiezi3Detial.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(Tiezi3Detial.this, platform, new UMAuthListener() {
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
                UMImage image = new UMImage(Tiezi3Detial.this,URLMannager.Imag_URL+acpic);

                new ShareAction(Tiezi3Detial.this)
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
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = Tiezi3Detial.this.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        Tiezi3Detial.this.getWindow().setAttributes(lp);
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
            prl.getRefreshableView().removeFooterView(gridView);
            prl.getRefreshableView().removeFooterView(listView_pl);
            prl.getRefreshableView().removeHeaderView(user_head);
            view_list=new ArrayList<>();
            ima_list=new ArrayList<>();
            pl_list=new ArrayList<>();
            initPRL(false);
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
                doPost();
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
    public void doPost(){
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
        SlingleVolleyRequestQueue.getInstance(Tiezi3Detial.this).addToRequestQueue(spr);
    }
    /*
    private void listenerSoftInput() {
        final View activityRootView = prl;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d("==============s2", heightDiff+"");
                if (heightDiff<300&&(!emojiShow)&&startlistern) { // 如果高度差超过100像素，就很有可能是有软键盘...

                }else {

                }
            }
        });

    }
    */

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
