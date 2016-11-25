package com.teacherhelp.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;

import butterknife.ButterKnife;

/**
 * Created by Mr-fang on 2016/1/13.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    /**
     * 标志 下拉刷新
     */
    protected final static int PULL_REFRESH = 1;
    /**
     * 标志 上拉刷新
     */
    protected final static int LOADING_MORE = 2;

    private View view;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = initLayout(inflater, container, savedInstanceState);

        //添加ButterKife框架
        ButterKnife.bind(this, view);
        //初始化变量
        initVariables();
        //添加监听时间
        addListener();

        return view;
    }

    /**
     * 初始化布局
     */
    protected abstract View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化变量
     */
    protected abstract void initVariables();

    /**
     * 添加监听事件
     */
    protected abstract void addListener();

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    protected  void setTmie(StringRequest request){
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                0,//默认最大尝试次数
                1.0f));


    }



}