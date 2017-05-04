package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.ProvinceCity;
import com.example.administrator.vegetarians824.entry.Shuguo;
import com.example.administrator.vegetarians824.entry.YuansuChild;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myView.MyExpandableListView;
import com.example.administrator.vegetarians824.myView.UserDefineScrollView;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class JKYuansuDetail extends AppCompatActivity {
    private String type,ys_name,ys_content,ys_pic,ys_id;
    private View contentv;
    private List<YuansuChild> ysc_list;
    private LinearLayout scrollView;
    private List<Shuguo> list_shuguo;
    MyExpandableListView lv2;
    ExpandableAdapter2 ea;
    private UserDefineScrollView scroll;
    TextView tv2_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkyuansu_detail);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        scrollView=(LinearLayout) findViewById(R.id.myscro);
        scroll=(UserDefineScrollView)findViewById(R.id.yuansu_detial_scroll);
        ysc_list=new ArrayList<>();
        list_shuguo=new ArrayList<>();
        initOperate();
        Intent intent=getIntent();
        String name=intent.getStringExtra("what");
        StringBuffer url=new StringBuffer();
        try {
            url.append(URLMannager.YuanSu_Detial).append(URLEncoder.encode(name,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpRequest(url.toString());
    }
    public void initOperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.yuansuD_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void HttpRequest(String s){

        StringRequest request=new StringRequest(s, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JKYuansuDetail.this).addToRequestQueue(request);
    }

    public void myJson(String s){

        try {
            JSONObject js1=new JSONObject(s);
            JSONObject js2=js1.getJSONObject("Result");
            type=js2.getString("type");

            if(type.equals("1")){
                JSONObject js3=js2.getJSONObject("info");
                ys_name=js3.getString("name");
                ys_content=js3.getString("content");
                ys_id=js3.getString("id");
                //ys_pic=js3.getString("pic");
                JSONArray ph=js3.getJSONArray("ph");
                for(int i=0;i<ph.length();i++){
                    Shuguo sg=new Shuguo();
                    JSONObject jo1=ph.getJSONObject(i);
                    sg.setId(jo1.getString("pid"));
                    sg.setPic(jo1.getString("pic"));
                    sg.setTitle(jo1.getString("title"));
                    sg.setAbout(jo1.getString("about"));
                    list_shuguo.add(sg);
                }
                initView();
            }

            if(type.equals("2")){
                JSONObject js4=js2.getJSONObject("info");
                JSONArray ja2=js2.getJSONArray("list");
                ys_name=js4.getString("name");
                ys_content=js4.getString("content");
                //ys_pic=js4.getString("pic");
                for(int j=0;j<ja2.length();j++){
                    JSONObject jo2=ja2.getJSONObject(j);
                    YuansuChild yc=new YuansuChild();
                    yc.setName(jo2.getString("name"));
                    yc.setId(jo2.getString("id"));
                    yc.setContent(jo2.getString("content"));
                    if(!jo2.isNull("contents")){
                        yc.setContents(jo2.getString("contents"));
                    }
                    JSONArray ph=jo2.getJSONArray("ph");
                    List<Shuguo> sg_list=new ArrayList<>();
                    for(int i=0;i<ph.length();i++){
                        Shuguo sg2=new Shuguo();
                        JSONObject jo1=ph.getJSONObject(i);
                        sg2.setId(jo1.getString("pid"));
                        sg2.setPic(jo1.getString("pic"));
                        sg2.setTitle(jo1.getString("title"));
                        sg2.setAbout(jo1.getString("about"));
                        sg_list.add(sg2);
                    }
                    yc.setSg_list(sg_list);
                    yc.setIsexpand(false);
                    ysc_list.add(yc);
                }
                initView2();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initView(){
        contentv=LayoutInflater.from(this).inflate(R.layout.yuansu_detial, null);
        //名称
        TextView tv_name=(TextView)findViewById(R.id.yuansu_names);
        tv_name.setText(ys_name);
        //内容
        TextView tv_content=(TextView)contentv.findViewById(R.id.ys_detial_content);
        tv_content.setText(ys_content);
        //排行列表
        ListViewForScrollView ls=(ListViewForScrollView) contentv.findViewById(R.id.ys_detial1_list);
        if(list_shuguo.size()>0) {
            ls.setAdapter(new PaihangAdapter(list_shuguo, getBaseContext()));
            TextView tv=(TextView) contentv.findViewById(R.id.chakan_allph);
            tv.setText("查看完整"+ys_name+"含量排行榜");
            tv.setVisibility(View.VISIBLE);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(JKYuansuDetail.this,JKPaihangDetial.class);
                    intent.putExtra("id",ys_id);
                    intent.putExtra("title",ys_name);
                    JKYuansuDetail.this.startActivity(intent);
                }
            });
        }
        scrollView.addView(contentv);
    }
    public void initView2(){
        contentv=LayoutInflater.from(this).inflate(R.layout.yuansu_detial2, null);
        //名称
        TextView tv2_name=(TextView)findViewById(R.id.yuansu_names);
        tv2_name.setText(ys_name);
        //内容
        tv2_content=(TextView)contentv.findViewById(R.id.ys_detial2_content);
        tv2_content.setText(ys_content);
        //元素列表
        lv2=(MyExpandableListView) contentv.findViewById(R.id.ys_detial2_list);
        //ya=new YuansuAdapter(ysc_list,JKYuansuDetail.this);
        ea=new ExpandableAdapter2();
        lv2.setAdapter(ea);
        //展开某组

        lv2.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                final int flag=i;
                //关闭其他展开项
                for(int x=0;x<ysc_list.size();x++){
                    if(x!=i)
                    {
                        lv2.collapseGroup(x);
                        ysc_list.get(x).setIsexpand(false);
                    }
                }
                ysc_list.get(i).setIsexpand(true);
                ea.notifyDataSetChanged();
                /*
                scroll.post(new Runnable() {
                    @Override
                    public void run() {
                        scroll.smoothScrollTo(0,ysc_list.get(flag).getY());
                    }

                });
                */
            }
        });
        //收起某组
        lv2.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                ysc_list.get(i).setIsexpand(false);
                ea.notifyDataSetChanged();
            }
        });

        scrollView.addView(contentv);
    }

    public class PaihangAdapter extends BaseAdapter {
        private  List<Shuguo> mydata;
        private Context context;
        public PaihangAdapter(List<Shuguo> mydata,Context context){
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
            view= LayoutInflater.from(context).inflate(R.layout.paihang_items,null);
            ImageView ima=(ImageView)view.findViewById(R.id.paihang_item_imag);
            com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(context);
            DisplayImageOptions options=ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL+""+mydata.get(i).getPic(),ima,options);
            TextView t=(TextView)view.findViewById(R.id.paihang_item_name);
            t.setText(mydata.get(i).getTitle());
            TextView t2=(TextView)view.findViewById(R.id.paihang_item_content);
            t2.setText(mydata.get(i).getAbout());
            TextView rank=(TextView)view.findViewById(R.id.paihang_item_rank);
            rank.setText((i+1)+"");
            rank.setBackgroundColor(0xc000aff0);
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(JKYuansuDetail.this,JKshuguoDetial.class);
                    intent.putExtra("id",mydata.get(x).getId());
                    JKYuansuDetail.this.startActivity(intent);
                }
            });

            return view;
        }
    }

    class ExpandableAdapter2 extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return ysc_list.size();
        }

        /**
         * 子item
         */
        @Override
        public int getChildrenCount(int groupPosition) {

            return 1;
        }

        /**
         *
         * 获取当前父item的数据
         */
        @Override
        public Object getGroup(int groupPosition) {
            return ysc_list.get(groupPosition);
        }

        /**
         * 获取子item的需要关联的数据
         */
        @Override
        public Object getChild(int groupPosition, int childPosition) {

            return ysc_list.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {

            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        /**
         * 设置父item的组件
         *
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yuansu_item, null);
            }
            TextView tv1=(TextView) convertView.findViewById(R.id.ysitem_tv1);
            tv1.setText(ysc_list.get(groupPosition).getName());
            ImageView more=(ImageView) convertView.findViewById(R.id.expand_more_ima);
            if(ysc_list.get(groupPosition).isexpand()){
                    /*
                    int[] location = new int[2];
                    convertView.getLocationOnScreen(location);
                    Log.d("===============which",location[1]+"");
                    Rect rectangle= new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
                    ysc_list.get(groupPosition).setY(location[1]+tv2_content.getHeight()+rectangle.top);
                    */
                more.setImageResource(R.drawable.barrow);
            }else {
                lv2.collapseGroup(groupPosition);
                more.setImageResource(R.drawable.barrow_2);
            }
            return convertView;
        }

        /**
         * 设置子item的组件
         *
         */
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yuansu_itemdetial, null);
            }
            TextView tv2=(TextView) convertView.findViewById(R.id.ysitem_tv2);
            tv2.setText(ysc_list.get(groupPosition).getContent());
            ListViewForScrollView listView=(ListViewForScrollView) convertView.findViewById(R.id.ys_detial1_list_list);
            listView.setAdapter(new PaihangAdapter(ysc_list.get(groupPosition).getSg_list(),getBaseContext()));
            //显示更多或contents
            TextView tvmore=(TextView)convertView.findViewById(R.id.chakan_allph2);
            final int x=groupPosition;
            if(ysc_list.get(groupPosition).getSg_list().size()>0){
                tvmore.setText("查看完整"+ysc_list.get(groupPosition).getName()+"含量排行榜");
                tvmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(JKYuansuDetail.this,JKPaihangDetial.class);
                        intent.putExtra("id",ysc_list.get(x).getId());
                        intent.putExtra("title","");
                        JKYuansuDetail.this.startActivity(intent);
                    }
                });

            }else {
                tvmore.setText(ysc_list.get(groupPosition).getContents());
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }// adapter结尾

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
