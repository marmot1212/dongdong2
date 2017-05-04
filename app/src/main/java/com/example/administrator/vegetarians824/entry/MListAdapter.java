package com.example.administrator.vegetarians824.entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;

import java.util.List;

/**
 * Created by Administrator on 2016-09-21.
 */
public class MListAdapter extends BaseAdapter {
    private List<SheruResult> mydata;
    private Context context;


    public  MListAdapter(List<SheruResult> mydata,Context context){
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
        View v= LayoutInflater.from(context).inflate(R.layout.result_item,null);
        TextView tv1=(TextView) v.findViewById(R.id.sheru_01);
        tv1.setText(mydata.get(i).getTitle());
        TextView tv2=(TextView) v.findViewById(R.id.sheru_02);
        tv2.setText(mydata.get(i).getTuijian());
        TextView tv3=(TextView) v.findViewById(R.id.sheru_03);
        TextView tv4=(TextView) v.findViewById(R.id.sheru_04);
        tv4.setText(mydata.get(i).getZuida());
        tv3.setText(mydata.get(i).getCankao());
        return v;
    }
}
