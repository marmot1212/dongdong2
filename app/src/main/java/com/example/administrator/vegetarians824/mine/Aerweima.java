package com.example.administrator.vegetarians824.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.vegetarians824.R;

public class Aerweima extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aerweima);
        TextView tv=(TextView) findViewById(R.id.saomiao_tv);
        Intent intent=getIntent();
        String r=intent.getStringExtra("result");
        tv.setText(r);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.saomiao_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
