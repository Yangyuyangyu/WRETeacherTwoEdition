package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.personalExperience.PersonalShareBean;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:成果分享的适配器
 * 作用：展示成果分享
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class STAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<PersonalShareBean> datas;
    private STAdapter.MyClick myClick;

    public STAdapter(Activity context, ArrayList<PersonalShareBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final PersonalShareBean bean = (PersonalShareBean) mDatas.get(position);
        viewHolder.TvContent.setText(bean.getContent());
        viewHolder.TvTitle.setText(bean.getTitle());
        viewHolder.TvYear.setText(bean.getDate().substring(0, 10));

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick.myClick(bean.getId(), bean.getTitle(), bean.getDate(), bean.getContent());
            }
        });

    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_st, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.title)
        TextView TvTitle;
        @Bind(R.id.content)
        TextView TvContent;
        @Bind(R.id.year)
        TextView TvYear;
        @Bind(R.id.item)
        LinearLayout item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setMyClick(MyClick click) {
        this.myClick = click;
    }


    public interface MyClick {
        void myClick(String id, String title, String time, String content);
    }
}
