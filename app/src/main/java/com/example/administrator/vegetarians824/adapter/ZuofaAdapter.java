package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Yongliao;
import com.example.administrator.vegetarians824.entry.Zuofa;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-09-19.
 */
public class ZuofaAdapter extends BaseAdapter {
    private List<Zuofa> mydata;
    private Context context;
    public  ZuofaAdapter(List<Zuofa> mydata,Context context){
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
        View v= LayoutInflater.from(context).inflate(R.layout.zuofa_item,null);
        TextView tv1=(TextView) v.findViewById(R.id.zuofa_step);
        TextView tv2=(TextView)v.findViewById(R.id.zuofa_content);
        tv1.setText(mydata.get(i).getStep());
        tv2.setText(mydata.get(i).getDep());
        MyImageView imageView=(MyImageView) v.findViewById(R.id.zuofa_imag);
        if(!mydata.get(i).getPic().equals("")&&!mydata.get(i).getInfo().equals("null")){
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),imageView,options);
        }else
            {
                LinearLayout line=(LinearLayout) v.findViewById(R.id.zuofa_line);
                imageView.setVisibility(View.GONE);
            }

        return v;
    }
}
