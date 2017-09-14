package com.chen.wakehao.easybottomtab;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by WakeHao on 2016/11/16.
 * 带小红点的底部tab控件
 * <p>
 * 通过设置DotView的的触摸事件
 */

public class EzTabItemWithDot extends FrameLayout {
    private DotView mDotView;
    private EzTabItem mEzTabItem;

    public EzTabItemWithDot(Context context, int measuredWidth, @DrawableRes int unSelectorImage_res,
                            @DrawableRes int selectorImage_res, String text_content, int hideMode) {
        super(context);
        mEzTabItem = new EzTabItem(context, measuredWidth, unSelectorImage_res, selectorImage_res, text_content, hideMode);
        mDotView = new DotView(context);
        init();
    }

    /**
     * 初始化DotView和EzTabItem
     */
    private void init() {
        addView(mEzTabItem);
        addView(mDotView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDotView.layout(dotLeft, dotTop, dotLeft + mDotView.getWidth(), dotTop + mDotView.getHeight());
    }

    /**
     * 设置显示的数字
     *
     * @param num 0不显示 -1显示小点 >99显示99+
     */
    public void showNum(int num) {
        hasMesPoint = true;
        mDotView.showNum(num);
    }

    //是否有红点
    private boolean hasMesPoint;
    private int dotLeft;
    private int dotTop;

    /**
     * 改变DotView的位置
     *
     * @param left
     * @param top
     */
    public void correctDotViewPosition(int left, int top) {
        if (!hasMesPoint) return;
        dotLeft = left;
        dotTop = top;
        mDotView.layout(left, top, left + mDotView.getWidth(), top + mDotView.getHeight());
    }

    public void disMissMes() {
        hasMesPoint = false;
        mDotView.disMisMes();
    }


}
