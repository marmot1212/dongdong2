package com.example.administrator.vegetarians824.homePage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mobstat.StatService;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.SellerCenter;
import com.example.administrator.vegetarians824.entry.User;
import com.example.administrator.vegetarians824.login.Login;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.mine.JifenRank;
import com.example.administrator.vegetarians824.mine.MyAdmin;
import com.example.administrator.vegetarians824.mine.MyCode;
import com.example.administrator.vegetarians824.mine.MyCollect2;
import com.example.administrator.vegetarians824.mine.MyDianping;
import com.example.administrator.vegetarians824.mine.MyDuihuan;
import com.example.administrator.vegetarians824.mine.MyEdit;
import com.example.administrator.vegetarians824.mine.MyFabu;
import com.example.administrator.vegetarians824.mine.MyFankui;
import com.example.administrator.vegetarians824.mine.MySetting;
import com.example.administrator.vegetarians824.myView.RoundImageView;
import com.example.administrator.vegetarians824.myapplications.BaseApplication;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.MFGT;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StringPostRequest;
import com.example.administrator.vegetarians824.video.VideoDownload;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.base.Status;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourthFragment extends Fragment {

    RoundImageView rpic;
    private User user;
    PopupWindow popWindow;
    private TextView jifen;
    private View jindu, jindu2;
    private static String maxbackid = "";

    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("=========ss", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);

        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.my_contribute)
    public void onClickMyContribute() {
        MFGT.gotoMyContribute(getContext());
    }

    @Override
    public void onResume() {
        Log.d("=========ss", "onResume");
        super.onResume();
        if (user.islogin()) {
            getInfo2();
        }
        ;
        if (user.islogin()) {
            httpRequest();
        }
        //签到
        LinearLayout qiandao = (LinearLayout) getView().findViewById(R.id.usercenter_qiandao);
        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.islogin()) {
                    doqiandao();
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //判断是否用户是否登录，是，加载用户信息
        rpic = (RoundImageView) getView().findViewById(R.id.my_pic);
        TextView name = (TextView) getView().findViewById(R.id.my_name);
        if (user.islogin()) {
            if (user.getPic() != null) {
                ImageLoader loader = ImageLoaderUtils.getInstance(getContext());
                DisplayImageOptions options = ImageLoaderUtils.getOpt();
                loader.displayImage(URLMannager.Imag_URL + "" + user.getPic(), rpic, options);
            }
            if (user.getName() != null) {
                name.setText(user.getName());
            }
        } else {
            rpic.setImageResource(R.drawable.cc_touxiang);
            name.setText("");
        }
        rpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = rpic.getDrawable();
                getPicPop(drawable, view);
            }
        });


        //设置
        ImageView setting = (ImageView) getView().findViewById(R.id.my_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MySetting.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });

        //编辑

        ImageView edit = (ImageView) getView().findViewById(R.id.my_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyEdit.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });


        //积分
        if (user.islogin()) {
            jifen = (TextView) getView().findViewById(R.id.my_jifen);
            jifen.setText(Float.valueOf(user.getJifen()) + "");
            jindu = getView().findViewById(R.id.my_jindu);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(user.getJifen()));
            jindu.setLayoutParams(param);
            jindu.requestLayout();
            jindu2 = getView().findViewById(R.id.my_jindu22);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(user.getJifen()));
            jindu2.setLayoutParams(param2);
            jindu2.requestLayout();
        } else {
            jifen = (TextView) getView().findViewById(R.id.my_jifen);
            jifen.setText(10.0f + "");
            jindu = getView().findViewById(R.id.my_jindu);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10.0f);
            jindu.setLayoutParams(param);
            jindu.requestLayout();
            jindu2 = getView().findViewById(R.id.my_jindu22);
            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 10.0f);
            jindu2.setLayoutParams(param2);
            jindu2.requestLayout();
        }

        //积分排名
        LinearLayout paiming = (LinearLayout) getView().findViewById(R.id.my_jifenrank);
        paiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), JifenRank.class);
                startActivity(intent);
            }
        });

        //下部列表
        //我的发布
        LinearLayout fabu = (LinearLayout) getView().findViewById(R.id.my_fabu);
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyFabu.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //我的点评
        LinearLayout dianping = (LinearLayout) getView().findViewById(R.id.my_dianping);
        dianping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyDianping.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //我的收藏
        LinearLayout collect = (LinearLayout) getView().findViewById(R.id.my_collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyCollect2.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //我的反馈
        LinearLayout fankui = (LinearLayout) getView().findViewById(R.id.my_fankui);
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyFankui.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //我的优惠码
        LinearLayout code = (LinearLayout) getView().findViewById(R.id.my_code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    Intent intent = new Intent(getContext(), MyCode.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });
        //商家认证
        LinearLayout shangjia = (LinearLayout) getView().findViewById(R.id.my_shangjia);
        shangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.islogin()) {
                    if (BaseApplication.getApp().getUser().getTenans().equals("1")) {
                        Intent intent = new Intent(getContext(), SellerCenter.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), MyAdmin.class);
                        intent.putExtra("flag", 1);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getContext(), Login.class);
                    startActivity(intent);
                }
            }
        });

        //我的下载
        LinearLayout download = (LinearLayout) getView().findViewById(R.id.my_download);
        TextView downloadtask = (TextView) getView().findViewById(R.id.my_downloadtask);
        List<DownloadFileInfo> filelist = FileDownloader.getDownloadFiles();
        int pause = 0;
        int downloading = 0;
        for (int i = 0; i < filelist.size(); i++) {
            if (filelist.get(i).getStatus() == Status.DOWNLOAD_STATUS_PAUSED) {
                pause++;
            }
            if (filelist.get(i).getStatus() == Status.DOWNLOAD_STATUS_DOWNLOADING) {
                downloading++;
            }
        }
        if ((pause + downloading) > 0) {
            downloadtask.setText(downloading + "/" + (downloading + pause));
        } else {
            downloadtask.setText("");
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoDownload.class);
                startActivity(intent);
            }
        });
        StatService.onResume(this);
    }

    public void getPicPop(Drawable drawable, View view) {
        View popView = LayoutInflater.from(getContext()).inflate(R.layout.image_pop, null);
        ImageView ima = (ImageView) popView.findViewById(R.id.image_show);
        ima.setImageDrawable(drawable);
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        ViewGroup.LayoutParams params = ima.getLayoutParams();
        params.width = Width;
        params.height = Width;
        ima.setLayoutParams(params);
        ima.requestLayout();
        popView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        popWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        popWindow.setAnimationStyle(R.anim.alpha);
        // 获取光标
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        // backgroundAlpha(0.3f);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onStart() {
        Log.d("=========ss", "onStart");
        super.onStart();
        user = BaseApplication.app.getUser();
        if (user != null && user.islogin())
            getInfo();

    }

    public void httpRequest() {

        StringPostRequest spr = new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1 = new JSONObject(s);
                    String code = js1.getString("Code");
                    if (code.equals("1")) {
                        JSONObject js2 = js1.getJSONObject("Result");
                        BaseApplication.app.getUser().setName(js2.getString("username"));
                        BaseApplication.app.getUser().setMobile(js2.getString("mobile"));
                        BaseApplication.app.getUser().setPic(js2.getString("user_head_img"));
                        BaseApplication.app.getUser().setProvince(js2.getString("province"));
                        BaseApplication.app.getUser().setCity(js2.getString("city"));
                        BaseApplication.app.getUser().setSex(js2.getString("sex"));
                        BaseApplication.app.getUser().setIntro(js2.getString("intro"));
                        BaseApplication.app.getUser().setJifen(js2.getString("jifen"));
                        BaseApplication.app.getUser().setTenans(js2.getString("tenans"));
                        SharedPreferences preferences = getContext().getSharedPreferences("shared", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", js2.getString("username"));
                        editor.putString("mobile", js2.getString("mobile"));
                        editor.putString("user_head_img", js2.getString("user_head_img"));
                        editor.putString("province", js2.getString("province"));
                        editor.putString("city", js2.getString("city"));
                        editor.putString("sex", js2.getString("sex"));
                        editor.putString("intro", js2.getString("intro"));
                        editor.putString("jifen", js2.getString("jifen"));
                        editor.putString("tenans", js2.getString("tenans"));
                        editor.commit();//提交数据
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
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void getInfo() {
        StringPostRequest spr = new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1 = new JSONObject(s);
                    JSONObject js2 = js1.getJSONObject("Result");
                    final String url = js2.getString("exchangelink");
                    if (js2.has("maxbackid")) {
                        maxbackid = js2.getString("maxbackid");
                    }
                    //TextView duihuan=(TextView) getView().findViewById(R.id.my_duihuan);
                    //duihuan.setVisibility(View.VISIBLE);
                    LinearLayout duihuan = (LinearLayout) getView().findViewById(R.id.my_jifenshop);
                    duihuan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), MyDuihuan.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void getInfo2() {
        StringPostRequest spr = new StringPostRequest(URLMannager.UserInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1 = new JSONObject(s);
                    JSONObject js2 = js1.getJSONObject("Result");
                    String back = "";
                    if (js2.has("maxbackid")) {
                        back = js2.getString("maxbackid");
                    }
                    if (!back.equals(maxbackid)) {
                        ImageView mark = (ImageView) getView().findViewById(R.id.my_mark);
                        mark.setVisibility(View.VISIBLE);
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
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    public void doqiandao() {
        StringPostRequest spr = new StringPostRequest(URLMannager.AddRegister, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("=============qiandao", s);
                try {
                    JSONObject js1 = new JSONObject(s);
                    if (js1.getString("Code").equals("1")) {
                        JSONObject js2 = js1.getJSONObject("Result");
                        String status = js2.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(getContext(), "签到成功", Toast.LENGTH_SHORT).show();
                            float x = Float.valueOf(js2.getString("jifen"));
                            jifen = (TextView) getView().findViewById(R.id.my_jifen);
                            jifen.setText(x + "");
                            jindu = getView().findViewById(R.id.my_jindu);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, x);
                            jindu.setLayoutParams(param);
                            jindu.requestLayout();
                            jindu2 = getView().findViewById(R.id.my_jindu22);
                            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, x);
                            jindu2.setLayoutParams(param2);
                            jindu2.requestLayout();
                        } else {
                            Toast.makeText(getContext(), "已签到", Toast.LENGTH_SHORT).show();
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
        spr.putValue("uid", BaseApplication.app.getUser().getId());
        SlingleVolleyRequestQueue.getInstance(getContext()).addToRequestQueue(spr);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
