package com.example.administrator.vegetarians824.mine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.UpLoadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAdmin extends AppCompatActivity {
    private EditText et1,et2,et3,et4,et5;
    private FrameLayout fram1,fram2;
    private ImageView ima1,ima2;
    private String url1,url2;
    private final  int PHONE_PHOTO1 = 1;//身份证
    private final  int PHONE_PHOTO2 = 2;//营业执照
    private Button bt;
    private String tx1,tx2,tx3,tx4,tx5;
    private int flag;//标记参数，用于区分是商家认证进来的还是认领进来的 1，商家认证 2，认领,0,错误跳转
    private String mess_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_admin);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        flag=intent.getIntExtra("flag",0);
        if(intent.hasExtra("mess_id")){
            mess_id=intent.getStringExtra("mess_id");
        }
        initView();
        getpic();
        sendpress();
    }
    public void initView(){
        et1=(EditText)findViewById(R.id.shangjia_et1);
        et2=(EditText)findViewById(R.id.shangjia_et2);
        et3=(EditText)findViewById(R.id.shangjia_et3);
        et4=(EditText)findViewById(R.id.shangjia_et4);
        et5=(EditText)findViewById(R.id.shangjia_et5);
        fram1=(FrameLayout)findViewById(R.id.shangjia_fram1);
        fram2=(FrameLayout)findViewById(R.id.shangjia_fram2);
        ima1=(ImageView)findViewById(R.id.shangjia_ima1);
        ima2=(ImageView)findViewById(R.id.shangjia_ima2);
        bt=(Button)findViewById(R.id.shangjia_bt);
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.admin_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void getpic(){
        fram1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PHONE_PHOTO1);
            }
        });
        fram2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PHONE_PHOTO2);
            }
        });
    }
    public void sendpress(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tx1=et1.getText().toString();
                tx2=et2.getText().toString();
                tx3=et3.getText().toString();
                tx4=et4.getText().toString();
                tx5=et5.getText().toString();
                if(et1.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(),"请添加真实姓名",Toast.LENGTH_SHORT).show();
                }else {
                    if(et2.getText().toString().equals("")){
                        Toast.makeText(getBaseContext(),"请添加手机号",Toast.LENGTH_SHORT).show();
                    }else {
                        if(et3.getText().toString().equals("")){
                            Toast.makeText(getBaseContext(),"请添加座机号",Toast.LENGTH_SHORT).show();
                        }else {
                            if(et4.getText().toString().equals("")){
                                Toast.makeText(getBaseContext(),"请添加微信号",Toast.LENGTH_SHORT).show();
                            }else {
                                if(et5.getText().toString().equals("")){
                                    Toast.makeText(getBaseContext(),"请添加电子邮件",Toast.LENGTH_SHORT).show();
                                }else {
                                    if(url1==null){
                                        Toast.makeText(getBaseContext(),"请上传身份证",Toast.LENGTH_SHORT).show();
                                    }else {
                                        if(url2==null){
                                            Toast.makeText(getBaseContext(),"请上传营业执照",Toast.LENGTH_SHORT).show();
                                        }else {
                                            new doPost().execute("https://www.isuhuo.com/plainLiving/Androidapi/userCenter/addtenans");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        });
    }
    public class doPost extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("uid", BaseApplication.app.getUser().getId());
            params.put("true_name",tx1);
            params.put("phone",tx2);
            params.put("tel",tx3);
            params.put("wx_num",tx4);
            params.put("email",tx5);
            if(flag==2){
                params.put("mess_id",mess_id);
                params.put("type","1");
            }
            final Map<String, File> files = new HashMap<String, File>();
            files.put("idcard",new File(url1));
            files.put("business",new File(url2));
            String s="";
            try {
                s= UpLoadUtil.post(strings[0], params, files);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject job=new JSONObject(s);
                if(job.getString("Code").equals("1")){
                    Toast.makeText(getBaseContext(),"发布成功,等待审核",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getBaseContext(),job.getString("Message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK &&requestCode==PHONE_PHOTO1){
            Uri uri = data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor =getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
                if (null == cursor) {
                    return;
                }
                cursor.moveToFirst();
                String capturePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                url1=capturePath;
                cursor.close();
                if (capturePath != null) {
                    ima1.setImageBitmap(BitmapFactory.decodeFile(capturePath));
                }
            }
        }

        if(resultCode==RESULT_OK &&requestCode==PHONE_PHOTO2){
            Uri uri = data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor =getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
                if (null == cursor) {
                    return;
                }
                cursor.moveToFirst();
                String capturePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                url2=capturePath;
                cursor.close();
                if (capturePath != null) {
                    ima2.setImageBitmap(BitmapFactory.decodeFile(capturePath));
                }
            }
        }

    }
    //滑动退键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
