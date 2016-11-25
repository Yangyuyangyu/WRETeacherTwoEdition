package com.teacherhelp.activity;


import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
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
import com.teacherhelp.activity.teachcontent.TeachContentDetailActivity;
import com.teacherhelp.adapter.teachcontent.TeachContentAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.TeachContentBean;
import com.teacherhelp.bean.TeachDataBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 教学内容
 */
public class TeachingContentActivity extends BaseActivity {

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
    private List<TeachContentBean> list;
    /*评价学生的适配器*/
    private TeachContentAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_teaching_content);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("教学内容");
        initRecyclerView();
    }

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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(TeachingContentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        adapter = new TeachContentAdapter(TeachingContentActivity.this, list);
        xRecyclerview.setAdapter(adapter);
        adapter.setMyClick(new TeachContentAdapter.MyClick() {
            @Override
            public void myClick(String id,String timeId) {

                Intent intent=new Intent(TeachingContentActivity.this, TeachContentDetailActivity.class);
                intent.putExtra("teachContentId",id);
                intent.putExtra("scheduleTimeId",timeId);
                startActivity(intent);
            }
        });
        getList("11111", "1");

    }

    /**
     * RecyclerView 上拉刷新和下拉加载的时间监听
     */
    XRecyclerView.LoadingListener loadingListener = new XRecyclerView.LoadingListener() {

        @Override
        public void onRefresh() {
            page = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    isdown = false;
                    getList("11111", "1");
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

    /**
     * 获取教学内容列表
     */
    private void getList(final String OrgId, final String schoolId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConfig.POST_TeachContent, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);

                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 1:
                        Gson gson = new Gson();
                        TeachDataBean data = gson.fromJson(
                                jsonObject.getString("data"), new TypeToken<TeachDataBean>() {
                                }.getType());
                        List<TeachContentBean> teach = data.getData();
                        if (teach.isEmpty()) {
                            return;
                        }
                        if (list.isEmpty()) {
                            list.addAll(teach);
                        } else {
                            list.clear();
                            list.addAll(teach);
                        }
                        adapter.notifyDataSetChanged();
                        xRecyclerview.refreshComplete();
                        xRecyclerview.setLoadingMoreEnabled(false);
                        break;
                    default:
                        CommonUtils.showToast("获取教学列表失败" + responseCode + jsonObject.get("msg"));
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
//                map.put("pageNow", "页数");
                map.put("orgId", OrgId);
                map.put("schoolId", schoolId);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleygetTeachContent");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getList("11111", "1");
    }
}
