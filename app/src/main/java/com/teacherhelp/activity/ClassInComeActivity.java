package com.teacherhelp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程统计
 * Created by Administrator on 2016/7/22.
 */
public class ClassInComeActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_classincome);
    }

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("课程收入统计");
        tvTitleright.setText("统计");
    }

    @OnClick({R.id.imgbtn_back, R.id.tv_titletwo, R.id.tv_titleright})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titletwo:
                break;
            case R.id.tv_titleright:
                break;
        }
    }

    /**
     * 获取课程列表、课程筛选列表、收入统计
     * 没有参数传递时 获取老师所有机构的所有课程信息
     * private String startTime; 开始时间
     * private String endTime; 结束时间
     * private String orgId; 机构id
     * 4种搭配方式，1.开始结束时间必须对应传递;2.机构id可单独传递;3.开始结束时间可单独传递;4.三个参数可同时传递
     */
    private void getStatistical(final String orgId, final String startTime, final String endTime) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_Statistical(), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Log.e("获取用户信息成功", jsonObject.get("data") + "");
                        Gson gson = new Gson();
//                        UserInfo user = gson.fromJson(
//                                jsonObject.getString("data"),
//                                new TypeToken<UserInfo>() {
//                                }.getType());

                        break;
                    default:
                        CommonUtils.showToast("获取用户信息失败" + responseCode + jsonObject.get("msg"));
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
                Map<String, String> map = new HashMap<String, String>();
                if (orgId != null) {
                    map.put("orgId", orgId);
                } else if (startTime != null) {
                    map.put("startTime", startTime);
                } else if (endTime != null) {
                    map.put("endTime", endTime);
                }
                return map;
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
        stringRequest.setTag("volleygetStatistical");
        MyApplication.getHttpQueue().add(stringRequest);
    }


}
