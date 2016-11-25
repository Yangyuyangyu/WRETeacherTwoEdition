package com.teacherhelp.activity.curriculum;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.teacherhelp.R;
import com.teacherhelp.adapter.curriculum.CurriculumDetailAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.curriculum.CurriculumDetailBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 课程详情
 */
public class CurriculumDetailActivity extends BaseActivity {

    @Bind(R.id.curriculumDetailRecycleview)
    RecyclerView mRecyclerView;
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    private CurriculumDetailAdapter adapter;
    private ArrayList<CurriculumDetailBean> list;


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_curriculum_detail);
    }

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        tvTitletwo.setText("课程详情");
        tvTitletwo.setVisibility(View.VISIBLE);
        list = new ArrayList<CurriculumDetailBean>();
        adapter = new CurriculumDetailAdapter(CurriculumDetailActivity.this, list);
        adapter.setMyListenter(new CurriculumDetailAdapter.MyListenter() {
            @Override
            public void myonclick(int position) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        initDate();
    }


    private void initDate() {

        CurriculumDetailBean c1 = new CurriculumDetailBean();
        CurriculumDetailBean c2 = new CurriculumDetailBean();
        CurriculumDetailBean c3 = new CurriculumDetailBean();
        CurriculumDetailBean c4 = new CurriculumDetailBean();
        CurriculumDetailBean c5 = new CurriculumDetailBean();
        CurriculumDetailBean c6 = new CurriculumDetailBean();
        CurriculumDetailBean c7 = new CurriculumDetailBean();
        CurriculumDetailBean c8 = new CurriculumDetailBean();
        c1.setLeftText("学校名字");
        c1.setRightText("锦江区小学");
        c1.setRightRow(false);
        c2.setLeftText("学校地址");
        c2.setRightText("锦江区天紫界");
        c2.setRightRow(true);
        c3.setLeftText("学校asd");
        c3.setRightText("锦江区小学");
        c3.setRightRow(false);
        c4.setLeftText("学校名字");
        c4.setRightText("锦江区小学");
        c4.setRightRow(false);
        c5.setLeftText("学校名字");
        c5.setRightText("锦江区小学");
        c5.setRightRow(true);
        c6.setLeftText("学校名字");
        c6.setRightText("锦江区小学");
        c6.setRightRow(false);
        c7.setLeftText("学校名字");
        c7.setRightText("锦江区小学");
        c7.setRightRow(false);
        c8.setLeftText("学校名字");
        c8.setRightText("锦江区小学");
        c8.setRightRow(true);

        list.add(c1);
        list.add(c2);
        list.add(c3);
        list.add(c4);
        list.add(c5);
        list.add(c6);
        list.add(c7);
        list.add(c8);
        adapter.notifyDataSetChanged();
    }


    @OnClick(R.id.imgbtn_back)
    public void onClick() {
        finish();
    }


    /**
     * 获取课程详情数据
     */
    private void getDate(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_DetailSchedule(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        break;
                    default:
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
        stringRequest.setTag("volleyCurriculumDetail");
        MyApplication.getHttpQueue().add(stringRequest);
    }


    /**
     * 获取课程下已报名的学生
     * {courseId}:课程id
     * {teacheMode}:课程教学方式(1个别课,2集体课)
     */
    private void getListCourse(String courseId, String teacheMode) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_ListCourse(courseId, teacheMode), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        break;
                    default:
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
        stringRequest.setTag("volleyCurriculumListCourse");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
