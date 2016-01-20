package com.iapps.ichat.adapter;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iapps.ichat.R;

import java.util.List;

import me.itangqi.greendao.DBMessage;

public class MessageAdapter
        extends ArrayAdapter<DBMessage> {

    public MessageAdapter(Context context, List<DBMessage> objects) {
        super(context, R.layout.cell_message, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.Theme_Themeisanagent);
            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            convertView = localInflater.inflate(R.layout.cell_message, parent, false);
            holder.llReceivedMsg = (LinearLayout) convertView.findViewById(R.id.llReceivedMsg);
            holder.llSendedMsg = (LinearLayout) convertView.findViewById(R.id.llSendedMsg);
            holder.tvReceivedMsg = (TextView) convertView.findViewById(R.id.tvReceivedMsg);
            holder.tvSendedMsg = (TextView) convertView.findViewById(R.id.tvSendedMsg);
            holder.imgFriendAvatar = (ImageView) convertView.findViewById(R.id.imgFriendAvatar);
            holder.imgMyAvatar = (ImageView) convertView.findViewById(R.id.imgMyAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DBMessage message = getItem(position);

        if(message.getFromMe()){
            holder.llReceivedMsg.setVisibility(View.GONE);
            holder.llSendedMsg.setVisibility(View.VISIBLE);
            holder.tvSendedMsg.setText(message.getMessage());
        }else{
            holder.llSendedMsg.setVisibility(View.GONE);
            holder.llReceivedMsg.setVisibility(View.VISIBLE);
            holder.tvReceivedMsg.setText(message.getMessage());
        }

        return convertView;
    }

    private class ViewHolder {
        LinearLayout llReceivedMsg;
        LinearLayout llSendedMsg;
        TextView tvReceivedMsg;
        TextView tvSendedMsg;
        ImageView imgFriendAvatar;
        ImageView imgMyAvatar;
    }

}
