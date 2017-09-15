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
 * Created by Administrator on 2017-05-18.
 */
public class CountryAdapter extends BaseAdapter {
    private List<Country> countrylist;
    private Context context;
    private String en_cn;
    public CountryAdapter(List<Country> countrylist,Context context){
        this.countrylist=countrylist;
        this.context=context;
    }

    public CountryAdapter(List<Country> countrylist,Context context,String cn_en){
        this.countrylist=countrylist;
        this.context=context;
        this.en_cn=cn_en;
    }

    @Override
    public int getCount() {
        return countrylist.size();
    }

    @Override
    public Object getItem(int i) {
        return countrylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(context).inflate(R.layout.adapter_country_item,null);
        TextView name=(TextView)view.findViewById(R.id.country_name);
        //TextView english=(TextView)view.findViewById(R.id.country_english);
        name.setText(countrylist.get(i).getName());
        //english.setText(countrylist.get(i).getEnglish_name());
        return view;
    }
}
