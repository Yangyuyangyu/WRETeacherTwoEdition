package com.teacherhelp.adapter.curriculum;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.allen.supertextviewlibrary.SuperTextView;
import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.curriculum.CollectiveBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by waycubeoxa on 16/11/2.集体课
 */

public class CurriculumAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private List<CollectiveBean> datas;
    private CurriculumAdapter.MyListenter myListenter;

    public CurriculumAdapter(Activity context, List<CollectiveBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }

    public void setMyListenter(CurriculumAdapter.MyListenter listenter) {
        this.myListenter = listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final CurriculumAdapter.ViewHolder viewHolder = (CurriculumAdapter.ViewHolder) holder;
        final CollectiveBean bean = (CollectiveBean) mDatas.get(position);
        viewHolder.LlItem.setLeftTopString(bean.getCourseName());
        viewHolder.LlItem.setLeftBottomString(bean.getStartTime() + "~" + bean.getEndTime());
        viewHolder.LlItem.setLeftBottomString2(bean.getState());
        viewHolder.LlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListenter.myonclick(bean.getSchedulingId());
            }
        });
    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.item_curriculum, parent, false);
        mViewHolder = new CurriculumAdapter.ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {

        @Bind(R.id.super_tv)
        SuperTextView LlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter {
        void myonclick(String id);
    }
}
