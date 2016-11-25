package com.teacherhelp.adapter.curriculum;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.allen.supertextviewlibrary.SuperTextView;
import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.curriculum.IndividualBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by waycubeoxa on 16/11/11.个别课
 */

public class CurriculumSingleAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private List<IndividualBean> datas;
    private CurriculumSingleAdapter.MyListenter myListenter;

    public CurriculumSingleAdapter(Activity context, List<IndividualBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }

    public void setMyListenter(CurriculumSingleAdapter.MyListenter listenter) {
        this.myListenter = listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final CurriculumSingleAdapter.ViewHolder viewHolder = (CurriculumSingleAdapter.ViewHolder) holder;
        final IndividualBean bean = (IndividualBean) mDatas.get(position);

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
        mViewHolder = new CurriculumSingleAdapter.ViewHolder(mView);
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
