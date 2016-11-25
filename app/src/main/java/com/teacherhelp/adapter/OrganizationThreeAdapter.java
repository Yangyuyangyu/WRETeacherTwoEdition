package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Administrator on 2016/7/22.
 */
public class OrganizationThreeAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<ClassBean> datas;

    public OrganizationThreeAdapter(Activity context, ArrayList<ClassBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas=datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ClassBean bean = (ClassBean) mDatas.get(position);
        viewHolder.LlGrade.setVisibility(View.VISIBLE);
        viewHolder.TvTime.setVisibility(View.GONE);
        viewHolder.TvName.setText(bean.getName());
        viewHolder.TvGrade.setText("2.0");
        viewHolder.TvType.setText("0年教龄");

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
        mView = mInflater.inflate(R.layout.recyclerview_organizationtwo, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.organization_img_head)
        ImageView ImgHead;
        @Bind(R.id.organization_tv_name)
        TextView TvName;
        @Bind(R.id.organization_tv_grade)
        TextView TvGrade;
        @Bind(R.id.organization_tv_class)
        TextView TvClass;
        @Bind(R.id.organization_tv_time)
        TextView TvTime;
        @Bind(R.id.organization_tv_type)
        TextView TvType;
        @Bind(R.id.organization_ll_grade)
        LinearLayout LlGrade;
        @Bind(R.id.organization_rl_item)
        RelativeLayout RlItem;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
