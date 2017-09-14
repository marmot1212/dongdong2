package com.example.administrator.vegetarians824.veganpass;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
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
public class MyAllergy extends Fragment {

    private Context context;
    private ListView listView;
    private EditText et;
    private TextView cancel;
    private String code="";
    private List<Ingredient> list_ingredient;
    private IngredientAdapter adapter;
    private GridLayout gridLayout;

    public MyAllergy() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my_allergy, container, false);
        if(getContext()!=null) {
            context=getContext();
            SharedPreferences pre=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            code=pre.getString("phoneid","");
            initView(v);
            addItem();
            getAvoid();

            v.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    listView.setVisibility(View.INVISIBLE);
                    return false;
                }
            });

        }
        return v;
    }
    public void initView(View v){
        listView=(ListView)v.findViewById(R.id.avoid_listview);
        et=(EditText)v.findViewById(R.id.avoid_et);
        cancel=(TextView)v.findViewById(R.id.avoid_cancel);
        gridLayout=(GridLayout)v.findViewById(R.id.avoid_grid);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")) {
                    getListData();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                list_ingredient=new ArrayList<>();
                adapter=new IngredientAdapter(list_ingredient,context);
                listView.setAdapter(adapter);
                listView.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void getListData(){
        list_ingredient=new ArrayList<>();
        StringRequest request=new StringRequest(URLManager.PreferenceSearch+"/code/"+code+"/type/3/keyword/"+et.getText().toString(), new Response.Listener<String>() {
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
                            list_ingredient.add(ingredient);
                        }
                    }
                    adapter=new IngredientAdapter(list_ingredient,context);
                    listView.setAdapter(adapter);
                    if(list_ingredient.size()>0) {
                        listView.setVisibility(View.VISIBLE);
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
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public void addItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addAvoid(list_ingredient.get(position).getId(),list_ingredient.get(position).getName());
            }
        });
    }

    public void addAvoid(String id, final String name){
        StringPostRequest spr=new StringPostRequest(URLManager.EditPreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        JSONObject js2=js1.getJSONObject("data");
                        final String ids=js2.getString("id");

                        View v=LayoutInflater.from(context).inflate(R.layout.item_ingredient_tips,null);
                        TextView tips=(TextView) v.findViewById(R.id.ingredient_tip);
                        tips.setText(name);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeAvoid(ids,v);
                            }
                        });
                        gridLayout.addView(v);
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
        spr.putValue("type","3");
        spr.putValue("food_id",id);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(spr);
    }

    public void removeAvoid(String id, final View v){
        StringPostRequest spr=new StringPostRequest(URLManager.EditPreference, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    if(js1.getString("status").equals("success")){
                        gridLayout.removeView(v);
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
                            if(jo.getString("type").equals("3")){
                                View v=LayoutInflater.from(context).inflate(R.layout.item_ingredient_tips,null);
                                TextView tips=(TextView) v.findViewById(R.id.ingredient_tip);
                                tips.setText(jo.getString("mess_name"));
                                final String ids=jo.getString("id");
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        removeAvoid(ids,v);
                                    }
                                });
                                gridLayout.addView(v);
                            }
                        }

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
        request.putValue("code",code);
        SlingleVolleyRequestQueue.getInstance(context).addToRequestQueue(request);
    }
}
