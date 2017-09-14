package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.administrator.vegetarians824.R;

import java.util.List;

/**
 * Created by Administrator on 2017-05-24.
 */
public class LetterAdapter extends BaseAdapter {
    private List<Letters> list;
    private Context context;
    public LetterAdapter(List<Letters> list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.adapter_letter_item,null);
        TextView tv=(TextView)convertView.findViewById(R.id.letter_item);
        tv.setText(list.get(position).getName());
        if(list.get(position).ischoose()){
            tv.setTextColor(0xffff0000);
        }else {
            tv.setTextColor(0xffffffff);
        }
        return convertView;
    }
}
