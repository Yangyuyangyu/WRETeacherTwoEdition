package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.base.BaseRecyclerViewHolder;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 集体考勤的Adapter
 * 作用:用于集体考勤的适配器
 * Created by Administrator on 2016/7/25.
 * 创建人：王浩淼
 */
public class SalaryAllAdapter extends BaseRecyclerAdapter{
    private Activity context;
    private ArrayList<ClassBean> datas;
    private MyListenter myListenter;
    /*判断选择的是哪一个签到状态  0-准时  1-迟到  2-旷课  3-请假*/
    private int type=0;
    private Button[] btns=new Button[4];
    /*监听事件放在activity中*/
    private MyClickListener mListener;

    public SalaryAllAdapter(Activity context, ArrayList<ClassBean> datas,MyClickListener listener) {
        super(context, datas);
        this.context = context;
        this.datas=datas;
        this.mListener=listener;
    }

    public void setMyListenter(MyListenter listenter){
        this.myListenter=listenter;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ClassBean bean = (ClassBean) mDatas.get(position);
        viewHolder.Tv_Name.setText(bean.getName());
        viewHolder.Btn_One.setTag(position);
        viewHolder.Btn_Two.setTag(position);
        viewHolder.Btn_Three.setTag(position);
        viewHolder.Btn_Four.setTag(position);
        btns[0]=viewHolder.Btn_One;
        btns[1]=viewHolder.Btn_Two;
        btns[2]=viewHolder.Btn_Three;
        btns[3]=viewHolder.Btn_Four;
        for (int i = 0; i <btns.length; i++) {
            btns[i].setTextColor(context.getResources().getColor(R.color.gray_color));
            btns[i].setBackgroundResource(R.drawable.salary_btn_bg);
        }
        switch (bean.getType()){
            case 0:
                viewHolder.Btn_One.setBackgroundResource(R.color.rer_color);
                viewHolder.Btn_One.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 1:
                viewHolder.Btn_Two.setBackgroundResource(R.color.rer_color);
                viewHolder.Btn_Two.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 2:
                viewHolder.Btn_Three.setBackgroundResource(R.color.rer_color);
                viewHolder.Btn_Three.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case 3:
                viewHolder.Btn_Four.setBackgroundResource(R.color.rer_color);
                viewHolder.Btn_Four.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }

        viewHolder.Btn_One.setOnClickListener(mListener);
        viewHolder.Btn_Two.setOnClickListener(mListener);
        viewHolder.Btn_Three.setOnClickListener(mListener);
        viewHolder.Btn_Four.setOnClickListener(mListener);
    }



    @Override
    protected BaseRecyclerViewHolder createViewHOldeHolder(ViewGroup parent, int viewType) {
        View mView = null;
        BaseRecyclerViewHolder mViewHolder = null;
        mView = mInflater.inflate(R.layout.recyclerview_salaryall, parent, false);
        mViewHolder = new ViewHolder(mView);
        return mViewHolder;
    }


    static class ViewHolder extends BaseRecyclerViewHolder {
        @Bind(R.id.all_btn_one)
        Button Btn_One;
        @Bind(R.id.all_btn_two)
        Button Btn_Two;
        @Bind(R.id.all_btn_three)
        Button Btn_Three;
        @Bind(R.id.all_btn_four)
        Button Btn_Four;
        @Bind(R.id.all_tv_name)
        TextView Tv_Name;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface MyListenter{
        void myonclick(int position);
    }


    /**
     * 用于回调的抽象类
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }
}
