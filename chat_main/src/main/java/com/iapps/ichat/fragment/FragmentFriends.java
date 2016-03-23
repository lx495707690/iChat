package com.iapps.ichat.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.iapps.ichat.R;
import com.iapps.ichat.activity.HomeActivity;
import com.iapps.ichat.adapter.SortFriendsAdapter;
import com.iapps.ichat.helper.BroadcastManager;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.ContactsSideBar;
import com.iapps.ichat.helper.GenericFragmentiChat;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.Keys;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBFriend;
import roboguice.inject.InjectView;

public class FragmentFriends extends GenericFragmentiChat {
    @InjectView(R.id.sbContacts)
    private ContactsSideBar sbContacts;
    @InjectView(R.id.tvDialog)
    private TextView tvDialog;
    @InjectView(R.id.lvContacts)
    private ListView lvContacts;
    @InjectView(R.id.title_layout)
    private LinearLayout titleLayout;
    @InjectView(R.id.title_layout_catalog)
    private TextView title;
    @InjectView(R.id.edtName)
    private EditText edtName;

//    @InjectView(R.id.LLLoading)
//    private LinearLayout LLLoading;

    private SortFriendsAdapter mFriendAdapter;

    private int lastFirstVisibleItem = -1;
    private List<DBFriend> mSourceDataList = new ArrayList<>();
    private boolean selectFriend = false;

    private ProgressDialog dialog;

