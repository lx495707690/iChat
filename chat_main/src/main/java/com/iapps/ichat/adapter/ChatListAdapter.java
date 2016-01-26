package com.iapps.ichat.adapter;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.DBManager;
import com.iapps.ichat.helper.Helper;
import com.iapps.libs.helpers.BaseHelper;
import com.iapps.libs.helpers.BaseUIHelper;

import java.util.Date;
import java.util.List;

import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBFriend;

public class ChatListAdapter
        extends ArrayAdapter<DBChat> {

    private DBManager dbManager;

    public ChatListAdapter(Context context, List<DBChat> objects) {
        super(context, R.layout.cell_chat, objects);

        dbManager = new DBManager(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Context contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.Theme_Themeisanagent);
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            convertView = localInflater.inflate(R.layout.cell_chat, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DBChat chat = getItem(position);
        holder.tvName.setText(chat.getName());
        holder.tvMessage.setText(chat.getMessage());
        if(!Helper.isEmpty(chat.getDate())){
            holder.tvDate.setText(BaseHelper.calcTimeDiff(new Date(chat.getDate())));
        }

        if(chat.getChannalId().equals(Constants.PRIVATE_CHANNEL_ID)){
            //private chat
            String friendId = chat.getFriend_userId();
            List<DBFriend> list = dbManager.getFriendById(friendId);
            DBFriend friend = null;
            String imgUrl = "";
            if(list.size() > 0){
                friend = list.get(0);
                imgUrl = friend.getImgUrl();
            }else{
                imgUrl = chat.getImgUrl();
            }

            if(!BaseHelper.isEmpty(imgUrl)){
                BaseUIHelper.loadImageWithPlaceholderResizeThumb(getContext(), imgUrl, holder.imgAvatar, R.drawable.default_useravatar);
            }else{
                holder.imgAvatar.setImageResource(R.drawable.default_useravatar);
            }

        }else{
            holder.imgAvatar.setImageResource(R.drawable.default_useravatar);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvMessage;
        TextView tvDate;
        ImageView imgAvatar;
    }
}
