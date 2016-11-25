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
import com.baiiu.filter.DropDownMenu;
import com.baiiu.filter.interfaces.OnFilterDoneListener;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.StudentSalaryAdapter;
import com.teacherhelp.adapter.screening.DropMenuAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.ClassBean;
import com.teacherhelp.bean.studentSalary.StudentSalaryBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.view.screening.entity.FilterUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 学生考勤页面
 * Created by Administrator on 2016/7/25.
 */
public class StudentSalaryActivity extends BaseActivity implements StudentSalaryAdapter.MyListenter, OnFilterDoneListener {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;

    @Bind(R.id.mFilterContentView)
    TextView mFilterContentView;

    private int page = 1;//分页，用于表示当前是获取第几页的内容
    private boolean isdown = false; //判断是否是上拉加载更多
    private ArrayList<StudentSalaryBean> list;
    private StudentSalaryAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_studentsalary);
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
        tvTitleright.setText("筛选");
        tvTitletwo.setText("学生考勤");


        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(StudentSalaryActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);
        GetData();

        adapter = new StudentSalaryAdapter(StudentSalaryActivity.this, list);
        adapter.setMyListenter(this);
        xRecyclerview.setAdapter(adapter);

        //初始化筛选菜单
        initFilterDropDownView();
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

    private void GetData() {
        for (int i = 0; i < 4; i++) {
            StudentSalaryBean bean = new StudentSalaryBean();
            bean.setName("古典舞蹈" + i);
            list.add(bean);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright: //筛选
                if (dropDownMenu.getVisibility() == View.VISIBLE)
                    dropDownMenu.setVisibility(View.GONE);
                else dropDownMenu.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void myonclick(String id) { //详情 根据数据判断是打开个人考勤还是集体考勤
        startActivity(new Intent(StudentSalaryActivity.this, SalaryDetailsActivity.class));
        //  startActivity(new Intent(StudentSalaryActivity.this,SalaryDetailsAllActivity.class));
    }

    /**
     * 考勤列表
     *
     * @param schoolId
     */
    private void getDate(String schoolId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_ListAttendance(schoolId), new Response.Listener<String>() {
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
        stringRequest.setTag("volleyListAttendance");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    private void initFilterDropDownView() {
        String[] titleList = new String[]{"选项一", "选项二", "选项三", "选项四"};
        dropDownMenu.setMenuAdapter(new DropMenuAdapter(this, titleList, this));
    }

    @Override
    public void onFilterDone(int position, String positionTitle, String urlValue) {
        if (position != 3) {
            dropDownMenu.setPositionIndicatorText(FilterUrl.instance().position, FilterUrl.instance().positionTitle);
        } else {
//            dropDownMenu.setVisibility(View.GONE);
        }

        dropDownMenu.close();
//        mFilterContentView.setText(FilterUrl.instance().toString());
        Log.e("菜单", FilterUrl.instance().toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FilterUrl.instance().clear();
    }
}
