package com.example.administrator.vegetarians824.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016-08-25.
 * ViewPager适配器
 */

public class VPAdapter extends PagerAdapter {
    private List<View> myData;
    public VPAdapter(List<View> mydData){
        this.myData=mydData;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v=myData.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(myData.get(position));
    }

}
