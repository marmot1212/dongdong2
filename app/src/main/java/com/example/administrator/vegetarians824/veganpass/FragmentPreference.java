package com.example.administrator.vegetarians824.veganpass;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPreference extends Fragment {
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TextView tv1,tv2,tv3;
    private String en_cn;
    private SharedPreferences pre;

    public FragmentPreference() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_fragment_preference, container, false);
        pre = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        en_cn=pre.getString("languagetype","");
        fm=getActivity().getSupportFragmentManager();
        ft=fm.beginTransaction();
        ft.replace(R.id.editfavour_content,new MyPreference());
        ft.commit();
        initView(v);
        return v;
    }
    public void initView(View v){
        tv1=(TextView)v.findViewById(R.id.editfavour_tv1);
        tv2=(TextView)v.findViewById(R.id.editfavour_tv2);
        tv3=(TextView)v.findViewById(R.id.editfavour_tv3);
        if(en_cn.equals("cn")){
            tv1.setText("偏好");
            tv2.setText("忌口");
            tv3.setText("过敏");
        }else {
            tv1.setText("My preference");
            tv2.setText("My abstinence");
            tv3.setText("My allergy");
        }
        //偏好
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv1.setBackgroundResource(R.drawable.button_bg01);
                tv1.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyPreference());
                ft.commit();
            }
        });
        //忌口
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv2.setBackgroundResource(R.drawable.button_bg01);
                tv2.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyAvoid());
                ft.commit();
            }
        });
        //过敏
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTextView();
                tv3.setBackgroundResource(R.drawable.button_bg01);
                tv3.setTextColor(0xff333333);
                ft=fm.beginTransaction();
                ft.replace(R.id.editfavour_content,new MyAllergy());
                ft.commit();
            }
        });

    }
    public void initTextView(){
        tv1.setBackground(null);
        tv1.setTextColor(0xffffffff);
        tv2.setBackground(null);
        tv2.setTextColor(0xffffffff);
        tv3.setBackground(null);
        tv3.setTextColor(0xffffffff);
    }
}
