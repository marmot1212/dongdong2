package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.TripInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-09-14.
 */
public class TripListAdapter extends BaseAdapter {
    private List<TripInfo> myData;
    private Context myContext;

    public TripListAdapter(List<TripInfo> myData,Context myContext){
        this.myData=myData;
        this.myContext=myContext;
    }
    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int i) {
        return myData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(myContext).inflate(R.layout.trip_item,null);
        TextView title=(TextView)view.findViewById(R.id.trip_item_title);
        TextView price=(TextView)view.findViewById(R.id.trip_item_price);
        TextView content=(TextView)view.findViewById(R.id.trip_item_content);
        ImageView image=(ImageView)view.findViewById(R.id.trip_item_imag);

        title.setText(myData.get(i).getTitle());
        price.setText("¥"+myData.get(i).getUnit_price());
        content.setText(myData.get(i).getContent());

        ImageView imageView=(ImageView) view.findViewById(R.id.canting_item_imageView);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(myContext);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+myData.get(i).getImg_url_1(),image,options);

        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(myContext, CantingDetail.class);
                intent.putExtra("item_id",myData.get(x).getId());
                myContext.startActivity(intent);
            }
        });
        return view;
    }
}
