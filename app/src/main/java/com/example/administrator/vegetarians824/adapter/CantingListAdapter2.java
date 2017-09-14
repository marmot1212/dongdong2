package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.DipPX;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2017-06-28.
 */
public class CantingListAdapter2 extends BaseAdapter {
    private List<CantingInfo> canting_info;
    private Context myContext;
    public CantingListAdapter2(List<CantingInfo> canting_info,Context myContext){
        this.canting_info=canting_info;
        this.myContext=myContext;
    }
    @Override
    public int getCount() {
        return canting_info.size();
    }

    @Override
    public Object getItem(int i) {
        return canting_info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(myContext).inflate(R.layout.canting_item2,null);
        ImageView imageView=(ImageView) view.findViewById(R.id.canting_item2_imageView);
        TextView title=(TextView)view.findViewById(R.id.canting_item2_title);
        TextView parking=(TextView)view.findViewById(R.id.canting_item2_park);
        TextView distance=(TextView)view.findViewById(R.id.canting_item2_local);
        TextView price=(TextView)view.findViewById(R.id.canting_item2_price);

        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(myContext);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+canting_info.get(i).getImg_url_th_1(),imageView,options);
        title.setText(canting_info.get(i).getTitle());
        if(canting_info.get(i).getVege_status().equals("2")){
           title.setTextColor(0xffff5e5e);

        }

        if(canting_info.get(i).getVege_status().equals("2")){
            parking.setText("已加入素食餐厅友好餐厅计划（点击发现素食）");
            parking.setTextColor(0xff51b30c);
        }else {
            parking.setText(canting_info.get(i).getContent());
        }
        if(canting_info.get(i).getSubway_status().equals("1")) {
            distance.setText("距地铁站" + canting_info.get(i).getDistance() + "米");
        }else {
            distance.setText("距您" + canting_info.get(i).getDistance() + "米");
        }

        if(canting_info.get(i).getDistance().equals("")){
            distance.setText("");
        }

        if(canting_info.get(i).getUnit_pric().equals("0")||canting_info.get(i).getUnit_pric().equals("99999999")){
            price.setText("");
        }else {
            price.setText("¥" + canting_info.get(i).getUnit_pric());
        }
        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(myContext, CantingDetail.class);
                intent.putExtra("vege_status",canting_info.get(x).getVege_status());
                intent.putExtra("item_id",canting_info.get(x).getId());
                myContext.startActivity(intent);
            }
        });
        LinearLayout level=(LinearLayout)view.findViewById(R.id.canting_item2_level);
        int lv=Integer.valueOf(canting_info.get(i).getVege_lv());
        for(int p=0;p<lv;p++){
            ImageView ima=new ImageView(myContext);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(DipPX.dip2px(myContext,16),DipPX.dip2px(myContext,16));
            ima.setLayoutParams(params);
            ima.setImageResource(R.mipmap.yezi);
            level.addView(ima);
        }
        return view;
    }
}