package com.example.administrator.vegetarians824.fabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class FabuShangHu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_shang_hu);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu_sh_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
