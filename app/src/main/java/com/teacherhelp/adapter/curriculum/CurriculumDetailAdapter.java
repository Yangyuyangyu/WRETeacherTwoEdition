package com.teacherhelp.adapter.curriculum;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.allen.supertextviewlibrary.SuperTextView;
import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.curriculum.CurriculumDetailBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by waycubeoxa on 16/11/3.
 */

public class CurriculumDetailAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<CurriculumDetailBean> datas;
    private CurriculumDetailAdapter.MyListenter myListenter;

    public CurriculumDetailAdapter(Activity context, ArrayList<CurriculumDetailBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }

    public void setMyListenter(CurriculumDetailAdapter.MyListenter listenter) {
        this.myListenter = listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final CurriculumDetailAdapter.ViewHolder viewHolder = (CurriculumDetailAdapter.ViewHolder) holder;
        final CurriculumDetailBean bean = (CurriculumDetailBean) mDatas.get(position);
        if (bean.getLeftText() != null) {
            viewHolder.LlItem.setLeftString(bean.getLeftText());
        }
        if (bean.getRightText() != null) {
            viewHolder.LlItem.setRightString(bean.getRightText());
        }
        if (bean.getRightRow() == true) {
            viewHolder.LlItem.setRightIcon(context.getResources().getDrawable(R.mipmap.hsjt));
            viewHolder.LlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myListenter.myonclick(position);
                }
            });
        }


    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.item_curriculumdetail, parent, false);
        mViewHolder = new CurriculumDetailAdapter.ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {

        @Bind(R.id.super_tv_detail)
        SuperTextView LlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter {
        void myonclick(int position);
    }
}
