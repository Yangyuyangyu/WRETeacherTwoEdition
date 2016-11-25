package com.teacherhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.teacherhelp.R;
import com.teacherhelp.bean.TeachBean;

import java.util.ArrayList;

/**
 * 名称:布置作业GridView的适配器
 * 作用：展示作业图片
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class GridViewHomeWorkAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<TeachBean> list;

    public GridViewHomeWorkAdapter(Context context, ArrayList<TeachBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_homework, null);
            holderView = new HolderView();
            holderView.Img = (ImageView) convertView.findViewById(R.id.gridview_img);
            holderView.item=(RelativeLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.Img.setBackgroundResource(list.get(position).getImg());
        return convertView;
    }

    class HolderView {
        ImageView Img;
        RelativeLayout item;
    }
}
