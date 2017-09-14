package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePwd extends AppCompatActivity {
    String oldpwd,newpwd;
    EditText et1,et2;
    boolean ismi;
    ImageView eye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        et1=(EditText)findViewById(R.id.edit7_edit1);
        et2=(EditText)findViewById(R.id.edit7_edit2);
        ismi=true;
        initoperate();
        updatepwd();
    }

    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.edit7_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        eye=(ImageView)findViewById(R.id.update_pwd_eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ismi){
                    eye.setImageResource(R.drawable.eyeon);
                    ismi=false;
                    et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    eye.setImageResource(R.drawable.eyeoff);
                    et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ismi=true;
                }
            }
        });
    }

    public void updatepwd(){
        Button bt=(Button)findViewById(R.id.edit7_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpwd=et1.getText().toString();
                newpwd=et2.getText().toString();
                if(oldpwd.equals(BaseApplication.app.getUser().getPwd()))
                {
                doPost();
                }
                else{;
                    Toast.makeText(getBaseContext(),"原密码错误！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void doPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/update_userpass", new Response.Listener<String>() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    String code=js.getString("Code");
                    if(code.equals("1")){
                        Toast.makeText(getBaseContext(),js.getString("Message"),Toast.LENGTH_SHORT).show();
                        BaseApplication.app.getUser().setPwd(newpwd);
                        SharedPreferences preferences=UpdatePwd.this.getSharedPreferences("shared",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("password",newpwd);
                        editor.commit();//提交数据
                        finish();
                    }else
                        Toast.makeText(getBaseContext(),js.getString("Message"),Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("s_password",newpwd);
        SlingleVolleyRequestQueue.getInstance(UpdatePwd.this).addToRequestQueue(spr);
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
