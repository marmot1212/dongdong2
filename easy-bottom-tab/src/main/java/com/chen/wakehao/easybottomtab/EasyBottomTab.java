package com.chen.wakehao.easybottomtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.chen.wakehao.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WakeHao on 2016/11/1.
 */

public class EasyBottomTab extends LinearLayout {
    private Context mContext;
    //底部tab的数量，默认为0
    private int mItemNum = 0;
    //存放图片资源的集合(未点击)
    private List<Integer> mBitmapsResourseList = new ArrayList<>();
    //存放图片资源的集合（点击）
    private List<Integer> mSelectorBitmapsResourseList = new ArrayList<>();
    //存放文字资源的集合
    private List<String> mTextList = new ArrayList<>();
    //设备宽
    private int deviceWidth;
    //该底部悬浮高度
    private final int M_ELEVATION = 6;
    //背景色
    private
    @ColorInt
    int backgroundColor;

    private List<Integer> colorLists;
    //被选中的item位置
    private int currentPosition;

    //存放fragment集合
    private List<Fragment> list_fragment;

    private
    @ColorInt
    int rippleColor;

    private
    @ColorInt
    int primaryColor;

    private int rippleAlpha;

    private int changeMode;

    private
    @ColorInt
    int selectedColor;

    private
    @ColorInt
    int unSelectedColor;
    //是否开启水纹效果
    private boolean isRipple;

    /**
     * hide模式下设置点击背景变换的颜色
     *
     * @param color
     */
    public EasyBottomTab setItemSelectedBackGroundColor(@ColorInt int color) {
        if (changeMode != 2) return this;
        if (colorLists == null) colorLists = new ArrayList<>();
        colorLists.add(color);
        return this;
    }

    public void disMissMes(int position) {
        ((EzTabItemWithDot) getChildAt(position)).disMissMes();
    }

    /**
     * 只有在显示消息的时候初始化 dragview BoomImageView并在拖拽完成后移除销毁
     *
     * @param position
     * @param num
     */
    public void showNumMes(int position, int num) {
        ((EzTabItemWithDot) getChildAt(position)).showNum(num);
    }

    public EasyBottomTab setRippleColor(@ColorInt int rippleColor) {
        this.rippleColor = rippleColor;
        return this;
    }

    public EasyBottomTab setRippleAlpha(int alpha) {
        this.rippleAlpha = alpha;
        return this;
    }

    //    private enum TabType{
//        //类似qq微信普通形式 （默认）
//        Normal(0),
//        //类似于微博简书带特殊图案
//        Add(1);
//        private int type;
//        TabType(int type)
//        {
//            this.type=type;
//        }
//    }
    public enum ChangeMode {
        //默认MD规范
        MATERIAL(0),
        //点击形状不做改变 类似微信只改变颜色
        NOCHANGE(1),
        //点击隐藏字体
        HIDE(2);
        private int changeMode;

        ChangeMode(int changeMode) {
            this.changeMode = changeMode;
        }
    }


    public EasyBottomTab(Context context) {
        this(context, null);
    }

