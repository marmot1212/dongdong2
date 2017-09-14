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
import com.baidu.mobstat.StatService;
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

public class LishiDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lishi_detail);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        operater();
        InitView();
    }
    public void InitView(){

        Intent intent=getIntent();
        String id=intent.getStringExtra("lsid");
        getLishiData(id);

    }
    public void operater(){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.lishidetail_fanhui);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LishiDetail.this.finish();
            }
        });
    }

    public void getLishiData(String id){
        Log.d("===============add",URLMannager.Huodong_Detail + "act_id/"+id);
        StringRequest request=new StringRequest(URLMannager.Huodong_Detail + "act_id/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray jsa=js1.getJSONArray("Result");
                    JSONObject jo=jsa.getJSONObject(0);
                    ImageView imageView=(ImageView)findViewById(R.id.lishihuodongdetail_img);
                    TextView textView=(TextView)findViewById(R.id.lishihuodongdetil_text);
                    textView.setText(jo.getString("content"));
                    ImageLoader loader= ImageLoaderUtils.getInstance(LishiDetail.this);
                    DisplayImageOptions options=ImageLoaderUtils.getOpt();
                    loader.displayImage(URLMannager.Imag_URL+""+jo.getString("img_url_1"),imageView,options);

                    final double longitude=jo.getDouble("longitude");
                    final double latitude=jo.getDouble("latitude");
                    TextView time=(TextView)findViewById(R.id.lishi_starttoend);
                    time.setText(jo.getString("start_time")+" 至 "+jo.getString("finish_time"));
                    //地址栏点击跳转
                    if(jo.getString("address").equals("")){
                        LinearLayout l=(LinearLayout)findViewById(R.id.lishi_line);
                        LinearLayout l2=(LinearLayout)findViewById(R.id.lishi_line2);
                        l.removeView(l2);
                    }else {
                        TextView address=(TextView) findViewById(R.id.lishi_address);
                        address.setText(jo.getString("address"));
                        address.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                Intent intent=new Intent(getBaseContext(),HuodongRoute.class);
                                intent.putExtra("longitude",longitude);
                                intent.putExtra("latitude",latitude);
                                LishiDetail.this.startActivity(intent);
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
