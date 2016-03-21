package com.iapps.ichat.activity;

import android.app.ActivityManager;
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
import android.widget.Toast;

import com.iapps.ichat.R;
import com.iapps.ichat.fragment.FragmentChatList;
import com.iapps.ichat.fragment.FragmentFriends;
import com.iapps.ichat.fragment.FragmentProfile;
import com.iapps.ichat.helper.BroadcastManager;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Converter;
import com.iapps.ichat.helper.DBManager;
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


public class HomeActivity extends GenericFragmentActivity implements View.OnClickListener {

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
    private boolean isActive = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
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
        if (mDataReceiver == null) {
            mDataReceiver = new MessageDataReceiver();
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_TO_USER);
        registerReceiver(mDataReceiver, filter);

        //get offline message
        BroadcastManager.sendGetOffLineMessage(HomeActivity.this);
    }

    public void changeTabBar(int position) {
        switch (position) {
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
        int position = 0;
        clearFragment();
        switch (id) {
            case R.id.re_weixin:
                position = 0;
                setFragment(new FragmentChatList());
                break;
            case R.id.re_contact_list:
                FragmentFriends f = new FragmentFriends();
                f.setSelectFriend(false);
                setFragment(f);
                position = 1;
                break;
            case R.id.re_profile:
                setFragment(new FragmentProfile());
                position = 2;
                break;
        }
        changeTabBar(position);

    }


    public void setActionBar(String Title) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        setSupportProgressBarIndeterminateVisibility(false);

        int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionTitle = (TextView) findViewById(titleId);
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
        } else {
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

    public Fragment getCurrentFragment() {
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

    //receive the command or message from the MessageService.
    private class MessageDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String clientId = UserInfoManager.getInstance(HomeActivity.this).getClientId();
            try {
                Bundle bundle = intent.getExtras();
                String data = bundle.getString(Keys.CONTENT);
                String cmd = bundle.getString(Keys.CMD);
                JSONObject json = null;
                try {
                    json = new JSONObject(data);
                } catch (Exception e) {
                }
                if (cmd.equals(Constants.CMD_RECONNECT)) {  //need  reconnect to server
                    if(isActive){
                        //reconnect to server
                        dialog.show();
                        BroadcastManager.sendReconnectMessage(HomeActivity.this);
                    }
                } else if (cmd.equals(Constants.CMD_CONNECT)) { //connect successfully.
                    //send login cmd again
                    BroadcastManager.sendLoginMessage(HomeActivity.this, UserInfoManager.getInstance(HomeActivity.this).getAccount(), UserInfoManager.getInstance(HomeActivity.this).getPWD(), UserInfoManager.getInstance(HomeActivity.this).getAvatar());
                } else if (json.getString(Keys.CMD).equals(Constants.CMD_LOGIN) && json.getInt(Keys.STATUS_CODE) == Constants.LOGIN_SUCCESSFULLY) {
                    //login successfully.
                    dialog.dismiss();
                    BroadcastManager.sendGetOffLineMessage(HomeActivity.this);
                } else if (json.getString(Keys.CMD).equals(Constants.CMD_LOGIN) && json.getInt(Keys.STATUS_CODE) != Constants.LOGIN_SUCCESSFULLY) {
                    //login failed
                    dialog.dismiss();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                } else if (json.getString(Keys.CMD).equals(Constants.CMD_OFFLINE_MESSAGE)) {
                    //off line message
                    List<DBMessage> messages = Converter.toListTxtMessage(data, clientId, false, true);
                    for (int i = 0; i < messages.size(); i++){
                        //create or update chat data
                        updateChatData(messages.get(i));
                        //save message
                        dbManager.saveMessage(messages.get(i));
                    }

                    messageListener.onReceive(json.getString(Keys.CMD), data);
                }  else if (json.getString(Keys.CMD).equals(Constants.CMD_FROMMESSAGE)) {
                    // receive message
                    DBMessage message = Converter.toTxtMessage(data, clientId, false, true);

                    //create or update chat data
                    updateChatData(message);

                    //save meesage
                    dbManager.saveMessage(message);
                    messageListener.onReceive(json.getString(Keys.CMD),data);
                } else{
                    messageListener.onReceive(json.getString(Keys.CMD),data);
                }
            } catch (Exception e) {
                Toast.makeText(HomeActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateChatData(DBMessage message) {
        List<DBChat> list;
        if(message.getChannelId().equals(Constants.PRIVATE_CHANNEL_ID)){
            list = dbManager.getChat(Constants.PRIVATE_CHANNEL_ID, message.getFromId());
        }else{
            list = dbManager.getChatByChannalId(message.getChannelId());
        }

        if (list.size() == 0) {
            // create new chat
            if (message.getChannelId().equals(Constants.PRIVATE_CHANNEL_ID)) {
                //private chat
                DBChat chat = new DBChat(null,UserInfoManager.getInstance(HomeActivity.this).getClientId(),message.getChannelId(),message.getFromId(),message.getFrom_name(),message.getMessage(), message.getDate(),message.getImage(),"1");
                dbManager.saveChat(chat);
            } else {
                //group chat
                DBChat chat = new DBChat(null,UserInfoManager.getInstance(HomeActivity.this).getClientId(),message.getChannelId(),Constants.GROUP_USER_ID,message.getChannal_name(),message.getMessage(), message.getDate(), message.getImage(),"1");
                dbManager.saveChat(chat);
            }
        } else {
            //update chat: message and date;
            DBChat chat = list.get(0);
            String unReadNum = (Integer.parseInt(chat.getUnread_num()) + 1) + "";
            dbManager.updateChat(chat.getId(), chat.getChannalId(), chat.getFriend_userId(), chat.getName(),
                    message.getMessage(),
                    message.getDate(), message.getImage(),unReadNum);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public interface MessageReceiveListener {
        public void onReceive(String cmd,String data);
    }

    public void setMessageListener(MessageReceiveListener messageListener) {
        this.messageListener = messageListener;
    }

    public void showBottomBar() {
        main_bottom.setVisibility(View.VISIBLE);
    }

    public void hideBottomBar() {
        main_bottom.setVisibility(View.GONE);
    }


    public DBManager getDBManager() {
        if (dbManager == null) {
            dbManager = new DBManager(this);
        }
        return dbManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDataReceiver);
        isActive = false;
        BroadcastManager.sendDisconnectMessage(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            isActive = false;
            BroadcastManager.sendDisconnectMessage(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            dialog.show();
            BroadcastManager.sendReconnectMessage(this);
        }
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
