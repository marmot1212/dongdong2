package com.example.administrator.vegetarians824.veganpass;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends Fragment {
    private String en_cn,type;
    private SharedPreferences pre;
    private TextView tv1,tv2,tv3,tv4;
    private TextView bt1,bt2,bt3,bt4,bt5;
    private TextView des1,des2,des3;
    private TextView setting1,setting2,bartitle;
    private CustomToast customToast;
    public FragmentSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_fragment_setting, container, false);
        pre=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        en_cn=pre.getString("languagetype","");
        type=pre.getString("type","");
        customToast=new CustomToast();
        initView(v);
        return v;
    }
    public void initView(View v){
        tv1=(TextView)v.findViewById(R.id.setting_title1);
        tv2=(TextView)v.findViewById(R.id.setting_title2);
        tv3=(TextView)v.findViewById(R.id.setting_title3);
        tv4=(TextView)v.findViewById(R.id.setting_title4);
        bt1=(TextView) v.findViewById(R.id.setting_bt1);
        bt2=(TextView)v.findViewById(R.id.setting_bt2);
        bt3=(TextView)v.findViewById(R.id.setting_bt3);
        bt4=(TextView)v.findViewById(R.id.setting_bt4);
        bt5=(TextView)v.findViewById(R.id.setting_bt5);
        des1=(TextView)v.findViewById(R.id.setting_des1);
        des2=(TextView)v.findViewById(R.id.setting_des2);
        des3=(TextView)v.findViewById(R.id.setting_des3);
        if(en_cn.equals("en")){
            bt1.setTextColor(0xffffffff);
            bt1.setBackgroundResource(R.drawable.button_bg02);
        }else {
            bt2.setTextColor(0xffffffff);
            bt2.setBackgroundResource(R.drawable.button_bg02);
        }
        switch (type){
            case "纯素":
                bt3.setTextColor(0xffffffff);
                bt3.setBackgroundResource(R.drawable.button_bg02);
                des1.setVisibility(View.VISIBLE);
                break;
            case "Vegan":
                bt3.setTextColor(0xffffffff);
                bt3.setBackgroundResource(R.drawable.button_bg02);
                des1.setVisibility(View.VISIBLE);
                break;
            case "净素":
                bt4.setTextColor(0xffffffff);
                bt4.setBackgroundResource(R.drawable.button_bg02);
                des2.setVisibility(View.VISIBLE);
                break;
            case "Vuddhist Vegan":
                bt4.setTextColor(0xffffffff);
                bt4.setBackgroundResource(R.drawable.button_bg02);
                des2.setVisibility(View.VISIBLE);
                break;
            case "蛋奶素":
                bt5.setTextColor(0xffffffff);
                bt5.setBackgroundResource(R.drawable.button_bg02);
                des3.setVisibility(View.VISIBLE);
                break;
            case "Ovo-Lacto Vegetarian":
                bt5.setTextColor(0xffffffff);
                bt5.setBackgroundResource(R.drawable.button_bg02);
                des3.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
        setLanguage();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt1.setTextColor(0xffffffff);
                bt1.setBackgroundResource(R.drawable.button_bg02);
                bt2.setBackgroundResource(R.drawable.button_bg01);
                bt2.setTextColor(0xff333333);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("languagetype","en");
                editor.apply();//提交数据
                en_cn="en";
                setLanguage();
                customToast.cancel();
                customToast.showToast(getContext(), "English");
                setting1.setText("Preference");
                setting2.setText("Setting");
                bartitle.setText("Setting");
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt2.setTextColor(0xffffffff);
                bt2.setBackgroundResource(R.drawable.button_bg02);
                bt1.setBackgroundResource(R.drawable.button_bg01);
                bt1.setTextColor(0xff333333);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("languagetype","cn");
                editor.apply();//提交数据
                en_cn="cn";
                setLanguage();
                customToast.cancel();
                customToast.showToast(getContext(), "中文");
                setting1.setText("偏好设置");
                setting2.setText("其他设置");
                bartitle.setText("设置");
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbutton(bt3,bt4,bt5);
                bt3.setTextColor(0xffffffff);
                bt3.setBackgroundResource(R.drawable.button_bg02);
                des1.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("type","纯素");
                editor.apply();//提交数据
                if(en_cn.equals("cn")){
                    customToast.cancel();
                    customToast.showToast(getContext(), "纯素");
                }else {
                    customToast.cancel();
                    customToast.showToast(getContext(), "Vegan");
                }
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbutton(bt3,bt4,bt5);
                bt4.setTextColor(0xffffffff);
                bt4.setBackgroundResource(R.drawable.button_bg02);
                des2.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("type","净素");
                editor.apply();//提交数据
                if(en_cn.equals("cn")){
                    customToast.cancel();
                    customToast.showToast(getContext(), "净素");
                }else {
                    customToast.cancel();
                    customToast.showToast(getContext(), "Vuddhist Vegan");
                }
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearbutton(bt3,bt4,bt5);
                bt5.setTextColor(0xffffffff);
                bt5.setBackgroundResource(R.drawable.button_bg02);
                des3.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor=pre.edit();
                editor.putString("type","蛋奶素");
                editor.apply();//提交数据
                if(en_cn.equals("cn")){
                    customToast.cancel();
                    customToast.showToast(getContext(), "蛋奶素");
                }else {
                    customToast.cancel();
                    customToast.showToast(getContext(), "Ovo-lacto vegetarianism");
                }
            }
        });
    }
    //初始化所有button
    public void clearbutton(View...views){
        for(View view:views){
            ((TextView)view).setBackgroundResource(R.drawable.button_bg01);
            ((TextView)view).setTextColor(0xff333333);
        }
        des1.setVisibility(View.GONE);
        des2.setVisibility(View.GONE);
        des3.setVisibility(View.GONE);
    }

    public void setLanguage(){
        if(en_cn.equals("cn")){
            tv1.setText("语言设置");
            tv2.setText("选择你的语言类型");
            bt3.setText("纯素");
            bt4.setText("净素");
            bt5.setText("蛋奶素");
            des1.setText("我是一名纯素者，我不吃任何肉类、蛋奶、海鲜、蜂蜜以及任何动物制品。");
            des2.setText("我是一名素食者，我不吃任何肉类、蛋奶、海鲜和任何动物制品，同时也不吃葱、蒜、洋葱、韭菜。");
            des3.setText("我是一名素食者，我不吃任何肉类、海鲜等，但我吃鸡蛋、奶类、蜂蜜。");
            tv3.setText("关于我们");
            tv4.setText("素活团队\nwajie007@163.com");
        }else {
            tv1.setText("Language Setting");
            tv2.setText("Choose your diet");
            bt3.setText("Vegan");
            bt4.setText("Vuddhist Vegan");
            bt5.setText("Ovo-Lacto Vegetarian");
            des1.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee.");
            des2.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, including eggs and bee. Including egg, diary product and bee. Especially, I don’t eat food containing Chinese onions, garlic, Chinese chives, rocambole, and onions.");
            des3.setText("I don’t eat fish, seafood, poultry, meat and any other animal product, not including eggs and bee.");
            tv3.setText("About us");
            tv4.setText("Team Su huo\nwajie007@163.com");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setting1=(TextView) activity.findViewById(R.id.setting_tv1);
        setting2=(TextView) activity.findViewById(R.id.setting_tv2);
        bartitle=(TextView)activity.findViewById(R.id.setting_bartitle);
    }
}
