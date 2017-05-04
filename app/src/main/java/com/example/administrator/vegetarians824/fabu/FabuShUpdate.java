package com.example.administrator.vegetarians824.fabu;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CitySelectScrollView;
import com.example.administrator.vegetarians824.entry.BitmapUtils;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.HttpUtil;
import com.example.administrator.vegetarians824.util.PhoneFormatCheckUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.UpLoadUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONArray;
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

public class FabuShUpdate extends AppCompatActivity {
    String item_id;
    private EditText et1,et2,et3,et4,et5;
    private Mhander handler;
    private GridView gridView1;              //网格显示缩略图
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private List<String> oldima;
    private HashMap<String, Object> map;
    private SimpleAdapter simpleAdapter;     //适配器
    TextView tv;
    FrameLayout fm;
    String title,content,tel,longitude,latitude,uid,address,price;
    Button button;
    private List<String> postImage;
    private static final int REQUEST_CODE = 732;
    private static final int EDIT_CODE=101;
    private ArrayList<String> mResults = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_sh_update);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        Intent intent=getIntent();
        item_id=intent.getStringExtra("item_id");
        initView();
        getData();
        admits();
    }
    public void initView(){
        et1=(EditText)findViewById(R.id.fabu000_et1);
        et2=(EditText)findViewById(R.id.fabu000_et2);
        et3=(EditText)findViewById(R.id.fabu000_et3);
        et4=(EditText)findViewById(R.id.fabu000_et4);
        et5=(EditText)findViewById(R.id.fabu000_et5);
        fm=(FrameLayout)findViewById(R.id.fabu000_getpic);
        tv=(TextView)findViewById(R.id.fabu000_tv);
        button=(Button)findViewById(R.id.fabu000_bt);
        gridView1 = (GridView)findViewById(R.id.gridView000);
        imageItem = new ArrayList<HashMap<String, Object>>();
        oldima=new ArrayList<>();
        postImage=new ArrayList<>();
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.updata00_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void getData(){
        StringRequest request=new StringRequest("http://www.isuhuo.com/plainliving/Androidapi/Map/restaurant_detial" + "/res_id/" + item_id + "/p/1/t/100", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONObject js2=js1.getJSONObject("Result");
                    JSONArray ja = js2.getJSONArray("detail");
                    JSONObject joo = ja.getJSONObject(0);
                    et1.setText(joo.getString("title"));
                    et2.setText(joo.getString("address"));
                    et3.setText(joo.getString("tel"));
                    et5.setText(joo.getString("unit_price"));
                    if(!joo.isNull("img_url_w_1")){
                        oldima.add(joo.getString("img_url_w_1"));
                    }
                    if(!joo.isNull("img_url_w_2")){
                        oldima.add(joo.getString("img_url_w_2"));
                    }
                    if(!joo.isNull("img_url_w_3")){
                        oldima.add(joo.getString("img_url_w_3"));
                    }
                    if(!joo.isNull("img_url_w_4")){
                        oldima.add(joo.getString("img_url_w_4"));
                    }
                    if(!joo.isNull("img_url_w_5")){
                        oldima.add(joo.getString("img_url_w_5"));
                    }
                    if(!joo.isNull("img_url_w_6")){
                        oldima.add(joo.getString("img_url_w_6"));
                    }
                    if(oldima.size()>0){
                        gridView1.setVisibility(View.VISIBLE);
                        fm.setBackgroundColor(0xfff);
                        tv.setVisibility(View.INVISIBLE);
                    }

                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic);
                    map = new HashMap<String, Object>();
                    map.put("itemImage", bmp);
                    imageItem.add(map);
                    handler = new Mhander();
                    if(oldima.size()>0) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                for(int i=0;i<oldima.size();i++){
                                    Bitmap bm= null;
                                    Bitmap delbmp = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                                    try {
                                        bm = HttpUtil.getBitmap(URLMannager.Imag_URL+oldima.get(i));
                                        map = new HashMap<String, Object>();
                                        map.put("itemImage",bm);
                                        map.put("del",delbmp);
                                        imageItem.add(map);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);// handle交给管理者处理
                            }
                        }.start();
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
        SlingleVolleyRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    class Mhander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2) {
                for(int i=1;i<imageItem.size();i++) {
                    saveBitmapFile((Bitmap) imageItem.get(i).get("itemImage"),"history"+i+".jpg");
                }
                simpleAdapter = new SimpleAdapter(getBaseContext(), imageItem, R.layout.griditem_addpic, new String[] { "itemImage","del"}, new int[] { R.id.imageView1,R.id.delete_pic});
                simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(View view, Object data,
                                                String textRepresentation) {
                        // TODO Auto-generated method stub
                        if (view instanceof ImageView && data instanceof Bitmap) {
                            ImageView i = (ImageView) view;
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
                                //每次选择1张图片，调用系统方法
                                //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                //startActivityForResult(intent, IMAGE_OPEN);
                                //自己写的框架 可多张勾选 可筛选 可拍照
                                mResults=new ArrayList<String>();
                                Intent intent = new Intent(getBaseContext(), ImagesSelectorActivity.class);
                                // 最多选几张图片
                                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 7-imageItem.size());
                                // 筛选10k以上的图片
                                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                                // 允许使用相机
                                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                                // pass current selected images as the initial value
                                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                                // start the selector
                                startActivityForResult(intent, REQUEST_CODE);
                            }
                            //通过onResume()刷新数据
                        }
                        else {
                            dialog(position);
                        }
                    }
                });
            }
        }
    }

    public void saveBitmapFile(Bitmap bitmap,String name){
        File file=new File("/sdcard/"+name);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       postImage.add(file.getPath());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mResults= data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
            }
        }else {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mResults.size()>0){
            for(String peth : mResults) {
                //图片压缩为原来的1/2
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap addbmp=BitmapFactory.decodeFile(peth,options);
                //加上右上角的x号
                Bitmap delbmp = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("itemImage", addbmp);
                map.put("del",delbmp);
                imageItem.add(map);

                postImage.add(peth);
            }
            simpleAdapter = new SimpleAdapter(getBaseContext(),
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


        }
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FabuShUpdate.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                postImage.remove(position-1);
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

    //提交按钮
    public void admits(){

        Button send=(Button)findViewById(R.id.fabu000_bt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et1.getText().toString();
                content=et4.getText().toString();
                tel=et3.getText().toString();
                price=et5.getText().toString();
                uid=BaseApplication.app.getUser().getId();
                address=et2.getText().toString();
                GeocodeSearch geocoderSearch = new GeocodeSearch(getBaseContext());
                GeocodeQuery query=new GeocodeQuery(address,BaseApplication.app.getMyLociation().getMyCity());
                geocoderSearch.getFromLocationNameAsyn(query);
                geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        latitude=geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude()+"";
                        longitude=geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude()+"";
                    }
                });
                if(title.length()<2){
                    Toast.makeText(getBaseContext(),"商户名称至少输入2个字",Toast.LENGTH_SHORT).show();
                }else {
                    if(tel.length()>0&& !(PhoneFormatCheckUtils.isChinaPhoneLegal(tel)||PhoneFormatCheckUtils.isFixedPhone(tel))){
                        Toast.makeText(getBaseContext(),"请输入正确格式的电话号码",Toast.LENGTH_SHORT).show();
                    }else {
                        if(content.length()<10){
                            Toast.makeText(getBaseContext(),"点评至少输入10个字",Toast.LENGTH_SHORT).show();
                        }else {
                           new picdoPost().execute(URLMannager.FabuSHPost);

                        }
                    }
                }
            }
        });
    }
    //post提交修改
    public class picdoPost extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("title",title);
            params.put("content",content);
            params.put("tel",tel);
            params.put("longitude",longitude);
            params.put("latitude",latitude);
            params.put("uid",uid);
            params.put("address",address);
            params.put("unit_price",price);
            params.put("id",item_id);
            final Map<String, File> files = new HashMap<String, File>();

            for(int i=0;i<postImage.size();i++){
                File file=new File(postImage.get(i));
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
            finish();
        }
    }
}
