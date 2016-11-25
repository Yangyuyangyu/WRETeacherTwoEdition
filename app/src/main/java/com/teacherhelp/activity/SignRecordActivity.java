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
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.SRAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.bean.ClassBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 名称:打卡记录
 * 作用：用于用户查看以前的打卡的记录
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class SignRecordActivity extends BaseActivity {
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
    private ArrayList<ClassBean> list;
    /*RecyclerView的适配器*/
    private SRAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_signrecord);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("打卡记录");

        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(SignRecordActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        GetData();
        adapter = new SRAdapter(SignRecordActivity.this, list);
        xRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object model) {
                startActivity(new Intent(SignRecordActivity.this, SignInDetailsActivity.class));
            }

            @Override
            public void onItemLongClick(View view, int position, Object model) {

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

    /**
     * 加入数据
     */
    private void GetData() {
        for (int i = 0; i < 4; i++) {
            ClassBean bean = new ClassBean();
            bean.setName("九眼桥校区" + i);
            list.add(bean);
        }
    }

    /**
     * 打卡记录
     */
    private void clockRecord(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_LogClock(id), new Response.Listener<String>() {
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
                Map<String, String> map = new HashMap<String, String>();
                map.put("pageNow", "当前页数");
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
        stringRequest.setTag("volleyLogClockList");
        MyApplication.getHttpQueue().add(stringRequest);
    }
}
