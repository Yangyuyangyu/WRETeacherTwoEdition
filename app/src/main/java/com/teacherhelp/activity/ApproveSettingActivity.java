package com.teacherhelp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.allen.supertextviewlibrary.SuperTextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.approve.ApproveIDBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 认证设置页面
 * Created by Administrator on 2016/7/22.
 */
public class ApproveSettingActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.as_rl_one)
    SuperTextView one;
    @Bind(R.id.as_rl_two)
    SuperTextView two;
    @Bind(R.id.as_rl_three)
    SuperTextView three;
    @Bind(R.id.as_rl_four)
    SuperTextView four;

    private String approveState1;//保存审核id 身份证
    private String approveState2;// 护照
    private String approveState3;//教师证
    private String approveState4;//毕业证
    private String approveState5;//资质证

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_approvesetting);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("认证设置");

        getPapersList();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.as_rl_one: //身份认证
                Intent intent = new Intent(ApproveSettingActivity.this, IdentityApproveActivity.class);
                intent.putExtra("approveID", approveState1);
                intent.putExtra("approvePassport", approveState2);
                startActivity(intent);
                break;
            case R.id.as_rl_two: //教师证认证
                Intent intent2 = new Intent(ApproveSettingActivity.this, TeachApproveActivity.class);
                intent2.putExtra("approveTeacher", approveState3);
                startActivity(intent2);
                break;
            case R.id.as_rl_three: //毕业证认证
                Intent intent3 = new Intent(ApproveSettingActivity.this, SchoolApproveActivity.class);
                intent3.putExtra("approveSchool", approveState4);
                startActivity(intent3);
                break;
            case R.id.as_rl_four: //专业资质认证
                Intent intent4 = new Intent(ApproveSettingActivity.this, MajorApproveActivity.class);
                intent4.putExtra("approveMajor", approveState5);
                startActivity(intent4);
                break;
        }
    }

    /**
     * 获取认证列表
     */
    private void getPapersList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_PaperList, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 1:
                        Log.e("认证列表信息", jsonObject.get("data") + "");
                        Gson gson = new Gson();
                        List<ApproveIDBean> list = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<List<ApproveIDBean>>() {
                                }.getType());
                        if (list.isEmpty()) {
                            return;
                        }
                        //1身份证 ， 2护照  3教师证,4毕业证，5专业资质，6其他
                        //0,未认证 1已提交待审核 2已提交审核未通过，3已提交审核通过
                        int size = list.size();
                        for (int i = 0; i < size; i++) {
                            if (list.get(i).getPeparType() == 1) {
                                setState(list, one, i);
                                approveState1 = list.get(i).getId();
                            } else if (list.get(i).getPeparType() == 2) {
                                approveState2 = list.get(i).getId();
                            } else if (list.get(i).getPeparType() == 3) {
                                setState(list, two, i);
                                approveState3 = list.get(i).getId();
                            } else if (list.get(i).getPeparType() == 4) {
                                setState(list, three, i);
                                approveState4 = list.get(i).getId();
                            } else if (list.get(i).getPeparType() == 5) {
                                setState(list, four, i);
                                approveState5 = list.get(i).getId();
                            }
                        }
                        break;
                    default:
                        CommonUtils.showToast("获取信息失败" + responseCode + jsonObject.get("msg"));
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                LogHelper.d("请求头:" + headers.toString());
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleypostPapersList");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    private void setState(List<ApproveIDBean> list, SuperTextView st, int position) {
        String state = null;
        switch (list.get(position).getPeparState()) {
            case 0:
                state = "未认证";
                st.setRightString(state);
                break;
            case 1:
                state = "待审核";
                st.setRightString(state);
                break;
            case 2:
                state = "未通过";
                st.setRightString(state);
                st.setRightTVColor(R.color.red);
                break;
            case 3:
                state = "审核通过";
                st.setRightString(state);
                st.setRightTVColor(R.color.deepskyblue);
                break;
        }

        if (list.get(position).getPeparCtime() != null) {
            st.setLeftBottomString2("提交:" + list.get(position).getPeparCtime());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //获取接口
        getPapersList();
    }
}
