package com.iapps.ichat.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.iapps.ichat.R;
import com.iapps.ichat.activity.HomeActivity;
import com.iapps.ichat.adapter.MessageAdapter;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Converter;
import com.iapps.ichat.helper.GenericFragmentiChat;
import com.iapps.ichat.helper.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.itangqi.greendao.DBMessage;
import roboguice.inject.InjectView;



public class FragmentChat
		extends GenericFragmentiChat {
	@InjectView(R.id.lvChat)
	private ListView lvChat;
	@InjectView(R.id.edtMessage)

	private EditText edtMessage;
	private MessageAdapter mMessageAdapter;
	private List<DBMessage> mBeanMessages = new ArrayList<>();
	private List<DBMessage> mListTemp = new ArrayList<>();
	private String channalId; // if 0 private, else group
	private String friendId;   //send message to  who
	private int mCurrentPage = Constants.DEFAULT_PAGE;
	private TextView tvLoading;
	private boolean loadFinished = false;

	public FragmentChat(String channalId,String friendId){
		this.channalId = channalId;
		this.friendId = friendId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		return inflater.inflate(R.layout.fragment_chat, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		home().setActionBar(getResources().getString(R.string.iapps));
		home().enableHomeUp();
		home().hideBottomBar();

		if(mMessageAdapter == null){
			init();
		}
	}

	private void init(){

		//init lvChat
		mMessageAdapter = new MessageAdapter(getActivity(),mBeanMessages);
		lvChat.setAdapter(mMessageAdapter);
		View header = LayoutInflater.from(getActivity()).inflate(R.layout.cell_loading,null);
		tvLoading = (TextView)header.findViewById(R.id.tvLoading);
		lvChat.addHeaderView(header);

		//get message data from DB
		mCurrentPage = Constants.DEFAULT_PAGE;
		mListTemp.clear();
		mListTemp = (ArrayList)home().getMessageRecord(Constants.PAGE_LIMIT, (mCurrentPage - 1) * Constants.PAGE_LIMIT,clientId ,friendId,channalId);
		mCurrentPage++;
		sortMessage();
		mBeanMessages.clear();
		mBeanMessages.addAll(mListTemp);

		//refresh lvChat
		mMessageAdapter.notifyDataSetChanged();
		lvChat.setSelection(mMessageAdapter.getCount());

		//send message
		edtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					Date date = new Date();
					DBMessage dbMessage = new DBMessage(null, edtMessage.getText().toString(), date.toString(), clientId, friendId, channalId, "", true);
					mBeanMessages.add(dbMessage);
					mMessageAdapter.notifyDataSetChanged();
					home().sendTxtMessage(edtMessage.getText().toString(), channalId, friendId);
					//save message
					home().saveMessage(dbMessage);
					edtMessage.setText("");
					handled = true;

				}
				return handled;
			}
		});

		home().setMessageListener(new HomeActivity.MessageReceiveListener() {
			@Override
			public void onReceive(String data) {
				try {
					DBMessage dbMessage = Converter.toTxtMessage(data, clientId, "", false);
					mBeanMessages.add(dbMessage);
					mMessageAdapter.notifyDataSetChanged();
				} catch (Exception e) {

				}
			}
		});

		lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int i) {

			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if(firstVisibleItem == 0 && !loadFinished){
					if (totalItemCount > 0) {
						GetMessageDataTask task = new GetMessageDataTask();
						task.execute();
					}
				}
			}
		});
	}

	private void sortMessage(){
		//sort the message
		Comparator<DBMessage> comparator = new Comparator<DBMessage>(){
			public int compare(DBMessage s1, DBMessage s2) {

				if(s1.getId()!=s2.getId()){
					return (int)(s1.getId()-s2.getId());
				}
				return 0;
			}
		};
		Collections.sort(mListTemp, comparator);
	}

	public class GetMessageDataTask extends AsyncTask<String,String,String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvLoading.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... strings) {

			mListTemp.clear();
			mListTemp = (ArrayList)home().getMessageRecord(Constants.PAGE_LIMIT, (mCurrentPage - 1) * Constants.PAGE_LIMIT, clientId, friendId, channalId);

			if(mListTemp.size() < Constants.PAGE_LIMIT){
				loadFinished = true;
			}else{
				mCurrentPage++;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String str) {
			super.onPostExecute(str);
			tvLoading.setVisibility(View.GONE);

			if(mListTemp.size() > 0){
				sortMessage();
				mListTemp.addAll(mBeanMessages);
				mBeanMessages.clear();
				mBeanMessages.addAll(mListTemp);
				mListTemp.clear();
				mMessageAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Helper.simulateKey(KeyEvent.KEYCODE_BACK);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
