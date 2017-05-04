package com.example.administrator.vegetarians824.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Xiugai extends AppCompatActivity {
    EditText phone,pwd,code;
    String number,msg,password;
    ImageView eye;
    Button send;
    boolean ismi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        ismi=true;
        send=(Button)findViewById(R.id.xiugai_send);
        initoperate();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout) findViewById(R.id.xiugai_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        phone=(EditText) findViewById(R.id.xiugai_phone);
        code=(EditText)findViewById(R.id.xiugai_code);
        pwd=(EditText)findViewById(R.id.xiugai_pwd);
        //send=(Button)findViewById(R.id.xiugai_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=phone.getText().toString();
                if(!number.equals("")) {
                    sendRequest();
                }else {
                    Toast.makeText(getBaseContext(),"请填写手机号",Toast.LENGTH_SHORT).show();
                }

            }
        });
        eye=(ImageView)findViewById(R.id.xiugai_eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismi){
                    eye.setImageResource(R.drawable.eyeon);
                    ismi=false;
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    eye.setImageResource(R.drawable.eyeoff);
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ismi=true;
                }
            }
        });
        Button bt=(Button)findViewById(R.id.xiugai_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SharedPreferences pre=getSharedPreferences("shared", MODE_PRIVATE);
                //Log.d("=====id",pre.getString("id",""));
                if((!phone.getText().equals(""))&&(!code.getText().equals(""))&&(!pwd.getText().equals(""))){
                    //第三方平台认证
                    if(code.getText().toString().equals(msg)){
                        //输入验证码与平台一致
                        password=pwd.getText().toString();
                        //Log.d("=======pwd",password);
                        doPosts();
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
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/androidapi/send/send", new Response.Listener<String>() {
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
        spr.putValue("mobile",number);
        spr.putValue("send_type","3");
        SlingleVolleyRequestQueue.getInstance(Xiugai.this).addToRequestQueue(spr);
    }
    
    public void doPosts(){
        StringPostRequest spr=new StringPostRequest("https://www.isuhuo.com/plainLiving/Androidapi/user/find_pass", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    Toast.makeText(getBaseContext(),js.getString("Message"),Toast.LENGTH_SHORT).show();
                    BaseApplication.app.getUser().setPwd(pwd.getText().toString());
                    SharedPreferences preferences=Xiugai.this.getSharedPreferences("shared",Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("password",pwd.getText().toString());
                    editor.commit();//提交数据
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("mobile",phone.getText().toString());
        spr.putValue("s_password",password);
        SlingleVolleyRequestQueue.getInstance(Xiugai.this).addToRequestQueue(spr);
    }

    public class myclock extends AsyncTask<Void,Integer,String>{

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
