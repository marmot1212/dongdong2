package com.example.administrator.vegetarians824.dongdong;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.vegetarians824.R;
import com.example.administrator.vegetarians824.entry.Question;
import com.example.administrator.vegetarians824.mannager.URLMannager;
import com.example.administrator.vegetarians824.util.ImageLoaderUtils;
import com.example.administrator.vegetarians824.util.SlingleVolleyRequestQueue;
import com.example.administrator.vegetarians824.util.StatusBarUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JKquestion extends AppCompatActivity {
    private int count;
    private List<Question> list;
    TextView ques;
    ImageView qpic;
    LinearLayout line1,line2;
    TextView op1,op2;
    Button an1,an2;
    TextView exp;
    TextView yorn;
    TextView scoretext;
    View jd;
    int score;
    LinearLayout f1,f3;
    FrameLayout f2;
    Button again;
    ImageView fanhui;
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jkquestion);
        StatusBarUtil.setColorDiff(this,0xff00aff0);
        list=new ArrayList<>();
        count=0;
        score=0;
        initView();
        getQuestion();

    }
    public void getQuestion(){
        StringRequest request=new StringRequest(URLMannager.QuestionList, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject js1=new JSONObject(s);
                    JSONArray ja=js1.getJSONArray("Result");
                    for (int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        Question qus=new Question();
                        qus.setAnswera(jo.getString("answera"));
                        qus.setAnswerb(jo.getString("answerb"));
                        qus.setExplain(jo.getString("explain"));
                        qus.setQuestion(jo.getString("question"));
                        qus.setQuestion_id(jo.getString("question_id"));
                        qus.setQuestionpic(jo.getString("questionpic"));
                        qus.setRight_answer(jo.getString("right_answer"));
                        list.add(qus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setView(list.get(count));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        SlingleVolleyRequestQueue.getInstance(JKquestion.this).addToRequestQueue(request);
    }

    public void initView(){
        ques=(TextView) findViewById(R.id.question_qus);
        qpic=(ImageView)findViewById(R.id.question_pic);
        line1=(LinearLayout)findViewById(R.id.question_line1);
        line2=(LinearLayout)findViewById(R.id.question_line2);
        op1=(TextView)findViewById(R.id.question_op1);
        op2=(TextView)findViewById(R.id.question_op2);
        an1=(Button)findViewById(R.id.question_an1);
        an2=(Button)findViewById(R.id.question_an2);
        exp=(TextView)findViewById(R.id.question_exp);
        yorn=(TextView)findViewById(R.id.question_yorn);
        jd=findViewById(R.id.question_jindu);
        f1=(LinearLayout)findViewById(R.id.question_layout1);
        f2=(FrameLayout) findViewById(R.id.question_layout2);
        f3=(LinearLayout)findViewById(R.id.question_layout3);
        scoretext=(TextView)findViewById(R.id.question_score);
        again=(Button)findViewById(R.id.question_again);
        fanhui=(ImageView)findViewById(R.id.question_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setView(final Question qus){
        ques.setText(qus.getQuestion());
        com.nostra13.universalimageloader.core.ImageLoader loader= ImageLoaderUtils.getInstance(getBaseContext());
        DisplayImageOptions options=ImageLoaderUtils.getOpt();
        loader.displayImage(URLMannager.Imag_URL+""+qus.getQuestionpic(),qpic,options);
        ViewGroup.LayoutParams params = qpic.getLayoutParams();
        double n=(double)params.width/params.width;
        params.height=(int)(n*params.height);
        qpic.setLayoutParams(params);
        qpic.requestLayout();
        an1.setText(qus.getAnswera());
        an2.setText(qus.getAnswerb());
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.INVISIBLE);
        op1.setVisibility(View.VISIBLE);
        op1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPopupWindow(qus);
                popupWindow.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        op2.setVisibility(View.INVISIBLE);

        f1.setVisibility(View.VISIBLE);
        f2.setVisibility(View.VISIBLE);
        f3.setVisibility(View.INVISIBLE);

        an1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                //进度条
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(count).floatValue());
                jd.setLayoutParams(param);
                jd.requestLayout();
                //选择答案
                if(qus.getRight_answer().equals("A")){
                    yorn.setText("回答正确");
                    exp.setText(qus.getExplain());
                    line2.setBackgroundColor(0xff63b963);
                    score=score+10;
                }else {
                    yorn.setText("回答错误");
                    line2.setBackgroundColor(0xffdf1e3a);
                    exp.setText(qus.getExplain());
                }
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                //底部
                if(count<10) {
                    op1.setVisibility(View.INVISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setView(list.get(count));
                        }
                    });
                }else {
                    op1.setVisibility(View.INVISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op2.setText("查看得分");
                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            f1.setVisibility(View.INVISIBLE);
                            f2.setVisibility(View.INVISIBLE);
                            scoretext.setText(score+"");
                            f3.setVisibility(View.VISIBLE);
                            again.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    list=new ArrayList<Question>();
                                    count=0;
                                    score=0;
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(0).floatValue());
                                    jd.setLayoutParams(param);
                                    jd.requestLayout();
                                    getQuestion();
                                }
                            });
                        }
                    });

                }
            }
        });

        an2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                //进度条
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(count).floatValue());
                jd.setLayoutParams(param);
                jd.requestLayout();
                //选择答案
                if(qus.getRight_answer().equals("B")){
                    yorn.setText("回答正确");
                    exp.setText(qus.getExplain());
                    line2.setBackgroundColor(0xff63b963);
                    score=score+10;
                }else {
                    yorn.setText("回答错误");
                    line2.setBackgroundColor(0xffdf1e3a);
                    exp.setText(qus.getExplain());
                }
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);
                //底部
                if(count<10) {
                    op1.setVisibility(View.INVISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setView(list.get(count));
                        }
                    });
                }else {
                    op1.setVisibility(View.INVISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op2.setText("查看得分");
                    op2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            f1.setVisibility(View.INVISIBLE);
                            f2.setVisibility(View.INVISIBLE);
                            scoretext.setText(score+"");
                            f3.setVisibility(View.VISIBLE);
                            again.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    list=new ArrayList<Question>();
                                    count=0;
                                    score=0;
                                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, Float.valueOf(0).floatValue());
                                    jd.setLayoutParams(param);
                                    jd.requestLayout();
                                    getQuestion();
                                }
                            });
                        }
                    });

                }

            }
        });

    }


    protected void initPopuptWindow(final Question qus) {
        // TODO Auto-generated method stub
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.popwindow_bottom, null,
                false);
        // 创建PopupWindow实例
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 点击其他地方消失


        LinearLayout weixin=(LinearLayout) popupWindow_view.findViewById(R.id.share_weixin);
        LinearLayout pengyouquan=(LinearLayout)popupWindow_view.findViewById(R.id.share_pengyouquan);
        LinearLayout qq=(LinearLayout)popupWindow_view.findViewById(R.id.share_qq);
        LinearLayout zone=(LinearLayout)popupWindow_view.findViewById(R.id.share_zone);
        LinearLayout weibo=(LinearLayout)popupWindow_view.findViewById(R.id.share_weibo);

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
                String url=URLMannager.ShareQuestion+qus.getQuestion_id();
                UMImage image = new UMImage(JKquestion.this,URLMannager.Imag_URL+qus.getQuestionpic());
                new ShareAction(JKquestion.this)
                        .setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText("")
                        .withTitle(qus.getQuestion())
                        .withTargetUrl(url)
                        //.withMedia(image)
                        .share();
            }
        });

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setQQZone("1105683168", "U2MDcVrp5vlfA3Xc");
                String url=URLMannager.ShareQuestion+qus.getQuestion_id();
                UMImage image = new UMImage(JKquestion.this,URLMannager.Imag_URL+qus.getQuestionpic());

                new ShareAction(JKquestion.this)
                        .setPlatform(SHARE_MEDIA.QZONE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withTitle(qus.getQuestion())
                        .withTargetUrl(url)
                       // .withMedia(image)
                        .share();
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String url=URLMannager.ShareQuestion+qus.getQuestion_id();
                UMImage image = new UMImage(JKquestion.this,URLMannager.Imag_URL+qus.getQuestionpic());

                new ShareAction(JKquestion.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withTitle(qus.getQuestion())
                        .withText("")
                        .withTargetUrl(url)
                        //.withMedia(image)
                        .share();
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlatformConfig.setWeixin("wxfa8558a0ee056f0c", "cf0c56f350578c651320a2b94675b379");
                String url=URLMannager.ShareQuestion+qus.getQuestion_id();
                UMImage image = new UMImage(JKquestion.this,URLMannager.Imag_URL+qus.getQuestionpic());

                new ShareAction(JKquestion.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withTitle(qus.getQuestion())
                        .withText("")
                        .withTargetUrl(url)
                        //.withMedia(image)
                        .share();
            }
        });
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config.REDIRECT_URL="http://www.isuhuo.com";
                UMShareAPI mShareAPI = UMShareAPI.get(JKquestion.this);
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(JKquestion.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {

                    }
                });

                PlatformConfig.setSinaWeibo("2225421609","835f1b19840f1f8bc90264a90e436321");
                String url=URLMannager.ShareQuestion+qus.getQuestion_id();
                UMImage image = new UMImage(JKquestion.this,URLMannager.Imag_URL+qus.getQuestionpic());

                new ShareAction(JKquestion.this)
                        .setPlatform(SHARE_MEDIA.SINA)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .withText(qus.getQuestion())
                        .withTargetUrl(url)
                        //.withMedia(image)
                        .share();
            }
        });





    }

    private void getPopupWindow(Question qus) {
            initPopuptWindow(qus);

    }
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

}
