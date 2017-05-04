package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.dongdong.HuodongDetail;
import com.example.administrator.vegetarians824.dongdong.HuodongDiqu;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.DDActivity;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-08-30.
 */
public class MyListAdapter extends BaseAdapter {
    private List<DDActivity> myData;
    private Context myContext;

    public MyListAdapter(List<DDActivity> myData,Context myContext){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view= LayoutInflater.from(myContext).inflate(R.layout.list_huodong,null);
        TextView tv1=(TextView) view.findViewById(R.id.huodong_title);
        tv1.setText(myData.get(i).getActivity_title().toString());
        TextView tv2=(TextView)view.findViewById(R.id.huodong_time);
        tv2.setText(myData.get(i).getActivity_start_time().toString()+" - "+myData.get(i).getActivity_finish_time());
        TextView tv3=(TextView)view.findViewById(R.id.huodong_content);
        tv3.setText(myData.get(i).getActivity_content());
        Button bt=(Button)view.findViewById(R.id.huodong_province);
        bt.setText(myData.get(i).getActivity_province().toString());

        ImageView imageView=(ImageView) view.findViewById(R.id.huodong_image);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(myContext);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+myData.get(i).getActivity_img_url_2(),imageView,options);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(myContext,HuodongDetail.class);
                intent.putExtra("image",myData.get(i).getActivity_img_url_1());
                intent.putExtra("content",myData.get(i).getActivity_content());
                intent.putExtra("id",myData.get(i).getActivity_id());
                myContext.startActivity(intent);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(myContext,HuodongDiqu.class);
                intent2.putExtra("province",myData.get(i).getActivity_province().toString());
                myContext.startActivity(intent2);
            }
        });
        return view;
    }
}
