package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePhone extends AppCompatActivity {
    String msg,number;
    EditText et1,et2,et3;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        et1=(EditText)findViewById(R.id.edit5_edit1);
        et2=(EditText)findViewById(R.id.edit5_edit2);
        et3=(EditText)findViewById(R.id.edit5_edit3);
        initop();
        updates();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit5_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mobile = (TextView) findViewById(R.id.edit5_text);
        StringBuffer buffer = new StringBuffer();
        if(!BaseApplication.app.getUser().getMobile().equals(""))
            buffer.append(BaseApplication.app.getUser().getMobile().substring(0, 3)).append("****").append(BaseApplication.app.getUser().getMobile().substring(7, 11));
        mobile.setText(buffer.toString());
    }

    public void updates(){
        send=(Button)findViewById(R.id.edit5_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=et1.getText().toString();
                if(!number.equals("")){
                    if(number.equals(BaseApplication.app.getUser().getMobile())){
                        sendRequest();
                    }
                    else {
                        Toast.makeText(getBaseContext(),"原手机号码输入有误！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getBaseContext(),"请填写手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button bt=(Button)findViewById(R.id.edit5_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!et1.getText().toString().equals(""))&&(!et2.getText().toString().equals(""))&&(!et3.getText().toString().equals(""))){
                    //第三方平台认证
                    if(et3.getText().toString().equals(msg)){
                        //输入验证码与平台一致
                        admits();
                    }
                    else {
                        Toast.makeText(getBaseContext(),"验证码错误！",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"请填写完整",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendRequest(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/androidapi/send/send", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1"))
                    {
                        new myclock().execute();
                        JSONObject js2=js1.getJSONObject("Result");
                        msg=js2.getString("code");
                    }
                    else {
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
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
        spr.putValue("mobile",et2.getText().toString());
        spr.putValue("send_type","2");
        SlingleVolleyRequestQueue.getInstance(UpdatePhone.this).addToRequestQueue(spr);
    }

    public void admits(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainliving/Androidapi/user/update_mobile", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                        BaseApplication.app.getUser().setMobile(et2.getText().toString());
                        SharedPreferences preferences=UpdatePhone.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("mobile",et2.getText().toString());
                        editor.commit();//提交数据
                        finish();
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
        spr.putValue("y_mobile",BaseApplication.app.getUser().getMobile());
        spr.putValue("n_mobile",et2.getText().toString());
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(UpdatePhone.this).addToRequestQueue(spr);
    }

    public class myclock extends AsyncTask<Void,Integer,String> {

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=1;i<=120;i++){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "请重新发送";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int current=120-values[0];
            send.setText(current+"秒后重发");
            send.setBackgroundResource(R.drawable.button_bg2);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            send.setText(s);
            send.setBackgroundResource(R.drawable.button_bg);
        }
    }
}
