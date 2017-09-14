package com.example.administrator.vegetarians824.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.article.ArticleDetial;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyImageView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Created by Administrator on 2017-07-04.
 */
public class ArticleListAdapter extends BaseAdapter {
    private List<Tiezi> mydata;
    private Context context;
    public ArticleListAdapter(List<Tiezi> mydata,Context context){
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
        convertView= LayoutInflater.from(context).inflate(R.layout.adapter_article_list_item,null);
        MyImageView ima=(MyImageView)convertView.findViewById(R.id.article_item_ima);
        TextView title=(TextView)convertView.findViewById(R.id.article_item_title);
        TextView content=(TextView)convertView.findViewById(R.id.article_item_content);
        TextView time=(TextView)convertView.findViewById(R.id.article_item_time);
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+mydata.get(position).getPic(),ima,options);
        title.setText(mydata.get(position).getTitle());
        content.setText(mydata.get(position).getContent());
        time.setText(mydata.get(position).getCreate_time_text());
        final int x=position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ArticleDetial.class);
                intent.putExtra("tid",mydata.get(x).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
