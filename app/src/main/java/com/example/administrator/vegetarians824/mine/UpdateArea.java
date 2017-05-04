package com.example.administrator.vegetarians824.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpdateArea extends AppCompatActivity {
    private List<String> province;
    private ListView listView;
    public static UpdateArea instance = null;
    private String sflag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_area);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        instance = this;
        province=new ArrayList<>();
        listView=(ListView)findViewById(R.id.province);
        Intent it=getIntent();
        sflag=it.getStringExtra("flag");
        if(sflag.equals("2")){
            province.add("全国");
        }
        initoperate();
        getProvince();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit6_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getProvince(){
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainLiving/androidapi/user/update_province", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        String p=jo.getString("name");
                        province.add(p);
                    }
                    listView.setAdapter( new ArrayAdapter<>(UpdateArea.this,android.R.layout.simple_list_item_1,province) );
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(sflag.equals("2")){
                                if(i==0||i==1||i==2||i==9||i==22||i==33||i==34){
                                    BaseApplication.app.setAcity(province.get(i));
                                    finish();
                                }else {
                                    Intent intent=new Intent(UpdateArea.this,UpdateAreaCity.class);
                                    intent.putExtra("pid",String.valueOf(i));
                                    intent.putExtra("province",province.get(i));
                                    intent.putExtra("flag",sflag);
                                    UpdateArea.this.startActivity(intent);
                                }
                            }else {
                                Intent intent=new Intent(UpdateArea.this,UpdateAreaCity.class);
                                intent.putExtra("pid",String.valueOf(i+1));
                                intent.putExtra("province",province.get(i));
                                intent.putExtra("flag",sflag);
                                UpdateArea.this.startActivity(intent);
                            }
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
    protected void onRestart() {
        super.onRestart();

    }


}
