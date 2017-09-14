package com.example.administrator.vegetarians824.veganpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.vegetarians824.R;

import java.util.List;

/**
 * Created by Administrator on 2017-05-22.
 */
public class FoodAdapter extends BaseAdapter {
    private List<Food> list;
    private Context context;
    public FoodAdapter(List<Food> list,Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.adapter_food_item,null);
        ImageView ima=(ImageView) view.findViewById(R.id.food_item_ima);
        ImageView fobid=(ImageView) view.findViewById(R.id.food_item_forbid);
        TextView name=(TextView) view.findViewById(R.id.food_item_name);
        ima.setImageResource(list.get(i).getId());
        name.setText(list.get(i).getName());
        if(list.get(i).ischoose()){
            fobid.setVisibility(View.VISIBLE);
        }else {
            fobid.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
