package com.example.administrator.vegetarians824;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.map_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
