package com.example.administrator.vegetarians824.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.vegetarians824.MapActivity;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.JKYuansuDetail;
import com.example.administrator.vegetarians824.homePage.personalProfile.MyContribute;
import com.example.administrator.vegetarians824.homePage.toolbox.nutridata.NutriData;
import com.example.administrator.vegetarians824.homePage.toolbox.nutridata.NutriDataDetails;
import com.example.administrator.vegetarians824.homePage.toolbox.nutridata.NutriQuery;
import com.example.administrator.vegetarians824.veganpass.CheckLanguage;
import com.example.administrator.vegetarians824.veganpass.PassportHome;
import com.example.administrator.vegetarians824.veganpass.PreferencePage;


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

    public static void gotoPreferencePage(Activity activity) {
        startActivity(activity, PreferencePage.class);
    }

    public static void gotoNutriQuery(Activity activity) {
        startActivity(activity, NutriQuery.class);
    }

    public static void gotoNutriData(Context context) {
        startActivity((Activity)context, NutriData.class);
    }

    public static void gotoNutriDataDetails(Activity activity, String key, String value) {
        Intent intent = new Intent(activity, JKYuansuDetail.class);
        intent.putExtra(key, value);
        startActivity(activity, intent);
    }

    public static void gotoMyContribute(Context context) {
        startActivity((Activity)context, MyContribute.class);
    }
}
