package com.teacherhelp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.bean.AddressBean;

import java.util.ArrayList;
import java.util.List;


public class MapSearchAdapter extends BaseAdapter {
	private List<AddressBean> list = new ArrayList<AddressBean>();
	private LayoutInflater inflater=null;
	private Activity activity;
	
	public MapSearchAdapter(Activity activity, List<AddressBean> list) {
		this.list = list;
		this.activity = activity;
		this.inflater=activity.getLayoutInflater();
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
	public View getView(final int position, View view, ViewGroup parent) {
		HolderView holderView = null;
		if (view==null) {
			view=inflater.inflate(R.layout.item_mapsearch_layout, null);
			holderView = new HolderView();
			holderView.tx_content=(TextView)view.findViewById(R.id.tx_search);
			holderView.tx_content_=(TextView)view.findViewById(R.id.tx_search_);
			view.setTag(holderView);
		}else {
			holderView = (HolderView)view.getTag();
		}
		holderView.tx_content.setText(list.get(position).getDetailAdress());
		holderView.tx_content_.setText(list.get(position).getDistrict());
		return view;
	}
	class HolderView {
		TextView tx_content;//商品分类描述
		TextView tx_content_;//商品分类描述
	}
}
