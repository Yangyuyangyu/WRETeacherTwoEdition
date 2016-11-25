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
import com.teacherhelp.adapter.TLRAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 名称:教师请假记录
 * 作用:新建和查看教师请假记录
 * Created on 2016/7/28.
 * 创建人:WangHaoMiao
 */
public class TeachLeaveRecordActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    private int page = 1;//分页，用于表示当前是获取第几页的内容
    private boolean isdown = false; //判断是否是上拉加载更多
    private ArrayList<ClassBean> list;
    private TLRAdapter adapter;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_leaverecord);
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
        tvTitleright.setText("新建");
        tvTitletwo.setText("请假记录");

        initRecyclerView();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                Intent intent=new Intent(TeachLeaveRecordActivity.this,TeachAmendLeaveActivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView(){
        list=new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(TeachLeaveRecordActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);

        GetData();
        adapter=new TLRAdapter(TeachLeaveRecordActivity.this,list);
        xRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object model) {
                Intent intent=new Intent(TeachLeaveRecordActivity.this,TeachAmendLeaveActivity.class);
                intent.putExtra("type","update");
                startActivity(intent);
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
            bean.setName("2016-07-28 21:0"+i);
            list.add(bean);
        }
    }
}
