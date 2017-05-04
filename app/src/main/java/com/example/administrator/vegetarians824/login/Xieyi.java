package com.example.administrator.vegetarians824.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

public class Xieyi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        WebView wv=(WebView)findViewById(R.id.xieyi_web);
        wv.loadUrl(URLMannager.XieYi);
        final LinearLayout fanhui=(LinearLayout)findViewById(R.id.xieyi_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
