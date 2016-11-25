package com.teacherhelp.adapter.teachcontent;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.TeachContentBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by waycubeoxa on 16/11/22.教学内容
 */

public class TeachContentAdapter extends BaseRecyclerAdapter {
    private MyClick myClick;
    private Activity context;
    private List<TeachContentBean> datas;

    public TeachContentAdapter(Activity context, List<TeachContentBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final TeachContentAdapter.ViewHolder viewHolder = (TeachContentAdapter.ViewHolder) holder;
        final TeachContentBean bean = (TeachContentBean) mDatas.get(position);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.ic_gf_default_photo)
                .showImageForEmptyUri(R.drawable.ic_gf_default_photo)
                .showImageOnLoading(R.drawable.ic_gf_default_photo)
                .build();
        if (bean.getTeacheMode().equals("2")) {//集体课
            viewHolder.TvName.setText(bean.getSubjectName());
            viewHolder.number.setText("("+bean.getRollCallStuNum()+")");//显示人数
            viewHolder.type.setText(bean.getCourseChildName());

        } else {//个别课
            viewHolder.TvName.setText(bean.getStudentName());
            viewHolder.number.setText(bean.getAge());//显示学生年龄
            viewHolder.type.setText(bean.getCourseChildName());
        }
        if (bean.getHeadImg() != null) {
            ImageLoader.getInstance().displayImage(bean.getHeadImg(), viewHolder.headImg, options);
        }
        viewHolder.createTime.setText(bean.getDate());
        viewHolder.time.setText(bean.getStartTime() + "~" + bean.getEndTime());
        if (bean.getState() != null) {
            if (bean.getState().equals("1")) {
                viewHolder.state.setText("已提交");
            } else {
                viewHolder.state.setText("未提交");
            }
        }

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick.myClick(bean.getId(),bean.getScheduleTimeId());
            }
        });

    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.item_teach_content, parent, false);
        mViewHolder = new TeachContentAdapter.ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.hw_tv_name)
        TextView TvName; //名字
        @Bind(R.id.hw_rl_item)
        RelativeLayout item;
        @Bind(R.id.hw_img_head)
        ImageView headImg;//图片
        @Bind(R.id.hw_tv_sex)
        TextView number;//人数
        @Bind(R.id.hw_tv_class)
        TextView type;//类型
        @Bind(R.id.hw_tv_time)
        TextView createTime;//创建日期
        @Bind(R.id.hw_tv_timetwo)
        TextView time;//上课时间
        @Bind(R.id.hw_tv_ping)
        TextView state;//状态

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setMyClick(MyClick click) {
        this.myClick = click;
    }

    public interface MyClick {
        void myClick(String id,String timeId);
    }
}
