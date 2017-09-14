package com.chen.wakehao.easybottomtab;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.andexert.library.RippleView;
import com.chen.wakehao.library.R;

/**
 * Created by WakeHao on 2016/11/4.
 * 底部tab栏
 */

public class EzTabItem extends View {
    private Context mContext;
    private int IMAGE_MARIN_TOP = 8;
    private int TEXTSIZE = 12;
    //是否点击字体隐藏
    private boolean isHideText = false;
    //传过来的宽度
    private int measuredWidth;
    //设置的图片
    private
    @DrawableRes
    int image_res;
    //设置的文字
    private String text_content;

    private Paint mPaint;

    //点击隐藏模式 0：标准md点击图片和字体微调 1：点击没变化 2：点击底部隐藏
    private int MODE_HIDE = 0;

    //隐藏模式的偏移量
    private int offset = 20;

    private float downX;
    private float downY;
    private float radiusMax;
    private int rippleRate = 10;
    private int rippleDuration = 200;
    private boolean isRippleDrawing;
    //上一个水纹是否结束
    private boolean isBeforEnding = true;
    private Paint ripplePaint;
    private int rippleAlpha = 60;
    private
    @ColorInt
    int mPaintColor;
    private
    @ColorInt
    int ripplePaintColor;
    private int rippleTime;

    private int lastClickPosition = 0;

