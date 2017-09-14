package com.example.administrator.vegetarians824.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.UserCenter;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.CheckPermission;
import com.example.administrator.vegetarians824.util.FileUitlity;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.example.administrator.vegetarians824.util.UpLoadUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class MyEdit extends AppCompatActivity {
    User user;
    String sexid;
    private PopupWindow sexpopupWindow,picpopupwindow;
    private View sexpopView,picpopView;
    private  String capturePath;
    private final  int TAKE_PHOTO   = 0;
    private final  int PHONE_PHOTO  = 1;
    private final  int RESULT_PHOTO = 2;
    ImageView pic;
    Bitmap mybit;
    File file,parent;
    private android.support.v7.app.AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_edit);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initoperate();
        pic = (ImageView) findViewById(R.id.my_edit_pic);
        user= BaseApplication.app.getUser();
        if(!user.getPic().equals("")) {
            com.nostra13.universalimageloader.core.ImageLoader loader = ImageLoaderUtils.getInstance(getBaseContext());
            DisplayImageOptions options = ImageLoaderUtils.getOpt();
            loader.displayImage(URLMannager.Imag_URL + "" + user.getPic(), pic, options);
        }
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.my_edit_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void initView(){
        user= BaseApplication.app.getUser();
        //用户昵称
        if(!user.getName().equals("")) {
            TextView name = (TextView) findViewById(R.id.my_edit_name);
            name.setText(user.getName());
        }
        //用户说明
        if(!user.getIntro().equals("null")) {
            TextView intro = (TextView) findViewById(R.id.my_edit_intro);
            if(!user.getIntro().equals(""))
                intro.setText(user.getIntro());
        }
        //用户性别
        if(!user.getSex().equals("")) {
            TextView sex = (TextView) findViewById(R.id.my_edit_sex);
            if (user.getSex().equals("1")) {
                sex.setText("女");
            } else
                sex.setText("男");
        }
        //用户手机号
        if(!user.getMobile().equals("")) {
            TextView mobile = (TextView) findViewById(R.id.my_edit_mobile);
            StringBuffer buffer = new StringBuffer();
            buffer.append(user.getMobile().substring(0, 3)).append("****").append(user.getMobile().substring(7, 11));
            mobile.setText(buffer.toString());
        }
        //用户地区
        if(!user.getProvince().equals("null")) {
            TextView province = (TextView) findViewById(R.id.my_edit_province);
            if(!user.getProvince().equals(""))
                province.setText(user.getProvince());
        }

        Button button=(Button)findViewById(R.id.my_edit_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                BaseApplication.app.getUser().initUser();
                SharedPreferences preferences=MyEdit.this.getSharedPreferences("shared", Context.MODE_WORLD_READABLE|Context.MODE_WORLD_WRITEABLE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putBoolean("islog",false);
                editor.commit();
                finish();*/
                getDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        updataUser();
        StatService.onResume(this);
    }
    public void updataUser(){
        RelativeLayout et2=(RelativeLayout)findViewById(R.id.edit02);
        et2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyEdit.this,UpdateName.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("id",user.getId());
                MyEdit.this.startActivity(intent);
            }
        });

        RelativeLayout et3=(RelativeLayout)findViewById(R.id.edit03);
        et3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyEdit.this,UpdateIntro.class);
                intent.putExtra("intro",user.getIntro());
                intent.putExtra("id",user.getId());
                MyEdit.this.startActivity(intent);
            }
        });

        RelativeLayout et6=(RelativeLayout)findViewById(R.id.edit06);
        et6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyEdit.this,UpdateArea.class);
                intent.putExtra("flag","1");
                MyEdit.this.startActivity(intent);
            }
        });

        RelativeLayout et7=(RelativeLayout)findViewById(R.id.edit07);
        et7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyEdit.this,UpdatePwd.class);
                MyEdit.this.startActivity(intent);
            }
        });

        RelativeLayout et4=(RelativeLayout)findViewById(R.id.edit04);
        initSexPopWindow();
        et4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexpopupWindow =  getPopWindow(sexpopView);
            }
        });

        RelativeLayout et1=(RelativeLayout)findViewById(R.id.edit01);
        initPicPopWindow();
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picpopupwindow =  getPopWindow(picpopView);

            }
        });
        RelativeLayout et5=(RelativeLayout)findViewById(R.id.edit05);
        et5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyEdit.this,UpdatePhone.class);
                MyEdit.this.startActivity(intent);
            }
        });


    }
    public void initSexPopWindow(){
        sexpopView = getLayoutInflater().inflate(R.layout.sex_popwindow,null);
        Button nan = (Button) sexpopView.findViewById(R.id.btn_sex_nan);
        Button nv = (Button) sexpopView.findViewById(R.id.btn_sex_nv);
        Button pop_cancel = (Button) sexpopView.findViewById(R.id.btn_sex_cancel);
        nan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexid="2";
                sexdoPost();
            }
        });
        nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexid="1";
                sexdoPost();
            }
        });
        pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sexpopupWindow.dismiss();
            }
        });
    }
    public void initPicPopWindow(){
        picpopView = getLayoutInflater().inflate(R.layout.pic_popwindow,null);
        Button pop_take_photo = (Button) picpopView.findViewById(R.id.btn_take_photo);
        Button pop_phone_photo = (Button) picpopView.findViewById(R.id.btn_phone_photo);
        Button pop_cancel = (Button) picpopView.findViewById(R.id.btn_cancel);
        pop_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_photo();
                //pic.setImageBitmap(mybit);
            }
        });
        pop_phone_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckPermission.requestReadStorageRuntimePermission(MyEdit.this)) {
                    phone_photo();
                }
                //pic.setImageBitmap(mybit);
            }
        });
        pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picpopupwindow.dismiss();
            }
        });
    }
    private PopupWindow getPopWindow(View contentView) {
        PopupWindow popWindow = new PopupWindow(contentView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 获取光标
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(findViewById(R.id.edit02), Gravity.BOTTOM, 0, 0);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //    backgroundAlpha(1f);
            }
        });
        return popWindow;
    }
    public void sexdoPost(){
        StringPostRequest spr=new StringPostRequest("http://www.isuhuo.com/plainLiving/Androidapi/user/editsex", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js=new JSONObject(s);
                    String code=js.getString("Code");
                    if(code.equals("1")){
                        BaseApplication.app.getUser().setSex(sexid);
                        SharedPreferences preferences=MyEdit.this.getSharedPreferences("shared",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("sex",sexid);
                        editor.commit();//提交数据

                        TextView sex = (TextView) findViewById(R.id.my_edit_sex);
                        if (sexid.equals("1")) {
                            sex.setText("女");
                        } else
                            sex.setText("男");
                        sexpopupWindow.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid",user.getId());
        spr.putValue("sexId",sexid);
        SlingleVolleyRequestQueue.getInstance(MyEdit.this).addToRequestQueue(spr);
    }
    public class picdoPost extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("uid", user.getId());
            final Map<String, File> files = new HashMap<String, File>();
            files.put("img_head", file);
            String s="";
            try {
                s=UpLoadUtil.post(strings[0], params, files);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return s;
        }

        @SuppressLint("CommitPrefEdits")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject js=new JSONObject(s);
                JSONObject js2=js.getJSONObject("Result");
                String path=js2.getString("path");
                BaseApplication.app.getUser().setPic(path);
                SharedPreferences preferences=MyEdit.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("user_head_img",path);
                editor.commit();//提交数据
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void take_photo(){
        //调用相机
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //camera.putExtra("camerasensortype", 1); // 调用前置摄像头
        parent  = FileUitlity.getInstance(getApplicationContext())
                .makeDir("head_img");
        capturePath = parent.getPath() + File.separatorChar + System.currentTimeMillis() + ".jpg";
        camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(camera,TAKE_PHOTO);
    }
    public void phone_photo(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PHONE_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        //相机返回结果,调用系统裁剪
        if(requestCode == TAKE_PHOTO){
            startPhoneZoom(Uri.fromFile(new File(capturePath)));
        }
        else if(requestCode == PHONE_PHOTO){
            Cursor cursor =
                    this.getContentResolver().query(
                            data.getData(),
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
            String capturePaths="";
            if(cursor!=null) {
                cursor.moveToFirst();
                capturePaths = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
            startPhoneZoom(Uri.fromFile(new File(capturePaths)));
        }
        //裁剪返回位图
        else if(requestCode == RESULT_PHOTO){
            Bundle bundle =data.getExtras();
            if(bundle!=null){
                //Bitmap bitmap = bundle.getParcelable("data");
                mybit=bundle.getParcelable("data");
                pic = (ImageView) findViewById(R.id.my_edit_pic);
                pic.setImageBitmap(mybit);
                saveBitmapFile(mybit);
                new picdoPost().execute("http://www.isuhuo.com/plainLiving/androidapi/user/member_upload");
                picpopupwindow.dismiss();
                //mybit=bitmap;
            }
        }
    }
    public void startPhoneZoom(Uri uri){
        Intent intent =
                new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop","true");
        //设置宽度高度比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //设置图片的高度宽度
        intent.putExtra("outputX",300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_PHOTO);
    }

    @SuppressLint("SdCardPath")
    public void saveBitmapFile(Bitmap bitmap){
        file=new File("/sdcard/01.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDialog(){
        builder = new android.support.v7.app.AlertDialog.Builder(MyEdit.this);
        builder.setTitle("退出").setMessage("是否要退出您当前登录的账户？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    // TODO 确定按钮
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        releasePhone();
                        BaseApplication.app.getUser().initUser();
                        SharedPreferences preferences=MyEdit.this.getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean("islog",false);
                        editor.commit();
                        finish();
                       // UserCenter.instance.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            // TODO 取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    public void releasePhone(){
        StringPostRequest spr=new StringPostRequest(URLMannager.ReleasePhone, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("phoneid", JPushInterface.getRegistrationID(getApplicationContext()));
        if(BaseApplication.app.getUser().getId()==null){
            spr.putValue("uid","");
        }else {
            spr.putValue("uid", BaseApplication.app.getUser().getId());
        }

        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(spr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==197){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                phone_photo();
            } else {
                Toast.makeText(getBaseContext(), "权限错误", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}
