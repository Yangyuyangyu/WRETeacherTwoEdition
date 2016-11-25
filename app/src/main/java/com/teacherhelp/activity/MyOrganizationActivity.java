package com.teacherhelp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.teacherhelp.R;
import com.teacherhelp.adapter.OrganizationAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.fragment.OrganizationOneFragment;
import com.teacherhelp.fragment.OrganizationThreeFragment;
import com.teacherhelp.fragment.OrganizationTwoFragment;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * 我的机构页面
 * Created by Administrator on 2016/7/20.
 */
public class MyOrganizationActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ratingbar)
    RatingBar ratingbar;
    @Bind(R.id.tv_grade)
    TextView tvGrade;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.ll_consult)
    LinearLayout llConsult;
    @Bind(R.id.ll_call)
    LinearLayout llCall;
    @Bind(R.id.ll_feedback)
    LinearLayout llFeedback;

    private String[] mTitles;
    private OrganizationOneFragment oneFragment;
    private OrganizationTwoFragment twoFragment;
    private OrganizationThreeFragment threeFragment;
    //frgment列表
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private OrganizationAdapter adapter;

    private String getId;//获取到的机构id

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_myorganization);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        llConsult.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);

        initFragments();
        mTitles = new String[]{"主页", "课程", "教师"};
        for (int i = 0; i < mTitles.length; i++) {
            tablayout.addTab(tablayout.newTab().setText(mTitles[i]));
        }
        adapter = new OrganizationAdapter(getSupportFragmentManager(), mTitles, fragments);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);

        getId = getIntent().getStringExtra("orgId");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.ll_consult: //聊天
                /**
                 * 启动单聊界面。
                 *
                 * @param context      应用上下文。
                 * @param targetUserId 要与之聊天的用户 Id。
                 * @param title        聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
                 */
                RongIM.getInstance().startPrivateChat(MyOrganizationActivity.this, "18281603016", "王大锤");
                break;
            case R.id.ll_call: //打电话
                Utility.showTextDialog(llCall, "是否拨打电话：15756242976", MyOrganizationActivity.this, "确定", "取消", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:15756242976"));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            LogHelper.i(e.getMessage());
                            Utility.showToast("拨打电话的权限未开启，请检查您的权限", MyOrganizationActivity.this);
                        }
                    }
                });
                break;
            case R.id.ll_feedback:
                startActivity(new Intent(MyOrganizationActivity.this, OrganizationFeedBackActivity.class));
                break;
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragments() {
        oneFragment = new OrganizationOneFragment();
        twoFragment = new OrganizationTwoFragment();
        threeFragment = new OrganizationThreeFragment();
        fragments.add(oneFragment);
        fragments.add(twoFragment);
        fragments.add(threeFragment);
    }


    /**
     * 获取机构主页详情
     */
    private void getOrgInfo(final String orgId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_OrgDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Gson gson = new Gson();
//                        UserInfo user = gson.fromJson(
//                                jsonObject.getString("data"),
//                                new TypeToken<UserInfo>() {
//                                }.getType());
                        break;
                    default:
                        CommonUtils.showToast("获取详情信息失败" + responseCode + jsonObject.get("msg"));
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
                map.put("orgId", orgId);
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
        stringRequest.setTag("volleygetOrgDetailInfo");
        MyApplication.getHttpQueue().add(stringRequest);
    }

}
