package com.teacherhelp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.OCAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:选择所属分校页面
 * 作用:用于用户选择分校
 * Created on 2016/7/29.
 * 创建人:WangHaoMiao
 */
public class SchoolChooseActivity extends BaseActivity {
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
    private ArrayList<ClassBean> list;
    /*评价学生的适配器*/
    private OCAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organizationchoose);
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
        tvTitleright.setText("确定");
        tvTitletwo.setText("选择所属分校");

        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(){
        list=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(SchoolChooseActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        GetData();
        adapter=new OCAdapter(SchoolChooseActivity.this,list);
        xRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object model) {
                for (int i = 0; i <list.size() ; i++) {
                    list.get(i).setIscheck(false);
                }
                list.get(position).setIscheck(true);
                adapter.notifyDataSetChanged();
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
    private void GetData(){
        for (int i = 0; i <4; i++) {
            ClassBean bean=new ClassBean();
            bean.setName("九眼桥校区"+i);
            list.add(bean);
        }
    }
}
