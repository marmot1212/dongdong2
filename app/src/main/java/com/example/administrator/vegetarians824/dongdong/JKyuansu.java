package com.example.administrator.vegetarians824.dongdong;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class JKyuansu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkyuansu);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initOperate();
    }
    public void initOperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.yuansu_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button search=(Button)findViewById(R.id.sherusearch_bt);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(JKyuansu.this,JKsheru.class);
                JKyuansu.this.startActivity(intent);
            }
        });
    }
    public void danbaizhi(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","蛋白质");
        JKyuansu.this.startActivity(intent);
    }
    public void tanshui(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","碳水化合物");
        JKyuansu.this.startActivity(intent);
    }
    public void xianwei(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","膳食纤维");
        JKyuansu.this.startActivity(intent);
    }
    public void zhilei(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","脂类");
        JKyuansu.this.startActivity(intent);
    }
    public void weishengsu(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","维生素");
        JKyuansu.this.startActivity(intent);
    }
    public void kuangwuzhi(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","矿物质");
        JKyuansu.this.startActivity(intent);
    }
    public void shui(View view){
        Intent intent=new Intent(JKyuansu.this,JKYuansuDetail.class);
        intent.putExtra("what","水");
        JKyuansu.this.startActivity(intent);
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
