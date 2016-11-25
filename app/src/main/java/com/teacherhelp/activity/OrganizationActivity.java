package com.teacherhelp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.OrganizaAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.app.UserInfo;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;

import com.teacherhelp.bean.org.OrgBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 我的机构页面
 * Created by Administrator on 2016/7/21.
 */
public class OrganizationActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    /*分页，用于表示当前是获取第几页的内容*/
    private int page = 1;
    /*判断是否是上拉加载更多*/
    private boolean isdown = false;
    /*数据存放的list*/
    private ArrayList<OrgBean> list;
    private OrganizaAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organization);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("我的机构");

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrganizationActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);


        adapter = new OrganizaAdapter(OrganizationActivity.this, list);
        adapter.setMyClick(new OrganizaAdapter.MyClick() {
            @Override
            public void myclick(String id) {
                Intent intent = new Intent(OrganizationActivity.this, MyOrganizationActivity.class);
                intent.putExtra("orgId", id);
                startActivity(intent);
            }
        });
        xRecyclerview.setAdapter(adapter);

        getOrgInfo();
    }

    XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {

        @Override
        public void onRefresh() {
            page = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isdown = false;
                    getOrgInfo();
                    xRecyclerview.refreshComplete();
                }

            }, 1000);            //refresh data here
        }

        @Override
        public void onLoadMore() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isdown = true;
                    xRecyclerview.loadMoreComplete();
                    xRecyclerview.refreshComplete();
                }
            }, 1000);
            page++;
        }
    };


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
        }
    }


    /**
     * 获取机构列表
     */
    private void getOrgInfo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_Organization, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Gson gson = new Gson();
                        ArrayList<OrgBean> info = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<ArrayList<OrgBean>>() {
                                }.getType());
                        if (list.isEmpty()) {
                            list.addAll(info);
                        } else {
                            list.clear();
                            list.addAll(info);
                        }
                        adapter.notifyDataSetChanged();
                        Log.e("我的机构列表", jsonObject.getString("data"));
                        break;
                    default:
                        CommonUtils.showToast("获取列表信息失败" + responseCode + jsonObject.get("msg"));
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
        stringRequest.setTag("volleygetOrgInfo");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
