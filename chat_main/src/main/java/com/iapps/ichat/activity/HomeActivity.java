package com.iapps.ichat.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iapps.ichat.R;
import com.iapps.ichat.fragment.FragmentChatList;
import com.iapps.ichat.fragment.FragmentFriends;
import com.iapps.ichat.fragment.FragmentProfile;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Converter;
import com.iapps.ichat.helper.DBManager;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.Keys;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.generics.GenericFragmentActivity;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.objects.SimpleBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBMessage;
import roboguice.inject.InjectView;


public class HomeActivity extends GenericFragmentActivity implements View.OnClickListener{

    private ArrayList<? extends SimpleBean> mResults = new ArrayList<SimpleBean>();
    private boolean isResultChanged;

    private MessageDataReceiver mDataReceiver;
    private MessageReceiveListener messageListener;

    @InjectView(R.id.main_bottom)
    private LinearLayout main_bottom;
    @InjectView(R.id.ib_weixin)
    private ImageView ib_weixin;
    @InjectView(R.id.ib_contact_list)
    private ImageView ib_contact_list;
    @InjectView(R.id.ib_profile)
    private ImageView ib_profile;
    @InjectView(R.id.re_weixin)
    private RelativeLayout re_weixin;
    @InjectView(R.id.re_contact_list)
    private RelativeLayout re_contact_list;
    @InjectView(R.id.re_profile)
    private RelativeLayout re_profile;

