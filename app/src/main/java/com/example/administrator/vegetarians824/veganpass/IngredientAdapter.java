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
 * Created by Administrator on 2017-06-14.
 */
public class IngredientAdapter extends BaseAdapter {
    private List<Ingredient> data;
    private Context context;
    public IngredientAdapter(List<Ingredient> data,Context context){
        this.data=data;
        this.context=context;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.adapter_ingredient_item,null);
        TextView tv=(TextView)convertView.findViewById(R.id.ingredient_item_tv);
        tv.setText(data.get(position).getName());
        return convertView;
    }
}
