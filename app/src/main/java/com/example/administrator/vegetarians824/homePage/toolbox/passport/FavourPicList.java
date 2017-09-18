package com.example.administrator.vegetarians824.homePage.toolbox.passport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class FavourPicList extends AppCompatActivity {
    private static final String TAG = "FavourPicList";
    private ImageView gridView;
    private SharedPreferences pre;
    private GestureDetector gestureDetector;
    final int RIGHT = 0;
    final int LEFT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour_pic_list);
        StatusBarUtil.setColorDiff(this,0x8e000000);
        initView();
        gestureDetector = new GestureDetector(this,onGestureListener);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.RIGHT);
            slide.setDuration(500);
            getWindow().setEnterTransition(slide);
            //TransitionManager.beginDelayedTransition(gridView, slide);
        }


        switch (pre.getString("type","")){
            case "纯素":gridView.setImageResource(R.drawable.bg_chun);break;
            case "净素":gridView.setImageResource(R.drawable.bg_jing);break;
            case "蛋奶素":gridView.setImageResource(R.drawable.bg_dan);break;
            case "Vegan":gridView.setImageResource(R.drawable.bg_chun);break;
            case "Su vegetarianism":gridView.setImageResource(R.drawable.bg_jing);break;
            case "Ovo-lacto":gridView.setImageResource(R.drawable.bg_dan);break;
            default:break;
        }
    }
    public void initView(){
        pre = getSharedPreferences("data", Context.MODE_PRIVATE);
        gridView=(ImageView)findViewById(R.id.favour_grid);

    }


    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x = e2.getX() - e1.getX();
            if (x >80) {
                doResult(RIGHT);
            } else if (x <-80) {
                doResult(LEFT);
            }
            return true;
        }
    };



    public void doResult(int action) {
            switch (action) {
                case RIGHT:
                    onBackPressed();
                    break;
                case LEFT:
                    break;
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
