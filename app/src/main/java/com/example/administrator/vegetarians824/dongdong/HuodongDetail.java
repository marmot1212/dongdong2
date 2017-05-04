package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HuodongDetail extends AppCompatActivity {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huodong_detail);
        StatusBarUtil.setColorDiff(this,0xff00aff0);

        operater();
        InitView();
        getHuodongData();
    }
    public void InitView(){

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }
    public void operater(){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.detail_fanhui);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HuodongDetail.this.finish();
            }
        });
    }
    public void getHuodongData(){
        Log.d("===============add",URLMannager.Huodong_Detail + "act_id/"+id);
        StringRequest request=new StringRequest(URLMannager.Huodong_Detail + "act_id/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray jsa=js1.getJSONArray("Result");
                    final JSONObject jo=jsa.getJSONObject(0);

                    final double longitude=jo.getDouble("longitude");
                    final double latitude=jo.getDouble("latitude");
                    TextView time=(TextView)findViewById(R.id.hddetial_starttoend);
                    time.setText(jo.getString("start_time")+" 至 "+jo.getString("finish_time"));
                    if(jo.getString("address").equals("")){
                        LinearLayout l=(LinearLayout)findViewById(R.id.hddetial_line);
                        LinearLayout l2=(LinearLayout)findViewById(R.id.hddetial_line2);
                        l.removeView(l2);
                    }else {
                        TextView address = (TextView) findViewById(R.id.huodong_address);
                        address.setText(jo.getString("address"));
                        address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(), HuodongRoute.class);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("latitude", latitude);
                                HuodongDetail.this.startActivity(intent);
                            }
                        });
                    }
                    ImageView imageView=(ImageView)findViewById(R.id.huodongdetail_img);
                    TextView textView=(TextView)findViewById(R.id.huodongdetil_text);
                    String c=jo.getString("content");
                    String pic=jo.getString("img_url_1");
                    textView.setText(c);
                    ImageLoader loader= ImageLoaderUtils.getInstance(HuodongDetail.this);
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+pic,imageView,options);

                    String type=jo.getString("type");
                    if(type.equals("3")){
                        final String url=jo.getString("url");
                        LinearLayout line=(LinearLayout)findViewById(R.id.huodongdetail_line);
                        line.setVisibility(View.VISIBLE);
                        TextView tvbm=(TextView)findViewById(R.id.huodongdetail_baoming);
                        tvbm.setText("前往报名");
                        line.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(HuodongDetail.this,HuodongWeb.class);
                                intent.putExtra("url",url);
                                HuodongDetail.this.startActivity(intent);
                            }
                        });
                    }

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


}
