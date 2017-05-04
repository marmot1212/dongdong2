package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.JKshuguoDetial;
import com.example.administrator.vegetarians824.entry.Yongliao;

import java.util.List;

/**
 * Created by Administrator on 2016-09-19.
 */
public class YongliaoAdapter extends BaseAdapter {
    private List<Yongliao> mydata;
    private Context context;
    public  YongliaoAdapter(List<Yongliao> mydata,Context context){
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
        View v= LayoutInflater.from(context).inflate(R.layout.yongliao_item,null);
        TextView tv1=(TextView) v.findViewById(R.id.yongliao_name);
        TextView tv2=(TextView)v.findViewById(R.id.yongliao_num);
        tv1.setText(mydata.get(i).getName());
        if(!mydata.get(i).getPid().equals("")){
            final String id=mydata.get(i).getPid();
            tv1.setTextColor(0xff00aff0);
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, JKshuguoDetial.class);
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }
            });
        }
        tv2.setText(mydata.get(i).getNum());
        return v;
    }
}
