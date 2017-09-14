package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Pinglun;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UserDetial;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.tb.emoji.EmojiUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017-07-18.
 */
public class CommentAdapter extends BaseAdapter {
    private List<Pinglun> mydata;
    private Context context;
    public CommentAdapter(List<Pinglun> mydata,Context context){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v= LayoutInflater.from(context).inflate(R.layout.comment_item,null);
        ImageView ima=(ImageView)v.findViewById(R.id.comment_item_ima);
        TextView c=(TextView)v.findViewById(R.id.comment_item_content);
        TextView n=(TextView)v.findViewById(R.id.comment_item_uname);
        TextView t=(TextView)v.findViewById(R.id.comment_item_time);
        ImageView lv=(ImageView)v.findViewById(R.id.comment_item_lv);
        c.setText(mydata.get(i).getContent());
        try {
            EmojiUtil.handlerEmojiText(c, c.getText().toString(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        n.setText(mydata.get(i).getUsername());
        t.setText(mydata.get(i).getCreate_time_text());
        switch (mydata.get(i).getLv()){
            case "1":lv.setImageResource(R.mipmap.lv1);break;
            case "2":lv.setImageResource(R.mipmap.lv2);break;
            case "3":lv.setImageResource(R.mipmap.lv3);break;
            default:break;
        }
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getUser_head_img_th(),ima,options);
        ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,UserDetial.class);
                intent.putExtra("uid",mydata.get(i).getUid());
                if(BaseApplication.app.getUser().islogin()){
                    intent.putExtra("id",BaseApplication.app.getUser().getId());
                }else {
                    intent.putExtra("id","");
                }
                context.startActivity(intent);
            }
        });
        return v;
    }
}