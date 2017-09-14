package com.example.administrator.vegetarians824.homePage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.article.ArticleList;
import com.example.administrator.vegetarians824.dongdong.JKlingshi;
import com.example.administrator.vegetarians824.dongdong.JKshuguo;
import com.example.administrator.vegetarians824.dongdong.JKyuansu;
import com.example.administrator.vegetarians824.dongdong.MyCalender;
import com.example.administrator.vegetarians824.dongdong.Trip;
import com.example.administrator.vegetarians824.dongdong.Wenda;
import com.example.administrator.vegetarians824.myView.CyclePager;
import com.example.administrator.vegetarians824.veganpass.WelcomePage;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {

    private LinearLayout module1,module2,module3,module4,module5,module6,module7,module8;
    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_third, container, false);
        LinearLayout group = (LinearLayout)v.findViewById(R.id.ditu_xiangqing_viewGroup2);// 展示小圆点
        ViewPager advPager = (ViewPager)v.findViewById(R.id.ditu_xiangqing_viewpager2);// ViewPager
        CyclePager cyclePager=new CyclePager(advPager,group,getContext());
        cyclePager.init("4");
        initView(v);
        return v;
    }

    public void initView(View v){
        module1=(LinearLayout)v.findViewById(R.id.home_01);
        module2=(LinearLayout)v.findViewById(R.id.home_02);
        module3=(LinearLayout)v.findViewById(R.id.home_03);
        module4=(LinearLayout)v.findViewById(R.id.home_04);
        module5=(LinearLayout)v.findViewById(R.id.home_05);
        module6=(LinearLayout)v.findViewById(R.id.home_06);
        module7=(LinearLayout)v.findViewById(R.id.home_07);
        module8=(LinearLayout)v.findViewById(R.id.home_08);

        module1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),WelcomePage.class);
                getActivity().startActivity(intent);
            }
        });
        module2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),JKyuansu.class);
                getActivity().startActivity(intent);
            }
        });

        module3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),JKshuguo.class);
                getActivity().startActivity(intent);
            }
        });

        module4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Trip.class);
                getActivity().startActivity(intent);
            }
        });

        module5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Wenda.class);
                getActivity().startActivity(intent);
            }
        });

        module6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ArticleList.class);
                getActivity().startActivity(intent);
            }
        });

        module7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MyCalender.class);
                getActivity().startActivity(intent);
            }
        });

        module8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),JKlingshi.class);
                getActivity().startActivity(intent);
            }
        });

    }
}
