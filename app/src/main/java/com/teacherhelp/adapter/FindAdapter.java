package com.teacherhelp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.TeachBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/14.
 */
public class FindAdapter extends BaseRecyclerAdapter {
    public FindAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    protected void showViewHolder(final BaseRecyclerViewHolder holder,final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final TeachBean bean = (TeachBean) mDatas.get(position);

        viewHolder.head.setBackgroundResource(bean.getImg());
        viewHolder.name.setText(bean.getName());

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position,null);
                }
            });

            // 长点击事件
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, position,null);
                    return true;
                }
            });
        }

    }

    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_teach, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.head)
        ImageView head;
        @Bind(R.id.name)
        TextView name;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