    public EasyBottomTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyBottomTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAppColorPrimary();
        getAttrsValues(attrs);
        init();
    }

    /**
     * 获取xml设置的值
     *
     * @param attrs
     */
    private void getAttrsValues(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.EasyBottomTab);
        backgroundColor = typedArray.getColor(R.styleable.EasyBottomTab_tabBackGroundColor, Color.WHITE);
        //默认primaryColor
        rippleColor = typedArray.getColor(R.styleable.EasyBottomTab_rippleColor, primaryColor);

        rippleAlpha = typedArray.getInteger(R.styleable.EasyBottomTab_rippleAlpha, 60);

        if (typedArray.hasValue(R.styleable.EasyBottomTab_changeMode))
            changeMode = typedArray.getInt(R.styleable.EasyBottomTab_changeMode, 0);

        selectedColor = typedArray.getColor(R.styleable.EasyBottomTab_selectedColor, primaryColor);

        unSelectedColor = typedArray.getColor(R.styleable.EasyBottomTab_unSelectedColor, Color.GRAY);

        isRipple=typedArray.getBoolean(R.styleable.EasyBottomTab_isRipple,true);

        typedArray.recycle();

    }

    private void init() {
        setOrientation(HORIZONTAL);
        setWeightSum(mItemNum);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(M_ELEVATION);
        }
        deviceWidth = getResources().getDisplayMetrics().widthPixels;
        if (backgroundColor != 0) this.setBackgroundColor(backgroundColor);
    }


    private void getAppColorPrimary() {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;
    }

    //是否显示同一张图片 三种hide模式 测量精确 2*3*2种case
    private void initChildView() {
        EzTabItemWithDot ezTabItem;
        int measuredWidth = deviceWidth / mItemNum;
        if (deviceWidth % mItemNum != 0) {
            int remain = deviceWidth % mItemNum;
            //用来校正不能整除的问题
            for (int i = 0; i < mItemNum; i++) {
                if (--remain >= 0)
                    ezTabItem = new EzTabItemWithDot(mContext, measuredWidth + 1, mBitmapsResourseList.get(i), mSelectorBitmapsResourseList.get(i), mTextList.get(i), changeMode);
                else
                    ezTabItem = new EzTabItemWithDot(mContext, measuredWidth, mBitmapsResourseList.get(i), mSelectorBitmapsResourseList.get(i), mTextList.get(i), changeMode);
                tabItemSet(ezTabItem, i);
            }
            return;
        }
        for (int i = 0; i < mItemNum; i++) {
            ezTabItem = new EzTabItemWithDot(mContext, measuredWidth, mBitmapsResourseList.get(i), mSelectorBitmapsResourseList.get(i), mTextList.get(i), changeMode);
            tabItemSet(ezTabItem, i);
        }
    }

    private void tabItemSet(EzTabItemWithDot ezTabItemWithDot, int tag) {
        EzTabItem ezTabItem = (EzTabItem) ezTabItemWithDot.getChildAt(0);
        ezTabItem.setTag(tag);
        ezTabItem.setRippleColor(rippleColor);
        ezTabItem.setSelectorColor(selectedColor);
//        ezTabItem.setUnSelectorColor(unSelectedColor);
        ezTabItem.setRippleAlpha(rippleAlpha);
        ezTabItem.setIsRipple(isRipple);
//        if(tag==mItemNum)ezTabItem.setIsRightEdge(true);
        addView(ezTabItemWithDot);
        setListener(ezTabItem);
        if (tag == 0)
        {
            selected(0, changeMode == 2, 0);
            if(colorLists!=null)setBackgroundColor(colorLists.get(0));
        }
        else unSelected(tag, changeMode == 2, 0);
    }

    private void setListener(EzTabItem ezTabItem) {
        ezTabItem.setmOnSelectorListenter(new EzTabItem.OnSelector() {
            @Override
            public void isSelector(int position) {
                currentPosition = position;
                for (int i = 0; i < mItemNum; i++) {
                    if (i == position) selected(i, changeMode == 2, position);
                    else unSelected(i, changeMode == 2, position);
                }
            }
        });
    }

    //记录上一次点击的位置 用于和本次比较判读是否重复点击
    private int lastClickPosition = 0;

    /**
     * 设置选中
     *
     * @param selected 被选中的item
     * @param isHide   hide模式下要传上个点击position
     */
    private void selected(int selected, boolean isHide, int position) {
        //外部提供的接口
        if (mOnItemTabSelectedListener != null && selected == lastClickPosition)
            mOnItemTabSelectedListener.onSelectedAgain(selected);
        if (mOnItemTabSelectedListener != null) mOnItemTabSelectedListener.onSelected(selected);

        if (isHide)
            (((EzTabItem) ((EzTabItemWithDot) getChildAt(selected)).getChildAt(0))).onSelector(selectedColor, position);
        else
            (((EzTabItem) ((EzTabItemWithDot) getChildAt(selected)).getChildAt(0))).onSelector(selectedColor);
        lastClickPosition = selected;
    }

    /**
     * 设置未选中
     *
     * @param unSelected
     * @param isHide
     * @param position
     */
    private void unSelected(int unSelected, boolean isHide, int position) {
        if (isHide)
            (((EzTabItem) ((EzTabItemWithDot) getChildAt(unSelected)).getChildAt(0))).onUnSelector(unSelectedColor, position);
        else
            (((EzTabItem) ((EzTabItemWithDot) getChildAt(unSelected)).getChildAt(0))).onUnSelector(unSelectedColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        MeasureSpec measureSpecHeight = new MeasureSpec();
        //将56dp手动转为measureSpec格式
        int measuredHeight = measureSpecHeight.makeMeasureSpec(Utils.dip2px(mContext, 56), MeasureSpec.EXACTLY);

        MeasureSpec measureSpecWidth = new MeasureSpec();
        int measuredWidth = measureSpecWidth.makeMeasureSpec(this.getResources().getDisplayMetrics().widthPixels, MeasureSpec.EXACTLY);

        //不管布局文件怎么设置，固定宽为match，高为56dp
        super.onMeasure(measuredWidth, measuredHeight);
    }

    /**
     * 添加一个tab
     *
     * @param resId
     * @param text
     */
    public EasyBottomTab addItem(@DrawableRes int resId, @NonNull String text) {
        mBitmapsResourseList.add(resId);
        mSelectorBitmapsResourseList.add(0);
        mTextList.add(text);
        mItemNum++;
        return this;
    }

    /**
     * @param selecterId   选中时的图案
     * @param unSelecterId 未被选中的图案
     * @param text
     * @return
     */
    public EasyBottomTab addItem(@DrawableRes int unSelecterId, @DrawableRes int selecterId, @NonNull String text) {
        mSelectorBitmapsResourseList.add(selecterId);
        mBitmapsResourseList.add(unSelecterId);
        mTextList.add(text);
        mItemNum++;
        return this;
    }

    /**
     * 带fragment的构造方法
     *
     * @param resId
     * @param text
     * @param fragment
     * @return
     */
    public EasyBottomTab addItem(@DrawableRes int resId, @NonNull String text, Fragment fragment) {
        initFragmentStatue();
        list_fragment.add(fragment);
        mBitmapsResourseList.add(resId);
        mSelectorBitmapsResourseList.add(0);
        mTextList.add(text);
        mItemNum++;
        return this;
    }

    public EasyBottomTab addItem(@DrawableRes int unSelectedId, @DrawableRes int selectedId, @NonNull String text, Fragment fragment) {
        initFragmentStatue();
        list_fragment.add(fragment);
        mSelectorBitmapsResourseList.add(unSelectedId);
        mBitmapsResourseList.add(selectedId);
        mTextList.add(text);
        mItemNum++;
        return this;
    }
    /**
     * 加入fragment的情况下的初始化
     */
    private void initFragmentStatue() {
        if (list_fragment == null) {
            list_fragment = new ArrayList<>();

        }
    }

    public void finishInit() {
        if (mItemNum >= 3 && mItemNum <= 5) {
            initChildView();
        } else {
            throw new RuntimeException("底部tab应该为3到5个");
        }
    }

    private OnItemTabSelectedListener mOnItemTabSelectedListener;

    interface OnItemTabSelectedListener {
        void onSelected(int selectorItemPosition);

        void onSelectedAgain(int position);
    }

    public void addItemTabSelectedListener(OnItemTabSelectedListener mItemTabSelectedListener) {
        this.mOnItemTabSelectedListener = mItemTabSelectedListener;
        mOnItemTabSelectedListener.onSelected(currentPosition);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && changeMode == 2) {
            if (!hasRippleInit) initRippleOptions();
            //如果点击的时候上个ripple还没结束
            if (!isBeforEnding) {
                stopLastWave();
//                isRippleDrawing=true;
                isBeforEnding = true;
                startWave(event);
            } else startWave(event);
        }
        return super.onTouchEvent(event);
    }

    private void stopLastWave() {
//        mHandler.removeCallbacks(runnable);
        isRippleDrawing = false;
        isBeforEnding = false;
        refreshView();
    }

    private void startWave(MotionEvent event) {
        downX = event.getX();
        downY = event.getY();
        isRippleDrawing = true;
        refreshView();
    }

    private void refreshView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private Paint mPaint;
    private int rippleTime;
    private int rippleRate;
    private int rippleDuration;
    private float downX;
    private float downY;
    private int radiusMax;
    private boolean isRippleDrawing;
    //上一个水纹是否结束
    private boolean isBeforEnding;
    private Handler mHandler;
    private Runnable runnable;

    private boolean hasRippleInit;

    private void initRippleOptions() {
        hasRippleInit = true;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(rippleColor);
        mPaint.setAlpha(rippleAlpha);
        radiusMax = getWidth();
        rippleDuration = 100;
        rippleRate = 5;
        isBeforEnding = true;
        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                refreshView();
            }
        };
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(!isRipple)return;
        if (changeMode == 2) {
            if (isRippleDrawing) {
                //暂时的方案是缩小ripple范围
                if (rippleDuration / 3 <= rippleTime * rippleRate) {
                    rippleRate = 30;
                } else {
                    rippleRate = 5;
                }
                if (rippleDuration <= rippleTime * rippleRate) {
                    isRippleDrawing = false;
                    isBeforEnding = true;
                    rippleTime = 0;
//                    invalidate();
                    try {
                        if (colorLists != null) setBackgroundColor(colorLists.get(currentPosition));
                    } catch (Exception e) {
                        setBackgroundColor(Color.WHITE);
                    }
                } else {
                    mHandler.postDelayed(runnable, 20);
                    isBeforEnding = false;
                }
                canvas.drawCircle(downX, downY, radiusMax * (((float) rippleTime * rippleRate) / rippleDuration), mPaint);
                rippleTime += 1;
            }
        }
    }

    public EasyBottomTab setIsRipple(boolean isRipple)
    {
        this.isRipple=isRipple;
        return this;
    }

    public EasyBottomTab setChangeMode(ChangeMode changeMode) {
        this.changeMode = changeMode.ordinal();
        return this;
    }


    public EasyBottomTab setTabBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public EasyBottomTab setSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    public EasyBottomTab setUnSelectedColor(@ColorInt int unSelectedColor) {
        this.unSelectedColor = unSelectedColor;
        return this;
    }

    //设置被选中 如果自动化集成fragment要实现联动
    public EasyBottomTab setTabSelected(int selectedPosition) {

        if(getFragmentList() != null && getFragmentList().size() == mItemNum)
        {
            if (mSwitchListener == null||!mSwitchListener.isBeforeSwitch(selectedPosition))
            switchFragment(selectedPosition,currentFragment,getFragmentList().get(selectedPosition),
                    mainActivity.getSupportFragmentManager().beginTransaction(),containerViewId);
        }
        else
        {
            for (int i = 0; i < mItemNum; i++) {
                if (i == selectedPosition) selected(i, changeMode == 2, currentPosition);
                else unSelected(i, changeMode == 2, currentPosition);
            }
        }
        this.currentPosition = selectedPosition;
        return this;
    }

    public interface SwitchListener{
        //是否跳转前做判断 返回true表示是 false表示不做判断
        boolean isBeforeSwitch(int selectedPosition);
    }

    private SwitchListener mSwitchListener;

    public void addSwitchListener(SwitchListener mSwitchListener)
    {
        this.mSwitchListener=mSwitchListener;
    }
    private List<Fragment> list_copy;

    /**
     * 自动化实现fragment间的切换
     * @param mainActivity 传入一个AppCompatActivity对象
     * @param containerViewId 布局文件中放fragment的容器
     * @param savedInstanceState
     */
    public void autoInitFragment(final AppCompatActivity mainActivity, @IdRes final int containerViewId, Bundle savedInstanceState) {
        this.isSavedInstanceState=savedInstanceState!=null;
        this.mainActivity=mainActivity;
        this.containerViewId=containerViewId;
        if (savedInstanceState != null) {
            list_copy=new ArrayList<>();
            for (int i = 0; i < mItemNum; i++) {
                Fragment fragmentByTag = mainActivity.getSupportFragmentManager().findFragmentByTag(getFragmentTag(i));
                if(fragmentByTag!=null)
                {
                    list_copy.add(i,fragmentByTag);
                }
                else {
                    list_copy.add(list_fragment.get(i));
                }
            }
            list_fragment.clear();
        }
        currentFragment = getFragmentList().get(0);
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        if (savedInstanceState != null) {
            for (int i = 0; i < mItemNum; i++) {
                if (i != 0) fragmentTransaction.hide(getFragmentList().get(i));
            }
        }
        if (!currentFragment.isAdded())
            fragmentTransaction.add(containerViewId, currentFragment, getFragmentTag(0));
        else fragmentTransaction.show(currentFragment);
        fragmentTransaction.commitAllowingStateLoss();

        if (getFragmentList() != null && getFragmentList().size() == mItemNum){

            for(int i=0;i<mItemNum;i++)
            {
                updateListener(((EzTabItem) ((EzTabItemWithDot) getChildAt(i)).getChildAt(0)));
            }
        }
    }

    private void updateListener(EzTabItem ezTabItem) {
        ezTabItem.setmOnSelectorListenter(null);
        ezTabItem.setmOnSelectorListenter(new EzTabItem.OnSelector() {
            @Override
            public void isSelector(int position) {
                //没设置判断和不做判断直接跳转
                    if (mSwitchListener == null||!mSwitchListener.isBeforeSwitch(position))
                        switchFragment(position, currentFragment, getFragmentList().get(position),
                                mainActivity.getSupportFragmentManager().beginTransaction(), containerViewId);
            }
        });
    }

    /**
     * 跳转一个tab之前的判断成立 即确定跳转该tab 不调用则不跳转
     * @param position
     */
    public void beforeVerifiedOk(int position) {
        switchFragment(position,currentFragment,getFragmentList().get(position),mainActivity.getSupportFragmentManager().beginTransaction(),containerViewId);
    }
    private Fragment currentFragment;

    private AppCompatActivity mainActivity;

    private boolean isSavedInstanceState;
    private @IdRes  int containerViewId;

    private void switchFragment(int addedPosition, Fragment from, Fragment to, FragmentTransaction fragmentTransaction, @IdRes int containerViewId) {
        if (from == to) return;
        currentPosition=addedPosition;
        //fragment和tab联动
        for (int i = 0; i < mItemNum; i++) {
            if (i == addedPosition) selected(i, changeMode == 2, addedPosition);
            else unSelected(i, changeMode == 2, addedPosition);
        }
        if (to.isAdded()) {
            fragmentTransaction.hide(from).show(to);
        }
        else
        {
            fragmentTransaction.hide(from).add(containerViewId, to, getFragmentTag(addedPosition));
            //页面意外消失的时候 页面bug修复
            if(isSavedInstanceState)
            {
                fragmentTransaction.hide(from).show(to);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        currentFragment = to;
    }

    /**
     * 在横竖屏切换时有的fragment被保存有的没有 所以用一个list备份集合来存储保存过的和new 的fragment
     * 来解决重叠或空指针异常问题
     * @return
     */
    private List<Fragment> getFragmentList()
    {
        if(list_fragment!=null&&list_fragment.size()==mItemNum)return list_fragment;
        else return list_copy;
    }
    private String getFragmentTag(int i) {
        String rtn = "";
        switch (i) {
            case 0:
                rtn = "one";
                break;
            case 1:
                rtn = "two";
                break;
            case 2:
                rtn = "three";
                break;
            case 3:
                rtn = "four";
                break;
            case 4:
                rtn = "five";
                break;
        }
        return rtn;
    }

    public void onDestroy()
    {
        if(mHandler!=null)mHandler.removeCallbacksAndMessages(null);
        for(int i=0;i<mItemNum;i++)
        {
            ((EzTabItem) ((EzTabItemWithDot) getChildAt(i)).getChildAt(0)).onDestroy();
            ((DotView) ((EzTabItemWithDot) getChildAt(i)).getChildAt(1)).onDestroy();
        }
        mContext=null;
        mainActivity=null;
    }
}
