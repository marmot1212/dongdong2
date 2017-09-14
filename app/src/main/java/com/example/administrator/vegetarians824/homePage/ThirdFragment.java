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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdFragment extends Fragment {

    @Bind(R.id.home_01)
    LinearLayout module1;
    @Bind(R.id.home_02)
    LinearLayout module2;
    @Bind(R.id.home_03)
    LinearLayout module3;
    @Bind(R.id.home_04)
    LinearLayout module4;
    @Bind(R.id.home_05)
    LinearLayout module5;
    @Bind(R.id.home_06)
    LinearLayout module6;
    @Bind(R.id.home_07)
    LinearLayout module7;
    @Bind(R.id.home_08)
    LinearLayout module8;
    @Bind(R.id.ditu_xiangqing_viewpager2)
    ViewPager advPager;
    @Bind(R.id.ditu_xiangqing_viewGroup2)
    LinearLayout group;
    // group 轮播小圆点；advPager ViewPager

    public ThirdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_third, container, false);
        ButterKnife.bind(this, v);

        /**
         * CyclePager是轮播工具类？？？
         */
        CyclePager cyclePager = new CyclePager(advPager, group, getContext());
        cyclePager.init("4");

        initListener(v);
        return v;
    }

    public void initListener(View v) {

        module1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomePage.class);
                getActivity().startActivity(intent);
            }
        });
        module2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JKyuansu.class);
                getActivity().startActivity(intent);
            }
        });

        module3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JKshuguo.class);
                getActivity().startActivity(intent);
            }
        });

        module4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Trip.class);
                getActivity().startActivity(intent);
            }
        });

        module5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Wenda.class);
                getActivity().startActivity(intent);
            }
        });

        module6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArticleList.class);
                getActivity().startActivity(intent);
            }
        });

        module7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyCalender.class);
                getActivity().startActivity(intent);
            }
        });

        module8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JKlingshi.class);
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
