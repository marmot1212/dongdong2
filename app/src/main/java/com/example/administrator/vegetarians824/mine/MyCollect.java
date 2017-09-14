package com.example.administrator.vegetarians824.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.entry.FabuInfo;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyCollect extends AppCompatActivity {
    private PullToRefreshListView prl;
    private List<FabuInfo> list;
    int p,totalpage;
    private boolean isup;
    private FabuInfoAdapter adapter;
    Long stime,etime;
    View popView;
    PopupWindow popWindow;
    private Date date;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        stime=new Long("0").longValue();
        etime=new Long("0").longValue();
        prl=(PullToRefreshListView) findViewById(R.id.subfrag_list2);
       // list=new ArrayList<>();

        date=new Date();
        isup=false;
        initop();
        //initprl();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout) findViewById(R.id.my_collect_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initprl(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        prl.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");
        prl.getLoadingLayoutProxy(false, true).setReleaseLabel("放开刷新...");
        prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);
        prl.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        prl.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);
        prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
        getdate();
        prl.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                new DataRefresh().execute();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                prl.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                prl.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                date=new Date();
                isup=true;
                new DataRefresh().execute();

            }
        });
    }
    public void getdate(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/mygatherlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    if(js2.isNull("totalpage")){
                        TextView tv=(TextView)findViewById(R.id.noinfo2);
                        tv.setText("您尚未收藏");
                        adapter=new FabuInfoAdapter(list,getBaseContext());
                        prl.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    String st=js2.getString("totalpage");
                    totalpage=Integer.valueOf(st).intValue();
                    if(totalpage==1){
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    JSONArray ja=js2.getJSONArray("list");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        FabuInfo f=new FabuInfo();
                        f.setId(jo.getString("id"));
                        f.setTitle(jo.getString("title"));
                        f.setCreate_time(jo.getString("create_time"));
                        f.setType_mess_id(jo.getString("type_mess_id"));
                        f.setList_id(jo.getString("list_id"));
                        list.add(f);
                    }
                    adapter=new FabuInfoAdapter(list,getBaseContext());
                    prl.setAdapter(adapter);
                    if(list.size()>0){
                        tv=new TextView(getBaseContext());
                        tv.setText("已经全部加载完毕");
                        tv.setTextSize(12);
                        tv.setTextColor(0xffa0a0a0);
                        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                        tv.setLayoutParams(params);
                        tv.setGravity(Gravity.CENTER);
                        prl.getRefreshableView().addFooterView(tv);
                    }
                    if(isup){
                        prl.getRefreshableView().setSelection(p*10-10);
                        isup=false;
                    }
                    adapter.notifyDataSetChanged();

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
        spr.putValue("p",String.valueOf(p).toString());
        spr.putValue("t","10");
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    public class FabuInfoAdapter extends BaseAdapter {
        private List<FabuInfo> mydate;
        private Context context;
        public FabuInfoAdapter(List<FabuInfo> mydate,Context context){
            this.mydate=mydate;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydate.size();
        }

        @Override
        public Object getItem(int i) {
            return mydate.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view= LayoutInflater.from(context).inflate(R.layout.my_fabu_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.my_fabu_title);
            TextView tv2=(TextView) view.findViewById(R.id.my_fabu_time);
            tv1.setText(mydate.get(i).getTitle());
            tv2.setText(mydate.get(i).getCreate_time());

            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MyCollect.this, Tiezi2Detial.class);
                    intent.putExtra("tid",mydate.get(x).getId());
                    MyCollect.this.startActivity(intent);
                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {
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
                        initpop(mydate.get(i),i);
                        getpop(popView);
                        return true;
                    }
                    else {
                        return false;
                    }
                    //return false;
                }
            });
            return view;
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
            p++;
            if(p<=totalpage){
                getdate();
            }
            else{
                Toast.makeText(getBaseContext(),"己加载全部",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initpop(final FabuInfo info, final int i){
        popView = LayoutInflater.from(getBaseContext()).inflate(R.layout.delete_popwindow,null);
        final TextView delete=(TextView) popView.findViewById(R.id.pop_delete);
        TextView cancel=(TextView) popView.findViewById(R.id.pop_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(i);
                adapter.notifyDataSetChanged();
                deletes(info);
                popWindow.dismiss();
            }
        });
    }
    public void getpop(View contentView){
        popWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setFocusable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(getCurrentFocus(), Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void deletes(FabuInfo info){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/mydel", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
       Log.d("=====aaa",BaseApplication.app.getUser().getId()+" "+info.getList_id());
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("list_id",info.getList_id());
        spr.putValue("type","3");
        spr.putValue("type_mess_id",info.getType_mess_id());
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list=new ArrayList<>();
        p=1;
        if(prl.getRefreshableView().getFooterViewsCount()>0){
            prl.getRefreshableView().removeFooterView(tv);
        }
        initprl();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
