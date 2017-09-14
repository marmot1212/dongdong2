package com.example.administrator.vegetarians824.veganpass;

import java.util.Random;

/**
 * Created by Administrator on 2017-06-14.
 */
public class CreatePhoneID {
    //生成指定长度字符串

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
