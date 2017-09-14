package com.example.administrator.vegetarians824.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017-03-29.
 */
public class SDHelper {
    //指定该应用的数据存储文件夹
    private static String dir="/sdcard/dongdongDownload";
    //判断是否有sd卡可供存储
    public static boolean isHasSdcard(){
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    //获取dir路径
    public static String getDir(){
        if(isHasSdcard()){
            File destDir = new File(dir);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            return destDir.getPath();
        }else {
            return "";
        }
    }



}
