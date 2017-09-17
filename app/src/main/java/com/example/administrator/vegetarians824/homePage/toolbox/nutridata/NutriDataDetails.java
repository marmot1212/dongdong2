package com.example.administrator.vegetarians824.homePage.toolbox.nutridata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

/**
 *参照JkYuansuDetail.java
 * 待完善
 */
public class NutriDataDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutri_data_details);
        StatusBarUtil.setColorDiff(this,0xff00aff0);

    }


}
