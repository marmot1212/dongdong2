package com.example.administrator.vegetarians824.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017-06-30.
 */
public class CheckPermission {
    public static boolean requestReadStorageRuntimePermission(Activity ac) {
        if (ContextCompat.checkSelfPermission(ac, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ac, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 197);
            return false;
        } else {
            return  true;
        }
    }

    public static boolean requestLocaltionPermission(Activity ac) {
        if (ContextCompat.checkSelfPermission(ac, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ac, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ac,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 198);
            return false;
        } else {
            return  true;
        }
    }
}
