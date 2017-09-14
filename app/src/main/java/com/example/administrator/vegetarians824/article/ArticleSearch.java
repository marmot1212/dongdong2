package com.example.administrator.vegetarians824.article;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.adapter.ArticleListAdapter;
import com.example.administrator.vegetarians824.entry.Tiezi;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleSearch extends AppCompatActivity {
    private EditText et;
    private TextView cancel;
    private TextView none;
    private ListView listView;
    private List<Tiezi> list_tz;
    private TextView tvf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_search);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        initView();
    }
    public void initView(){
        tvf=new TextView(getBaseContext());
        tvf.setText("已经全部加载完毕");
        tvf.setTextSize(12);
        tvf.setTextColor(0xffa0a0a0);
        AbsListView.LayoutParams params=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        tvf.setLayoutParams(params);
        tvf.setGravity(Gravity.CENTER);
        et=(EditText)findViewById(R.id.article_search_et);
        cancel=(TextView)findViewById(R.id.article_search_cancel);
        none=(TextView)findViewById(R.id.article_search_none);
        listView=(ListView)findViewById(R.id.article_search_list);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")){
                    getList(s.toString());
                }else {
                    none.setVisibility(View.GONE);
                    list_tz=new ArrayList<>();
                    if(listView.getFooterViewsCount()>0)
                        listView.removeFooterView(tvf);
                    listView.setAdapter(new ArticleListAdapter(list_tz,ArticleSearch.this));
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void getList(String keyword){
        none.setVisibility(View.GONE);
        list_tz=new ArrayList<>();
        if(listView.getFooterViewsCount()>0)
            listView.removeFooterView(tvf);
        StringRequest request=new StringRequest(URLMannager.FaXian+"p/1/t/1000/keyword/"+keyword, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("Code").equals("1")){
                        JSONObject js2=js1.getJSONObject("Result");
                        JSONArray ja=js2.getJSONArray("list");
                        if(ja.length()==0){
                            none.setVisibility(View.VISIBLE);
                        }else {
                            listView.addFooterView(tvf);
                        }
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Tiezi tz=new Tiezi();
                            tz.setId(jo.getString("id"));
                            tz.setTitle(jo.getString("title"));
                            tz.setPic(jo.getString("img_url_th_1"));
                            tz.setContent(jo.getString("content"));
                            tz.setCreate_time_text(jo.getString("create_time_text"));
                            list_tz.add(tz);
                        }
                        listView.setAdapter(new ArticleListAdapter(list_tz,ArticleSearch.this));

                    }else {
                        none.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(),js1.getString("Message"),Toast.LENGTH_SHORT).show();
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

}
