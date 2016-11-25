package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/25.
 */
public class LeaveAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<ClassBean> datas;
    private MyListenter myListenter;

    public LeaveAdapter(Activity context, ArrayList<ClassBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas=datas;
    }

    public void setMyListenter(MyListenter listenter){
        this.myListenter=listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ClassBean bean = (ClassBean) mDatas.get(position);
        viewHolder.TvName.setText(bean.getName());
        viewHolder.LlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListenter.myonclick(position);
            }
        });
    }



    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_leave, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.leave_tv_name)
        TextView TvName;
        @Bind(R.id.leave_tv_time)
        TextView TvTime;
        @Bind(R.id.leave_tv_timetwo)
        TextView TvTimeTwo;
        @Bind(R.id.leave_tv_type)
        TextView TvType;
        @Bind(R.id.leave_ll_item)
        LinearLayout LlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter{
        void myonclick(int position);
    }
}
