package com.iapps.ichat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.iapps.ichat.R;
import com.iapps.ichat.activity.HomeActivity;
import com.iapps.ichat.adapter.ChatListAdapter;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.GenericFragmentiChat;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.DBChat;
import roboguice.inject.InjectView;

public class FragmentChatList
	extends GenericFragmentiChat {

	@InjectView(R.id.lvChat)
	private ListView lvChat;
	private ChatListAdapter mChatAdapter;
	private List<DBChat> mChatList = new ArrayList<>();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_chat_list, container, false);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_chat_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_create_group:
				FragmentFriends f = new FragmentFriends();
				f.setSelectFriend(true);
				home().setFragment(f);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		home().showBottomBar();
		home().setActionBar(getResources().getString(R.string.iapps));
		init();
	}
	private void init(){

		updateChatData();
		mChatAdapter = new ChatListAdapter(getActivity(),mChatList);
		lvChat.setAdapter(mChatAdapter);

		lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				DBChat chat = mChatList.get(i);

				home().getDBManager().updateChat(chat.getId(), chat.getChannalId(), chat.getFriend_userId(), chat.getName(),
						chat.getMessage(),
						chat.getDate(), chat.getImgUrl(), "0",chat.getReceive_message_date());

				home().setFragment(new FragmentChat(mChatList.get(i).getId(), mChatList.get(i).getChannalId(), mChatList.get(i).getFriend_userId(),mChatList.get(i).getName()));
			}
		});

		home().setMessageListener(new HomeActivity.MessageReceiveListener() {
			@Override
			public void onReceive(String cmd,String data) {

				if(cmd.equals(Constants.CMD_FROMMESSAGE)|| cmd.equals(Constants.CMD_OFFLINE_MESSAGE)){
					try {
						updateChatData();
						mChatAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private void updateChatData(){
		mChatList.clear();
		//get data from database.
		List<DBChat> list = home().getDBManager().searchDB(Constants.DB_CHAT);
		if(list != null){
			if(list.size() > 0){
				mChatList.addAll(list);
			}
		}
	}
}
