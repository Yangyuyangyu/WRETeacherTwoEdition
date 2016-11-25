package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:教师请假记录适配器
 * 作用:展示教师请假的记录
 * Created on 2016/7/28.
 * 创建人:WangHaoMiao
 */
public class TLRAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<ClassBean> datas;

    public TLRAdapter(Activity context, ArrayList<ClassBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas=datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ClassBean bean = (ClassBean) mDatas.get(position);
        viewHolder.TvTime.setText(bean.getName());

        if (mOnItemClickListener != null) {
            viewHolder.RlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position,null);
                }
            });

            // 长点击事件
            viewHolder.RlItem.setOnLongClickListener(new View.OnLongClickListener() {
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
        mView = mInflater.inflate(R.layout.recyclerview_teachleave, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.tv_time)
        TextView TvTime;
        @Bind(R.id.tv_type)
        TextView TvType;
        @Bind(R.id.rl_item)
        RelativeLayout RlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
