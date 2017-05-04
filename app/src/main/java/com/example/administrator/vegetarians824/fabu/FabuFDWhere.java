package com.example.administrator.vegetarians824.fabu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.dongdong.CantingDetail;
import com.example.administrator.vegetarians824.entry.CantingInfo;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FabuFDWhere extends AppCompatActivity {
    private TextView fanhui;
    private EditText et;
    private ListView listView;
    private List<CantingInfo> list_ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu_fdwhere);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initView();
    }
    public void initView(){
        fanhui=(TextView)findViewById(R.id.mapsearch_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView=(ListView)findViewById(R.id.mapsearch_list);
        et=(EditText)findViewById(R.id.mapsearch_et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().equals("")){
                    listView.setVisibility(View.INVISIBLE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                    searchRequest(editable.toString());
                }

            }
        });
    }
    public void searchRequest(String s){
        list_ct=new ArrayList<>();
        StringBuffer url=new StringBuffer();
        try {
            url.append(URLMannager.MiShiSearchKeyword).append(URLEncoder.encode(s, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringRequest request=new StringRequest(url.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {

                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for (int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        CantingInfo ct=new CantingInfo();
                        ct.setTitle(jo.getString("title"));
                        ct.setId(jo.getString("id"));
                        ct.setType(jo.getString("type"));
                        list_ct.add(ct);
                    }
                    if(list_ct.size()>0){
                        SearchAdapter sa=new SearchAdapter(list_ct,getBaseContext());
                        listView.setAdapter(sa);
                        sa.notifyDataSetChanged();
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
    public class SearchAdapter extends BaseAdapter {
        private List<CantingInfo> mydata;
        private Context context;
        public SearchAdapter(List<CantingInfo> mydata,Context context){
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
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(context).inflate(R.layout.search_map_item, null);
            TextView tv= (TextView) view.findViewById(R.id.search_map_tv);
            tv.setText(mydata.get(i).getTitle());
            ImageView ima = (ImageView) view.findViewById(R.id.search_map_ima);
            if (mydata.get(i).getType().equals("6")) {
                ima.setImageResource(R.drawable.lenovo2);
            }
            final int x=i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getBaseContext(),FabuFD.class);
                    intent.putExtra("id",mydata.get(x).getId());
                    intent.putExtra("title",mydata.get(x).getTitle());
                    setResult(14,intent);
                    finish();
                }
            });
            return view;
        }
    }
}
