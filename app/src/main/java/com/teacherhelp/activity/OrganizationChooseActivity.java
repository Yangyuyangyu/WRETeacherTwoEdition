package com.teacherhelp.activity;

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

/**
 * 名称:机构选择页面
 * 作用:用于用户进行机构选择
 * Created on 2016/7/29.
 * 创建人:WangHaoMiao
 */
public class OrganizationChooseActivity extends BaseActivity {
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
    /*评价学生的适配器*/
    private OCAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_organizationchoose);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("机构选择");

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
    private void initRecyclerView(){
        list=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrganizationChooseActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        GetData();
        adapter=new OCAdapter(OrganizationChooseActivity.this,list);
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
            bean.setName("申荣琴行"+i);
            list.add(bean);
        }
    }
}
