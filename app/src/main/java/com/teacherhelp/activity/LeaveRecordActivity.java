package com.teacherhelp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.LeaveAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 请假记录页面
 * Created by Administrator on 2016/7/25.
 */
public class LeaveRecordActivity extends BaseActivity implements LeaveAdapter.MyListenter {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    private int page = 1;//分页，用于表示当前是获取第几页的内容
    private boolean isdown = false; //判断是否是上拉加载更多
    private ArrayList<ClassBean> list;
    private LeaveAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_leaverecord);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("请假记录");

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(LeaveRecordActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);
        GetData();

        adapter = new LeaveAdapter(LeaveRecordActivity.this, list);
        adapter.setMyListenter(this);
        xRecyclerview.setAdapter(adapter);
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
            ClassBean bean = new ClassBean();
            bean.setName("陈同学" + i);
            list.add(bean);
        }
    }

    @Override
    public void myonclick(int position) { //跳转详情页面
        startActivity(new Intent(LeaveRecordActivity.this, LeaveDetailsActivity.class));
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
}