    private DBManager dbManager;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){
        //connect to server  dialog.
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);

        //init bottom bar
        re_weixin.setOnClickListener(this);
        re_contact_list.setOnClickListener(this);
        re_profile.setOnClickListener(this);
        ib_weixin.setSelected(true);
        setFragment(new FragmentChatList());

        //register  broadcast
        if(mDataReceiver == null){
            mDataReceiver = new MessageDataReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_SERVICE_TO_USER);
        registerReceiver(mDataReceiver, filter);
        NotificationManager m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        m_NotificationManager.cancel(1989);

    }

    public void changeTabBar(int position){
        switch (position){
            case 0:
                ib_weixin.setSelected(true);
                ib_contact_list.setSelected(false);
                ib_profile.setSelected(false);
                break;
            case 1:
                ib_weixin.setSelected(false);
                ib_contact_list.setSelected(true);
                ib_profile.setSelected(false);
                break;

            case 2:
                ib_weixin.setSelected(false);
                ib_contact_list.setSelected(false);
                ib_profile.setSelected(true);
                break;
            default:
                ib_weixin.setSelected(true);
                ib_contact_list.setSelected(false);
                ib_profile.setSelected(false);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int position  = 0;
        clearFragment();
        switch (id){
            case R.id.re_weixin:
                position = 0;
                setFragment(new FragmentChatList());
                break;
            case R.id.re_contact_list:
                setFragment(new FragmentFriends());
                position = 1;
                break;
            case R.id.re_profile:
                setFragment(new FragmentProfile());
                position = 2;
                break;
        }
        changeTabBar(position);

    }


    public void setActionBar(String Title){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        setSupportProgressBarIndeterminateVisibility(false);

        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionTitle = (TextView)findViewById(titleId);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + Title + "</font>"));
        actionTitle.setTextAppearance(this, android.R.style.TextAppearance_Large);
    }

    public void enableHomeUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /******************************************
     * INTERMEDIARY FUNCTIONS (Help to communicate between fragments)
     ****************************************/
    @SuppressWarnings("unchecked")
    public void setResultList(ArrayList<? extends SimpleBean> alResult) {
        isResultChanged = true;
        this.mResults = (ArrayList<? extends SimpleBean>) alResult.clone();
    }

    public ArrayList<? extends SimpleBean> getResultList() {
        isResultChanged = false;
        return this.mResults;
    }

    public boolean isResultChanged() {
        return isResultChanged;
    }

    public void setResultChanged(boolean isResultChanged) {
        this.isResultChanged = isResultChanged;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            this.moveTaskToBack(true);
        }
        else {
            super.onBackPressed();
        }

    }

    public String getTitle(int res) {
        return getResources().getString(res);
    }

    /******************************************
     * FRAGMENT FUNCTIONS
     ****************************************/
    public void addFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().add(R.id.layoutFragment, frag).addToBackStack(null).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        BaseUIHelper.hideKeyboard(this);
    }

    public void setFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.layoutFragment, frag).addToBackStack(null).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setSupportProgressBarIndeterminateVisibility(false);
        BaseUIHelper.hideKeyboard(this);
    }

    public void clearFragment() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public Fragment getCurrentFragment(){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.layoutFragment);
        return f;
    }

    public void popBackstack() {
        getSupportFragmentManager().popBackStack();
    }

    public Fragment getBackstack() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list != null && !list.isEmpty()) {
            // Sometimes the last fragment in the list is null
            // Idk why
            if (list.get(list.size() - 1) == null)
                return list.get(list.size() - 2);

            return list.get(list.size() - 1);
        }

        return null;
    }

    public Fragment getCurrentstack() {
        List<Fragment> list = getSupportFragmentManager().getFragments();
        if (list != null && !list.isEmpty()) {
            return list.get(list.size() - 1);
        }

        return null;
    }

   /**
     * Clear all fragments in the backstack, except the bottom of the stack
     */
    public void reloadFirst() {
        try {
            FragmentManager manager = getSupportFragmentManager();

            if (manager.getBackStackEntryCount() > 1) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTxtMessage(String message,String channalId,String toId){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_USER_TO_SERVICE);
        String mClientId = UserInfoManager.getInstance(this).getClientId();
        message = Helper.generateTxtMessage(message,channalId,mClientId,toId);
        intent.putExtra(Keys.CONTENT, message);
        intent.putExtra(Keys.CMD, Constants.CMD_MESSAGE);
        sendBroadcast(intent);
    }

    private class MessageDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Bundle bundle = intent.getExtras();
                String data = bundle.getString(Keys.CONTENT);
                String cmd = bundle.getString(Keys.CMD);
                JSONObject json = null;
                try {
                    json = new JSONObject(data);
                }catch (Exception e){

                }
                if(cmd.equals(Constants.CMD_RECONNECT)){
                    //tell service to reconnect to server
                    dialog.show();
                    sendReconnectMessage();
                }else if(cmd.equals(Constants.CMD_CONNECT)){
                    //send login cmd again
                    sendLoginMessage();
                }else if(json.getString(Keys.CMD).equals(Constants.CMD_LOGIN) && json.getInt(Keys.STATUS_CODE) == Constants.LOGIN_SUCCESSFULLY){
                    dialog.dismiss();
                }else if(json.getString(Keys.CMD).equals(Constants.CMD_LOGIN) && json.getInt(Keys.STATUS_CODE) != Constants.LOGIN_SUCCESSFULLY){
                    dialog.dismiss();
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                    finish();
                }else if(json.getString(Keys.CMD).equals(Constants.CMD_FRIEND_LIST)){
                    messageListener.onReceive(data);
                }else if(json.getString(Keys.CMD).equals(Constants.CMD_FROMMESSAGE)){

                    String clientId = UserInfoManager.getInstance(HomeActivity.this).getClientId();
                    DBMessage message = Converter.toTxtMessage(data,clientId,false,true);

                    //create or update chat data
                    updateChatData(message);

                    dbManager.saveMessage(message);
                    messageListener.onReceive(data);
                }
            } catch (Exception e) {
            }
        }
    }

    private void updateChatData(DBMessage message){
        List<DBChat> list = dbManager.getChat(message.getChannelId(), message.getFromId());
        if(list.size() == 0){
            // create new chat
            if(message.getChannelId().equals(Constants.PRIVATE_CHANNEL_ID)){
                //private chat
                dbManager.saveChat(message.getChannelId(), message.getFromId(), message.getFromName(), message.getMessage(), message.getDate(), message.getImgUrl());
            }else{
                //group chat
                dbManager.saveChat(message.getChannelId(), message.getFromId(), message.getChannalName(), message.getMessage(), message.getDate(), "");
            }
        }else{
            //update chat: message and date;
            dbManager.updateChat(list.get(0).getId(), list.get(0).getChannalId(), list.get(0).getFriend_userId(), list.get(0).getName(),
                    message.getMessage(),
                    message.getDate(), message.getImgUrl());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public interface MessageReceiveListener {
        public void onReceive(String data);
    }

    public void setMessageListener(MessageReceiveListener messageListener){
        this.messageListener = messageListener;
    }

    public void showBottomBar(){
        main_bottom.setVisibility(View.VISIBLE);
    }

    public void hideBottomBar(){
        main_bottom.setVisibility(View.GONE);
    }

    public void sendGetFriends(){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_USER_TO_SERVICE);
        intent.putExtra(Keys.CMD, Constants.CMD_MESSAGE);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(this);
        String message = Helper.generateGetFriendMessage(userInfoManager.getClientId());
        intent.putExtra(Keys.CONTENT, message);
        sendBroadcast(intent);
    }

    public void sendReconnectMessage(){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_USER_TO_SERVICE);
        intent.putExtra(Keys.CMD, Constants.CMD_RECONNECT);
        intent.putExtra(Keys.CONTENT, "");
        sendBroadcast(intent);
    }

    public void sendLoginMessage(){
        Intent intent = new Intent();
        intent.setAction(Constants.CMD_USER_TO_SERVICE);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(this);
        String message = Helper.generateLoginMessage(userInfoManager.getAccount(), userInfoManager.getPWD(), userInfoManager.getAvatar());
        intent.putExtra(Keys.CMD, Constants.CMD_MESSAGE);
        intent.putExtra(Keys.CONTENT, message);
        sendBroadcast(intent);
    }

    public DBManager getDBManager(){
        if(dbManager == null){
            dbManager = new DBManager(this);
        }
        return dbManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDataReceiver);
    }

    public void logout(){
//        UserInfoManager.getInstance(this).logout();
////        clearFragment();
//        finish();
//        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}
