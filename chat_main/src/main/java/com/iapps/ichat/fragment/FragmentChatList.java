package com.iapps.ichat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
		return inflater.inflate(R.layout.fragment_chat_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		home().showBottomBar();
		home().setActionBar(getResources().getString(R.string.iapps));
		init();
	}
	private void init(){
		mChatList.clear();

		//get data from database.
		List<DBChat> list = home().getDBManager().searchDB(Constants.DB_CHAT);
		if(list != null){
			if(list.size() > 0){
				mChatList.addAll(list);
			}
		}

		mChatAdapter = new ChatListAdapter(getActivity(),mChatList);
		lvChat.setAdapter(mChatAdapter);

		lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				home().setFragment(new FragmentChat(mChatList.get(i).getId(), mChatList.get(i).getChannalId(), mChatList.get(i).getFriend_userId()));
			}
		});

		home().setMessageListener(new HomeActivity.MessageReceiveListener() {
			@Override
			public void onReceive(String data) {
				try {
					updateChatUI();
				} catch (Exception e) {

				}
			}
		});
	}

	private void updateChatUI(){

		mChatList.clear();
		//get data from database.
		List<DBChat> list = home().getDBManager().searchDB(Constants.DB_CHAT);
		if(list != null){
			if(list.size() > 0){
				mChatList.addAll(list);
			}
		}
		mChatAdapter.notifyDataSetChanged();
	}
}
