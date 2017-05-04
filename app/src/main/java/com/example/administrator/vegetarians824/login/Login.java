package com.example.administrator.vegetarians824.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText name,pwd;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        name=(EditText)findViewById(R.id.log_uname);
        pwd=(EditText)findViewById(R.id.log_upwd);
        type="3";
        initoperate();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.login_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView zhuce=(TextView)findViewById(R.id.login_zhuce);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Zhuce.class);
                Login.this.startActivity(intent);
            }
        });
        TextView xiugai=(TextView)findViewById(R.id.login_xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Xiugai.class);
                Login.this.startActivity(intent);
            }
        });
        Button bt=(Button)findViewById(R.id.log_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname=name.getText().toString();
                String upwd=pwd.getText().toString();

                if(uname.length()==11){
                    boolean isphone=true;
                    for(int i=0;i<uname.length();i++ ){
                        if (!Character.isDigit(uname.charAt(i))){
                            isphone=false;
                        }
                    }
                    if(isphone) {
                        type = "3";
                        httpRequest(uname, upwd);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    if(uname.length()<=10){
                        type="1";
                        httpRequest(uname,upwd);
                    }
                else{
                        Toast.makeText(getBaseContext(),"请输入正确的用户名/手机号！",Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

    public void httpRequest(String username,String userpwd){

        StringPostRequest spr=new StringPostRequest(URLMannager.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    String code=js1.getString("Code");
                    if(code.equals("1")){
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                        JSONObject js2=js1.getJSONObject("Result");
                        User user=new User();
                        user.setId(js2.getString("id"));
                        user.setName(js2.getString("username"));
                        user.setPwd(pwd.getText().toString());
                        user.setMobile(js2.getString("mobile"));
                        user.setPic(js2.getString("user_head_img"));
                        user.setStatus(js2.getString("status"));
                        user.setProvince(js2.getString("province"));
                        user.setCity(js2.getString("city"));
                        user.setSex(js2.getString("sex"));
                        user.setIntro(js2.getString("intro"));
                        user.setJifen(js2.getString("jifen"));
                        if(!js2.isNull("tenans"))
                            user.setTenans(js2.getString("tenans"));
                        user.setType(type);
                        user.setIslogin(true);
                        saveToPre(user);
                        BaseApplication.app.setUser(user);
                        finish();
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
        spr.putValue("mobile",username);
        spr.putValue("type",type);
        spr.putValue("password",userpwd);
        SlingleVolleyRequestQueue.getInstance(Login.this).addToRequestQueue(spr);
    }
    public void saveToPre(User u){
        SharedPreferences preferences=Login.this.getSharedPreferences("shared", Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
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
        editor.putString("type",u.getType());
        editor.putString("tenans",u.getTenans());
        editor.commit();//提交数据
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
