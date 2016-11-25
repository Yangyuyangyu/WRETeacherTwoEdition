package com.teacherhelp.adapter.teachcontent;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.teacherhelp.R;

public class Adapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> data;

    public Adapter(Context context, List<Bitmap> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.griditem, null);
            holder = new Holder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.imageView.setScaleType(ScaleType.CENTER_CROP);
        int[] parameter = {data.get(position).getWidth(), data.get(position).getHeight()};
        holder.imageView.setTag(parameter);
        holder.imageView.setImageBitmap(data.get(position));
        return convertView;
    }


    class Holder {
        private ImageView imageView;
    }

}
