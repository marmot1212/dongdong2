package com.example.administrator.vegetarians824.veganpass;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPreference extends Fragment {


    public MyPreference() {
        // Required empty public constructor
    }

    private Context context;
    private String code="";
    private List<Ingredient> list_preference;
    private GridView gridView;
    private GridViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my_preference, container, false);
        if(getContext()!=null) {
            context=getContext();
            SharedPreferences pre=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            code=pre.getString("phoneid","");
            list_preference=new ArrayList<>();
            initView(v);
            getData();

        }
        return v;
    }

    public void initView(View v){
        gridView=(GridView)v.findViewById(R.id.pregerence_grid);
    }
    public void getData(){
        StringRequest request=new StringRequest(URLManager.PreferenceSearch+"/code/"+code+"/type/1", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("=======s",s);
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        JSONArray ja=js1.getJSONArray("data");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            Ingredient ingredient=new Ingredient();
                            ingredient.setId(jo.getString("id"));
                            ingredient.setName(jo.getString("name"));
                            ingredient.setStatus(jo.getString("status"));
                            list_preference.add(ingredient);
                        }
                    }

                    getAvoid();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public class GridViewAdapter extends BaseAdapter{
        private List<Ingredient> data;
        private Context context;
        public GridViewAdapter(List<Ingredient> data,Context context){
            this.data=data;
            this.context=context;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv=new TextView(context);
            tv.setText(data.get(position).getName());
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(16,8,16,8);
            final int x=position;

            if(data.get(position).ischeck()){
                tv.setBackgroundColor(0x8e000000);
                tv.setTextColor(0xffffffff);
            }else {
                tv.setBackgroundColor(0xffffffff);
                tv.setTextColor(0xff333333);
            }

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!data.get(x).ischeck()) {
                        addAvoid(data.get(x).getId(),x);
                    }else {
                        removeAvoid(data.get(x).getIds(),x);
                    }
                }
            });

            return tv;
        }
    }

    public void addAvoid(String id, final int x){
        StringPostRequest spr=new StringPostRequest(URLManager.EditPreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        JSONObject js2=js1.getJSONObject("data");
                        String ids=js2.getString("id");
                        list_preference.get(x).setIds(ids);
                        list_preference.get(x).setIscheck(true);
                        adapter.notifyDataSetChanged();
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
        spr.putValue("code",code);
        spr.putValue("sign","add");
        spr.putValue("type","1");
        spr.putValue("food_id",id);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(spr);
    }

    public void removeAvoid(String id, final int x){
        StringPostRequest spr=new StringPostRequest(URLManager.EditPreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        list_preference.get(x).setIscheck(false);
                        adapter.notifyDataSetChanged();
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
        spr.putValue("code",code);
        spr.putValue("sign","del");
        spr.putValue("id",id);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(spr);
    }

    public void getAvoid(){
        StringPostRequest request=new StringPostRequest(URLManager.GetPreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        JSONArray ja=js1.getJSONArray("data");
                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            if(jo.getString("type").equals("1")){
                                for(int y=0;y<list_preference.size();y++){
                                    if(list_preference.get(y).getName().equals(jo.getString("mess_name"))){
                                        list_preference.get(y).setIscheck(true);
                                        list_preference.get(y).setIds(jo.getString("id"));
                                    }
                                }

                            }
                        }
                    }

                    adapter=new GridViewAdapter(list_preference,context);
                    gridView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.putValue("code",code);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(request);
    }

}
