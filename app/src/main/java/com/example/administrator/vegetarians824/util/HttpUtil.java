package com.example.administrator.vegetarians824.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/5/3.
 */
public class HttpUtil  {
    public static String doGet(String u){
        HttpURLConnection con=null;
        InputStream is=null;
        StringBuffer buffer=new StringBuffer();
        try {
            URL url=new URL(u);
            try {
                con=(HttpURLConnection)url.openConnection();
                con.setConnectTimeout(5*1000);
                con.setReadTimeout(5*1000);
                if(con.getResponseCode()==200){
                    is=con.getInputStream();
                    int next=0;
                    byte[] bt=new byte[1024];
                    while((next=is.read(bt))>0){
                        buffer.append(new String(bt,0,next));
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            if(con!=null){
                con.disconnect();
            }
        }

        return buffer.toString();
    }

    public static Drawable doGetImag(String u){
        HttpURLConnection con=null;
        InputStream is=null;
        Drawable d=null;
        try {
            URL url=new URL(u);
            try {
                con=(HttpURLConnection)url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(5 * 1000);
                if(con.getResponseCode()==200){
                    is=con.getInputStream();
                    d=Drawable.createFromStream(is, "aa");
                }

            } catch (IOException e) {
                try {
                    if(is!=null)
                        is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            if(con!=null)
                con.disconnect();
        }
        return d;
    }


    public static String doPost(String u,Bitmap bitmap){

        HttpURLConnection httpURLConnection=null;
        InputStream is=null;
        StringBuffer buffer=null;
        URL url= null;
        try {
            url = new URL(u);
            try {
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Charset", "UTF-8");
                httpURLConnection.setRequestProperty("Content-type","application/x-www-foem-urlencoded");

                OutputStream os=httpURLConnection.getOutputStream();
                os.write(bitmapToBytes(bitmap));
                os.flush();
                os.close();

                if(httpURLConnection.getResponseCode()==200)
                {
                    is=httpURLConnection.getInputStream();
                    int next=0;
                    byte[]b=new byte[1024*10];
                    while((next=is.read(b))>0){
                        buffer.append(b.toString());
                }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return buffer.toString();
    }

    public static Bitmap getBitmapFromUrl(String imgUrl) {
        InputStream is=null;
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            try {
                if(is!=null)
                    is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Drawable loadImageFromNetwork(String imageUrl)
    {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(
                    new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {

        }
        if (drawable == null) {

        } else {

        }

        return drawable ;
    }

    public static Bitmap getBitmap(String path) throws IOException{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