    private Handler mHandler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshView();
        }
    };

    //点击是否是同一个图案,默认是
    private boolean isBitmapDiff;
    private
    @DrawableRes
    int selectorImage_res;
    private
    @DrawableRes
    int unSelectorImage_res;
    private int middleX;
    private int startX;

    private boolean isItemSelected;

    private Paint mTextPaint;
    public EzTabItem(Context context, int measuredWidth, @DrawableRes int unSelectorImage_res,
                     @DrawableRes int selectorImage_res, String text_content, int hideMode) {
        super(context);
        if (selectorImage_res != 0) isBitmapDiff = true;
        this.selectorImage_res = selectorImage_res;
        this.MODE_HIDE = hideMode;
        this.mContext = context;
        this.measuredWidth = measuredWidth;
        this.unSelectorImage_res = unSelectorImage_res;
        this.text_content = text_content;
        //由于在该view中宽一定比长 长所以圆环的半径最大值为宽
        radiusMax = measuredWidth;
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        middleX = measuredWidth / 2 - Utils.dip2px(mContext, 12);
        startX = middleX;
        image_res = unSelectorImage_res;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        ripplePaint = new Paint();
        ripplePaint.setStyle(Paint.Style.FILL);
        ripplePaint.setAntiAlias(true);
    }

    /**
     * 设置被点击颜色
     *
     * @param selectorColor
     * @return
     */
    public void setSelectorColor(@ColorInt int selectorColor) {
        mPaintColor = selectorColor;
    }

    /**
     * 设置水纹颜色
     *
     * @param rippleColor
     */
    public void setRippleColor(@ColorInt int rippleColor) {
        ripplePaintColor = rippleColor;
    }

    /**
     * 设置未选中的颜色
     *
     * @param
     * @return
     */
    public void setUnSelectorColor(@ColorInt int unSelectorColor) {
        mPaintColor = unSelectorColor;
//        mPaint.setColor(unSelectorColor);
    }

    private Bitmap resToBitmap(@DrawableRes int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);.\
        //固定高为56dp，宽为1/n屏幕
        setMeasuredDimension(measuredWidth, Utils.dip2px(mContext, 56));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mPaintColor);
        ripplePaint.setColor(ripplePaintColor);
        ripplePaint.setAlpha(rippleAlpha);

        if (MODE_HIDE == 2) {
            IMAGE_MARIN_TOP = 16;
            if (isSelected()) {
                IMAGE_MARIN_TOP = 6;
                drawText(canvas);
            }
            drawIcon(canvas);

            //这个模式水纹效果是viewgroup绘制全局的
            //仅在有小红点的时候更新
            correctionDotViewPosition(startX + Utils.dip2px(mContext, 24) - Utils.dip2px(mContext, 5), Utils.dip2px(mContext, IMAGE_MARIN_TOP) - Utils.dip2px(mContext, 5));
            return;
        } else {
            drawIcon(canvas);
            drawText(canvas);
        }
        correctionDotViewPosition(startX + Utils.dip2px(mContext, 24) - Utils.dip2px(mContext, 5), Utils.dip2px(mContext, IMAGE_MARIN_TOP) - Utils.dip2px(mContext, 5));
        if(!isRipple)return;
        //点击才开始绘制ripple
        if (isRippleDrawing) {
            //已经绘制最大圆环 停止继续绘制
            if (rippleDuration <= rippleTime * rippleRate) {
                isRippleDrawing = false;
                isBeforEnding = true;
                rippleTime = 0;
                refreshView();
            } else {
                mHandler.postDelayed(runnable, 10);
                isBeforEnding = false;
            }
            canvas.drawCircle(downX, downY, radiusMax * (((float) rippleTime * rippleRate) / rippleDuration), ripplePaint);
            rippleTime++;
        }
    }

    /**
     * 当位置变化的时候 告诉父容器改变DotView的位置
     */
    private void correctionDotViewPosition(int left, int top) {
        ((EzTabItemWithDot) getParent()).correctDotViewPosition(left, top);
    }

    /**
     * 绘画文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        if(mTextPaint.getColor()!=mPaintColor)mTextPaint.setColor(mPaintColor);
        //paint set单位是px，而TextView.setTextSize单位是sp，get是px
        mTextPaint.setTextSize(Utils.sp2px(mContext, TEXTSIZE));
        Rect text_bound = new Rect();
        mTextPaint.getTextBounds(text_content, 0, text_content.length(), text_bound);
        if (MODE_HIDE == 2 && ((int) getTag()) == 0)
            canvas.drawText(text_content, getWidth() / 2 - text_bound.width() / 2 + offset, Utils.dip2px(mContext, 46), mTextPaint);
        else
            canvas.drawText(text_content, getWidth() / 2 - text_bound.width() / 2, Utils.dip2px(mContext, 46), mTextPaint);
    }

    /**
     * 绘画图标
     *
     * @param canvas
     */
    private void drawIcon(Canvas canvas) {
        Bitmap bitmap = resToBitmap(image_res);
        if (!isBitmapDiff) changeBitmapColor(mPaintColor);

        //图片完整绘制
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //计算绘制的中间点

        //图片scale处理
        RectF rectF = new RectF(startX, Utils.dip2px(mContext, IMAGE_MARIN_TOP)
                , Utils.dip2px(mContext, 24) + startX, Utils.dip2px(mContext, 24) + Utils.dip2px(mContext, IMAGE_MARIN_TOP));
        canvas.drawBitmap(bitmap, rect, rectF, mPaint);
    }

    private void drawIcon(Canvas canvas, int startX) {
        Bitmap bitmap = resToBitmap(image_res);
        if (!isBitmapDiff) changeBitmapColor(mPaintColor);

        //图片完整绘制
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        //计算绘制的中间点

        //图片scale处理
        RectF rectF = new RectF(startX, Utils.dip2px(mContext, IMAGE_MARIN_TOP)
                , Utils.dip2px(mContext, 24) + startX, Utils.dip2px(mContext, 24) + Utils.dip2px(mContext, IMAGE_MARIN_TOP));
        canvas.drawBitmap(bitmap, rect, rectF, mPaint);
    }

    private void changeBitmapColor(@ColorInt int color) {
        ColorFilter filter = new LightingColorFilter(color, 1);
        mPaint.setColorFilter(filter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnSelector != null) mOnSelector.isSelector((Integer) getTag());
                //连续点击不绘制
                if (isBeforEnding) startWave(event);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开启水纹效果
     *
     * @param event
     */
    private void startWave(MotionEvent event) {
        downX = event.getX();
        downY = event.getY();
        isRippleDrawing = true;
        refreshView();
    }

    public void setRippleAlpha(int rippleAlpha) {
        this.rippleAlpha = rippleAlpha;
    }

    private boolean isRipple=true;

    public void setIsRipple(boolean isRipple) {
        this.isRipple=isRipple;
    }

    interface OnSelector {
        void isSelector(int position);
    }

    private OnSelector mOnSelector;

    public void setmOnSelectorListenter(OnSelector mOnSelector) {
        this.mOnSelector = mOnSelector;
    }

    public void onSelector(@ColorInt int selectedColor) {
        setSelected(true);
        if (isBitmapDiff) {
            image_res = selectorImage_res;
        }
        mPaintColor = selectedColor;
        switch (MODE_HIDE) {
            case 0:
                changeSize();
                break;
            case 1:
                IMAGE_MARIN_TOP = 8;
                TEXTSIZE = 12;
                break;
            case 2:
                changeSize();
                break;
        }
        jugeMoveWay(true);
        refreshView();
    }

    public void onSelector(@ColorInt int selectedColor, int position) {
        this.onSelector(selectedColor);
    }

    //当点击一个item，其他item获得被点击的item的位置
    public void onUnSelector(@ColorInt int unSelectedColor, int position) {
        lastClickPosition = position;
        this.onUnSelector(unSelectedColor);
    }

    public void onUnSelector(@ColorInt int unSelectedColor) {
        setSelected(false);

        if (isBitmapDiff) {
            image_res = unSelectorImage_res;
        }
        mPaintColor = unSelectedColor;
        IMAGE_MARIN_TOP = 8;
        TEXTSIZE = 12;
        jugeMoveWay(false);
        refreshView();
    }

    private void refreshView()
    {
        if(Looper.getMainLooper()==Looper.myLooper())invalidate();
        else postInvalidate();
    }

    private void changeSize() {
        IMAGE_MARIN_TOP = 6;
        TEXTSIZE = 12;
    }

    public void setItemSelected(boolean isSelected, int selectedColor) {
        isItemSelected = isSelected;
        onSelector(selectedColor);
    }

    public boolean isItemSelected() {
        return isItemSelected;
    }

    //中间的item拥有三个状态
    public void leftStatus() {
        startX = middleX - offset;
    }

    public void middleStatus() {
        startX = middleX;
    }

    public void rightStatus() {
        startX = middleX + offset;
    }

    private void jugeMoveWay(boolean isSelected) {
        if (MODE_HIDE != 2) return;
        //最左边
        if (((int) getTag()) == 0) {
            if (isSelected) rightStatus();
            else middleStatus();
        } else {
            if (isSelected) middleStatus();
            else {
                if (lastClickPosition > ((int) getTag())) leftStatus();
                else rightStatus();
            }
        }
    }

    public void setLastClickPosition(int lastClickPosition) {
        this.lastClickPosition = lastClickPosition;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void onDestroy()
    {
        if(mHandler!=null)mHandler.removeCallbacksAndMessages(null);
        mContext=null;
    }
}
