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

import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-09-05.
 */
public class CantingListAdapter extends BaseAdapter {
    private List<CantingInfo> canting_info;
    private Context myContext;
    public CantingListAdapter(List<CantingInfo> canting_info,Context myContext){
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
        view= LayoutInflater.from(myContext).inflate(R.layout.canting_item,null);
        TextView name=(TextView) view.findViewById(R.id.canting_item_name);
        name.setText(canting_info.get(i).getTitle());
        //荤餐厅
        if(canting_info.get(i).getVege_status().equals("2")){
            name.setTextColor(0xffff5e5e);
        }
        TextView jiage=(TextView)view.findViewById(R.id.canting_item_jiage);
        LinearLayout jiageline=(LinearLayout)view.findViewById(R.id.canting_item_jiageline);
        if(canting_info.get(i).getUnit_pric().equals("0")||canting_info.get(i).getUnit_pric().equals("99999999")) {
            jiageline.setVisibility(View.GONE);
        }else {
            jiage.setText("¥" + canting_info.get(i).getUnit_pric());
        }
        TextView juli=(TextView) view.findViewById(R.id.canting_item_juli);
        juli.setText("距您"+canting_info.get(i).getDistance()+"米");
        final TextView content=(TextView)view.findViewById(R.id.canting_item_neirong);
        content.setText(canting_info.get(i).getContent());
        //荤餐厅
        if(canting_info.get(i).getVege_status().equals("2")){
            content.setText("已加入素食餐厅友好餐厅计划（点击发现素食）");
            content.setTextColor(0xff51b30c);
        }
        ImageView logo=(ImageView) view.findViewById(R.id.canting_item_logo);
        if(canting_info.get(i).getType().equals("1")){
            logo.setImageResource(R.mipmap.maplistdini);
        }else if(canting_info.get(i).getType().equals("6")){
            logo.setImageResource(R.mipmap.maplisthot);
        }
        ImageView imageView=(ImageView) view.findViewById(R.id.canting_item_imageView);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(myContext);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+canting_info.get(i).getImg_url_th_1(),imageView,options);
        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(canting_info.get(x).getType().equals("1")||canting_info.get(x).getType().equals("6")){
                    Intent intent=new Intent(myContext, CantingDetail.class);
                    intent.putExtra("item_id",canting_info.get(x).getId());
                    myContext.startActivity(intent);
                }else if(canting_info.get(x).getType().equals("2")){
                    Intent intent=new Intent(myContext, HuodongDetail.class);
                    intent.putExtra("id",canting_info.get(x).getId());
                    myContext.startActivity(intent);
                }
            }
        });
        return view;
    }
}
