package com.example.administrator.vegetarians824.video;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.ArticleListAdapter;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter2;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myView.RoundImageView;
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
import java.util.List;

public class VideoList extends AppCompatActivity {
    private LinearLayout fanhui;
    private PullToRefreshListView prl;
    private Date date;
    private int count=1;//第几次加载
    private int totalpage=1;//总页数
    private boolean isup=false;
    private TextView foot;
    private List<Caidan> list;
    private LoadingDialog loadingDialog;
    private VideoAdapter adapter;
    private ImageView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        loadingDialog=new LoadingDialog(VideoList.this);
        loadingDialog.show();
        initView();
    }
    public void initView(){
        fanhui=(LinearLayout)findViewById(R.id.video_list_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search=(ImageView)findViewById(R.id.video_list_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),VideoSearch.class));
            }
        });
        prl=(PullToRefreshListView)findViewById(R.id.video_list_prl);
        foot = new TextView(getBaseContext());
        foot.setTextSize(12);
        foot.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
        foot.setLayoutParams(params2);
        foot.setGravity(Gravity.CENTER);
        foot.setText("已经全部加载完毕");
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
    }

    public void getList(){
        prl.setMode(PullToRefreshBase.Mode.BOTH);
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(foot);
        }
        StringRequest request=new StringRequest(URLMannager.Caidan_URL+"/p/"+count+"/t/10/video_status/2", new Response.Listener<String>() {
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
                            prl.getRefreshableView().addFooterView(foot);
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        JSONArray ja=js2.getJSONArray("list");
                        for (int i = 0; i < ja.length(); i++) {
                            Caidan mCaidanBean = new Caidan();
                            JSONObject array1_2 = ja.getJSONObject(i);
                            mCaidanBean.id = array1_2.getString("id");
                            mCaidanBean.title = array1_2.getString("title");
                            mCaidanBean.content = array1_2.getString("content");
                            mCaidanBean.pic = array1_2.getString("pic");
                            if(!array1_2.isNull("stype")){
                                mCaidanBean.type=array1_2.getString("stype");
                            }else {
                                mCaidanBean.type="";
                            }
                            mCaidanBean.username=array1_2.getString("username");
                            mCaidanBean.user_head_img=array1_2.getString("user_head_img");
                            mCaidanBean.viewcount=array1_2.getString("view");
                            mCaidanBean.gathercount=array1_2.getString("gather_count");
                            list.add(mCaidanBean);
                        }

                    }else {
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                    }

                    adapter=new VideoAdapter(list,VideoList.this);
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

    public class VideoAdapter extends BaseAdapter {
        private List<Caidan> mydata;
        private Context context;
        public VideoAdapter(List<Caidan> mydata,Context context){
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(context).inflate(R.layout.adapter_video_list,null);
            ImageView ima=(ImageView) view.findViewById(R.id.caipu_video_ima);
            ImageView userima=(ImageView) view.findViewById(R.id.caipu_video_userims);
            TextView title=(TextView)view.findViewById(R.id.caipu_video_title);
            TextView username=(TextView)view.findViewById(R.id.caipu_video_username);
            TextView viewcount=(TextView)view.findViewById(R.id.caipu_video_viewcount);
            TextView gathercount=(TextView)view.findViewById(R.id.caipu_video_gathercount);
            com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options2=ImageLoaderUtils.getOpt();
            loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),ima,options2);
            loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getUser_head_img(),userima,options2);
            title.setText(mydata.get(i).getTitle());
            username.setText(mydata.get(i).getUsername());
            viewcount.setText(mydata.get(i).getViewcount()+"浏览");
            gathercount.setText(mydata.get(i).getGathercount()+"收藏");
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,VideoDetail.class);
                    intent.putExtra("id",mydata.get(x).getId());
                    context.startActivity(intent);
                }
            });
            return view;
        }
    }

}
