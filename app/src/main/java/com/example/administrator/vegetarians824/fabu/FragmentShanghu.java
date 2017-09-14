package com.example.administrator.vegetarians824.fabu;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.LoadingDialog;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.PhoneFormatCheckUtils;
import com.example.administrator.vegetarians824.util.UpLoadUtil;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentShanghu extends Fragment {

    private GridView gridView1;              //网格显示缩略图
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private HashMap<String, Object> map;
    private List<String> filePath;
    private SimpleAdapter simpleAdapter;     //适配器
    private TextView tv;
    private FrameLayout fm;
    private EditText et1,et2,et3,et4;
    private String title,content,tel,longitude,latitude,uid,address;
    private Button button;
    private RadioGroup group1,group2;
    private static final int REQUEST_CODE = 732;
    private ArrayList<String> mResults = new ArrayList<>();
    private int type=0,park=0;
    private TextView time1,time2;
    private String start="",end="";
    private int hour;
    private int minute;
    private Calendar calendar;
    private LoadingDialog loadingDialog;
    public FragmentShanghu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_fragment_shanghu, container, false);
        loadingDialog=new LoadingDialog(getActivity());
        loadingDialog.setMessage("正在上传");
        et1=(EditText)v.findViewById(R.id.fabu01_et1);
        et2=(EditText)v.findViewById(R.id.fabu01_et2);
        et3=(EditText)v.findViewById(R.id.fabu01_et3);
        et4=(EditText)v.findViewById(R.id.fabu01_et4);
        fm=(FrameLayout)v.findViewById(R.id.fabu01_getpic);
        tv=(TextView)v.findViewById(R.id.fabu01_tv);
        button=(Button) v.findViewById(R.id.fabu01_bt);
        gridView1 = (GridView) v.findViewById(R.id.gridView1);
        imageItem = new ArrayList<HashMap<String, Object>>();
        group1=(RadioGroup)v.findViewById(R.id.fabu01_group1);
        group2=(RadioGroup)v.findViewById(R.id.fabu01_group2);
        time1=(TextView)v.findViewById(R.id.fabu01_time1);
        time2=(TextView)v.findViewById(R.id.fabu01_time2);
        calendar = Calendar.getInstance();
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        initop();
        setpic();
        sendPost();
        // Inflate the layout for this fragment
        return v;
    }
    public void initop(){
        if(BaseApplication.app.getMyLociation()!=null)
            et2.setText(BaseApplication.app.getMyLociation().getMyaddress());
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("========sel1",checkedId+"");
                type=checkedId;
            }
        });
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("========sel2",checkedId+"");
                park=checkedId;
            }
        });
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time1.setText(i+":"+i1);
                        start=i+":"+i1;
                    }
                },hour, minute, true);
                timePicker.show();
            }
        });
        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker=new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time2.setText(i+":"+i1);
                        end=i+":"+i1;
                    }
                },hour, minute, true);
                timePicker.show();
            }
        });
    }

    public void setpic(){

        //放置加号图
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic);
        map = new HashMap<String, Object>();
        filePath=new ArrayList<>();
        map.put("itemImage", bmp);
        imageItem.add(map);

        simpleAdapter = new SimpleAdapter(getContext(), imageItem, R.layout.griditem_addpic, new String[] { "itemImage"}, new int[] { R.id.imageView1});

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
                        Toast.makeText(getContext(),"最多只能选择6张",Toast.LENGTH_SHORT).show();
                    }else {
                        //每次选择1张图片，调用系统方法
                        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //startActivityForResult(intent, IMAGE_OPEN);

                        //自己写的框架 可多张勾选 可筛选 可拍照
                        mResults=new ArrayList<String>();
                        Intent intent = new Intent(getContext(), ImagesSelectorActivity.class);
                        // 最多选几张图片
                        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 6);
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
        fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageItem.size()==1) {
                    mResults=new ArrayList<String>();
                    //Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(intent, IMAGE_OPEN);
                    Intent intent = new Intent(getContext(), ImagesSelectorActivity.class);
                    // max number of images to be selected
                    intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 6);
                    // min size of image which will be shown; to filter tiny images (mainly icons)
                    intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
                    // show camera or not
                    intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
                    // pass current selected images as the initial value
                    intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
                    // start the selector
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE) {
            if(resultCode == getActivity().RESULT_OK) {
                gridView1.setVisibility(View.VISIBLE);
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mResults.size()>0){
            fm.setBackgroundColor(0xfff);
            tv.setVisibility(View.INVISIBLE);
            filePath.add(pathImage);
            /*
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            Bitmap delbmp = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            map.put("del",delbmp);
            imageItem.add(map);
            */
            imageItem=new ArrayList<>();
            //重置所有图片 加上第一张添加图片的图
            HashMap<String, Object> maps = new HashMap<String, Object>();
            maps.put("itemImage", bmp);
            imageItem.add(map);

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
            }

            simpleAdapter = new SimpleAdapter(getContext(),
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

        }
        StatService.onResume(this);
    }

    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                //filePath.remove(position-1);
                mResults.remove(position-1);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=et1.getText().toString();
                content=et4.getText().toString();
                tel=et3.getText().toString();
                longitude=BaseApplication.app.getMyLociation().getLongitude();
                latitude=BaseApplication.app.getMyLociation().getLatitude();
                uid=BaseApplication.app.getUser().getId();
                address=et2.getText().toString();
                if(title.length()<2){
                    Toast.makeText(getContext(),"商户名称至少输入2个字",Toast.LENGTH_SHORT).show();
                }else {
                    if(tel.length()>0&& !(PhoneFormatCheckUtils.isChinaPhoneLegal(tel)||PhoneFormatCheckUtils.isFixedPhone(tel))){
                        Toast.makeText(getContext(),"请输入正确格式的电话号码",Toast.LENGTH_SHORT).show();
                    }else {
                        if(content.length()<10){
                            Toast.makeText(getContext(),"点评至少输入10个字",Toast.LENGTH_SHORT).show();
                        }else {
                            loadingDialog.show();
                            new picdoPost().execute(URLMannager.FabuSHPost);
                        }
                    }
                }
                //new picdoPost().execute("http://www.isuhuo.com/plainLiving/Androidapi/addapi/add_restaurant");
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
            params.put("tel",tel);
            params.put("longitude",longitude);
            params.put("latitude",latitude);
            params.put("uid",uid);
            params.put("address",address);
            if(type!=0){
                params.put("food_type",type+"");
            }
            if(park!=0){
                params.put("parking_status",(park-3)+"");
            }
            if(!start.equals("")&&!end.equals("")){
                params.put("open_times",start+"~"+end);
            }
            final Map<String, File> files = new HashMap<String, File>();

            for(int i=0;i<mResults.size();i++){
                File file=new File(mResults.get(i));
                Log.d("======ss1",file.exists()+"");
                Log.d("======ss2",file.getName());
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
            try {
                JSONObject js1=new JSONObject(s);
                if(js1.getString("Code").equals("1")){
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(),"发布成功，等待审核",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else {
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
