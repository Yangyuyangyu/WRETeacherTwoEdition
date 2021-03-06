package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.ClassBean;
import com.teacherhelp.bean.TeachBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 名称:机构选择适配器
 * 作用:选择机构
 * Created on 2016/7/29.
 * 创建人:WangHaoMiao
 */
public class OCAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<ClassBean> datas;
    private ArrayList<TeachBean> imglist=new ArrayList<>();
    private GridViewHomeWorkAdapter adapter;

    public OCAdapter(Activity context, ArrayList<ClassBean> datas) {
        super(context, datas);
        this.context = context;
        this.datas=datas;
    }


    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ClassBean bean = (ClassBean) mDatas.get(position);
        viewHolder.TvName.setText(bean.getName());
        if(bean.ischeck()){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        if (mOnItemClickListener != null) {
            viewHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position,null);
                }
            });

            // 长点击事件
            viewHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
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
        mView = mInflater.inflate(R.layout.recyclerview_oc, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.tv_name)
        TextView TvName;
        @Bind(R.id.rl_item)
        RelativeLayout item;
        @Bind(R.id.checkbox)
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
