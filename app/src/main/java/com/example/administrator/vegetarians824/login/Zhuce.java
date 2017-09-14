package com.example.administrator.vegetarians824.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Zhuce extends AppCompatActivity {
    EditText phone,pwd,code;
    String number,msg,password;
    ImageView eye;
    boolean ismi;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        ismi=true;
        initoperate();
    }
    //初始化操作按钮
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout) findViewById(R.id.zhuce_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView xieyi=(TextView)findViewById(R.id.zhuce_xieyi);
        xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Zhuce.this,Xieyi.class);
                Zhuce.this.startActivity(intent);
            }
        });

        phone=(EditText) findViewById(R.id.zhuce_phone);
        code=(EditText)findViewById(R.id.zhuce_code);
        pwd=(EditText)findViewById(R.id.zhuce_pwd);
        send=(Button)findViewById(R.id.zhuce_send);
        //发送验证码
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number=phone.getText().toString();
                if(!number.equals("")){
                    sendRequest();
                }else {
                    Toast.makeText(getBaseContext(),"请填写手机号",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //密码可见不可见
        eye=(ImageView)findViewById(R.id.zhuce_eye);
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
        //注册按钮
        Button bt=(Button)findViewById(R.id.zhuce_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断信息是否填写完整
                if((!phone.getText().equals(""))&&(!code.getText().equals(""))&&(!pwd.getText().equals(""))){
                    //第三方平台认证
                    if(code.getText().toString().equals(msg)){
                        //输入验证码与平台一致
                        password=pwd.getText().toString();
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

    //请求服务器发送验证码
    public void sendRequest(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/androidapi/send/send", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1"))
                    {
                        new myclock().execute();
                        JSONObject js2=js1.getJSONObject("Result");
                        //取得验证码
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
        spr.putValue("send_type","1");
        SlingleVolleyRequestQueue.getInstance(Zhuce.this).addToRequestQueue(spr);
    }
    //本地审核通过后提交注册信息
    public void admits(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        /*
                        JSONObject js2=js1.getJSONObject("Result");
                        User user=new User();
                        user.setId(js2.getString("id"));
                        user.setName(js2.getString("username"));
                        user.setPwd(pwd.getText().toString());
                        user.setMobile(js2.getString("mobile"));
                        user.setPic(js2.getString("user_head_img"));
                        user.setJifen(js2.getString("jifen"));
                        if(!js2.isNull("tenans")){
                            user.setTenans(js2.getString("tenans"));
                        }
                        //设置登陆状态为已登陆
                        user.setIslogin(true);
                        //存本地
                        saveToPre(user);
                        //app的application中重置用户信息
                        BaseApplication.app.setUser(user);
                        */
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
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
        spr.putValue("mobile",number);
        spr.putValue("password",password);
        SlingleVolleyRequestQueue.getInstance(Zhuce.this).addToRequestQueue(spr);
    }
    //存到本地的sharedpreference
    public void saveToPre(User u){
        SharedPreferences preferences=Zhuce.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("id",u.getId());
        editor.putString("username",u.getName());
        editor.putString("password",u.getPwd());
        editor.putString("mobile",u.getMobile());
        editor.putString("user_head_img",u.getPic());
        editor.putString("status",u.getStatus());
        editor.putString("province",u.getProvince());
        editor.putString("city",u.getCity());
        editor.putString("sex",u.getSex());
        editor.putString("intro",u.getIntro());
        editor.putString("jifen",u.getJifen());
        editor.putBoolean("islog",u.islogin());
        editor.putString("tenans",u.getTenans());
        editor.commit();//提交数据
    }
    //异步任务进行120秒计时
    public class myclock extends AsyncTask<Void,Integer,String> {

        @Override
        protected String doInBackground(Void... voids) {

            //一个120秒的进程
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
            //计时过程实时动态改变textview的显示
            int current=120-values[0];
            send.setText(current+"秒后重发");
            send.setBackgroundResource(R.drawable.button_bg2);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //计时结束恢复，重新发送
            send.setText(s);
            send.setBackgroundResource(R.drawable.button_bg);
        }
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
