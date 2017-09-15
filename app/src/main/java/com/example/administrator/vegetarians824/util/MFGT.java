package com.example.administrator.vegetarians824.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.administrator.vegetarians824.MapActivity;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.veganpass.CheckLanguage;
import com.example.administrator.vegetarians824.veganpass.PassportHome;


/**
 * Created by apple on 2017/3/30.
 */

public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void startActivity(Activity activity, Class cls) {
        activity.startActivity(new Intent(activity, cls));
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMapActivity(Activity activity) {
        startActivity(activity, MapActivity.class);
    }

    public static void gotoPassportHome(Activity activity) {
        startActivity(activity, PassportHome.class);
    }

    public static void gotoCheckLanguage(Context context) {
        startActivity((Activity)context,CheckLanguage.class);
    }
}
