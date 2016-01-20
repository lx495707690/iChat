package com.iapps.adapters;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iapps.common_library.R;

public class SectionedSimpleAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;

	private ArrayList<String> mData = new ArrayList<String>();
	private LayoutInflater mInflater;

	private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();

	public SectionedSimpleAdapter(Context ctx) {
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void clear() {
		mData.clear();
		mSeparatorsSet.clear();
	}

	public void addItem(final String item) {
		mData.add(item);
	}

	public void addSeparatorItem(final String itemLabel) {
		mData.add(itemLabel);
		mSeparatorsSet.add(mData.size() - 1);
	}

	@Override
	public int getItemViewType(int position) {
		return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_MAX_COUNT;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public String getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {

		TextView cell;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(R.layout.cell_single, null);
				holder.cell = (TextView) convertView.findViewById(R.id.text1);
				break;
			case TYPE_SEPARATOR:
				convertView = mInflater.inflate(R.layout.cell_header, null);
				holder.cell = (TextView) convertView
						.findViewById(R.id.textViewHeaderName);
				convertView.setEnabled(false);
				convertView.setOnClickListener(null); // header item should not
														// be clickable
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String content = mData.get(position);
		holder.cell.setText(content);

		return convertView;
	}

}
