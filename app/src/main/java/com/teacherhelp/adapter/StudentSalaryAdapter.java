package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.studentSalary.StudentSalaryBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/25.学生考勤
 */
public class StudentSalaryAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<StudentSalaryBean> datas;
    private MyListenter myListenter;

    public StudentSalaryAdapter(Activity context, ArrayList<StudentSalaryBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }

    public void setMyListenter(MyListenter listenter) {
        this.myListenter = listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final StudentSalaryBean bean = (StudentSalaryBean) mDatas.get(position);
        viewHolder.TvName.setText(bean.getName());
        viewHolder.RlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListenter.myonclick(bean.getId());
            }
        });
    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_salary, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.salary_tv_name)
        TextView TvName;
        @Bind(R.id.salary_tv_sex)
        TextView TvSex;
        @Bind(R.id.salary_tv_class)
        TextView TvClass;
        @Bind(R.id.salary_tv_type)
        TextView TvType;
        @Bind(R.id.salary_tv_time)
        TextView TvTime;
        @Bind(R.id.salary_tv_timetwo)
        TextView TvTimeTwo;
        @Bind(R.id.salary_rl_item)
        RelativeLayout RlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter {
        void myonclick(String id);
    }
}
