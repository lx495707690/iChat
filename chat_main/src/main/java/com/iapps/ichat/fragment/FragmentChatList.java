package com.iapps.ichat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iapps.ichat.R;
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

//		home().saveChat("0", "1", "李祥", "黑蛇白蛇眼镜蛇~谁也没招", "上午9:09", "http://pic.baike.soso.com/p/20140404/20140404162443-1075855132.jpg");
//		home().saveChat("0", "2", "小刚", "二十多年了，雷打不动的三点收市，1月4日变了，当券", "上午9:09", "http://pic2.sc.chinaz.com/files/pic/pic9/201601/apic17755.jpg");
//		home().saveChat("0", "3", "118班", "借辣椒面……三天后，隔壁老王裤子都来不及脱，小明", "上午9:09", "http://pic.sc.chinaz.com/files/pic/pic9/201601/apic17761.jpg");
//		home().saveChat("0", "4", "红叶李", "钱明天我就先转过去。下周一我看请两天假去看看。", "上午9:09", "http://pic.sc.chinaz.com/files/pic/pic9/201512/apic17580.jpg");

		//get data from database.
		mChatList.addAll(home().searchDB(Constants.PAGE_LIMIT,0, Constants.DB_CHAT));
		mChatAdapter = new ChatListAdapter(getActivity(),mChatList);
		lvChat.setAdapter(mChatAdapter);

		lvChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				home().setFragment(new FragmentChat(mChatList.get(i).getChannelId(), mChatList.get(i).getUserId()));
			}
		});
	}
}
