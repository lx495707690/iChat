package com.iapps.ichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.iapps.ichat.R;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;

import java.util.List;


public class AvatarAdapter extends BaseAdapter {
    private List<String> urls = null;
    private Context mContext;

    public AvatarAdapter(Context mContext, List<String> urls) {
        this.mContext = mContext;
        this.urls = urls;
    }

    public int getCount() {
        return this.urls.size();
    }

    public Object getItem(int position) {
        return urls.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.cell_avatar, null);
            viewHolder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String url = urls.get(position);
        if(!BaseHelper.isEmpty(url)){
            BaseUIHelper.loadImageWithPlaceholderResizeThumb(mContext, url, viewHolder.img, R.drawable.default_useravatar);
        }else{
            viewHolder.img.setImageResource(R.drawable.default_useravatar);
        }

        return view;

    }

    final static class ViewHolder {
        ImageView img;
    }
}