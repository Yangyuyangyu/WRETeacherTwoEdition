package com.teacherhelp.adapter.personPic;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.personPic.PersonPicBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by waycubeoxa on 16/11/1.
 */


public class PersonPicAdapter extends BaseRecyclerAdapter {
    private Activity context;
    private ArrayList<PersonPicBean> datas;
    private MyListenter myListenter;

    public PersonPicAdapter(Activity context, ArrayList<PersonPicBean> datas) {
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
        final PersonPicBean bean = (PersonPicBean) mDatas.get(position);


        viewHolder.LlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListenter.myonclick(position, viewHolder.LlItem);
            }
        });
    }


    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.item_personpic, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }

    static class ViewHolder extends BaseRecyclerViewHolder {

        @Bind(R.id.item_personPic_img)
        ImageView LlItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter {
        void myonclick(int position, ImageView iv);
    }
}