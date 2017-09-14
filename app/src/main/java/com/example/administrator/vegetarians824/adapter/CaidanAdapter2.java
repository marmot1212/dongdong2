package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CaipuDetail;
import com.example.administrator.vegetarians824.entry.Caidan;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2016-11-22.
 */
public class CaidanAdapter2 extends BaseAdapter {
    private List<Caidan> mydata;
    private Context context;
    private WindowManager windowManager;
    public CaidanAdapter2(List<Caidan> mydata,Context context,WindowManager windowManager){
        this.mydata=mydata;
        this.context=context;
        this.windowManager=windowManager;
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
        view= LayoutInflater.from(context).inflate(R.layout.caidan_item2,null);
        ImageView ima=(ImageView) view.findViewById(R.id.caipu2_ima);
        com.nostra13.universalimageloader.core.ImageLoader loader2= ImageLoaderUtils.getInstance(context);
        DisplayImageOptions options2=ImageLoaderUtils.getOpt();
        loader2.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),ima,options2);
        //裁剪图片
        /*
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params=ima.getLayoutParams();
        double n=(double)Width/params.width;
        params.width=Width;
        params.height=(int)(Width*n);
        ima.setLayoutParams(params);
        ima.requestLayout();
        ima.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams lLayoutlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,360);
        ima.setLayoutParams(lLayoutlayoutParams);
        ima.requestLayout();
        */
        //text
        TextView tv1=(TextView)view.findViewById(R.id.caipu2_title);
        tv1.setText(mydata.get(i).getTitle());
        TextView tv2=(TextView)view.findViewById(R.id.caipu2_content);
        tv2.setText(mydata.get(i).getContent());
        final int x=i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CaipuDetail.class);
                intent.putExtra("id", mydata.get(x).getId());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
