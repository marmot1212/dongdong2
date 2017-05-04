package com.example.administrator.vegetarians824.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.Tiezi2Detial;
import com.example.administrator.vegetarians824.dongdong.TieziDetial;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.entry.FabuInfo;
import com.example.administrator.vegetarians824.fabu.FabuShUpdate;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectFragment extends Fragment {
    private PullToRefreshListView prl;
    private List<FabuInfo> list;
    int p,totalpage;
    private boolean isup;
    String type,which;
    private FabuInfoAdapter adapter;
    Long stime,etime;
    View popView;
    PopupWindow popWindow;
    private Date date;
    public SubjectFragment() {
        // Required empty public constructor
        date=new Date();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_subject, container, false);
        prl=(PullToRefreshListView) v.findViewById(R.id.subfrag_list);
        stime=new Long("0").longValue();
        etime=new Long("0").longValue();
        list=new ArrayList<>();
        p=1;
        totalpage=0;
        isup=false;
        Bundle bundle=getArguments();
        String what=bundle.getString("switch");
        which=bundle.getString("which");
        switch (what){
            case "餐馆":
                type="1";
                break;
            case "产品":
                type="2";
                break;
            case "活动":
                type="4";
                break;
            case "酒店":
                type="6";
                break;
            case "帖子":
                type="3";
                break;
            case "菜谱":
                type="7";
                break;
        }
        initprl();
        return v;
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

                list=new ArrayList<FabuInfo>();
                p=0;
                prl.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
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
        Log.d("=========type",type);
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/Androidapi/userCenter/"+which, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("================s",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    if(js2.getJSONArray("list").length()==0){
                        TextView tv=(TextView)getView().findViewById(R.id.noinfo);
                        if(which.equals("myshare_list")){
                            tv.setText("您尚未发布");
                            prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                        else
                            if(which.equals("my_commentlist")){
                                tv.setText("您尚未点评");
                                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                    }
                    if(!js2.isNull("totalpage"))
                    {
                        String st=js2.getString("totalpage");
                        totalpage=Integer.valueOf(st).intValue();
                    }
                    if(totalpage==1){
                        prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    JSONArray ja=js2.getJSONArray("list");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        FabuInfo f=new FabuInfo();
                        f.setId(jo.getString("id"));
                        if(!jo.isNull("list_id"))
                            f.setList_id(jo.getString("list_id"));
                        f.setTitle(jo.getString("title"));
                        f.setCreate_time(jo.getString("create_time"));
                        f.setType_mess_id(jo.getString("type_mess_id"));
                        if(!jo.isNull("mess_id"))
                            f.setMess_id(jo.getString("mess_id"));
                        list.add(f);
                    }
                        adapter=new FabuInfoAdapter(list,getContext());
                        prl.setAdapter(adapter);
                        if(list.size()>0&&prl.getRefreshableView().getFooterViewsCount()<=1){
                            TextView tv=new TextView(getContext());
                            tv.setText("已经全部加载完毕");
                            tv.setTextSize(12);
                            tv.setTextColor(0xffa0a0a0);
                            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                            tv.setLayoutParams(params);
                            tv.setGravity(Gravity.CENTER);
                            prl.getRefreshableView().addFooterView(tv);
                        }
                        if(isup){
                            prl.getRefreshableView().setSelection(p*10-19);
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
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("type",type);
        spr.putValue("p",String.valueOf(p).toString());
        spr.putValue("t","10");
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public class FabuInfoAdapter extends BaseAdapter{
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
            view=LayoutInflater.from(context).inflate(R.layout.my_fabu_item,null);
            TextView tv1=(TextView) view.findViewById(R.id.my_fabu_title);
            TextView tv2=(TextView) view.findViewById(R.id.my_fabu_time);
            tv1.setText(mydate.get(i).getTitle());
            tv2.setText(mydate.get(i).getCreate_time());
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (type){
                        case "1":
                            //餐厅
                            //Intent intent=new Intent(getContext(), CantingDetail.class);
                            //intent.putExtra("item_id",mydate.get(x).getId());
                            //getActivity().startActivity(intent);
                            if(which.equals("myshare_list")) {
                                Intent intent = new Intent(getContext(), CantingDetail.class);
                                intent.putExtra("item_id", mydate.get(x).getId());
                                getActivity().startActivity(intent);
                            }else {
                                Intent intent=new Intent(getContext(), CantingDetail.class);
                                intent.putExtra("item_id",mydate.get(x).getId());
                                getActivity().startActivity(intent);
                            }

                            break;
                        case "2":
                            //产品
                            break;
                        case "3":
                            //帖子
                            Intent intent3=new Intent(getContext(), Tiezi2Detial.class);
                            intent3.putExtra("tid",mydate.get(x).getId());
                            getActivity().startActivity(intent3);
                            break;
                        case "4":
                            //活动
                            Intent intent4=new Intent(getContext(), HuodongDetail.class);
                            intent4.putExtra("id",mydate.get(x).getId());
                            getActivity().startActivity(intent4);
                            break;
                        case "6":
                            //酒店
                            Intent intent5=new Intent(getContext(), CantingDetail.class);
                            intent5.putExtra("item_id",mydate.get(x).getId());
                            getActivity().startActivity(intent5);
                            break;
                        case "7":
                            //菜谱
                            Intent intent6=new Intent(getContext(), CaipuDetail.class);
                            intent6.putExtra("id",mydate.get(x).getId());
                            getActivity().startActivity(intent6);
                            break;
                    }
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
                if (totalpage ==0) {
                    Toast.makeText(getContext(),"暂无信息",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();

                prl.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }

    public void initpop(final FabuInfo info, final int i){
        popView = LayoutInflater.from(getContext()).inflate(R.layout.delete_popwindow,null);
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
        popWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    public void deletes(FabuInfo info){
        Log.d("=========ss",info.getId()+" "+info.getType_mess_id());
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/userCenter/mydel", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        String t="";
        if(which.equals("myshare_list")){
            t="1";
            spr.putValue("list_id",info.getId());
            spr.putValue("type_mess_id",info.getType_mess_id());
        }
        else if(which.equals("my_commentlist"))
        {
            t="2";
            spr.putValue("list_id",info.getList_id());
        }
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        //spr.putValue("list_id",info.getList_id());
        spr.putValue("type",t);
        //spr.putValue("type_mess_id",info.getType_mess_id());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }
}
