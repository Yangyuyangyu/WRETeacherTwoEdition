package com.teacherhelp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.OrganizationTwoAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.bean.ClassBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 我的机构--课程
 * Created by Administrator on 2016/7/20.
 */
public class OrganizationTwoFragment extends BaseFragment {
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    private int page = 1;//分页，用于表示当前是获取第几页的内容
    private boolean isdown = false; //判断是否是上拉加载更多
    private ArrayList<ClassBean> list;
    private OrganizationTwoAdapter adapter;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization_two, null);
    }

    @Override
    protected void initVariables() {
        list=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);
        GetData();

        adapter=new OrganizationTwoAdapter(getActivity(),list);
        xRecyclerview.setAdapter(adapter);
    }

    @Override
    protected void addListener() {

    }


    XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {

        @Override
        public void onRefresh() {
            page = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isdown = false;
                    //  list.clear();
//                    GetData();
//                    listenter.onChange();
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
       //             GetData();
                    xRecyclerview.refreshComplete();
                }
            }, 1000);
            page++;
        }
    };

    private void GetData(){
        for (int i = 0; i <4; i++) {
            ClassBean bean=new ClassBean();
            bean.setName("民族声乐"+i);
            list.add(bean);
        }
    }

    /**
     * 获取机构课程详情
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
