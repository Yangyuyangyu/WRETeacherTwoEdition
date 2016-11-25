package com.teacherhelp.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.personalExperience.PersonalExpBean;
import com.teacherhelp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:个人经历的适配器
 * 作用：展示个人经历
 * Created on 2016/7/26.
 * 创建人：WangHaoMiao
 */
public class PEAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private List<PersonalExpBean> datas;
    private MyClick myClick;

    public PEAdapter(Activity context, List<PersonalExpBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final PersonalExpBean bean = (PersonalExpBean) mDatas.get(position);
        viewHolder.TvContent.setText(bean.getExperience());
        viewHolder.TvYear.setText(bean.getStartTime().substring(0, 10) + "~" + bean.getEndTime().substring(0, 10));
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick.myClick(bean.getId(), bean.getStartTime(), bean.getEndTime(), bean.getExperience());
            }
        });


    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_pe, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
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
        void myClick(String id, String startTime, String endTime, String content);
    }
}
