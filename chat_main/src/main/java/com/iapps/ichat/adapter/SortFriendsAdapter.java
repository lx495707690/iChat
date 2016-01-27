package com.iapps.ichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;

import java.util.List;

import me.itangqi.greendao.DBFriend;


public class SortFriendsAdapter extends BaseAdapter {
	private List<DBFriend> list = null;
	private Context mContext;

	public SortFriendsAdapter(Context mContext, List<DBFriend> list) {
		this.mContext = mContext;
		this.list = list;
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final DBFriend mFriend = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.cell_friend, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
			viewHolder.imgTick = (ImageView) view.findViewById(R.id.imgTick);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mFriend.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		if(!BaseHelper.isEmpty(mFriend.getImgUrl())){
			BaseUIHelper.loadImageWithPlaceholderResizeThumb(mContext, mFriend.getImgUrl(), viewHolder.imgAvatar, R.drawable.default_useravatar);
		}else{
			viewHolder.imgAvatar.setImageResource(R.drawable.default_useravatar);
		}

		if(mFriend.getIsSelected()){
			viewHolder.imgTick.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imgTick.setVisibility(View.GONE);
		}
		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView imgAvatar;
		ImageView imgTick;
	}

	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}
}