package com.example.administrator.vegetarians824.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.calendardata.CalendarAdapter;
import com.example.administrator.vegetarians824.calendardata.CalendarAdapter2;
import com.example.administrator.vegetarians824.calendardata.CheckFestival;
import com.example.administrator.vegetarians824.calendardata.SpecialCalendar;
import com.example.administrator.vegetarians824.entry.CaleadarDay;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Zangli extends Fragment implements View.OnClickListener {
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private  static int jumpMonth2= 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private  static int jumpYear2 = 0;
    private String currentDate = "";
    private ViewFlipper flipper = null;
    private TextView currentMonth;
    private int gvFlag = 0;
    int dayofweek;
    /** 上个月 */
    private ImageView prevMonth;
    /** 下个月 */
    private ImageView nextMonth;
    private GridView gridView = null;
    private CalendarAdapter2 ca;
    private int stepYear,stepMonth;
    private List<CaleadarDay> list_day;
    private Button qiandao;
    private  boolean isqd=false;
    private static int qdflag=0;
    PopupWindow popWindow;
    private static TextView tv;
    private static int acolor=0;
    private static Drawable bgdraw=null;
    public Zangli() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_zangli, container, false);
        tv=new TextView(getContext());
        list_day=new ArrayList<>();
        currentMonth = (TextView) v.findViewById(R.id.currentMonth2);
        prevMonth = (ImageView) v.findViewById(R.id.prevMonth2);
        nextMonth = (ImageView) v.findViewById(R.id.nextMonth2);
        qiandao=(Button)v.findViewById(R.id.qiandao_bt2) ;
        setListener();
        flipper = (ViewFlipper) v.findViewById(R.id.flipper2);
        flipper.removeAllViews();
        addGridView();
        stepYear = year_c + jumpYear2;
        stepMonth = month_c + jumpMonth2;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        addTextToTopTextView();
        getData(stepYear,stepMonth);
        flipper.addView(gridView, 0);
        return v;
    }
    public void getData(int year, final int month){
        list_day=new ArrayList<>();
        prevMonth.setVisibility(View.VISIBLE);
        nextMonth.setVisibility(View.VISIBLE);
        SpecialCalendar sc=new SpecialCalendar();
         dayofweek=sc.getWeekdayOfMonth(year,month);
        for(int i=0;i<dayofweek;i++){
            CaleadarDay cd=new CaleadarDay();
            list_day.add(cd);
        }
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/androidapi/Indexs/register_lists", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("zangli");
                    JSONObject joleft=ja.getJSONObject(0);
                    JSONObject joright=ja.getJSONObject(2);
                    if(joleft.getJSONArray("list").length()==0){
                        prevMonth.setVisibility(View.INVISIBLE);
                    }
                    if(joright.getJSONArray("list").length()==0){
                        nextMonth.setVisibility(View.INVISIBLE);
                    }
                    JSONObject jo=ja.getJSONObject(1);
                    JSONArray ja2=jo.getJSONArray("list");
                    for(int i=0;i<ja2.length();i++){
                        JSONObject joo=ja2.getJSONObject(i);
                        CaleadarDay cd=new CaleadarDay();
                        cd.setId(joo.getString("id"));
                        cd.setZangli(joo.getString("zangli"));
                        cd.setY(joo.getString("y"));
                        cd.setM(joo.getString("m"));
                        cd.setD(joo.getString("d"));
                        cd.setTime(joo.getString("time"));
                        if(!joo.isNull("zanglis")){
                            cd.setZanglis(joo.getString("zanglis"));
                        }
                        if(!joo.isNull("active")){
                            JSONArray jay=joo.getJSONArray("active");
                            List<String> ls=new ArrayList();
                            for(int j=0;j<jay.length();j++){
                                ls.add(jay.getString(j));
                            }
                            cd.setActive(ls);
                        }
                        list_day.add(cd);

                    }
                    if(!js2.isNull("register")){
                        JSONArray ja22=js2.getJSONArray("register");
                        for (int i=0;i<ja22.length();i++){
                            JSONObject jo22=ja22.getJSONObject(i);
                            if(month==Integer.valueOf(jo22.getString("m")).intValue()){
                                int d=Integer.valueOf(jo22.getString("d")).intValue()+dayofweek-1;
                                list_day.get(d).setIsmark(true);
                            }
                        }
                    }
                    ca=new CalendarAdapter2(list_day,getContext());
                    gridView.setAdapter(ca);
                    addqiandao();
                    list_day=new ArrayList<>();
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
        spr.putValue("day",year+"-"+month);
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }
    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(getContext());
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setBackgroundDrawable(getResources().getDrawable(R.drawable.zangli));
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                CaleadarDay cal=list_day.get(position);
                String s1=cal.getD();
                String s2=cal.getY();
                String s3=cal.getM();
                String s4=cal.getZanglis();
                String s5="";
                if(cal.getActive()!=null){
                    StringBuffer sb=new StringBuffer();
                    for(int i=0;i<cal.getActive().size();i++){
                        sb.append(cal.getActive().get(i)).append(",");
                    }
                    s5=sb.toString().substring(0,sb.length()-1);
                }

                tv.setBackground(bgdraw);
                tv.setTextColor(acolor);
                TextView textView = (TextView) arg1.findViewById(R.id.tvtext);
                tv=textView;
                acolor=tv.getCurrentTextColor();
                bgdraw=tv.getBackground();
                textView.setBackgroundResource(R.drawable.button_bg5);
                textView.setTextColor(Color.WHITE);

                getPopwindow(s1,s2,s3,s4,s5);
            }
        });
        gridView.setLayoutParams(params);
    }


    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth2++; // 下一个月
        stepYear = year_c + jumpYear2;
        stepMonth = month_c + jumpMonth2;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        addTextToTopTextView();
        getData(stepYear,stepMonth);
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_left_out));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth2--; // 上一个月
        stepYear = year_c + jumpYear2;
        stepMonth = month_c + jumpMonth2;
        if (stepMonth > 0) {
            // 往下一个月滑动
            if (stepMonth % 12 == 0) {
                stepYear = year_c + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = year_c + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            // 往上一个月滑动
            stepYear = year_c - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
            if (stepMonth % 12 == 0) {

            }
        }
        addTextToTopTextView();
        getData(stepYear,stepMonth);
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.nextMonth2: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth2: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            default:
                break;
        }
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }

    public void addTextToTopTextView() {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(stepYear).append("年").append(stepMonth).append("月").append("\t");
        currentMonth.setText(textDate);
    }

    public void addqiandao(){
        if(qdflag==1){
            qiandao.setClickable(false);
            qiandao.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg2));
            qiandao.setText("已签到");
        }else {
            qiandao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doPost();
                }
            });
        }
        if(!isqd){
            if(list_day.get(day_c+dayofweek-1).ismark()){
                qiandao.setClickable(false);
                qiandao.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg2));
                qiandao.setText("已签到");
                qdflag=1;
            }else {
                qiandao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doPost();
                    qdflag=2;
                }
            });
            }
        }
        isqd=true;
    }

    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/androidapi/Indexs/register_day", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                qiandao.setClickable(false);
                qiandao.setBackgroundColor(0xffa0a0a0);
                qiandao.setText("已签到");
                list_day.get(day_c+dayofweek-1).setIsmark(true);
                ca.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void getPopwindow(String s1,String s2,String s3,String s4,String s5){
        View popView =LayoutInflater.from(getContext()).inflate(R.layout.calendar_pop,null);
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        TextView tv1=(TextView)popView.findViewById(R.id.calpop_day) ;
        TextView tv2=(TextView)popView.findViewById(R.id.calpop_date) ;
        TextView tv3=(TextView)popView.findViewById(R.id.calpop_date2) ;
        TextView tv4=(TextView)popView.findViewById(R.id.calpop_fes) ;
        tv1.setText(s1);
        tv2.setText(s2+"年"+s3+"月");
        tv3.setText(s4);
        tv4.setText(s5);
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 获取光标
        popWindow.setFocusable(false);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //    backgroundAlpha(1f);
            }
        });
    }

}
