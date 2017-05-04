package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JKsheru extends AppCompatActivity {
    private NumberPicker numberPicker;
    private RadioGroup radioGroup;
    private Button button;
    private List<Yuansu> list_all;
    static boolean ischeck[]=new boolean[16];
    private Button bt1,bt2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11,bt12,bt13,bt14,bt15,bt16;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jksheru);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list_all=new ArrayList<>();
        getMydata();
        initopera();

    }
    public void initopera(){
        numberPicker=(NumberPicker)findViewById(R.id.numpicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(80);
        numberPicker.setValue(30);
        setNumberPickerDividerColor(numberPicker);
        //numberPicker.clearFocus();
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(numberPicker.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        radioGroup=(RadioGroup) findViewById(R.id.radiogroups);
        button=(Button)findViewById(R.id.sheru_bt);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.sheru_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt1=(Button) findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(!ischeck[0]) {
                        bt1.setBackgroundColor(0xff00aff0);
                        bt1.setTextColor(0xffffffff);
                        bt2.setBackgroundColor(0xff00aff0);
                        bt2.setTextColor(0xffffffff);
                        bt3.setBackgroundColor(0xff00aff0);
                        bt3.setTextColor(0xffffffff);
                        bt4.setBackgroundColor(0xff00aff0);
                        bt4.setTextColor(0xffffffff);
                        bt5.setBackgroundColor(0xff00aff0);
                        bt5.setTextColor(0xffffffff);
                        bt6.setBackgroundColor(0xff00aff0);
                        bt6.setTextColor(0xffffffff);
                        bt7.setBackgroundColor(0xff00aff0);
                        bt7.setTextColor(0xffffffff);
                        bt8.setBackgroundColor(0xff00aff0);
                        bt8.setTextColor(0xffffffff);
                        bt9.setBackgroundColor(0xff00aff0);
                        bt9.setTextColor(0xffffffff);
                        bt10.setBackgroundColor(0xff00aff0);
                        bt10.setTextColor(0xffffffff);
                        bt11.setBackgroundColor(0xff00aff0);
                        bt11.setTextColor(0xffffffff);
                        bt12.setBackgroundColor(0xff00aff0);
                        bt12.setTextColor(0xffffffff);
                        bt13.setBackgroundColor(0xff00aff0);
                        bt13.setTextColor(0xffffffff);
                        bt14.setBackgroundColor(0xff00aff0);
                        bt14.setTextColor(0xffffffff);
                        bt15.setBackgroundColor(0xff00aff0);
                        bt15.setTextColor(0xffffffff);
                        bt16.setBackgroundColor(0xff00aff0);
                        bt16.setTextColor(0xffffffff);
                        ischeck[0]=true;
                        for(int i=1;i<16;i++){
                            ischeck[i]=true;
                        }
                    }
                    else
                    {
                        bt1.setBackgroundColor(0xffe2e2e2);
                        bt1.setTextColor(0xff000000);
                        bt2.setBackgroundColor(0xffe2e2e2);
                        bt2.setTextColor(0xff000000);
                        bt3.setBackgroundColor(0xffe2e2e2);
                        bt3.setTextColor(0xff000000);
                        bt4.setBackgroundColor(0xffe2e2e2);
                        bt4.setTextColor(0xff000000);
                        bt5.setBackgroundColor(0xffe2e2e2);
                        bt5.setTextColor(0xff000000);
                        bt6.setBackgroundColor(0xffe2e2e2);
                        bt6.setTextColor(0xff000000);
                        bt7.setBackgroundColor(0xffe2e2e2);
                        bt7.setTextColor(0xff000000);
                        bt8.setBackgroundColor(0xffe2e2e2);
                        bt8.setTextColor(0xff000000);
                        bt9.setBackgroundColor(0xffe2e2e2);
                        bt9.setTextColor(0xff000000);
                        bt10.setBackgroundColor(0xffe2e2e2);
                        bt10.setTextColor(0xff000000);
                        bt11.setBackgroundColor(0xffe2e2e2);
                        bt11.setTextColor(0xff000000);
                        bt12.setBackgroundColor(0xffe2e2e2);
                        bt12.setTextColor(0xff000000);
                        bt13.setBackgroundColor(0xffe2e2e2);
                        bt13.setTextColor(0xff000000);
                        bt14.setBackgroundColor(0xffe2e2e2);
                        bt14.setTextColor(0xff000000);
                        bt15.setBackgroundColor(0xffe2e2e2);
                        bt15.setTextColor(0xff000000);
                        bt16.setBackgroundColor(0xffe2e2e2);
                        bt16.setTextColor(0xff000000);
                        ischeck[0]=false;
                        for(int i=1;i<16;i++){
                            ischeck[i]=false;
                        }
                    }

            }
        });

        bt2=(Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("===================c",ischeck[1]+"");
                if(!ischeck[1]) {
                    bt2.setBackgroundColor(0xff00aff0);
                    bt2.setTextColor(0xffffffff);
                    ischeck[1]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt2.setBackgroundColor(0xffe2e2e2);
                    bt2.setTextColor(0xff000000);
                    ischeck[1]=false;
                }
                Log.d("===================c2",ischeck[1]+"");
            }
        });

        bt3=(Button) findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[2]) {
                    bt3.setBackgroundColor(0xff00aff0);
                    bt3.setTextColor(0xffffffff);
                    ischeck[2]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt3.setBackgroundColor(0xffe2e2e2);
                    bt3.setTextColor(0xff000000);
                    ischeck[2]=false;
                }

            }
        });

        bt4=(Button) findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[3]) {
                    bt4.setBackgroundColor(0xff00aff0);
                    bt4.setTextColor(0xffffffff);
                    ischeck[3]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt4.setBackgroundColor(0xffe2e2e2);
                    bt4.setTextColor(0xff000000);
                    ischeck[3]=false;
                }

            }
        });

        bt5=(Button) findViewById(R.id.bt5);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[4]) {
                    bt5.setBackgroundColor(0xff00aff0);
                    bt5.setTextColor(0xffffffff);
                    ischeck[4]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt5.setBackgroundColor(0xffe2e2e2);
                    bt5.setTextColor(0xff000000);
                    ischeck[4]=false;
                }

            }
        });
        bt6=(Button) findViewById(R.id.bt6);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[5]) {
                    bt6.setBackgroundColor(0xff00aff0);
                    bt6.setTextColor(0xffffffff);
                    ischeck[5]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt6.setBackgroundColor(0xffe2e2e2);
                    bt6.setTextColor(0xff000000);
                    ischeck[5]=false;
                }

            }
        });

        bt7=(Button) findViewById(R.id.bt7);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[6]) {
                    bt7.setBackgroundColor(0xff00aff0);
                    bt7.setTextColor(0xffffffff);
                    ischeck[6]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt7.setBackgroundColor(0xffe2e2e2);
                    bt7.setTextColor(0xff000000);
                    ischeck[6]=false;
                }

            }
        });

        bt8=(Button) findViewById(R.id.bt8);
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[7]) {
                    bt8.setBackgroundColor(0xff00aff0);
                    bt8.setTextColor(0xffffffff);
                    ischeck[7]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt8.setBackgroundColor(0xffe2e2e2);
                    bt8.setTextColor(0xff000000);
                    ischeck[7]=false;
                }

            }
        });

        bt9=(Button) findViewById(R.id.bt9);
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[8]) {
                    bt9.setBackgroundColor(0xff00aff0);
                    bt9.setTextColor(0xffffffff);
                    ischeck[8]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt9.setBackgroundColor(0xffe2e2e2);
                    bt9.setTextColor(0xff000000);
                    ischeck[8]=false;
                }

            }
        });
        bt10=(Button) findViewById(R.id.bt10);
        bt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[9]) {
                    bt10.setBackgroundColor(0xff00aff0);
                    bt10.setTextColor(0xffffffff);
                    ischeck[9]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt10.setBackgroundColor(0xffe2e2e2);
                    bt10.setTextColor(0xff000000);
                    ischeck[9]=false;
                }

            }
        });
        bt11=(Button) findViewById(R.id.bt11);
        bt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[10]) {
                    bt11.setBackgroundColor(0xff00aff0);
                    bt11.setTextColor(0xffffffff);
                    ischeck[10]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt11.setBackgroundColor(0xffe2e2e2);
                    bt11.setTextColor(0xff000000);
                    ischeck[10]=false;
                }

            }
        });
        bt12=(Button) findViewById(R.id.bt12);
        bt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[11]) {
                    bt12.setBackgroundColor(0xff00aff0);
                    bt12.setTextColor(0xffffffff);
                    ischeck[11]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt12.setBackgroundColor(0xffe2e2e2);
                    bt12.setTextColor(0xff000000);
                    ischeck[11]=false;
                }

            }
        });

        bt13=(Button) findViewById(R.id.bt13);
        bt13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[12]) {
                    bt13.setBackgroundColor(0xff00aff0);
                    bt13.setTextColor(0xffffffff);
                    ischeck[12]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt13.setBackgroundColor(0xffe2e2e2);
                    bt13.setTextColor(0xff000000);
                    ischeck[12]=false;
                }

            }
        });
        bt14=(Button) findViewById(R.id.bt14);
        bt14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[13]) {
                    bt14.setBackgroundColor(0xff00aff0);
                    bt14.setTextColor(0xffffffff);
                    ischeck[13]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt14.setBackgroundColor(0xffe2e2e2);
                    bt14.setTextColor(0xff000000);
                    ischeck[13]=false;
                }

            }
        });

        bt15=(Button) findViewById(R.id.bt15);
        bt15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[14]) {
                    bt15.setBackgroundColor(0xff00aff0);
                    bt15.setTextColor(0xffffffff);
                    ischeck[14]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt15.setBackgroundColor(0xffe2e2e2);
                    bt15.setTextColor(0xff000000);
                    ischeck[14]=false;
                }

            }
        });
        bt16=(Button) findViewById(R.id.bt16);
        bt16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ischeck[15]) {
                    bt16.setBackgroundColor(0xff00aff0);
                    bt16.setTextColor(0xffffffff);
                    ischeck[15]=true;
                }
                else
                {
                    bt1.setBackgroundColor(0xffe2e2e2);
                    bt1.setTextColor(0xff000000);
                    ischeck[0]=false;
                    bt16.setBackgroundColor(0xffe2e2e2);
                    bt16.setTextColor(0xff000000);
                    ischeck[15]=false;
                }

            }
        });

        RadioButton radioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String sex="";
                if(radioButton.getText().equals("男")){
                    sex="1";
                }
                else{
                    sex="2";
                }
                String age=numberPicker.getValue()+"";

                String type="";


                    for(int i=1;i<16;i++){
                        if(ischeck[i])
                            type=type+list_all.get(i-1).getId()+",";
                    }

                    if(type.equals("")){
                        for(int j=0;j<list_all.size();j++)
                            type=type+list_all.get(j).getId()+",";
                    }

                type=type.substring(0,type.length()-1);

                Log.d("=============s",type);

                Intent intent=new Intent(JKsheru.this,JKsheruResult.class);
                intent.putExtra("age",age);
                intent.putExtra("sex",sex);
                intent.putExtra("type",type);
                JKsheru.this.startActivity(intent);

            }
        });

    }


    public void getMydata(){
        StringRequest request=new StringRequest(URLMannager.YuanSu_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja=js2.getJSONArray("trophictype");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        Yuansu ys=new Yuansu();
                        ys.setId(jo.getString("id"));
                        ys.setTitle(jo.getString("title"));
                        list_all.add(ys);
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
        SlingleVolleyRequestQueue.getInstance(JKsheru.this).addToRequestQueue(request);
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(this.getResources().getColor(R.color.mgray)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
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
