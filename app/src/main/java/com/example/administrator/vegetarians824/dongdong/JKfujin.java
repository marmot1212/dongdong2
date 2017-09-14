package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.CantingListAdapter;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SensorManagerHelper;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JKfujin extends AppCompatActivity {
    private List<CantingInfo> list_info;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkfujin);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initoperate();
        list_info=new ArrayList<>();
        list=(ListView)findViewById(R.id.fujin_list);
        if(BaseApplication.app.getMyLociation()!=null){
            Log.d("===========aa","aa");
            initdata();
        }
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fujin_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initdata(){
        String url= URLMannager.Mishi_Canting+"longitude/" + BaseApplication.app.getMyLociation().getLongitude()+ "/latitude/" + BaseApplication.app.getMyLociation().getLatitude()+"/order/distance";
        StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("===========s",s);
                try {
                    JSONObject jsonObj1 = new JSONObject(s);
                    JSONObject jsonObj2 = jsonObj1.getJSONObject("Result");
                    JSONArray array1 = jsonObj2.getJSONArray("list");
                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject array1_2 = array1.getJSONObject(i);
                        CantingInfo cantingInfo = new CantingInfo();
                        cantingInfo.id = array1_2.getString("id");
                        cantingInfo.title = array1_2.getString("title");
                        if (array1_2.getString("tel") != null)
                            cantingInfo.tel = array1_2.getString("tel");
                        cantingInfo.content = array1_2.getString("content");
                        cantingInfo.unit_pric = array1_2.getString("unit_price");
                        cantingInfo.longitude = array1_2.getString("longitude");
                        cantingInfo.latitude = array1_2.getString("latitude");
                        if (array1_2.getString("img_url_1") != null)
                            cantingInfo.img_url_1 = array1_2.getString("img_url_1");
                        cantingInfo.type = array1_2.getString("type");
                        cantingInfo.create_time = array1_2.getString("create_time");
                        cantingInfo.uid = array1_2.getString("uid");
                        cantingInfo.address = array1_2.getString("address");
                        if (array1_2.getString("img_url_th_1") != null)
                            cantingInfo.img_url_th_1 = array1_2.getString("img_url_th_1");
                        if (array1_2.getString("user_head_img") != null)
                            cantingInfo.user_head_img = array1_2.getString("user_head_img");
                        if (array1_2.getString("user_head_img_th") != null)
                            cantingInfo.user_head_img_th = array1_2.getString("user_head_img_th");
                        cantingInfo.username = array1_2.getString("username");
                        cantingInfo.distance = array1_2.getString("distance");
                        list_info.add(cantingInfo);
                    }
                    list.setAdapter(new CantingListAdapter(list_info,JKfujin.this));
                    TextView foot = new TextView(getBaseContext());
                    foot.setTextSize(12);
                    foot.setTextColor(0xffa0a0a0);
                    AbsListView.LayoutParams params2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 160);
                    foot.setLayoutParams(params2);
                    foot.setGravity(Gravity.CENTER);
                    foot.setText("查看更多请点击首页素食地图");
                    list.addFooterView(foot);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }
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
