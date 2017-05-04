package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.HotCity;
import com.example.administrator.vegetarians824.entry.Province;
import com.example.administrator.vegetarians824.entry.ProvinceCity;
import com.example.administrator.vegetarians824.fragment.MapFragment;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.MyExpandableListView;
import com.example.administrator.vegetarians824.myView.UserDefineScrollView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitySelectScrollView extends AppCompatActivity {

    private MyExpandableListView listView;// ListView子类，可展开的列表组件
    private MyHandler.ExpandableAdapter elv_Adapter;// 二级ListView适配器
    private GridView gridview;// 九宫格
    private MyHandler.GridAdapter grid_Adapter;// gridViwe的Adapter

    private MyHandler handler;// 自定义handle对象
    private LinearLayout lin_back, lin_currentcity;// 返回
    private TextView tv_currentCity;// 当前城市
    String city_current;// 当前城市
    private RelativeLayout xianggang,aomen,taiwan;
    private UserDefineScrollView scrollView;
    private static int which=-1;
    private static int where=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select_scroll_view);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        HttpGetRequest();// 请求服务器拿数据；
        initViews();
        initDatas();
        initOpers();
        initjump();
    }
    public void initViews() {
        listView = (MyExpandableListView) this
                .findViewById(R.id.city_select_expandableListview);
        listView.setGroupIndicator(null);// 去掉箭头
        lin_back = (LinearLayout) this.findViewById(R.id.city_select_lin_back);// 返回
        tv_currentCity = (TextView) this
                .findViewById(R.id.city_select_tv_currentcity);// 当前城市
        lin_currentcity = (LinearLayout) this
                .findViewById(R.id.city_select_lin_currentcity);
        gridview = (GridView) this.findViewById(R.id.city_select_gridview);// 九宫格
        scrollView=(UserDefineScrollView) findViewById(R.id.cityselect_scrollview);

    }

    public void initDatas() {
        if(BaseApplication.app.getMyLociation()!=null)
        {
            city_current = BaseApplication.app.getMyLociation().getMyCity();// 拿到当前城市
        }else {
            city_current="定位中";
        }

        tv_currentCity.setText(city_current);// 展示当前城市

    }

    public void initOpers() {
        // 设置back的监听事件
        lin_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
               // Intent intent = new Intent(CitySelectScrollView.this, Mishi.class);
                //intent.putExtra("city", city_current);
                //setResult(0, intent);
                finish();// 关闭此页面
            }
        });


        // 设置back的监听事件
        lin_currentcity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(CitySelectScrollView.this,
                        MapFragment.class);
                intent.putExtra("city", city_current);
                setResult(11, intent);
                finish();// 关闭此页面
            }
        });

        xianggang=(RelativeLayout) findViewById(R.id.city_select_rel_hangkong);
        aomen=(RelativeLayout) findViewById(R.id.city_select_rel_aomen);
        taiwan=(RelativeLayout) findViewById(R.id.city_select_rel_taiwan);

        xianggang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitySelectScrollView.this,
                        MapFragment.class);
                intent.putExtra("city", "香港");
                setResult(11, intent);
                finish();// 关闭此页面
            }
        });
        aomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitySelectScrollView.this,
                        MapFragment.class);
                intent.putExtra("city", "澳门");
                setResult(11, intent);
                finish();// 关闭此页面
            }
        });
        taiwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitySelectScrollView.this,
                        MapFragment.class);
                intent.putExtra("city", "台湾");
                setResult(11, intent);
                finish();// 关闭此页面
            }
        });
        // 二级ListView
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
                                        int arg3, long arg4) {
                elv_Adapter.notifyDataSetChanged();// 二级listView
                //setListViewHeight(listView);// 动态获取ListView(包含子类)的高
                return false;
            }
        });


    }

    public void initjump(){
        TextView jump=(TextView)findViewById(R.id.guoji_jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CitySelectScrollView.this,Trip.class);
                CitySelectScrollView.this.startActivity(intent);
            }
        });
    }
    /**
     * 网络编程，自定义get方法服务器拿数据,带一个经纬度对象
     */
    public void HttpGetRequest() {
        handler = new MyHandler();
        new Thread() {
            public void run() {
                try {
                    // 拼写url
                    String str = URLMannager.CityList;
                    // 正式开始请求
                    URL url = new URL(str);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setReadTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        StringBuilder sb = new StringBuilder();
                        while ((len = in.read(bytes)) != -1) {
                            sb.append(new String(bytes, 0, len));
                        }
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = sb.toString();
                        handler.sendMessage(msg);// handle交给管理者处理
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        }.start();

    }
    /**
     * 网络编程，josn解析,内部类数据保存到appliciation,
     */
    class MyHandler extends Handler {
        public List<HotCity> hotcity_list = null;// 热门城市的集合
        public HotCity hotCity;// 热门城市
        public Map<String, List<ProvinceCity>> map_province_city = null;// 省份和子城市的map
        public List<Province> province_list = null;// 省份集合
        public Province province;// 省份
        public List<ProvinceCity> province_City_list = null;// 城市集合
        public ProvinceCity province_City;// 省份下的子城市

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Json解析数据
            if (msg.what == 2) {
                // 初始化三个集合
                hotcity_list = new ArrayList<HotCity>();
                province_list = new ArrayList<Province>();
                map_province_city = new HashMap<String, List<ProvinceCity>>();
                // 拿到数据
                String strData = (String) msg.obj;

                try {
                    JSONObject jsonObj1 = new JSONObject(strData);
                    JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                    // 热门城市，保存在同一个集合里
                    JSONObject jsonObj2_1 = jsonObj2.getJSONObject("hotcity");
                    JSONArray array2_1 = jsonObj2_1.getJSONArray("list");
                    for (int i = 0; i < array2_1.length(); i++) {
                        JSONObject jsonObj2_2 = array2_1.getJSONObject(i);
                        hotCity = new HotCity();
                        hotCity.setName(jsonObj2_2.get("name").toString());
                        hotcity_list.add(hotCity);// 保存集合
                    }

                    // 开始解析所有城市
                    JSONArray array3_1 = jsonObj2.getJSONArray("allcity");
                    for (int i = 0; i < array3_1.length(); i++) {
                        JSONObject jsonObj3_1 = array3_1.getJSONObject(i);// 拿到省份object对象
                        province = new Province();// new一个新省份
                        province.setName(jsonObj3_1.getString("name"));
                        province_list.add(province);// 把省份添加到集合

                        province_City_list = new ArrayList<ProvinceCity>();// 循环一次，new一下城市集合
                        JSONArray array4_1 = array3_1.getJSONObject(i)
                                .getJSONArray("list");
                        for (int j = 0; j < array4_1.length(); j++) {
                            JSONObject jsonObj4_1 = array4_1.getJSONObject(j);// 拿到省份下子城市object对象
                            province_City = new ProvinceCity();// new一个新城市
                            province_City.setName(jsonObj4_1.getString("name")
                                    .toString());
                            province_City_list.add(province_City);// 添加城市到集合

                            //Log.d("======c",jsonObj4_1.getString("name"));
                        }
                        // Map,保存对应省份，以及子城市集合，
                        map_province_city.put(province.toString(),
                                province_City_list);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // 保存到BaseApplication
               // BaseApplication.app.setHotcity_list(hotcity_list);// 热门城市集合
                //BaseApplication.app.setProvince_list(province_list);// 保存省份集合
               // BaseApplication.app.setMap_province_city(map_province_city);// 保存Map
                initView();
            }

        }// handleMessage结尾

        // 自定义方法，绑定适配器
        public void initView() {
            grid_Adapter = new GridAdapter();
            gridview.setAdapter(grid_Adapter);// 绑定适配器
            gridview.setEnabled(false);// 设置gridview不可滑动

            elv_Adapter = new ExpandableAdapter();
            listView.setAdapter(elv_Adapter);// 绑定适配器，
            if(which>=0) {
                listView.expandGroup(which);
                Log.d("=========which",which+"");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0,where);
                    }
                });
            }
            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    which=i;
                    where=scrollView.getScrollY();
                    return false;
                }
            });

            //setListViewHeight(listView);// 动态获取ListView的高度

        }

        /**
         * 自定义适配器（放在了内部类里边）
         *
         * @author Administrator
         */
        class ExpandableAdapter extends BaseExpandableListAdapter {
            /**
             * 父item的个数
             */
            @Override
            public int getGroupCount() {
                return province_list.size();
            }

            /**
             * 子item
             */
            @Override
            public int getChildrenCount(int groupPosition) {
                String key = province_list.get(groupPosition).toString();
                if (map_province_city.get(key) == null) {
                }

                return map_province_city.get(key).size();
            }

            /**
             *
             * 获取当前父item的数据
             */
            @Override
            public Object getGroup(int groupPosition) {
                return province_list.get(groupPosition);
            }

            /**
             * 获取子item的需要关联的数据
             */
            @Override
            public Object getChild(int groupPosition, int childPosition) {
                String key = province_list.get(groupPosition).toString();
                return map_province_city.get(key).get(childPosition);
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
                    convertView = getLayoutInflater().inflate(
                            R.layout.city_select_list_item, null);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.city_select_list_item_tv);
                tv.setText(province_list.get(groupPosition).getName().toString());// textView展示省份名字

                return convertView;
            }

            /**
             * 设置子item的组件
             *
             */
            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

                String key = province_list.get(groupPosition).toString();
                final ProvinceCity city_info = map_province_city.get(key).get(
                        childPosition);

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(
                            R.layout.city_select_list_itemitem, null);
                }
                LinearLayout lin_ChildItem = (LinearLayout) convertView
                        .findViewById(R.id.city_select_list_itemitem_lin);
                TextView tv = (TextView) convertView
                        .findViewById(R.id.city_select_list_itemitem_tv);
                tv.setText(city_info.getName().toString());
                /**
                 * ChildItem设置监听事件，点击返回当前的城市名；
                 */
                lin_ChildItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(CitySelectScrollView.this,
                                MapFragment.class);
                        intent.putExtra("city", city_info.getName().toString());
                        setResult(11, intent);
                        finish();// 关闭此界面
                    }
                });
                return convertView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return true;
            }
        }// adapter结尾

        /**
         * 自定义，gridView的adapter
         */
        class GridAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return hotcity_list.size();

            }

            @Override
            public Object getItem(int position) {
                return hotcity_list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView,
                                ViewGroup parent) {
                ViewHodler vh;
                if (convertView == null) {
                    convertView = CitySelectScrollView.this.getLayoutInflater()
                            .inflate(R.layout.city_select_grid_item, null);
                    vh = new ViewHodler();
                    vh.tv_name = (TextView) convertView
                            .findViewById(R.id.city_select_grid_item_tv);
                    vh.linear = (LinearLayout) convertView
                            .findViewById(R.id.city_select_grid_item_lin);
                    convertView.setTag(vh);
                } else {
                    vh = (ViewHodler) convertView.getTag();
                }
                vh.tv_name.setText(hotcity_list.get(position).getName()
                        .toString());
                // item设置监听事件
                vh.linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(CitySelectScrollView.this,
                                MapFragment.class);
                        intent.putExtra("city", hotcity_list.get(position)
                                .getName().toString());
                        setResult(11, intent);
                        finish();// 关闭此界面
                    }
                });

                return convertView;
            }
        }

        // 工具类
        class ViewHodler {
            private TextView tv_name;
            private LinearLayout linear;

        }

    }// 结尾
    @Override
    protected void onPause() {
        super.onPause();
        super.finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            finish();// 关闭此页面
        }

        return false;
    }
}
