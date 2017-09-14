package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2017-08-01.
 */
public class CaidanFriendlyAdapter extends BaseAdapter {
    private List<Caidan> mydata;
    private Context context;
    public CaidanFriendlyAdapter(List<Caidan> mydata,Context context){
        this.mydata=mydata;
        this.context=context;
    }
    @Override
    public int getCount() {
        return mydata.size();
    }

    @Override
    public Object getItem(int position) {
        return mydata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.caidan_item,null);
        TextView title=(TextView)convertView.findViewById(R.id.itemcaidan_tv);
        TextView price=(TextView)convertView.findViewById(R.id.itemcaidan_price);
        TextView content=(TextView)convertView.findViewById(R.id.itemcaidan_tv2);
        MyImageView ima=(MyImageView)convertView.findViewById(R.id.itemcaidan_iv);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getPic(),ima,options);
        title.setText(mydata.get(position).getTitle());
        price.setText("¥"+mydata.get(position).getPrice());
        content.setText(mydata.get(position).getContent());
        return convertView;
    }
}
