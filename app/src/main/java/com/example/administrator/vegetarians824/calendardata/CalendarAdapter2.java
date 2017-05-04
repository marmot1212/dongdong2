package com.example.administrator.vegetarians824.calendardata;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.CaleadarDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-10-27.
 */
public class CalendarAdapter2 extends BaseAdapter{
    private List<CaleadarDay> mydata;
    private Context context;
    private String currentDate="";
    public CalendarAdapter2(List<CaleadarDay> mydata,Context context){
        this.mydata=mydata;
        this.context=context;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date); // 当期日期
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
        View v= LayoutInflater.from(context).inflate(R.layout.calendar_item,null);
        TextView tv=(TextView)v.findViewById(R.id.tvtext);
        TextView tv2=(TextView)v.findViewById(R.id.tvtext2);
        tv.setText(mydata.get(i).getD());
        tv2.setText(mydata.get(i).getZanglis());
        tv.setTextColor(Color.BLACK);// 当月字体设黑
        tv2.setTextColor(Color.BLACK);// 当月字体设黑
        if (i % 7 == 0 || i % 7 == 6) {
            // 当前月信息显示
            tv.setTextColor(Color.RED);// 当月字体设黑
            tv2.setTextColor(Color.RED);// 当月字体设黑
//				drawable = res.getDrawable(R.drawable.calendar_item_selected_bg);
        }
        if (currentDate.equals(mydata.get(i).getTime())){
            tv.setBackgroundResource(R.drawable.button_bg4);
            tv.setTextColor(Color.WHITE);
        }
        ImageView mark1=(ImageView)v.findViewById(R.id.qiandaomark);
        ImageView mark2=(ImageView)v.findViewById(R.id.festivalmark);
        if(mydata.get(i).getActive()!=null){
            mark2.setVisibility(View.VISIBLE);
        }
        if(mydata.get(i).ismark()){
            mark1.setVisibility(View.VISIBLE);
        }
        return v;
    }
}
