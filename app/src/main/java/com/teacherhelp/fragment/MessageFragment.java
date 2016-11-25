package com.teacherhelp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseFragment;

import butterknife.Bind;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * 消息Fragment
 * Created by Administrator on 2016/7/14.
 */
public class MessageFragment extends BaseFragment {
    @Bind(R.id.message_btn_one)
    RadioButton BtnOne;
    @Bind(R.id.message_btn_two)
    RadioButton BtnTwo;
    @Bind(R.id.message_segmented)
    SegmentedGroup Segmented;
    @Bind(R.id.message_rl_title)
    RelativeLayout RlTitle;
    @Bind(R.id.message_content)
    FrameLayout Content;

    private int index = 0; //判断当前是哪个fragment显示
    private int currentTabIndex = 0;// 当前fragment的index
    private Fragment[] fragments; //fragment集合
    //   private MessageOneFragment messageOneFragment;
    private MessageTwoFragment messageTwoFragment;
    private ConversationListDynamicFragment dynamicFragment;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, null);
    }

    @Override
    protected void initVariables() {
        BtnOne.setChecked(true);
        initFragment();
    }

    @Override
    protected void addListener() {
        BtnOne.setOnClickListener(this);
        BtnTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.message_btn_one:
                MyApplication.setIsmessage(true);
                index = 0;
                break;
            case R.id.message_btn_two:
                MyApplication.setIsmessage(false);
                index = 1;
                break;
        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getChildFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.message_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;
    }

    private void initFragment() {
        //     messageOneFragment=new MessageOneFragment();
        messageTwoFragment = new MessageTwoFragment();
        dynamicFragment = new ConversationListDynamicFragment();
        fragments = new Fragment[]{dynamicFragment, messageTwoFragment};

        // 添加显示第一个fragment
        getChildFragmentManager().beginTransaction()
                .add(R.id.message_content, dynamicFragment)
                .hide(dynamicFragment).show(dynamicFragment).commit();
    }

}