    public void setSelectFriend(boolean selectFriend) {
        this.selectFriend = selectFriend;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (selectFriend) {
            setHasOptionsMenu(true);
            home().hideBottomBar();
            edtName.setVisibility(View.VISIBLE);
            home().enableHomeUp();
        }else{
            setHasOptionsMenu(false);
            home().showBottomBar();
            edtName.setVisibility(View.GONE);
        }
        initView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_friend_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tick:
                //create group.
                String groupName = edtName.getText().toString().trim();
                if(Helper.isEmpty(groupName)){
                    Toast.makeText(getActivity(),"Please input the group name.",Toast.LENGTH_SHORT).show();
                    return false;
                }

                String inviteUsers = clientId;
                for (int i = 0; i < mSourceDataList.size();i++){
                    if(mSourceDataList.get(i).getIsSelected()){
                        if(Helper.isEmpty(inviteUsers)){
                            inviteUsers = mSourceDataList.get(i).getFriend_userId();
                        }else{
                            inviteUsers += "," + mSourceDataList.get(i).getFriend_userId();
                        }

                        mSourceDataList.get(i).setIsSelected(false);
                    }
                }

                if(Helper.isEmpty(inviteUsers)){
                    Toast.makeText(getActivity(),"Please select one friend at least.",Toast.LENGTH_SHORT).show();
                    return false;
                }
                BroadcastManager.sendCreateGroupMessage(getActivity(),groupName,"",inviteUsers);
                dialog.show();
                return true;
            case android.R.id.home:
                Helper.simulateKey(KeyEvent.KEYCODE_BACK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView() {

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.loading));

        sbContacts.setTextView(tvDialog);
        sbContacts.setOnTouchingLetterChangedListener(new ContactsSideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = mFriendAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    lvContacts.setSelection(position);
                }
            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (!selectFriend) {
                    home().setFragment(new FragmentChatList());
                    home().changeTabBar(0);
                    DBFriend friend = mSourceDataList.get(position);

                    long dbChannelId;
                    //check if the chat already exist;  if not create a new chat;
                    List<DBChat> chatList = home().getDBManager().getChat(Constants.PRIVATE_CHANNEL_ID, friend.getFriend_userId());
                    if (chatList.size() == 0) {
                        //create new chat
                        DBChat chat = new DBChat(null,clientId,Constants.PRIVATE_CHANNEL_ID,friend.getFriend_userId(),friend.getName(),"","",friend.getAvatar(),"0");
                        dbChannelId = home().getDBManager().saveChat(chat);
                    } else {
                        dbChannelId = chatList.get(0).getId();
                    }

                    home().setFragment(new FragmentChat(dbChannelId, Constants.PRIVATE_CHANNEL_ID, friend.getFriend_userId(),friend.getName()));
                } else {
                    if (mSourceDataList.get(position).getIsSelected()) {
                        mSourceDataList.get(position).setIsSelected(false);
                    } else {
                        mSourceDataList.get(position).setIsSelected(true);
                    }
                    mFriendAdapter.notifyDataSetChanged();
                }
            }
        });

        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mSourceDataList.size() > 0) {
                    int section = getSectionForPosition(firstVisibleItem);
                    int nextSection = getSectionForPosition(firstVisibleItem + 1);
                    int nextSecPosition = getPositionForSection(+nextSection);
                    if (firstVisibleItem != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                .getLayoutParams();
                        params.topMargin = 0;
                        titleLayout.setLayoutParams(params);
                        title.setText(mSourceDataList.get(
                                getPositionForSection(section)).getSortLetters());
                    }
                    if (nextSecPosition == firstVisibleItem + 1) {
                        View childView = view.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = titleLayout.getHeight();
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) titleLayout
                                    .getLayoutParams();
                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                params.topMargin = (int) pushedDistance;
                                titleLayout.setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    titleLayout.setLayoutParams(params);
                                }
                            }
                        }
                    }
                    lastFirstVisibleItem = firstVisibleItem;
                }
            }
        });

        home().setMessageListener(new HomeActivity.MessageReceiveListener() {
            @Override
            public void onReceive(String cmd, String data) {
                try {
                    dialog.dismiss();
                    JSONObject json = new JSONObject(data);
                    if (cmd.equals(Constants.CMD_FRIEND_LIST)) {
                        JSONArray jsonFriends = json.getJSONArray(Keys.DATA);
                        mSourceDataList.clear();
                        for (int i = 0; i < jsonFriends.length(); i++) {
                            String friendId = jsonFriends.getJSONObject(i).getString(Keys.USER_ID);
                            String username = jsonFriends.getJSONObject(i).getString(Keys.USER_NAME);
                            String avatar = jsonFriends.getJSONObject(i).getString(Keys.USER_AVATAR);
                            DBFriend friend = new DBFriend(null, clientId, friendId, username, avatar, "", "");
                            mSourceDataList.add(friend);
                            //save friend
                            home().getDBManager().saveFriend(friend);
                        }

                        filledData();
                        mFriendAdapter = new SortFriendsAdapter(getActivity(), mSourceDataList);
                        lvContacts.setAdapter(mFriendAdapter);


                    } else if (cmd.equals(Constants.CMD_CREATE_GROUP) && json.getInt(Keys.STATUS_CODE) == Constants.CREATE_GROUP_SUCCESSFULLY) {
                        //create group successfully.
                        JSONObject groupData = json.getJSONObject(Keys.DATA);
                        String channalId = groupData.getString(Keys.ID);
                        String groupName = groupData.getString(Keys.NAME);
                        String groupAvatar = groupData.getString(Keys.USER_AVATAR);

                        Date date = new Date();
                        SimpleDateFormat sdf =   new SimpleDateFormat( Constants.DATE_TIME_JSON );

                        DBChat chat = new DBChat(null,clientId,channalId,Constants.GROUP_USER_ID,groupName,"",sdf.format(date),groupAvatar,"0");
                        long dbChannelId = home().getDBManager().saveChat(chat);
                        home().popBackstack();
                        home().setFragment(new FragmentChat(dbChannelId, channalId, Constants.GROUP_USER_ID,groupName));
                    }else{
                        dialog.dismiss();
                        if(json.has(Keys.MESSAGE)){
                            Helper.showAlert(getActivity(), json.getString(Keys.MESSAGE));
                        }
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

//        //get friend from database first.
//        List<DBFriend> list = home().getDBManager().searchDB(Constants.DB_FRIEND);
//        if (list.size() > 0) {
//            mSourceDataList.clear();
//            mSourceDataList.addAll(list);
//            filledData();
//            mFriendAdapter = new SortFriendsAdapter(getActivity(), mSourceDataList);
//            lvContacts.setAdapter(mFriendAdapter);
//        } else {
            //get friends
            BroadcastManager.sendGetFriends(getActivity());
//        }
    }

    private void filledData() {

        for (int i = 0; i < mSourceDataList.size(); i++) {
            String pinyin = Helper.converterToPinYin(mSourceDataList.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                mSourceDataList.get(i).setSortLetters(sortString.toUpperCase());
            } else {
                mSourceDataList.get(i).setSortLetters("#");
            }
        }

        Collections.sort(mSourceDataList);
    }

    public int getSectionForPosition(int position) {

        if (mSourceDataList.size() <= position) {
            return mSourceDataList.size() - 1;
        }
        return mSourceDataList.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < mSourceDataList.size(); i++) {
            String sortStr = mSourceDataList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
