package com.example.administrator.vegetarians824.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.calendardata.CalendarAdapter;
import com.example.administrator.vegetarians824.calendardata.CheckFestival;
import com.example.administrator.vegetarians824.calendardata.SpecialCalendar;
import com.example.administrator.vegetarians824.dongdong.MyCalender;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * A simple {@link Fragment} subclass.
 */
public class Nongli extends Fragment implements View.OnClickListener{
    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private ViewFlipper flipper = null;
    private GridView gridView = null;
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    /** 每次添加gridview到viewflipper中时给的标记 */
    private int gvFlag = 0;
    /** 当前的年月，现在日历顶端 */
    private TextView currentMonth;
    /** 上个月 */
    private ImageView prevMonth;
    /** 下个月 */
    private ImageView nextMonth;
    private Button qiandao;
    private int dayOfWeek = 0;
    private SpecialCalendar sc = null;
    private boolean[] schDateTagFlag =new boolean[42];
    private static TextView tv;
    private static int acolor=0;
    private static Drawable bgdraw=null;
    PopupWindow popWindow;
    private String currentDate = "";
    public Nongli() {
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
        View v=inflater.inflate(R.layout.fragment_nongli, container, false);
        //
        tv=new TextView(getContext());
        qiandao=(Button)v.findViewById(R.id.qiandao_bt);
        currentMonth = (TextView) v.findViewById(R.id.currentMonth);
        prevMonth = (ImageView) v.findViewById(R.id.prevMonth);
        nextMonth = (ImageView) v.findViewById(R.id.nextMonth);
        setListener();
        flipper = (ViewFlipper) v.findViewById(R.id.flipper);
        flipper.removeAllViews();
        addGridView();

        getMarker(year_c + jumpYear,month_c + jumpMonth);
       // calV = new CalendarAdapter(getContext(), getActivity().getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,schDateTagFlag);
        //gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        //addTextToTopTextView(currentMonth);
        // Inflate the layout for this fragment

        return v;
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        getMarker(year_c + jumpYear,month_c + jumpMonth);
       // calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,schDateTagFlag);
        //gridView.setAdapter(calV);
        //addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
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
        jumpMonth--; // 上一个月
        getMarker(year_c + jumpYear,month_c + jumpMonth);
        //calV = new CalendarAdapter(getContext(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,schDateTagFlag);
        //gridView.setAdapter(calV);
        gvFlag++;
        //addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);

        flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.push_right_out));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
        //((MyCalender)getActivity()).setYear(Integer.valueOf(calV.getShowYear()));
        //((MyCalender)getActivity()).setMonth(Integer.valueOf(calV.getShowMonth()));
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
        gridView.setBackgroundDrawable(getResources().getDrawable(R.drawable.nongli));
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
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                     String scheduleLunarDay = calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    //Toast.makeText(getContext(), scheduleYear + "年" + scheduleMonth + "月" + scheduleDay+'\n'+scheduleLunarDay, Toast.LENGTH_SHORT).show();
                     //Toast.makeText(getContext(), calV.getDateByClickItem(position)+" "+startPosition,Toast.LENGTH_SHORT).show();
                    tv.setBackground(bgdraw);
                    tv.setTextColor(acolor);
                    TextView textView = (TextView) arg1.findViewById(R.id.tvtext);
                    tv=textView;
                    acolor=tv.getCurrentTextColor();
                    bgdraw=tv.getBackground();
                    textView.setBackgroundResource(R.drawable.button_bg5);
                    textView.setTextColor(Color.WHITE);

                    getPopwindow(scheduleDay, scheduleYear, scheduleMonth,scheduleLunarDay);
                }

            }
        });


        gridView.setLayoutParams(params);
    }

    private void setListener() {
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.nextMonth: // 下一个月
                enterNextMonth(gvFlag);
                break;
            case R.id.prevMonth: // 上一个月
                enterPrevMonth(gvFlag);
                break;
            default:
                break;
        }
    }

    public void getMarker(int year,final int month){
        schDateTagFlag =new boolean[42];
        sc = new SpecialCalendar();
        dayOfWeek = sc.getWeekdayOfMonth(year, month);
        if(BaseApplication.app.getUser().islogin()){
            StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/androidapi/Indexs/register_lists", new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject js1=new JSONObject(s);
                        JSONObject js2=js1.getJSONObject("Result");
                        if(!js2.isNull("register")){
                            JSONArray ja=js2.getJSONArray("register");
                            for (int i=0;i<ja.length();i++){
                                JSONObject jo=ja.getJSONObject(i);
                                if(month==Integer.valueOf(jo.getString("m")).intValue()){
                                    int d=Integer.valueOf(jo.getString("d")).intValue()+dayOfWeek-1;
                                    schDateTagFlag[d]=true;
                                }
                            }
                        }
                        calV = new CalendarAdapter(getContext(), getActivity().getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,schDateTagFlag);
                        gridView.setAdapter(calV);
                        addTextToTopTextView(currentMonth);
                        addqiandao();
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
        }else {
            calV = new CalendarAdapter(getContext(), getActivity().getResources(), jumpMonth, jumpYear, year_c, month_c, day_c,schDateTagFlag);
            gridView.setAdapter(calV);
            addTextToTopTextView(currentMonth);
            addqiandao();
        }
    }

    public void addqiandao(){
        //Log.d("=================qd",schDateTagFlag[day_c+dayOfWeek-1]+"");
        if(schDateTagFlag[day_c+dayOfWeek-1]){
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
    }

    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/androidapi/Indexs/register_day", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                qiandao.setClickable(false);
                qiandao.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg2));
                qiandao.setText("已签到");

                if(jumpMonth==0&&jumpYear==0) {
                    schDateTagFlag[day_c + dayOfWeek - 1] = true;
                    calV.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void getPopwindow(String s1,String s2,String s3,String s4){
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
        String fes=s3+"-"+s1;
        CheckFestival cf=new CheckFestival(fes);
        tv4.setText(cf.getFestival());
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

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
