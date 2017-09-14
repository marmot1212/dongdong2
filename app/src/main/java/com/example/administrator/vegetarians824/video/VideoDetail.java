package com.example.administrator.vegetarians824.video;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.utils.NetworkUtils;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CommentAdapter;
import com.example.administrator.vegetarians824.adapter.YongliaoAdapter;
import com.example.administrator.vegetarians824.adapter.ZuofaAdapter;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.entry.Yongliao;
import com.example.administrator.vegetarians824.entry.Zuofa;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SDHelper;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.example.administrator.vegetarians824.video.utlis.MediaUtils;
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
import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloadManager;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.base.Status;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;
import org.wlf.filedownloader.listener.OnRenameDownloadFileListener;
import org.wlf.filedownloader.listener.OnRetryableFileDownloadStatusListener;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VideoDetail extends AppCompatActivity {
    private PlayerView player;
    private Context mContext;
    private PowerManager.WakeLock wakeLock;
    private List<VideoijkBean> list;
    private String id;
    private LoadingDialog loadingDialog;
    private TextView vtitle,vusername,vviewcount,vcreatetime;
    private List<Yongliao> yl_list;
    private List<Zuofa> zf_list;
    private List<Pinglun> comment_list;
    private ListViewForScrollView listview_comment;
    private CommentAdapter adapter;
    private TextView count1,count2,count3;
    private LinearLayout op1,op2,op3,op4;
    private boolean collectmark=false;
    private ImageView collectima;
    private ScrollView scrollView;
    private LinearLayout commentline;
    LinearLayout inputParent;
    FaceFragment faceFragment;
    LinearLayout inputline;
    InputMethodManager inputManager;
    FrameLayout fram_emoji;
    EditText input;
    private boolean emojiShow=false;
    private PopupWindow popWindow;
    private String content,title,pic;
    private View rootView;
    private Toast mtoast;
    private SharedPreferences pre;
    private String videopath;
    private String []infolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        rootView = getLayoutInflater().from(this).inflate(R.layout.activity_video_detail, null);
        setContentView(rootView);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        pre=getSharedPreferences("shared", Context.MODE_PRIVATE);
        Intent intent=getIntent();
        if(intent.hasExtra("url")){
            videopath=intent.getStringExtra("url");
            String info=pre.getString(videopath,"");
            infolist=info.split("\\|");
            id=infolist[0];
        }else {
            id=getIntent().getStringExtra("id");
        }
        list=new ArrayList<>();
        yl_list=new ArrayList<>();
        zf_list=new ArrayList<>();
        loadingDialog=new LoadingDialog(VideoDetail.this);
        loadingDialog.show();
        initView();
        setPlayer();
        getDetial();
        getComment();
        initCommentLine();
        initPopuptWindow();

    }

    public void getDetial(){
        String uid="";
        if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()){
            uid="/uid/"+BaseApplication.app.getUser().getId();
        }
        StringRequest request=new StringRequest(URLMannager.Caipu_Detail + id+uid, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONObject js3=js2.getJSONObject("dish");
                        //设置视频资源
                        title=js3.getString("title");
                        content=js3.getString("content");
                        player.setTitle(title);
                        VideoijkBean m1 = new VideoijkBean();
                        String url1=URLMannager.Imag_URL+js3.getString("video");
                        Log.d("===========vurl",url1);
                        m1.setStream("标清");
                        m1.setUrl(url1);
                        list.add(m1);
                        pic=js3.getString("pic");
                        player.showThumbnail(new OnShowThumbnailListener() {
                            @Override
                            public void onShowThumbnail(ImageView ivThumbnail) {
                                Glide.with(mContext)
                                        .load(URLMannager.Imag_URL+""+pic)
                                        .placeholder(R.color.cl_default)
                                        .error(R.color.cl_error)
                                        .into(ivThumbnail);
                            }
                        }).setPlaySource(list);
                        boolean autoplay_wifi=pre.getBoolean("autoplay_wifi",true);
                        if(autoplay_wifi&& NetworkUtils.getNetworkType(getBaseContext())==3){
                            player.startPlay();
                        }else {
                            player.stopPlay();
                        }
                        //player.setPlaySource(list).startPlay();

                        //视频下面的内容
                        vtitle.setText(title);
                        vusername.setText(js3.getString("username"));
                        vviewcount.setText("播放："+js3.getString("view"));
                        vcreatetime.setText(js3.getString("create_time"));
                        //用料
                        JSONArray ja1=js3.getJSONArray("dress");
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
                        yl_lv.setAdapter(new YongliaoAdapter(yl_list,VideoDetail.this));

                        //做法
                        JSONArray ja2=js3.getJSONArray("step");
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
                        zf_lv.setAdapter(new ZuofaAdapter(zf_list,VideoDetail.this));

                        //收藏 评论 分享 下载数量
                        count1.setText(js3.getString("gather_count"));
                        count2.setText(js2.getString("total"));
                        count3.setText(js3.getString("share"));
                        if(js3.getString("gather_status").equals("1")){
                            collectima.setImageResource(R.mipmap.video_collect1);
                            collectmark=true;
                        }else {
                            collectima.setImageResource(R.mipmap.video_collect2);
                            collectmark=false;
                        }
                        op1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                                    if (!collectmark) {
                                        collectmark=true;
                                        doCollect();
                                    } else {
                                        collectmark=false;
                                        cancelCollect();
                                    }
                                }else {
                                    startActivity(new Intent(getBaseContext(),Login.class));
                                }
                            }
                        });
                        op2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                scrollView.smoothScrollTo(0,commentline.getTop());
                            }
                        });
                        op3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                backgroundAlpha(0.5f);
                                popWindow.showAtLocation(v, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                            }
                        });
                        op4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(SDHelper.isHasSdcard()) {
                                    SDHelper.getDir();
                                    FileDownloader.registerDownloadStatusListener(new OnRetryableFileDownloadStatusListener() {
                                        @Override
                                        public void onFileDownloadStatusRetrying(DownloadFileInfo downloadFileInfo, int retryTimes) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusWaiting(DownloadFileInfo downloadFileInfo) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusPreparing(DownloadFileInfo downloadFileInfo) {
                                            Log.d("============status3",downloadFileInfo.getStatus()+"");
                                            if(downloadFileInfo.getUrl().equals(list.get(0).getUrl())) {
                                                if (mtoast == null) {
                                                    mtoast = Toast.makeText(getBaseContext(), "开始下载", Toast.LENGTH_SHORT);
                                                    mtoast.show();
                                                } else {
                                                    mtoast.cancel();
                                                    mtoast = Toast.makeText(getBaseContext(), "开始下载", Toast.LENGTH_SHORT);
                                                    mtoast.show();
                                                }
                                                SharedPreferences.Editor editor=pre.edit();
                                                editor.putString(list.get(0).getUrl(),id+"|"+vtitle.getText().toString()+"|"+vusername.getText().toString()+"|"+vviewcount.getText().toString()+"|"+vcreatetime.getText().toString());
                                                editor.commit();
                                            }
                                        }

                                        @Override
                                        public void onFileDownloadStatusPrepared(DownloadFileInfo downloadFileInfo) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusDownloading(DownloadFileInfo downloadFileInfo, float downloadSpeed, long remainingTime) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusPaused(DownloadFileInfo downloadFileInfo) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusCompleted(DownloadFileInfo downloadFileInfo) {

                                        }

                                        @Override
                                        public void onFileDownloadStatusFailed(String url, DownloadFileInfo downloadFileInfo, FileDownloadStatusFailReason failReason) {
                                            Log.d("============status6",downloadFileInfo.getStatus()+"");
                                            String msg = getString(R.string.main__download_error);

                                            if (failReason != null) {
                                                if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__check_network);
                                                } else if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__url_illegal);
                                                } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__network_timeout);
                                                } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__storage_space_is_full);
                                                } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_CAN_NOT_WRITE.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__storage_space_can_not_write);
                                                } else if (FileDownloadStatusFailReason.TYPE_FILE_NOT_DETECT.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__file_not_detect);
                                                } else if (FileDownloadStatusFailReason.TYPE_BAD_HTTP_RESPONSE_CODE.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__http_bad_response_code);
                                                } else if (FileDownloadStatusFailReason.TYPE_HTTP_FILE_NOT_EXIST.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__http_file_not_exist);
                                                } else if (FileDownloadStatusFailReason.TYPE_SAVE_FILE_NOT_EXIST.equals(failReason.getType())) {
                                                    msg += getString(R.string.main__save_file_not_exist);
                                                }
                                            }
                                            if(mtoast==null){
                                                mtoast=Toast.makeText(getBaseContext(), msg,Toast.LENGTH_SHORT);
                                                mtoast.show();
                                            }else {
                                                mtoast.cancel();
                                                mtoast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
                                                mtoast.show();
                                            }

                                        }
                                    });
                                    boolean download_gprs=pre.getBoolean("download_gprs",false);
                                    int i=NetworkUtils.getNetworkType(getBaseContext());
                                    if(download_gprs){
                                        //允许使用gprs下载

                                        //判断文件是否重复
                                        boolean exit=false;
                                        List<DownloadFileInfo> filelist= FileDownloader.getDownloadFiles();
                                        Log.d("=========urlss",filelist.size()+"");
                                        for(int x=0;x<filelist.size();x++){
                                            Log.d("=========url",filelist.get(x).getUrl());
                                            if(filelist.get(x).getUrl().equals(list.get(0).getUrl())){
                                                exit=true;
                                            }

                                        }
                                        if(!exit){
                                            FileDownloader.start(list.get(0).getUrl());
                                            //FileDownloader.createAndStart(list.get(0).getUrl(),SDHelper.getDir()+"/",title);
                                        }else {
                                            Toast.makeText(getBaseContext(),"文件已存在",Toast.LENGTH_SHORT).show();
                                        }

                                    }else {
                                        //不允许gprs下载
                                        if(i==4||i==5||i==6){
                                            Toast.makeText(getBaseContext(),"请切换至WiFi网络下下载",Toast.LENGTH_SHORT);
                                        }else {


                                            boolean exit=false;
                                            List<DownloadFileInfo> filelist= FileDownloader.getDownloadFiles();
                                            Log.d("=========urlss",filelist.size()+"");
                                            for(int x=0;x<filelist.size();x++){
                                                Log.d("=========url",filelist.get(x).getUrl());
                                                if(filelist.get(x).getUrl().equals(list.get(0).getUrl())){
                                                    exit=true;
                                                }

                                            }
                                            if(!exit){
                                                FileDownloader.start(list.get(0).getUrl());
                                                //FileDownloader.createAndStart(list.get(0).getUrl(),SDHelper.getDir()+"/",title);
                                            }else {
                                                Toast.makeText(getBaseContext(),"文件已存在",Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    }

                                }else {
                                    Toast.makeText(getBaseContext(),"找不到SD卡",Toast.LENGTH_SHORT);
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.dismiss();
                VideoijkBean m1 = new VideoijkBean();
                String url1=FileDownloader.getDownloadFile(videopath).getFilePath();
                m1.setStream("标清");
                m1.setUrl(url1);
                list.add(m1);
                player.showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
                                .load(R.drawable.logo120)
                                .placeholder(R.color.cl_default)
                                .error(R.color.cl_error)
                                .into(ivThumbnail);
                    }
                }).setPlaySource(list).startPlay();

                vtitle.setText(infolist[1]);
                vusername.setText(infolist[2]);
                vviewcount.setText(infolist[3]);
                vcreatetime.setText(infolist[4]);

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(mContext, false);
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    public void setPlayer(){
        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        player = new PlayerView(this,rootView){
            @Override
            public PlayerView toggleProcessDurationOrientation() {
                hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
            }
            @Override
            public PlayerView setPlaySource(List<VideoijkBean> list) {
                return super.setPlaySource(list);
            }
        }
                .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
                .setScaleType(PlayStateParams.wrapcontent)
                .hideMenu(true)
                .forbidTouch(false)
                .hideSteam(true)
                .hideCenterPlayer(true);

                /*
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
                                .load(R.color.cl_default)
                                .placeholder(R.color.cl_default)
                                .error(R.color.cl_error)
                                .into(ivThumbnail);
                    }
                });
                */
               // .setPlaySource(list)
               // .startPlay();
        boolean play_gprs=pre.getBoolean("play_gprs",false);
        if(play_gprs){
            player.setNetWorkTypeTie(true);
        }else {
            player.setNetWorkTypeTie(false);
        }
        //player.setNetWorkTypeTie(true);
    }

    public void initView(){
        vtitle=(TextView)findViewById(R.id.video_detail_title);
        vusername=(TextView)findViewById(R.id.video_detail_username);
        vviewcount=(TextView)findViewById(R.id.video_detail_viewcount);
        vcreatetime=(TextView)findViewById(R.id.video_detail_createtime);
        listview_comment=(ListViewForScrollView)findViewById(R.id.caipu_commentlist);
        scrollView=(ScrollView)findViewById(R.id.video_detail_scroll);
        count1=(TextView)findViewById(R.id.video_collect_count);
        count2=(TextView)findViewById(R.id.video_comment_count);
        count3=(TextView)findViewById(R.id.video_share_count);
        op1=(LinearLayout)findViewById(R.id.video_detail_collect);
        op2=(LinearLayout)findViewById(R.id.video_detail_comment);
        op3=(LinearLayout)findViewById(R.id.video_detail_share);
        op4=(LinearLayout)findViewById(R.id.video_detail_download);
        collectima=(ImageView)findViewById(R.id.video_detail_collectima);
        commentline=(LinearLayout)findViewById(R.id.video_commentline);
    }

    public void getComment(){
        comment_list=new ArrayList<>();
        StringRequest request=new StringRequest(URLMannager.CommentList+"7/mess_id/"+id+"/p/1/t/10000", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Pinglun pl=new Pinglun();
                            pl.setUid(jo.getString("uid"));
                            pl.setId(jo.getString("id"));
                            pl.setContent(jo.getString("content"));
                            pl.setUsername(jo.getString("username"));
                            pl.setCreate_time_text(jo.getString("create_time_text"));
                            pl.setUser_head_img_th(jo.getString("user_head_img"));
                            pl.setLv(jo.getString("lv"));
                            comment_list.add(pl);
                        }
                    }
                    adapter=new CommentAdapter(comment_list,VideoDetail.this);
                    listview_comment.setAdapter(adapter);

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

    public void initCommentLine(){
        inputline=(LinearLayout)findViewById(R.id.commentcp_inputline) ;
        fram_emoji=(FrameLayout)findViewById(R.id.cpemoji_fram);
        faceFragment = FaceFragment.Instance();
        input=(EditText) findViewById(R.id.cpcomment_input);
        inputParent=(LinearLayout)findViewById(R.id.cpcomment_input_parent);

        getSupportFragmentManager().beginTransaction().add(R.id.cpemoji_fram,faceFragment).commit();
        inputManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Button bt=(Button) findViewById(R.id.cpcomment_send);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emojiShow){
                    ViewGroup.LayoutParams params=fram_emoji.getLayoutParams();
                    params.height=0;
                    fram_emoji.setLayoutParams(params);
                    fram_emoji.requestLayout();
                    input.requestFocus();
                    emojiShow=false;
                }
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                    doPost();
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    }
                    input.setText("");
                }else {
                    startActivity(new Intent(getBaseContext(), Login.class));
                }
            }
        });
        ImageView icon=(ImageView)findViewById(R.id.cpemoji_icon);
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

    }

    public void doPost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.CommentAdd, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        Toast.makeText(getBaseContext(),"发布成功",Toast.LENGTH_SHORT).show();
                        getComment();
                    }else {
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
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
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        spr.putValue("mess_id", id);
        spr.putValue("type_mess_id","7");
        spr.putValue("content",input.getText().toString());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void doCollect(){
        StringPostRequest spr=new StringPostRequest(URLMannager.AddDishGather, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        collectima.setImageResource(R.mipmap.video_collect1);
                        int num=Integer.valueOf(count1.getText().toString());
                        count1.setText((num+1)+"");
                        setDialog();
                    }else {
                        Toast.makeText(getBaseContext(), js1.getString("Message"), Toast.LENGTH_SHORT).show();
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("id",id);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public void cancelCollect(){
        StringPostRequest spr=new StringPostRequest(URLMannager.CancelDishGather, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        collectima.setImageResource(R.mipmap.video_collect2);
                        int num=Integer.valueOf(count1.getText().toString());
                        count1.setText((num-1)+"");
                        Toast.makeText(getBaseContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getBaseContext(), js1.getString("Message"), Toast.LENGTH_SHORT).show();
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("id",id);
        spr.putValue("type_mess_id","7");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    protected void initPopuptWindow() {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_bottom, null,
                false);
        // 创建PopupWindow实例
        popWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popWindow.setFocusable(true);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        // 点击其他地方消失
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
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

                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(VideoDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(VideoDetail.this)
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

                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(VideoDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(VideoDetail.this)
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

                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(VideoDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(VideoDetail.this)
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

                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(VideoDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(VideoDetail.this)
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
                UMShareAPI mShareAPI = UMShareAPI.get(VideoDetail.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(VideoDetail.this, platform, new UMAuthListener() {
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


                String url=URLMannager.ShareDish+id;
                UMImage image = new UMImage(VideoDetail.this,URLMannager.Imag_URL+pic);

                new ShareAction(VideoDetail.this)
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

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp =getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void setDialog(){
        AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("菜谱视频已收藏！").setMessage("可以再我的收藏 视频菜谱中查看").setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }



}
