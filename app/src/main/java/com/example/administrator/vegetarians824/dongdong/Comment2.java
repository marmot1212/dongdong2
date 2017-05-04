package com.example.administrator.vegetarians824.dongdong;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.UpLoadUtil;
import com.tb.emoji.Emoji;
import com.tb.emoji.EmojiUtil;
import com.tb.emoji.FaceFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment2 extends AppCompatActivity {
    private final int IMAGE_OPEN = 1;
    private GridView gridView1;
    private Bitmap bmp;
    private ArrayList<HashMap<String, Object>> imageItem;
    private HashMap<String, Object> map;
    private List<String> filePath;
    private SimpleAdapter simpleAdapter;
    private String pathImage;
    FrameLayout fm;
    TextView tv;
    EditText et1;
    String context,pid,ptype;
    FrameLayout mainline;
    LinearLayout emojiLine;
    FrameLayout fram_emoji;
    FaceFragment faceFragment;
    ImageView icon;
    private boolean emisShow=false;
    LinearLayout etparent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment2);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        et1=(EditText)findViewById(R.id.comment2_et);
        etparent=(LinearLayout)findViewById(R.id.comment2_etparent);
        mainline=(FrameLayout)findViewById(R.id.comment2_main);
        emojiLine=(LinearLayout)findViewById(R.id.comment_inputline_ct);
        initop();
        listenerSoftInput();
        getEmoji();
        setpic();
        sendPost();

    }
    public void initop(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.comment2_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setpic(){
        gridView1 = (GridView) findViewById(R.id.comment2_gridView);
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
                if( imageItem.size() == 5) { //第一张为默认图片
                    Toast.makeText(getBaseContext(),"最多只能选择4张",Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为+ 0对应0张图片

                    //选择图片
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    //通过onResume()刷新数据
                }
                else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "点击第"+(position + 1)+" 号图片",
                    //      Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
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
                pathImage= cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }  //end if 打开图片
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            fm=(FrameLayout)findViewById(R.id.comment2_getpic);
            fm.setBackgroundColor(0xfff);
            tv=(TextView)findViewById(R.id.comment2_tv);
            tv.setVisibility(View.INVISIBLE);
            filePath.add(pathImage);
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
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
        }

    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Comment2.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                filePath.remove(position-1);
                simpleAdapter.notifyDataSetChanged();
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
        Button button=(Button) findViewById(R.id.comment2_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context=et1.getText().toString();
                Intent intent=getIntent();
                pid=intent.getStringExtra("id");
                ptype=intent.getStringExtra("type");
                new picdoPost().execute(URLMannager.CommentAdd);

            }
        });
    }

    public class picdoPost extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("mess_id",pid);
            params.put("type_mess_id",ptype);
            params.put("content",context);
            params.put("uid",BaseApplication.app.getUser().getId());
            final Map<String, File> files = new HashMap<String, File>();

            for(int i=0;i<filePath.size();i++){
                File file=new File(filePath.get(i));
                StringBuffer buffer=new StringBuffer();
                buffer.append("img_url_").append(i+1);
                files.put(buffer.toString(),file);
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
            Toast.makeText(getBaseContext(),"发布成功",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void listenerSoftInput() {
        final View activityRootView = mainline;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect outRect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                Log.d("==============s2", heightDiff+"");
                if (heightDiff>400) { // 如果高度差超过100像素，就很有可能是有软键盘..//// .
                    emojiLine.setVisibility(View.VISIBLE);
                    if(fram_emoji.getHeight()>0){
                        ViewGroup.LayoutParams params = fram_emoji.getLayoutParams();
                        params.height = 0;
                        fram_emoji.setLayoutParams(params);
                        fram_emoji.requestLayout();
                        emisShow=false;
                    }
                }else {
                    //emojiLine.setVisibility(View.INVISIBLE);
                    if(emisShow) {
                        ViewGroup.LayoutParams params = fram_emoji.getLayoutParams();
                        params.height = 210 * 2;
                        fram_emoji.setLayoutParams(params);
                        fram_emoji.requestLayout();
                    }else {
                        ViewGroup.LayoutParams params = fram_emoji.getLayoutParams();
                        params.height = 0;
                        fram_emoji.setLayoutParams(params);
                        fram_emoji.requestLayout();
                        emojiLine.setVisibility(View.INVISIBLE);
                        emisShow=false;
                    }
                }
            }
        });

    }

    private void getEmoji(){
        fram_emoji=(FrameLayout)findViewById(R.id.emoji_fram_ct);
        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.emoji_fram_ct,faceFragment).commit();
        icon=(ImageView)findViewById(R.id.emoji_icon_ct);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emisShow=true;
                InputMethodManager inputManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                }
                etparent.setFocusable(true);
                etparent.setFocusableInTouchMode(true);
                etparent.requestFocus();
            }
        });
        faceFragment.setListener(new FaceFragment.OnEmojiClickListener() {
            @Override
            public void onEmojiDelete() {
                String text = et1.getText().toString();
                if (text.isEmpty()) {
                    return;
                }
                if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                    int index = text.lastIndexOf("[");
                    if (index == -1) {
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        et1.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        try {
                            EmojiUtil.handlerEmojiText2(et1, et1.getText().toString(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    et1.getText().delete(index, text.length());
                    try {
                        EmojiUtil.handlerEmojiText2(et1, et1.getText().toString(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                et1.onKeyDown(KeyEvent.KEYCODE_DEL, event);

            }

            @Override
            public void onEmojiClick(Emoji emoji) {

                if (emoji != null) {
                    int index = et1.getSelectionStart();
                    Editable editable = et1.getEditableText();
                    editable.append(emoji.getContent());
                    try {
                        EmojiUtil.handlerEmojiText2(et1, et1.getText().toString(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
}
