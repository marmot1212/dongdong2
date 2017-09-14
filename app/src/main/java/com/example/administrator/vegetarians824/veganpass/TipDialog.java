package com.example.administrator.vegetarians824.veganpass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.administrator.vegetarians824.R;


/**
 * Created by Administrator on 2017-05-25.
 */
public class TipDialog extends Dialog {
    private ImageView tips_ima;
    public TipDialog(final Context context) {
        super(context);
        /**设置对话框背景透明*/
        View v= LayoutInflater.from(context).inflate(R.layout.tips,null);
        //ViewGroup.LayoutParams params=new LinearLayout.LayoutParams(80,80);
        setContentView(v);
        //setCanceledOnTouchOutside(true);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 300; // 宽度
        lp.height = 300; // 高度
        lp.gravity=Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        tips_ima = (ImageView) findViewById(R.id.tips_ima);

    }

    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param id 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public TipDialog setImage(int id) {
        tips_ima.setImageResource(id);
        return this;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.


        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            cancel();
            return true;
        }

        return super.onTouchEvent(event);
    }


}