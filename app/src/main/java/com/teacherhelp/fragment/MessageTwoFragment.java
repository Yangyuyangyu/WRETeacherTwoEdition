package com.teacherhelp.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.utils.contact.ContactAdapter;
import com.teacherhelp.utils.contact.DividerItemDecoration;
import com.teacherhelp.utils.contact.LetterView;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * 联系人界面
 * Created by Administrator on 2016/7/15.
 */
public class MessageTwoFragment extends BaseFragment {
    @Bind(R.id.contact_list)
    RecyclerView contactList;
    @Bind(R.id.letter_view)
    LetterView letterView;
    private String[] contactNames;
    private LinearLayoutManager layoutManager;

    private ContactAdapter adapter;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messagetwo, null);
    }

    @Override
    protected void initVariables() {
        contactNames = new String[]{"张三丰", "郭靖", "黄蓉", "黄老邪", "赵敏", "123", "天山童姥", "任我行", "于万亭", "陈家洛", "韦小宝", "$6", "穆人清", "陈圆圆", "郭芙", "郭襄", "穆念慈", "东方不败", "梅超风", "林平之", "林远图", "灭绝师太", "段誉", "鸠摩智"};

        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ContactAdapter(getActivity(), contactNames);
        adapter.setMyclick(new ContactAdapter.Myclick() {
            @Override
            public void myClick(String id) {
                contact(id + "");
            }
        });
        contactList.setLayoutManager(layoutManager);
        contactList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        contactList.setAdapter(adapter);

        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0);
            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
    }

    @Override
    protected void addListener() {

    }

    /**
     * 启动单聊界面。
     * <p>
     * 应用上下文。
     * 要与之聊天的用户 Id。
     * 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
     */

    private void contact(String id) {
        RongIM.getInstance().startPrivateChat(getActivity(), "15756242976", id);
    }
}
