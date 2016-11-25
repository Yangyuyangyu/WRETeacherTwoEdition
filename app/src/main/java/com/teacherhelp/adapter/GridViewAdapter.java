package com.teacherhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.bean.TeachBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15.
 */
public class GridViewAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<TeachBean> list;

    public GridViewAdapter(Context context, ArrayList<TeachBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recyclerview_teach, null);
            holderView = new HolderView();
            holderView.name = (TextView) convertView.findViewById(R.id.name);
            holderView.head = (ImageView) convertView.findViewById(R.id.head);
            holderView.item=(LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.name.setText(list.get(position).getName());
        holderView.head.setBackgroundResource(list.get(position).getImg());
        return convertView;
    }

    class HolderView {
        TextView name;
        ImageView head;
        LinearLayout item;
    }
}
