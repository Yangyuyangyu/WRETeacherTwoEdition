package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.org.OrgBean;


import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/22.
 */
public class OrganizaAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<OrgBean> datas;
    private MyClick myClick;

    public OrganizaAdapter(Activity context, ArrayList<OrgBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final OrgBean bean = (OrgBean) mDatas.get(position);
        viewHolder.TvName.setText(bean.getOrgName());

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClick.myclick(bean.getOrgId());
            }
        });


    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_organization, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.organiza_img_head)
        ImageView ImgHead;
        @Bind(R.id.organiza_tv_name)
        TextView TvName;
        //        @Bind(R.id.organiza_tv_grade)
//        TextView TvGrade;
//        @Bind(R.id.oratingbar_ratingbar)
//        RatingBar ratingBar;
        @Bind(R.id.organiza_rl_item)
        RelativeLayout item;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setMyClick(MyClick click) {
        this.myClick = click;
    }

    public interface MyClick {
        void myclick(String id);
    }
}
