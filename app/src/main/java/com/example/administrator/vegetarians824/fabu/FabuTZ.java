package com.example.administrator.vegetarians824.fabu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.CheckPermission;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.UpLoadUtil;
import com.tb.emoji.Emoji;
import com.tb.emoji.EmojiUtil;
import com.tb.emoji.FaceFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabuTZ extends AppCompatActivity {
    private GridView gridView1;              //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private HashMap<String, Object> map;
    private List<String> filePath;
    private SimpleAdapter simpleAdapter;     //适配器
    EditText et1,et2;
    String title,content,uid;
    TextView tv;
    FrameLayout fm;
    Boolean isEmojishow=false;
    FrameLayout fram_emoji;
    FaceFragment faceFragment;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_tz);
        StatusBarUtil.setColorDiff(this,0xff00aff0);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //获取控件对象

        loadingDialog=new LoadingDialog(this);
        loadingDialog.setMessage("正在上传");
        et1=(EditText)findViewById(R.id.fabu06_et1);
        et2=(EditText)findViewById(R.id.fabu06_et2);
        fm=(FrameLayout)findViewById(R.id.fabu06_getpic);
        tv=(TextView)findViewById(R.id.fabu06_tv);
        initop();
        setpic();
        sendPost();
    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu06_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fram_emoji=(FrameLayout)findViewById(R.id.emoji_fram_ctt);
        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.emoji_fram_ctt,faceFragment).commit();
        faceFragment.setListener(new FaceFragment.OnEmojiClickListener() {
            @Override
            public void onEmojiDelete() {
                String text = et2.getText().toString();
                if (text.isEmpty()) {
                    return;
                }
                if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                    int index = text.lastIndexOf("[");
                    if (index == -1) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        et2.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        try {
                            EmojiUtil.handlerEmojiText2(et2, et2.getText().toString(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    et2.getText().delete(index, text.length());
                    try {
                        EmojiUtil.handlerEmojiText2(et2, et2.getText().toString(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et2.onKeyDown(KeyEvent.KEYCODE_DEL, event);
            }

            @Override
            public void onEmojiClick(Emoji emoji) {
                if (emoji != null) {
                    int index = et2.getSelectionStart();
                    Editable editable = et2.getEditableText();
                    editable.append(emoji.getContent());
                    try {
                        EmojiUtil.handlerEmojiText2(et2, et2.getText().toString(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        ImageView emoji=(ImageView)findViewById(R.id.fabu_tz_emoji);
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmojishow) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(et2.getWindowToken(), 0);
                    }
                    ViewGroup.LayoutParams params = fram_emoji.getLayoutParams();
                    params.height = 210 * 2;
                    fram_emoji.setLayoutParams(params);
                    fram_emoji.requestLayout();
                    isEmojishow=true;
                }else {
                    ViewGroup.LayoutParams params = fram_emoji.getLayoutParams();
                    params.height =0;
                    fram_emoji.setLayoutParams(params);
                    fram_emoji.requestLayout();
                    isEmojishow=false;
                }
            }
        });
    }

    public void setpic(){
        gridView1 = (GridView) findViewById(R.id.gridView3);
        //放置加号图片
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic);
        imageItem = new ArrayList<HashMap<String, Object>>();
        map = new HashMap<String, Object>();
        filePath=new ArrayList<>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});
        /*
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如
         * map.put("itemImage", R.drawable.img);
         * 解决方法:
         *              1.自定义继承BaseAdapter实现
         *              2.ViewBinder()接口实现
         *  参考 http://blog.csdn.net/admin_/article/details/7257901
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });

        gridView1.setAdapter(simpleAdapter);

        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if(position == 0) { //点击图片位置为+ 0对应0张图片
                    if( imageItem.size() == 7) { //第一张为默认图片
                        Toast.makeText(getBaseContext(),"最多只能选择6张",Toast.LENGTH_SHORT).show();
                    }else {
                        //选择图片
                        if(CheckPermission.requestReadStorageRuntimePermission(FabuTZ.this)) {
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, IMAGE_OPEN);
                        }
                    }
                }
                else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //      Toast.LENGTH_SHORT).show();
                }
            }
        });
        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageItem.size()==1) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            gridView1.setVisibility(View.VISIBLE);
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                //查询选择图片
                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                //返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                //光标移动至开头 获取图片路径
                cursor.moveToFirst();

                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        } else {
            gridView1.setVisibility(View.INVISIBLE);
        }

        //end if 打开图片
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){

            fm.setBackgroundColor(0xfff);
            tv.setVisibility(View.INVISIBLE);
            filePath.add(pathImage);

            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            Bitmap delbmp = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            map.put("del",delbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage","del"}, new int[] { R.id.imageView1,R.id.delete_pic});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
            //及时回收图片，防止占用多余内存

        }
        StatService.onResume(this);
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FabuTZ.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                filePath.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
                if(imageItem.size()==1){
                    fm.setBackgroundColor(0xffe2e2e2);
                    tv.setVisibility(View.VISIBLE);
                    gridView1.setVisibility(View.INVISIBLE);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void sendPost(){
        Button button=(Button) findViewById(R.id.fabu06_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et1.getText().toString();
                content=et2.getText().toString();
                uid=BaseApplication.app.getUser().getId();
                if(title.length()<2){
                    Toast.makeText(getBaseContext(),"标题请至少输入4个字",Toast.LENGTH_SHORT).show();
                }else {
                    if(content.length()<10){
                        Toast.makeText(getBaseContext(),"内容请至少输入10个字",Toast.LENGTH_SHORT).show();
                    }else {
                        loadingDialog.show();
                        new picdoPost().execute(URLMannager.FabuTZPost);
                    }
                }
                //new picdoPost().execute("http://www.isuhuo.com/plainLiving/Androidapi/addapi/add_posts");
                //Log.d("==========d",title+" "+content+" "+tel+" "+longitude+" "+latitude+" "+uid+" "+address+" "+filePath.size());
            }
        });
    }

    public class picdoPost extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("title",title);
            params.put("content",content);
            params.put("uid",uid);
            final Map<String, File> files = new HashMap<String, File>();

            for(int i=0;i<filePath.size();i++){


                // File file=new File(filePath.get(i));
                //StringBuffer buffer=new StringBuffer();
                //buffer.append("img_url_").append(i+1);
                //files.put(buffer.toString(),file);

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap addbmp=BitmapFactory.decodeFile(filePath.get(i),options);
                    File file=new File("/sdcard/"+"qus"+i+".jpg");//将要保存图片的路径

                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    addbmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();

                    StringBuffer buffer=new StringBuffer();
                    buffer.append("img_url_").append(i+1);
                    files.put(buffer.toString(),file);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
                JSONObject js1=new JSONObject(s);
                if(js1.getString("Code").equals("1")){
                    loadingDialog.dismiss();
                    Toast.makeText(getBaseContext(),"发布成功，等待审核",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    loadingDialog.dismiss();
                    Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==197){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
            } else {
                Toast.makeText(getBaseContext(), "权限错误", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
}
