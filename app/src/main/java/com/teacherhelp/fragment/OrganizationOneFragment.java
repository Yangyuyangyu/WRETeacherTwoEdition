package com.teacherhelp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.teacherhelp.R;
import com.teacherhelp.activity.AgencyApprovalActivity;
import com.teacherhelp.activity.IncludeOrganizationActivity;
import com.teacherhelp.activity.OrganizationPicActivity;
import com.teacherhelp.activity.OrganizationVidioActivity;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 我的机构---主页
 * Created by Administrator on 2016/7/20.
 */
public class OrganizationOneFragment extends BaseFragment {
    @Bind(R.id.oa_tv_type)
    TextView TvType;
    @Bind(R.id.oa_rl_approve)
    RelativeLayout RlApprove;
    @Bind(R.id.oa_tv_introduce)
    TextView TvIntroduce;
    @Bind(R.id.oa_rl_otherschool)
    RelativeLayout RlOtherschool;
    @Bind(R.id.oa_rl_school)
    RelativeLayout RlSchool;
    @Bind(R.id.oa_ll_photo)
    LinearLayout LlPhoto;
    @Bind(R.id.oa_ll_vidio)
    LinearLayout LlVidio;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization_one, null);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void addListener() {
        RlApprove.setOnClickListener(this);
        RlOtherschool.setOnClickListener(this);
        RlSchool.setOnClickListener(this);
        LlPhoto.setOnClickListener(this);
        LlVidio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.oa_rl_approve: //认证
                startActivity(new Intent(getActivity(), AgencyApprovalActivity.class));
                break;
            case R.id.oa_ll_photo: //机构相册
                startActivity(new Intent(getActivity(), OrganizationPicActivity.class));
                break;
            case R.id.oa_ll_vidio: //视频展示
                startActivity(new Intent(getActivity(), OrganizationVidioActivity.class));
                break;
            case R.id.oa_rl_otherschool: //其它分校
                startActivity(new Intent(getActivity(), IncludeOrganizationActivity.class));
                break;
            case R.id.oa_rl_school: //分校
                break;
        }
    }

}
