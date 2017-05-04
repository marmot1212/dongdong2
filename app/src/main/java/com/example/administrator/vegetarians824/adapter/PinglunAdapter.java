package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tb.emoji.EmojiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-09-08.
 */
public class PinglunAdapter extends BaseAdapter {
    private Context context;
    private List<Pinglun> list_Pinglun = new ArrayList<Pinglun>();// 评论集合
    private com.nostra13.universalimageloader.core.ImageLoader loader;// ImageLoader工具类
    private DisplayImageOptions options;// ImageLoader工具类
    private PopupWindow popWindow;
    private WindowManager windowManager;
    public PinglunAdapter() {
        super();
    }

    public PinglunAdapter(Context context, List<Pinglun> list_Pinglun,WindowManager windowManager) {
        super();
        this.context = context;
        this.list_Pinglun = list_Pinglun;
        this.windowManager=windowManager;
    }

    @Override
    public int getCount() {
        return list_Pinglun.size();
    }

    @Override
    public Object getItem(int position) {
        return list_Pinglun.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ditu_pinglun_listview_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.xiangqing_pinglun_tv_username);
            TextView neirong = (TextView) convertView.findViewById(R.id.xiangqing_pinglun_tv_content);
            TextView time = (TextView) convertView.findViewById(R.id.xiangqing_pinglun_tv_time);
            RoundImageView touxiang = (RoundImageView) convertView.findViewById(R.id.xiangqing_pinglun_img_icon);
            ImageView lv=(ImageView)convertView.findViewById(R.id.xiangqing_pinglun_tv_lv);
            final ImageView ima1=(ImageView)convertView.findViewById(R.id.xiangqing_pinglun_tv_ima1);
            final ImageView ima2=(ImageView)convertView.findViewById(R.id.xiangqing_pinglun_tv_ima2);
            final ImageView ima3=(ImageView)convertView.findViewById(R.id.xiangqing_pinglun_tv_ima3);
            final ImageView ima4=(ImageView)convertView.findViewById(R.id.xiangqing_pinglun_tv_ima4);
            LinearLayout line=(LinearLayout)convertView.findViewById(R.id.xiangqing_pinglun_tv_imaline);
            final Pinglun mPinglun = list_Pinglun.get(position);

            name.setText(mPinglun.getUsername().toString());
            neirong.setText(mPinglun.getContent().toString());
        try {
            EmojiUtil.handlerEmojiText(neirong, neirong.getText().toString(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
            time.setText(mPinglun.getCreate_time_text().toString());
            switch (mPinglun.getLv()){
                case "1":lv.setImageResource(R.mipmap.lv1);break;
                case "2":lv.setImageResource(R.mipmap.lv2);break;
                case "3":lv.setImageResource(R.mipmap.lv3);break;
                default:break;
            }
        // ImageLoader工具类
            loader = ImageLoaderUtils.getInstance(context);
            options = ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+mPinglun.getUser_head_img_th(), touxiang, options);
            touxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,UserDetial.class);
                    intent.putExtra("uid",mPinglun.getUid());
                    if(BaseApplication.app.getUser().islogin()){
                        intent.putExtra("id",BaseApplication.app.getUser().getId());
                    }else {
                        intent.putExtra("id","");
                    }
                    context.startActivity(intent);
                }
            });

        if(list_Pinglun.get(position).getImg_url_01()!=null){
            Log.d("=============ima",URLMannager.Imag_URL+mPinglun.getImg_url_01());
            loader.displayImage(URLMannager.Imag_URL+mPinglun.getImg_url_01(), ima1, options);
            ima1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(ima1.getDrawable(),view);
                }
            });
        }else {
            line.removeView(ima1);
            line.removeView(ima2);
            line.removeView(ima3);
            line.removeView(ima4);
        }

        if(list_Pinglun.get(position).getImg_url_02()!=null){
            loader.displayImage(URLMannager.Imag_URL+mPinglun.getImg_url_02(), ima2, options);
            ima2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(ima2.getDrawable(),view);
                }
            });
        }
        if(list_Pinglun.get(position).getImg_url_03()!=null){
            loader.displayImage(URLMannager.Imag_URL+mPinglun.getImg_url_03(), ima3, options);
            ima3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(ima3.getDrawable(),view);
                }
            });
        }
        if(list_Pinglun.get(position).getImg_url_04()!=null){
            loader.displayImage(URLMannager.Imag_URL+mPinglun.getImg_url_04(), ima4, options);
            ima4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPicPop(ima4.getDrawable(),view);
                }
            });
        }
        return convertView;
    }
    public void getPicPop(Drawable dr,View view){
        View popView = LayoutInflater.from(context).inflate(R.layout.image_pop,null);
        ImageView imashow=(ImageView) popView.findViewById(R.id.image_show);
        imashow.setImageDrawable(dr);
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = imashow.getLayoutParams();
        double n=(double)(Width/dr.getIntrinsicWidth());
        params.width=Width;
        params.height=(int)(dr.getIntrinsicHeight()*n);
        imashow.setLayoutParams(params);
        imashow.requestLayout();
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setAnimationStyle(R.anim.alpha);
        // 获取光标
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

}
