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
import com.teacherhelp.adapter.PEAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.bean.personalExperience.PersonalExpBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 名称:个人经历页面
 * 作用：查看与设置个人经历
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class PersonalExperienceActivity extends BaseActivity {
    private final int REQUESTCODE = 222;
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    /*分页，用于表示当前是获取第几页的内容*/
    private int page = 1;
    /*判断是否是上拉加载更多*/
    private boolean isdown = false;
    /*数据存放的list*/
    private List<PersonalExpBean> list;
    /*RecyclerView的适配器*/
    private PEAdapter adapter;
    private String[] itemInfo;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_personalexperience);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("个人经历");
        tvTitleright.setText("新增");

        initRecyclerView();
        getDate();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                break;
            case R.id.tv_titleright: //新增
                Intent intent = new Intent(PersonalExperienceActivity.this, AddPeActivity.class);
                intent.putExtra("type", "add");
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        list = new ArrayList<PersonalExpBean>();
        adapter = new PEAdapter(PersonalExperienceActivity.this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(PersonalExperienceActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        xRecyclerview.setAdapter(adapter);
        adapter.setMyClick(new PEAdapter.MyClick() {
            @Override
            public void myClick(String id, String startTime, String endTime, String content) {
                itemInfo = new String[4];
                itemInfo[0] = id;
                itemInfo[1] = startTime;
                itemInfo[2] = endTime;
                itemInfo[3] = content;
                Intent intent = new Intent(PersonalExperienceActivity.this, AddPeActivity.class);
                intent.putExtra("type", "item");
                intent.putExtra("info", itemInfo);
                startActivity(intent);
            }
        });

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
                    getDate();
                }

            }, 500);            //refresh data here
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
            }, 500);
            page++;
        }
    };


    /**
     * 获取个人经历列表
     */
    private void getDate() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.PersonalExperience.GET_List, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Log.e("获取个人列表成功", jsonObject.get("data") + "");
                        Gson gson = new Gson();
                        List<PersonalExpBean> user = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<List<PersonalExpBean>>() {
                                }.getType());
//                        list = user;
                        if (list.isEmpty()) {
                            list.addAll(user);
                        } else {
                            list.clear();
                            list.addAll(user);
                        }

                        adapter.notifyDataSetChanged();
                        xRecyclerview.refreshComplete();
                        xRecyclerview.setLoadingMoreEnabled(false);

                        break;
                    default:
                        CommonUtils.showToast("获取个人列表失败" + jsonObject.get("msg"));
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
        stringRequest.setTag("volleyPersonalExperienceList");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDate();
    }
}
