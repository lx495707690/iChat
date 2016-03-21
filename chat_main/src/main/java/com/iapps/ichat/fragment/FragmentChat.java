package com.iapps.ichat.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;
import com.iapps.ichat.R;
import com.iapps.ichat.activity.HomeActivity;
import com.iapps.ichat.adapter.MessageAdapter;
import com.iapps.ichat.helper.BroadcastManager;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Converter;
import com.iapps.ichat.helper.GenericFragmentiChat;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.UserInfoManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBMessage;
import roboguice.inject.InjectView;

@SuppressLint("ValidFragment")
public class FragmentChat
        extends GenericFragmentiChat {
    @InjectView(R.id.lvChat)
    private ListView lvChat;
    @InjectView(R.id.edtMessage)

    private EditText edtMessage;
    private MessageAdapter mMessageAdapter;
    private List<DBMessage> mBeanMessages = new ArrayList<>();
    private List<DBMessage> mListTemp = new ArrayList<>();
    private long dbChannleId;
    private String channalId; // if 0 private, else group
    private String friendId;   //send message to  who
    private int mCurrentPage = Constants.DEFAULT_PAGE;
    private TextView tvLoading;
    private boolean loadFinished = false;
    private String chatName = "iApps";

    public FragmentChat(){

    }

    public FragmentChat(long dbChannleId, String channalId, String friendId,String chatName) {
        this.channalId = channalId;
        this.friendId = friendId;
        this.dbChannleId = dbChannleId;
        this.chatName = chatName;
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
        home().setActionBar(chatName);
        home().enableHomeUp();
        home().hideBottomBar();

        if (mMessageAdapter == null) {
            init();
        }
    }

    private void init() {

        //init lvChat
        mMessageAdapter = new MessageAdapter(getActivity(), mBeanMessages);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.cell_loading, null);
        tvLoading = (TextView) header.findViewById(R.id.tvLoading);
        lvChat.addHeaderView(header);
        lvChat.setAdapter(mMessageAdapter);

        //get message data from DB
        mCurrentPage = Constants.DEFAULT_PAGE;
        mListTemp.clear();
        mListTemp = (ArrayList) home().getDBManager().getMessageRecord(Constants.PAGE_LIMIT, (mCurrentPage - 1) * Constants.PAGE_LIMIT, clientId, friendId, channalId);
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
                    SimpleDateFormat sdf =   new SimpleDateFormat( Constants.DATE_TIME_JSON );
                    DBMessage dbMessage = new DBMessage(null, clientId,edtMessage.getText().toString(), sdf.format(date), clientId, friendId, channalId, UserInfoManager.getInstance(getActivity()).getAvatar(), "","",true,true);
                    mBeanMessages.add(dbMessage);
                    mMessageAdapter.notifyDataSetChanged();
                    BroadcastManager.sendTxtMessage(getActivity(),edtMessage.getText().toString(), channalId, friendId);
                    //save message
                    home().getDBManager().saveMessage(dbMessage);
                    edtMessage.setText("");
                    handled = true;
                }
                return handled;
            }
        });

        home().setMessageListener(new HomeActivity.MessageReceiveListener() {
            @Override
            public void onReceive(String cmd,String data) {
                if(cmd.equals(Constants.CMD_FROMMESSAGE)){
                    try {
                        DBMessage dbMessage = Converter.toTxtMessage(data, clientId, false, true);
                        filterMessage(dbMessage);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }else if(cmd.equals(Constants.CMD_OFFLINE_MESSAGE)){
                    //off line message
                    List<DBMessage> messages = Converter.toListTxtMessage(data, clientId, false, true);
                    for (int i = 0; i < messages.size(); i++){
                        filterMessage(messages.get(i));
                    }
                }
            }
        });

        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0 && !loadFinished) {
                    if (totalItemCount > 0) {
                        GetMessageDataTask task = new GetMessageDataTask();
                        task.execute();
                    }
                }
            }
        });
    }

    private void filterMessage(DBMessage dbMessage){
        if((dbMessage.getChannelId() .equals(Constants.PRIVATE_CHANNEL_ID)  && dbMessage.getFromId().equals(friendId))
                || (!dbMessage.getChannelId().equals(Constants.PRIVATE_CHANNEL_ID)  && dbMessage.getChannelId().equals(channalId))){

            mBeanMessages.add(dbMessage);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    private void sortMessage() {
        //sort the message
        Comparator<DBMessage> comparator = new Comparator<DBMessage>() {
            public int compare(DBMessage s1, DBMessage s2) {

                if (s1.getId() != s2.getId()) {
                    return (int) (s1.getId() - s2.getId());
                }
                return 0;
            }
        };
        Collections.sort(mListTemp, comparator);
    }

    public class GetMessageDataTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            mListTemp.clear();
            mListTemp = (ArrayList) home().getDBManager().getMessageRecord(Constants.PAGE_LIMIT, (mCurrentPage - 1) * Constants.PAGE_LIMIT, clientId, friendId, channalId);

            if (mListTemp.size() < Constants.PAGE_LIMIT) {
                loadFinished = true;
            } else {
                mCurrentPage++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            tvLoading.setVisibility(View.GONE);

            if (mListTemp.size() > 0) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBeanMessages.size() > 0){
            updateChat();
        }
    }

    public void updateChat(){
        //update chat: message and date;
        List<DBChat> list = home().getDBManager().getChat(channalId, friendId);
        home().getDBManager().updateChat(dbChannleId, channalId, friendId, list.get(0).getName(),
                mBeanMessages.get(mBeanMessages.size() - 1).getMessage(),
                mBeanMessages.get(mBeanMessages.size() - 1).getDate(), list.get(0).getImage(),"0");
    }
}
