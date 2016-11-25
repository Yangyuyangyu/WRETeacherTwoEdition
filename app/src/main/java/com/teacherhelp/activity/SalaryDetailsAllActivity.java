package com.teacherhelp.activity;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.SalaryAllAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 集体考勤页面
 * 作用：用于集体考勤的页面
 * Created by Administrator on 2016/7/25.
 * 创建人：王浩淼
 */
public class SalaryDetailsAllActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.salaryall_tv_num)
    TextView TvNum;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    /*分页，用于表示当前是获取第几页的内容*/
    private int page = 1;

    /*判断是否是上拉加载更多*/
    private boolean isdown = false;

    /*数据存放的list*/
    private ArrayList<ClassBean> list;

    /*适配器*/
    private SalaryAllAdapter adapter;


    /**
     * 初始化布局
     */
    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_salarydetailsall);
    }

    /**
     * 监听事件绑定
     */
    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    /**
     * 初始化变量
     */
    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitletwo.setText("集体考勤");
        tvTitleright.setText("确定");

        list=new ArrayList<>();
        /*RecyclerView属性初始化*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(SalaryDetailsAllActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);
        /*RecyclerView设置刷新样式*/
        xRecyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerview.setLoadingListener(loadingListener);
        GetData();

        adapter=new SalaryAllAdapter(SalaryDetailsAllActivity.this,list,myClickListener);
        xRecyclerview.setAdapter(adapter);
    }

    /**
     * onClick事件监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgbtn_back: //返回
                finish();
                break;
            case R.id.tv_titleright: //确定
                break;
        }
    }

    /**
     * RecyclerView上拉刷新,下拉加载的监听事件
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
     * 初始化数据
     */
    private void GetData(){
        for (int i = 0; i <4; i++) {
            ClassBean bean=new ClassBean();
            bean.setName("王同学"+i);
            bean.setType(0);
            list.add(bean);
        }
        TvNum.setText(list.size()+"人");
    }

    SalaryAllAdapter.MyClickListener myClickListener=new SalaryAllAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            switch (v.getId()){
                case R.id.all_btn_one:
                    list.get(position).setType(0);
                    break;
                case R.id.all_btn_two:
                    list.get(position).setType(1);
                    break;
                case R.id.all_btn_three:
                    list.get(position).setType(2);
                    break;
                case R.id.all_btn_four:
                    list.get(position).setType(3);
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    };
}
