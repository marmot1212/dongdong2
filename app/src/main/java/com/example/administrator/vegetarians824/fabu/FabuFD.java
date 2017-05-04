package com.example.administrator.vegetarians824.fabu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Yongliao;
import com.example.administrator.vegetarians824.entry.Zuofa;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.myView.ListViewForScrollView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.example.administrator.vegetarians824.util.UpLoadUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FabuFD extends AppCompatActivity {
    ListViewForScrollView listview1,listview2;
    List<Yongliao> list_yl;
    List<Zuofa> list_zf;
    FabuAdapter01 adapter01;
    FabuAdapter02 adapter02;
    ImageView headima;
    int flag;
    int count;
    String headname;
    List<Bitmap> list_b;
    List<Boolean> list_is;
    boolean isfist;
    EditText et1,et2,et3,et4;
    String dress,stp;
    private List<String> list_think;
    private String res_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_fd);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        listview1=(ListViewForScrollView)findViewById(R.id.fabu04_list1);
        listview2=(ListViewForScrollView)findViewById(R.id.fabu04_list2);
        list_yl=new ArrayList<>();
        list_zf=new ArrayList<>();
        list_b=new ArrayList<>();
        list_is=new ArrayList<>();
        list_think=new ArrayList<>();
        isfist=true;
        et1=(EditText)findViewById(R.id.fabu04_et1) ;
        et2=(EditText)findViewById(R.id.fabu04_et2) ;
        et3=(EditText)findViewById(R.id.fabu04_et3) ;
        et4=(EditText)findViewById(R.id.fabu04_et4) ;
        flag=0;
        count=0;
        headima=(ImageView)findViewById(R.id.fabu_food_headima);
        initoperate();
        initList();
        setHeadIma();
        send();
    }
    public void initoperate(){
        LinearLayout fanhui=(LinearLayout)findViewById(R.id.fabu04_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FabuFD.this,FabuFDWhere.class);
                startActivityForResult(intent,13);
            }
        });
    }

    public void initList(){
        //初始化list1
        Yongliao yl=new Yongliao();
        list_yl.add(yl);
        adapter01=new FabuAdapter01(list_yl,getBaseContext());
        listview1.setAdapter(adapter01);

        LinearLayout add1=(LinearLayout)findViewById(R.id.fabu04_add1);
        add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Yongliao yy=new Yongliao();
                list_yl.add(yy);
                adapter01.notifyDataSetChanged();
            }
        });
        //初始化list2
        Zuofa zf=new Zuofa();
        list_zf.add(zf);
        adapter02=new FabuAdapter02(list_zf,getBaseContext());
        listview2.setAdapter(adapter02);

        LinearLayout add2=(LinearLayout)findViewById(R.id.fabu04_add2);
        add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Zuofa zz=new Zuofa();
                list_zf.add(zz);
                adapter02.notifyDataSetChanged();
            }
        });

    }

    public void setHeadIma(){
        FrameLayout fram=(FrameLayout)findViewById(R.id.fabu_food_head);
        fram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

    }

    public void send(){
        Button bt=(Button)findViewById(R.id.fabu04_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer buffer1=new StringBuffer();
                for(int i=0;i<list_yl.size();i++){
                    buffer1.append(list_yl.get(i).getName()).append(":").append(list_yl.get(i).getNum()).append("|");
                }
                dress=buffer1.toString();
                dress=dress.substring(0,dress.length()-1);
                StringBuffer buffer2=new StringBuffer();
                for(int i=0;i<list_zf.size();i++){
                    buffer2.append(list_zf.get(i).getDep());
                    if(!list_zf.get(i).getPic().equals("")){
                        buffer2.append("[img]").append(list_zf.get(i).getPic()).append("|");
                    }else {
                        buffer2.append("|");
                    }

                }
                stp=buffer2.toString();
                stp=stp.substring(0,stp.length()-1);
                if(headima.getDrawable()==null){
                    Toast.makeText(getBaseContext(),"请添加封面",Toast.LENGTH_SHORT).show();
                }else {
                    if(et1.getText().toString().length()<2){
                        Toast.makeText(getBaseContext(),"菜谱名称至少输入2个字",Toast.LENGTH_SHORT).show();
                    }else {
                        if(et2.getText().toString().length()<10){
                            Toast.makeText(getBaseContext(),"菜谱介绍至少输入10个字",Toast.LENGTH_SHORT).show();
                        }else {
                            if(!(list_yl.size()>=1&&!list_yl.get(0).getName().equals(""))){
                                Toast.makeText(getBaseContext(),"请添加用料",Toast.LENGTH_SHORT).show();
                            }else {
                                if(!(list_zf.size()>=1&&!list_zf.get(0).getDep().equals(""))){
                                    Toast.makeText(getBaseContext(),"请添加步骤",Toast.LENGTH_SHORT).show();
                                }else {
                                    sendpost();
                                }
                            }
                        }
                    }
                }
                //sendpost();
            }
        });
    }

    public class FabuAdapter01 extends BaseAdapter{
        private List<Yongliao> mydata;
        private Context context;
        public FabuAdapter01(List<Yongliao> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
             view= LayoutInflater.from(context).inflate(R.layout.fabu_food_yl,null);
             final AutoCompleteTextView et1=(AutoCompleteTextView) view.findViewById(R.id.fabu_food_et1);
             et1.setThreshold(1);
             EditText et2=(EditText) view.findViewById(R.id.fabu_food_et2);
            LinearLayout del=(LinearLayout) view.findViewById(R.id.fabu_food_del);
            et1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!editable.toString().equals(""))
                        getAssociation(editable.toString(),et1);
                    mydata.get(i).setName(editable.toString());

                }
            });
            et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    mydata.get(i).setNum(editable.toString());

                }
            });
            et1.setText(mydata.get(i).getName());
            et2.setText(mydata.get(i).getNum());
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mydata.remove(i);
                    adapter01.notifyDataSetChanged();
                }
            });
            return view;
        }
    }
    public class FabuAdapter02 extends BaseAdapter{
        private List<Zuofa> mydata;
        private Context context;
        public FabuAdapter02(List<Zuofa> mydata,Context context){
            this.mydata=mydata;
            this.context=context;
        }
        @Override
        public int getCount() {
            return mydata.size();
        }

        @Override
        public Object getItem(int i) {
            return mydata.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View views=LayoutInflater.from(context).inflate(R.layout.fabu_food_zf,null);
            TextView step=(TextView)views.findViewById(R.id.fabu_food_step);
            FrameLayout fram=(FrameLayout)views.findViewById(R.id.fabu_food_fram);
            LinearLayout del=(LinearLayout)views.findViewById(R.id.fabu_food_del2);
            EditText stepdes=(EditText) views.findViewById(R.id.fabu_food_stepdes);
            step.setText((i+1)+"");
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mydata.remove(i);
                    adapter02.notifyDataSetChanged();
                }
            });
            stepdes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    mydata.get(i).setDep(editable.toString());
                }
            });
            stepdes.setText(mydata.get(i).getDep());
            fram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flag=2;
                    count=i;
                    list_is.add(true);
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,1);
                }
            });
            if(mydata.get(i).getBp()!=null){
                ImageView headima=(ImageView)views.findViewById(R.id.fabu_food_stepima);
                headima.setImageBitmap(mydata.get(i).getBp());
            }
            return views;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK &&requestCode == 1){
            Cursor cursor =
                    this.getContentResolver().query(
                            data.getData(),
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
            if (null == cursor) {
                return;
            }
            cursor.moveToFirst();
            String pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
            if(!pathImage.equals("")) {
                Bitmap mybit = BitmapFactory.decodeFile(pathImage);
                if (flag == 1) {
                    headima.setImageBitmap(mybit);
                } else if (flag == 2) {
                    list_zf.get(count).setBp(mybit);
                    adapter02.notifyDataSetChanged();
                }
                new doPost().execute(pathImage);
            }
        }else {
            if(resultCode==14){
                et4.setText(data.getStringExtra("title"));
                res_id=data.getStringExtra("id");
            }else {
                return;
            }
        }


    }

    public class doPost extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            final Map<String, String> params = new HashMap<String, String>();
            params.put("uid", BaseApplication.app.getUser().getId());

            if(flag==1){
                if(!isfist){
                    params.put("oldpic",headname);
                }else {
                    isfist=false;
                }
            }

            if(flag==2){
                if(!list_is.get(count)){
                 params.put("oldpic",headname);
                }else {
                    list_is.set(count,false);
                }
            }

            final Map<String, File> files = new HashMap<String, File>();
            File file=new File(strings[0]);
            files.put("img_title ",file);
            String s="";
            try {
                s= UpLoadUtil.post(URLMannager.FabuFDPostIMA,params,files);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(flag==1){
                try {
                    JSONObject js1=new JSONObject(s);
                    headname=js1.getString("Result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(flag==2){
                try {
                    JSONObject js1=new JSONObject(s);
                    String name=js1.getString("Result");
                    list_zf.get(count).setPic(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendpost(){
        StringPostRequest spr=new StringPostRequest(URLMannager.FabuFDPostTXT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Log.d("========sssss",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    String m=js1.getString("Message");
                    if(js1.getString("Code").equals("1")){
                        Toast.makeText(getBaseContext(),"发布成功，等待审核",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getBaseContext(),m,Toast.LENGTH_SHORT).show();
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
        /*
        Log.d("=========id",BaseApplication.app.getUser().getId());
        Log.d("=========title",et1.getText().toString());
        Log.d("=========content",et2.getText().toString());
        Log.d("=========reminded",et3.getText().toString());
        Log.d("=========dress",dress);
        Log.d("=========step",stp);
        Log.d("=========img_title",headname);
        */
        spr.putValue("uid",BaseApplication.app.getUser().getId());
        spr.putValue("title",et1.getText().toString());
        spr.putValue("content",et2.getText().toString());
        spr.putValue("reminded",et3.getText().toString());
        spr.putValue("dress",dress);
        spr.putValue("step",stp);
        spr.putValue("img_title",headname);
        if(!res_id.equals("")){
            spr.putValue("restaurant_id",res_id);
        }
        SlingleVolleyRequestQueue.getInstance(FabuFD.this).addToRequestQueue(spr);
    }

    public void getAssociation(String s, final AutoCompleteTextView aut){
        list_think=new ArrayList<>();
        String url= null;
        try {
            url = URLMannager.FabuFDSearch+ URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final StringRequest request=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        list_think.add(jo.getString("title"));
                    }
                    if(list_think.size()>0){
                        ArrayAdapter ad=new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1,list_think);
                        aut.setAdapter(ad);
                        ad.notifyDataSetChanged();
                        Log.d("========s",list_think.get(0));
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
        SlingleVolleyRequestQueue.getInstance(getBaseContext()).addToRequestQueue(request);
    }
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
