package com.example.administrator.vegetarians824.fabu;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.UpdateArea;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.UpLoadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHuodong extends Fragment {
    private Calendar calendar;
    String mcontent,mtitle,mstart_time,mfinish_time,murl,mprovince,maddress;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private TextView time01,date01,time02,date02;
    private ImageView pic;
    TextView mpro;
    File file;
    EditText et1,et2,et3,et4;
    private final  int PHONE_PHOTO  = 1;
    FrameLayout fram;
    LinearLayout date11;
    Button bt;
    LinearLayout time11;
    LinearLayout date22;
    LinearLayout time22;
    LinearLayout area;
    public FragmentHuodong() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_huodong, container, false);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour=calendar.get(Calendar.HOUR_OF_DAY);
        minute=calendar.get(Calendar.MINUTE);
        pic=(ImageView)v.findViewById(R.id.fabu05_pic);
        et1=(EditText)v.findViewById(R.id.fabu05_et1);
        et2=(EditText)v.findViewById(R.id.fabu05_et2);
        et3=(EditText)v.findViewById(R.id.fabu05_et3);
        et4=(EditText)v.findViewById(R.id.fabu05_et4);
        mpro=(TextView)v.findViewById(R.id.fabu05_city);

        date01=(TextView) v.findViewById(R.id.fabu05_date1_tv);
        date02=(TextView) v.findViewById(R.id.fabu05_date2_tv);
        time01=(TextView) v.findViewById(R.id.fabu05_time1_tv);
        time02=(TextView) v.findViewById(R.id.fabu05_time2_tv);
        date11=(LinearLayout)v.findViewById(R.id.fabu05_date1);
        time11=(LinearLayout)v.findViewById(R.id.fabu05_time1);
        date22=(LinearLayout)v.findViewById(R.id.fabu05_date2);
        time22=(LinearLayout)v.findViewById(R.id.fabu05_time2);
        area=(LinearLayout)v.findViewById(R.id.fabu05_area);
        bt=(Button) v.findViewById(R.id.fabu05_bt);
        fram=(FrameLayout)v.findViewById(R.id.fabu05_fram);
        initop();
        getpic();
        sendPost();

        return v;
    }
    public void initop(){

        date11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date01.setText(i+"-"+(i1+1)+"-"+i2);
                    }
                },year, month, day);
                datePicker.show();


            }
        });

        time11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time01.setText(i+":"+i1);
                    }
                },hour, minute, true);

                timePicker.show();
            }
        });

        date22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date02.setText(i+"-"+(i1+1)+"-"+i2);
                    }
                },year, month, day);
                datePicker.show();

            }
        });


        time22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        time02.setText(i+":"+i1);
                    }
                },hour, minute, true);

                timePicker.show();
            }
        });
        if(BaseApplication.app.getMyLociation()!=null)
            et4.setText(BaseApplication.app.getMyLociation().getMyaddress());

        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), UpdateArea.class);
                intent.putExtra("flag","2");
                getActivity().startActivity(intent);
            }
        });
    }

    public void getpic(){
        fram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,PHONE_PHOTO);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK &&requestCode==PHONE_PHOTO){
            Log.d("=========ss",data.toString());
            Uri uri = data.getData();
            if(!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor = getActivity().getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                if (null == cursor) {
                    return;
                }
                cursor.moveToFirst();
                String capturePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                if (capturePath != null) {
                    Bitmap mybit = BitmapFactory.decodeFile(capturePath);
                    saveBitmapFile(mybit);
                    pic.setImageBitmap(mybit);
                }
            }
        }

    }

    public void saveBitmapFile(Bitmap bitmap){
        file=new File("/sdcard/02.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mpro.setText(BaseApplication.app.getAcity());
    }

    public void sendPost(){

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtitle=et1.getText().toString();
                mcontent=et2.getText().toString();
                mstart_time=date01.getText().toString()+" "+time01.getText().toString();
                mfinish_time=date02.getText().toString()+" "+time02.getText().toString();
                murl=et3.getText().toString();
                mprovince=mpro.getText().toString();
                if(pic.getDrawable()==null){
                    Toast.makeText(getContext(),"请添加图片",Toast.LENGTH_SHORT).show();
                }else {
                    if(mtitle.length()<2){
                        Toast.makeText(getContext(),"活动名称至少输入2个字",Toast.LENGTH_SHORT).show();
                    }else {
                        if(mcontent.length()<10){
                            Toast.makeText(getContext(),"活动内容至少输入10个字",Toast.LENGTH_SHORT).show();
                        }else {
                            if(mstart_time.equals("")||mfinish_time.equals("")){
                                Toast.makeText(getContext(),"请添加时间",Toast.LENGTH_SHORT).show();
                            }else {
                                if(mprovince.equals("")){
                                    Toast.makeText(getContext(),"请添加地区",Toast.LENGTH_SHORT).show();
                                }else {
                                    new picdoPost().execute(URLMannager.FabuHDPost);
                                }
                            }
                        }
                    }
                }

            }
        });
    }

    public class picdoPost extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("title",mtitle);
            params.put("content",mcontent);
            params.put("start_time",mstart_time);
            params.put("finish_time",mfinish_time);
            params.put("url",murl);
            params.put("province",mprovince);
            params.put("address",BaseApplication.app.getMyLociation().getMyaddress());
            params.put("longitude",BaseApplication.app.getMyLociation().getLongitude());
            params.put("latitude",BaseApplication.app.getMyLociation().getLatitude());
            params.put("uid",BaseApplication.app.getUser().getId());
            final Map<String, File> files = new HashMap<String, File>();
            files.put("img_url",file);
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
                    Toast.makeText(getContext(),"发布成功,等待审核",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else {
                    Toast.makeText(getContext(),job.getString("Message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
