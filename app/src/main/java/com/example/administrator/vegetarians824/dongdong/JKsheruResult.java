package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.MListAdapter;
import com.example.administrator.vegetarians824.entry.SheruResult;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JKsheruResult extends AppCompatActivity {

    private List<SheruResult> result;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jksheru_result);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        result=new ArrayList<>();
        initoperate();
        HttpRequest();
        listView=(ListView) findViewById(R.id.result_list);

    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.sheruresult_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void HttpRequest(){
        StringPostRequest spr=new StringPostRequest(URLMannager.YuanSu_XQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
                Log.d("======",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for (int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        SheruResult r=new SheruResult();
                        r.setTitle(jo.getString("title"));
                        r.setTuijian(jo.getString("RNI"));
                        r.setZuida(jo.getString("AI"));
                        r.setCankao(jo.getString("UL"));
                        result.add(r);
                    }
                    listView.setAdapter(new MListAdapter(result,JKsheruResult.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        Intent intent=getIntent();
        Log.d("======",intent.getStringExtra("type"));
        spr.putValue("sex",intent.getStringExtra("sex"));
        spr.putValue("year_type",intent.getStringExtra("age"));
        spr.putValue("foodtype",intent.getStringExtra("type"));
        SlingleVolleyRequestQueue.getInstance(JKsheruResult.this).addToRequestQueue(spr);
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
