package com.iapps.ichat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.ichat.helper.DBManager;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;

import java.util.List;

import me.itangqi.greendao.DBFriend;
import me.itangqi.greendao.DBMessage;

public class MessageAdapter
        extends ArrayAdapter<DBMessage> {
    private DBManager dbManager;

    public MessageAdapter(Context context, List<DBMessage> objects) {
        super(context, R.layout.cell_message, objects);
        dbManager = new DBManager(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
//            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), null);
            // clone the inflater using the ContextThemeWrapper
//            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
//            convertView = localInflater.inflate(R.layout.cell_message, parent, false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_message,null);
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

        if(message.getFrom_me()){
            holder.llReceivedMsg.setVisibility(View.GONE);
            holder.llSendedMsg.setVisibility(View.VISIBLE);
            holder.tvSendedMsg.setText(message.getMessage());

            if(!BaseHelper.isEmpty(UserInfoManager.getInstance(getContext()).getAvatar())){
                BaseUIHelper.loadImageWithPlaceholderResizeThumb(getContext(), UserInfoManager.getInstance(getContext()).getAvatar(), holder.imgMyAvatar, R.drawable.default_useravatar);
            }else{
                holder.imgMyAvatar.setImageResource(R.drawable.default_useravatar);
            }

        }else{

            String friendId = message.getFromId();
            List<DBFriend> list = dbManager.getFriendById(friendId);
            DBFriend friend = null;
            String imgUrl = "";
            if(list.size() > 0){
                friend = list.get(0);
                imgUrl = friend.getAvatar();
            }else{
                imgUrl = message.getImage();
            }

            if(!BaseHelper.isEmpty(imgUrl)){
                BaseUIHelper.loadImageWithPlaceholderResizeThumb(getContext(), imgUrl, holder.imgFriendAvatar, R.drawable.default_useravatar);
            }else{
                holder.imgFriendAvatar.setImageResource(R.drawable.default_useravatar);
            }

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
