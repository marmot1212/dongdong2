package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CommentAdapter;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Comment extends AppCompatActivity {
    String type,id;
    CommentAdapter adapter;
    private int totalpage;//总页数
    private PullToRefreshListView prl;
    private List<Pinglun> list;//帖子列表
    private int count;//第几次加载
    EditText input;
    TextView nocomment;

    private boolean emojiShow=false;
    LinearLayout inputParent;
    FaceFragment faceFragment;
    LinearLayout inputline;
    InputMethodManager inputManager;
    FrameLayout fram_emoji;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        id=intent.getStringExtra("id");
        list=new ArrayList<>();
        nocomment=(TextView)findViewById(R.id.nocomment);
        count=1;
        prl=(PullToRefreshListView)findViewById(R.id.comment_prl);

        inputline=(LinearLayout)findViewById(R.id.commentcp_inputline) ;
        fram_emoji=(FrameLayout)findViewById(R.id.cpemoji_fram);
        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.cpemoji_fram,faceFragment).commit();

        initPRL();
        initop();
    }
    public void initPRL(){
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉可以刷新");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据");
        // 设置上拉刷新文本
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据");
        //设置加载动画
        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        getData();
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list=new ArrayList<Pinglun>();
                count=1;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new DataRefresh().execute();

            }
        });
    }
    public void getData(){
        StringRequest request=new StringRequest(URLMannager.CommentList+type+"/mess_id/"+id+"/p/"+count+"/t/10", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                adapter= new CommentAdapter(list,Comment.this);
                prl.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(list.size()>0){

                    if(prl.getRefreshableView().getFooterViewsCount()==1) {
                        TextView tv = new TextView(getBaseContext());
                        tv.setText("已经全部加载完毕");
                        tv.setTextSize(12);
                        tv.setTextColor(0xffa0a0a0);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                        tv.setLayoutParams(params);
                        tv.setGravity(Gravity.CENTER);
                        prl.getRefreshableView().addFooterView(tv);
                    }
                    nocomment.setVisibility(View.INVISIBLE);
                }else {
                    nocomment.setVisibility(View.VISIBLE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }
    public void myJson(String s){
        try {
            JSONObject js1=new JSONObject(s);
            JSONObject js2=js1.getJSONObject("Result");
            String st=js2.getString("totalpage");
            totalpage=Integer.valueOf(st).intValue();
            if(totalpage==1){
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
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
                list.add(pl);
            }

        } catch (JSONException e) {
            e.printStackTrace();
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
            count++;
            if(count<=totalpage){
                getData();
            }
            else{
                Toast.makeText(getBaseContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.comment_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView fabiao=(TextView)findViewById(R.id.comment_fabiao);
        fabiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopwindow();
                //listenerSoftInput();
            }
        });
    }
    public void getPopwindow(){
        inputline.setVisibility(View.VISIBLE);
        input=(EditText) findViewById(R.id.cpcomment_input);
        inputParent=(LinearLayout)findViewById(R.id.cpcomment_input_parent);
        inputManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(input, 0);
        inputManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);

        Button bt=(Button) findViewById(R.id.cpcomment_send);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPost();
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                }
                inputline.setVisibility(View.INVISIBLE);
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
        StringPostRequest spr=new StringPostRequest(URLMannager.CommentAdd, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                list=new ArrayList<Pinglun>();
                count=1;
                getData();
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
        SlingleVolleyRequestQueue.getInstance(Comment.this).addToRequestQueue(spr);
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
