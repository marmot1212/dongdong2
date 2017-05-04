package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.JKshuguoDetial;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-09-18.
 */
public class CaidanAdapter extends BaseAdapter {
    private List<Caidan> mydata;
    private Context context;

    public CaidanAdapter(List<Caidan> mydata,Context context){
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
        view= LayoutInflater.from(context).inflate(R.layout.caidan_item,null);
        TextView tv1=(TextView) view.findViewById(R.id.itemcaidan_tv);
        TextView tv2=(TextView)view.findViewById(R.id.itemcaidan_tv2);
        ImageView ima=(ImageView)view.findViewById(R.id.itemcaidan_iv);
        tv1.setText(mydata.get(i).getTitle());
        tv2.setText(mydata.get(i).getContent());
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),ima,options);
        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mydata.get(x).getType().equals("9")){
                    Intent intent = new Intent(context, JKshuguoDetial.class);
                    intent.putExtra("id", mydata.get(x).getId());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, CaipuDetail.class);
                    intent.putExtra("id", mydata.get(x).getId());
                    context.startActivity(intent);
                }
            }
        });
        return view;
    }
}
