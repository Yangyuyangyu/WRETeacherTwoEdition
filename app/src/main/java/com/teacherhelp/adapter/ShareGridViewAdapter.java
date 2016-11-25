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
import com.teacherhelp.base.ShareBean;
import com.teacherhelp.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/20.
 */
public class ShareGridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ShareBean> list;
    private ShareGridViewAdapter.MyClick myClick;

    public ShareGridViewAdapter(Context context, ArrayList<ShareBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recyclerview_share, null);
            holderView = new HolderView();
            holderView.name = (TextView) convertView.findViewById(R.id.name);
            holderView.head = (ImageView) convertView.findViewById(R.id.head);
            holderView.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        holderView.name.setText(list.get(position).getName());
        holderView.head.setBackgroundResource(list.get(position).getImg());
        holderView.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        myClick.wxFriend();
                        break;
                    case 1:
                        myClick.wxShare();
                        break;
                    case 2:
                        myClick.qqZone();
                        break;
                    case 3:
                        myClick.qqFriend();
                        break;
                    case 4:
                        myClick.wbSina();
                        break;
                }
            }
        });
        return convertView;
    }

    class HolderView {
        TextView name;
        ImageView head;
        LinearLayout item;
    }

    public void setMyClick(ShareGridViewAdapter.MyClick click) {
        this.myClick = click;
    }


    public interface MyClick {
        void wxFriend();

        void wxShare();

        void qqZone();

        void qqFriend();

        void wbSina();
    }
}
