package com.example.administrator.vegetarians824.homePage;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CaidanAdapter2;
import com.example.administrator.vegetarians824.dongdong.JKsearch;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.entry.Subway;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.CyclePager;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.search.SearchRestaurant;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.video.CaipuGet;
import com.example.administrator.vegetarians824.video.VideoList;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment implements View.OnClickListener{
    private List<Caidan> list_caidan;
    private PullToRefreshListView listView;
    int count=1;
    int total=0;
    boolean isup;
    private CaidanAdapter2 adapter;
    private Date date;
    private LoadingDialog loadingDialog;
    private FloatingActionButton fab;
    private PopupWindow popWindow;
    private String label="";
    private TextView label1,label2,label3,label4,label5,label6,label7,label8,label9;
    private TextView foot;
    private String type="";
    private EditText et;
    private String keyword="";
    private LinearLayout qiuline;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_second, container, false);
        listView=(PullToRefreshListView) v.findViewById(R.id.jiankang_list);
        fab = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), VideoList.class));
            }
        });
        initPop();
        initLunbo();
        list_caidan=new ArrayList<>();
        isup=false;
        date=new Date();
        loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.show();
        initView();
        initData();
        return v;
    }
    public void initLunbo(){
        View head=LayoutInflater.from(getContext()).inflate(R.layout.head_home2,null);
        LinearLayout group = (LinearLayout)head.findViewById(R.id.ditu_xiangqing_viewGroup2);// 展示小圆点
        ViewPager advPager = (ViewPager)head.findViewById(R.id.ditu_xiangqing_viewpager2);// ViewPager
        CyclePager cyclePager=new CyclePager(advPager,group,getContext());
        cyclePager.init("2");
        TextView foodtype=(TextView)head.findViewById(R.id.food_type_label);
        foodtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.showAsDropDown(v);
            }
        });
        label1=(TextView)head.findViewById(R.id.food_label1);
        label2=(TextView)head.findViewById(R.id.food_label2);
        label3=(TextView)head.findViewById(R.id.food_label3);
        label4=(TextView)head.findViewById(R.id.food_label4);
        label5=(TextView)head.findViewById(R.id.food_label5);
        label6=(TextView)head.findViewById(R.id.food_label6);
        label7=(TextView)head.findViewById(R.id.food_label7);
        label8=(TextView)head.findViewById(R.id.food_label8);
        label9=(TextView)head.findViewById(R.id.food_label9);
        label1.setOnClickListener(this);
        label2.setOnClickListener(this);
        label3.setOnClickListener(this);
        label4.setOnClickListener(this);
        label5.setOnClickListener(this);
        label6.setOnClickListener(this);
        label7.setOnClickListener(this);
        label8.setOnClickListener(this);
        label9.setOnClickListener(this);
        qiuline=(LinearLayout)head.findViewById(R.id.caipu_qiu);
        Button bt=(Button)head.findViewById(R.id.bt_qiu);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseApplication.app.getUser()!=null&&BaseApplication.app.getUser().islogin()) {
                    startActivity(new Intent(getContext(), CaipuGet.class));
                }else {
                    startActivity(new Intent(getContext(), Login.class));
                }
            }
        });
        listView.getRefreshableView().addHeaderView(head);
    }
    public void initData(){
        if(listView.getRefreshableView().getFooterViewsCount()>0){
            listView.getRefreshableView().removeFooterView(foot);
        }
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        StringRequest request=new StringRequest(URLMannager.Caidan_URL+"/p/"+count+"/t/10"+label+type+keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
                if(getActivity()!=null) {
                    adapter = new CaidanAdapter2(list_caidan, getContext(), getActivity().getWindowManager());
                    listView.setAdapter(adapter);
                    if (isup) {
                        listView.getRefreshableView().setSelection(count * 10 - 10);
                        isup = false;
                    }
                    adapter.notifyDataSetChanged();
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }
    public void myJson(String s){
        try {
            Log.d("==========ss",s);
            JSONObject jsonObj1 = new JSONObject(s);
            if(jsonObj1.getString("Code").equals("1")) {
                JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                if (!jsonObj2.isNull("totalpage")) {
                    total = Integer.valueOf(jsonObj2.getString("totalpage"));
                    if (total == count) {
                        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        listView.getRefreshableView().addFooterView(foot);
                    }
                }
                JSONArray array1 = jsonObj2.getJSONArray("list");
                if(array1.length()>0) {
                    qiuline.setVisibility(View.GONE);
                    for (int i = 0; i < array1.length(); i++) {
                        Caidan mCaidanBean = new Caidan();
                        JSONObject array1_2 = array1.getJSONObject(i);
                        mCaidanBean.id = array1_2.getString("id");
                        mCaidanBean.title = array1_2.getString("title");
                        mCaidanBean.content = array1_2.getString("content");
                        mCaidanBean.pic = array1_2.getString("pic");
                        if (!array1_2.isNull("stype")) {
                            mCaidanBean.type = array1_2.getString("stype");
                        } else {
                            mCaidanBean.type = "";
                        }
                        list_caidan.add(mCaidanBean);
                    }
                }else {
                    qiuline.setVisibility(View.VISIBLE);
                    JSONArray ja2=jsonObj2.getJSONArray("like_dish");
                    for (int i = 0; i < ja2.length(); i++) {
                        Caidan mCaidanBean = new Caidan();
                        JSONObject array1_2 = ja2.getJSONObject(i);
                        mCaidanBean.id = array1_2.getString("id");
                        mCaidanBean.title = array1_2.getString("title");
                        mCaidanBean.content = array1_2.getString("content");
                        mCaidanBean.pic = array1_2.getString("pic");
                        if (!array1_2.isNull("stype")) {
                            mCaidanBean.type = array1_2.getString("stype");
                        } else {
                            mCaidanBean.type = "";
                        }
                        list_caidan.add(mCaidanBean);
                    }
                }
            }else {
                foot.setText(jsonObj1.getString("Message"));
                listView.getRefreshableView().addFooterView(foot);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void initView(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDate = sdf.format(date); // 当期日期
        listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉可以刷新");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开立即刷新");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate);
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉刷新...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开刷新...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...\t今天"+currentDate);

        listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                String currentDate2 = sdf2.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate2);
                date=new Date();
                list_caidan=new ArrayList<Caidan>();
                count=0;
                listView.setMode(PullToRefreshBase.Mode.BOTH);//同时支持上拉下拉刷新
                new DataRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                String currentDate3 = sdf3.format(date); // 当期日期
                listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新数据\t今天"+currentDate3);
                date=new Date();
                isup=true;
                new DataRefresh().execute();
            }
        });
        foot = new TextView(getContext());
        foot.setTextSize(12);
        foot.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        foot.setLayoutParams(params2);
        foot.setGravity(Gravity.CENTER);
        foot.setText("已经全部加载完毕");

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
            listView.onRefreshComplete();
            count++;
            if(count<=total){
                initData();
            }
            else{
                Toast.makeText(getContext(),"己加载全部",Toast.LENGTH_SHORT).show();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        }
    }
    public void initPop(){
        View popView =LayoutInflater.from(getContext()).inflate(R.layout.pop_select04,null);
        TextView all=(TextView)popView.findViewById(R.id.food_type_00);
        TextView chun=(TextView)popView.findViewById(R.id.food_type_01);
        TextView dan=(TextView)popView.findViewById(R.id.food_type_02);
        TextView jing=(TextView)popView.findViewById(R.id.food_type_03);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="";
                count=1;
                total=0;
                list_caidan=new ArrayList<>();
                popWindow.dismiss();
                loadingDialog.show();
                initData();
            }
        });
        chun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="/type/纯素";
                count=1;
                total=0;
                list_caidan=new ArrayList<>();
                popWindow.dismiss();
                loadingDialog.show();
                initData();
            }
        });
        dan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="/type/蛋奶素";
                count=1;
                total=0;
                list_caidan=new ArrayList<>();
                popWindow.dismiss();
                loadingDialog.show();
                initData();
            }
        });
        jing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="/type/净素";
                count=1;
                total=0;
                list_caidan=new ArrayList<>();
                popWindow.dismiss();
                loadingDialog.show();
                initData();
            }
        });
        popWindow= new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new ColorDrawable());
    }
    public void setLabel(View ... views){
        for(View view:views){
            ((TextView)view).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
    @Override
    public void onClick(View v) {
        setLabel(label1,label2,label3,label4,label5,label6,label7,label8,label9);
        ((TextView)v).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        if(((TextView) v).getText().equals("全部")){
            label="";
            count=1;
            total=0;
            list_caidan=new ArrayList<>();
            loadingDialog.show();
            initData();
        }else {
            label="/label/"+((TextView) v).getText().toString();
            count=1;
            total=0;
            list_caidan=new ArrayList<>();
            loadingDialog.show();
            initData();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        et=(EditText)activity.findViewById(R.id.home_et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    keyword="/keyword/"+s.toString();
                }else {
                    keyword="";
                }
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
                {
                    list_caidan=new ArrayList<Caidan>();
                    count=1;
                    total=0;
                    loadingDialog.show();
                    initData();
                    return true;
                }
                return false;
            }
        });
    }
}
