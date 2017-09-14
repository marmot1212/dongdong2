package com.example.administrator.vegetarians824.dongdong;

import android.app.Service;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.SensorManagerHelper;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class JKtiaozhan extends AppCompatActivity {
   private ImageView yao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jktiaozhan);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initoperate();

    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.tiaozhan_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        yao=(ImageView)findViewById(R.id.yaoyiyao);
        yao.setVisibility(View.INVISIBLE);
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
        sensorHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener() {
            @Override
            public void onShake() {
                yao.setVisibility(View.VISIBLE);
            }
        });
        yao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(JKtiaozhan.this,JKquestion.class);
                JKtiaozhan.this.startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        yao.setVisibility(View.INVISIBLE);
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
