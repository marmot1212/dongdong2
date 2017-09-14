package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Yuansu;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JKshuguoPH extends AppCompatActivity {
    private List<Yuansu> list;
    private List<String> string_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkshuguo_ph);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list=new ArrayList<>();
        string_list=new ArrayList<>();
        initoperate();
        getData();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.paihang_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getData(){
        StringRequest request=new StringRequest(URLMannager.ShuGuo_Paihang, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        Yuansu ys=new Yuansu();
                        ys.setId(jo.getString("hid"));
                        ys.setTitle(jo.getString("name"));
                        list.add(ys);
                        string_list.add(jo.getString("name"));
                    }

                    ListView listView=(ListView) findViewById(R.id.paihang_list);
                    listView.setAdapter( new ArrayAdapter<>(JKshuguoPH.this,android.R.layout.simple_list_item_1,string_list) );

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id=list.get(i).getId();
                            String title=list.get(i).getTitle();
                            Intent  intent=new Intent(JKshuguoPH.this,JKPaihangDetial.class);
                            intent.putExtra("id",id);
                            intent.putExtra("title",title);
                            JKshuguoPH.this.startActivity(intent);
                        }
                    });
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
