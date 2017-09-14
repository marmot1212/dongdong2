package com.example.administrator.vegetarians824.mine;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.article.ArticleDetial;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.dongdong.Tiezi3Detial;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.CollectInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.example.administrator.vegetarians824.video.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCollect2 extends AppCompatActivity {
    private LinearLayout fanhui;
    private TextView artical,food;
    private EditText et;
    private PullToRefreshListView prl;
    private int count;//第几次加载
    private int totalpage;//总页数
    private boolean isup=false;
    private TextView foot;
    private LoadingDialog loadingDialog;
    private List<CollectInfo> list;
    private String keyword="",type="3";
    private CollectAdapter adapter;
    private android.support.v7.app.AlertDialog.Builder builder;
    Long stime,etime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect2);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        stime=new Long("0").longValue();
        etime=new Long("0").longValue();
        initView();
    }
    public void initView(){
        fanhui=(LinearLayout)findViewById(R.id.my_collect_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        artical=(TextView)findViewById(R.id.my_collect_artical);
        food=(TextView)findViewById(R.id.my_collect_food);
        et=(EditText)findViewById(R.id.my_collect_et);
        prl=(PullToRefreshListView)findViewById(R.id.my_collect_prl);
        foot = new TextView(getBaseContext());
        foot.setTextSize(12);
        foot.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
        foot.setLayoutParams(params2);
        foot.setGravity(Gravity.CENTER);
        foot.setText("已经全部加载完毕");
        loadingDialog=new LoadingDialog(MyCollect2.this);
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list=new ArrayList<>();
                count=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isup=true;
                new DataRefresh().execute();
            }
        });
        artical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artical.setBackgroundResource(R.drawable.underlineblue);
                food.setBackgroundColor(0xffffffff);
                type="3";
                list=new ArrayList<CollectInfo>();
                count=1;
                totalpage=0;
                et.setHint("输入文章");
                getList();
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setBackgroundResource(R.drawable.underlineblue);
                artical.setBackgroundColor(0xffffffff);
                type="7";
                list=new ArrayList<CollectInfo>();
                count=1;
                totalpage=0;
                et.setHint("输入菜谱");
                getList();
            }
        });
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    keyword = editable.toString();
                }else{
                    keyword="";
                }

            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    list=new ArrayList<CollectInfo>();
                    count=1;
                    totalpage=0;
                    getList();
                    return true;
                }
                return false;
            }
        });
    }

    public void getList(){
        prl.setMode(PullToRefreshBase.Mode.BOTH);
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(foot);
        }
        StringPostRequest request=new StringPostRequest(URLMannager.GetGatherList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        totalpage=Integer.valueOf(js2.getString("totalpage"));
                        if(count==totalpage){
                            foot.setText("已经全部加载完毕");
                            prl.getRefreshableView().addFooterView(foot);
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        JSONArray ja=js2.getJSONArray("list");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            CollectInfo cf=new CollectInfo();
                            cf.setId(jo.getString("id"));
                            cf.setTitle(jo.getString("title"));
                            cf.setContent(jo.getString("content"));
                            cf.setImg_url_1(jo.getString("img_url_1"));
                            cf.setType_mess_id(jo.getString("type_mess_id"));
                            if(jo.has("view")){
                                cf.setViews(jo.getString("view"));
                            }
                            if(jo.has("gather_count")){
                                cf.setGather_count(jo.getString("gather_count"));
                            }
                            list.add(cf);
                        }

                    }else {
                        foot.setText("暂无结果");
                        prl.getRefreshableView().addFooterView(foot);
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }

                    adapter=new CollectAdapter(list,MyCollect2.this);
                    prl.setAdapter(adapter);

                    if(isup){
                        prl.getRefreshableView().setSelection(count*10-10);
                        isup=false;
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
        request.putValue("p",count+"");
        request.putValue("t","20");
        request.putValue("uid", BaseApplication.app.getUser().getId());
        request.putValue("type",type);
        request.putValue("keyword",keyword);
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }

    public class DataRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //给系统2秒时间用来做出反应
            SystemClock.sleep(1500);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            prl.onRefreshComplete();
            count++;
            if(count<=totalpage){
                getList();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        list=new ArrayList<>();
        count=1;
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(foot);
        }
        getList();
    }

    public class CollectAdapter extends BaseAdapter{
        private List<CollectInfo> mydata;
        private Context context;
        public CollectAdapter(List<CollectInfo> mydata,Context context){
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
            final int x=position;
            if(mydata.get(position).getType_mess_id().equals("7")){
                convertView= LayoutInflater.from(context).inflate(R.layout.adapter_caipu_item02,null);
                TextView title=(TextView) convertView.findViewById(R.id.caipu_item02_title);
                TextView dress=(TextView) convertView.findViewById(R.id.caipu_item02_dress);
                TextView views=(TextView)convertView.findViewById(R.id.caipu_item02_view);
                MyImageView ima=(MyImageView)convertView.findViewById(R.id.caipu_item02_ima);
                title.setText(mydata.get(position).getTitle());
                dress.setText(mydata.get(position).getContent());
                views.setText(mydata.get(position).getViews()+"浏览\t"+mydata.get(position).getGather_count()+"收藏");
                com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
                DisplayImageOptions options=ImageLoaderUtils.getOpt();
                loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getImg_url_1(),ima,options);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CaipuDetail.class);
                        intent.putExtra("id", mydata.get(x).getId());
                        context.startActivity(intent);
                    }
                });
            }else {
                convertView= LayoutInflater.from(context).inflate(R.layout.my_fabu_item,null);
                TextView tv1=(TextView) convertView.findViewById(R.id.my_fabu_title);
                TextView tv2=(TextView) convertView.findViewById(R.id.my_fabu_time);
                tv1.setText(mydata.get(position).getTitle());
                tv2.setText(mydata.get(position).getContent());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ArticleDetial.class);
                        intent.putExtra("tid",mydata.get(x).getId());
                        context.startActivity(intent);
                    }
                });
            }
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        Date dt= new Date();
                        stime= dt.getTime();
                    }else
                    if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                        Date dt= new Date();
                        etime= dt.getTime();
                    }
                    if(etime-stime>600){
                        getDialog(mydata.get(x));
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            return convertView;
        }
    }

    public void getDialog(final CollectInfo cf){
        builder = new android.support.v7.app.AlertDialog.Builder(MyCollect2.this);
        builder.setTitle("取消收藏").setMessage("是否取消收藏？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelCollect(cf);
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void cancelCollect(CollectInfo cf){
        StringPostRequest spr=new StringPostRequest(URLMannager.CancelDishGather, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        Toast.makeText(getBaseContext(),"已取消收藏",Toast.LENGTH_SHORT).show();
                        list=new ArrayList<CollectInfo>();
                        count=1;
                        totalpage=0;
                        getList();
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("id",cf.getId());
        spr.putValue("type_mess_id",cf.getType_mess_id());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }
}
