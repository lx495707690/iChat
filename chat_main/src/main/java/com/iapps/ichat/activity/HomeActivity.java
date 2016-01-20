package com.iapps.ichat.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.iapps.ichat.fragment.FragmentContacts;
import com.iapps.ichat.helper.Constants;
import com.iapps.ichat.helper.Converter;
import com.iapps.ichat.helper.Helper;
import com.iapps.ichat.helper.Keys;
import com.iapps.ichat.helper.UserInfoManager;
import com.iapps.libs.generics.GenericFragmentActivity;
import com.iapps.libs.helpers.BaseUIHelper;
import com.iapps.libs.objects.SimpleBean;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBChatDao;
import me.itangqi.greendao.DBFriendDao;
import me.itangqi.greendao.DBMessage;
import me.itangqi.greendao.DBMessageDao;
import me.itangqi.greendao.DaoMaster;
import me.itangqi.greendao.DaoSession;
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
//    @InjectView(R.id.ib_find)
//    private ImageView ib_find;
    @InjectView(R.id.ib_profile)
    private ImageView ib_profile;

    @InjectView(R.id.re_weixin)
    private RelativeLayout re_weixin;
    @InjectView(R.id.re_contact_list)
    private RelativeLayout re_contact_list;
//    @InjectView(R.id.re_find)
//    private RelativeLayout re_find;
    @InjectView(R.id.re_profile)
    private RelativeLayout re_profile;

    //DB
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init(){

        //init database
        setupDatabase();

        //init bottom bar
        re_weixin.setOnClickListener(this);
        re_contact_list.setOnClickListener(this);
//        re_find.setOnClickListener(this);
        re_profile.setOnClickListener(this);
        ib_weixin.setSelected(true);
        setFragment(new FragmentChatList());

        //register  broadcast
        mDataReceiver = new MessageDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CMD_RECEIVEMESSAGE);
        registerReceiver(mDataReceiver, filter);
        NotificationManager m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        m_NotificationManager.cancel(1989);
    }

    private void changeTabBar(int position){
        switch (position){
            case 0:
                ib_weixin.setSelected(true);
                ib_contact_list.setSelected(false);
//                ib_find.setSelected(false);
                ib_profile.setSelected(false);
                break;
            case 1:
                ib_weixin.setSelected(false);
                ib_contact_list.setSelected(true);
//                ib_find.setSelected(false);
                ib_profile.setSelected(false);
                break;

            case 2:
                ib_weixin.setSelected(false);
                ib_contact_list.setSelected(false);
//                ib_find.setSelected(true);
                ib_profile.setSelected(false);
                break;

//            case 3:
//                ib_weixin.setSelected(false);
//                ib_contact_list.setSelected(false);
//                ib_find.setSelected(false);
//                ib_profile.setSelected(true);
//                break;
            default:
                ib_weixin.setSelected(true);
                ib_contact_list.setSelected(false);
//                ib_find.setSelected(false);
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
                setFragment(new FragmentContacts());
                position = 1;
                break;

//            case R.id.re_find:
//                setFragment(new FragmentContacts());
//                position = 2;
//                break;

            case R.id.re_profile:
                setFragment(new FragmentContacts());
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
        intent.setAction(Constants.CMD_SENDMESSAGE);
        String mClientId = UserInfoManager.getInstance(this).getClientId();
        message = Helper.generateTxtMessage(message,channalId,mClientId,toId);
        intent.putExtra(Keys.MESSAGE_DATA, message);
        sendBroadcast(intent);
    }

    private class MessageDataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Bundle bundle = intent.getExtras();
                String data = bundle.getString(Keys.MESSAGE_DATA);
                String clientId = UserInfoManager.getInstance(HomeActivity.this).getClientId();
                DBMessage message = Converter.toTxtMessage(data, clientId, "", false);
                saveMessage(message);

                messageListener.onReceive(data);
            } catch (Exception e) {
            }
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

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        getDBChatDao();

        String textColumn = DBChatDao.Properties.Message.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(getDBChatDao().getTablename(), getDBChatDao().getAllColumns(), null, null, null, null, orderBy);

    }

    //add chat record
    public void saveChat(String channelId, String userId, String name, String message, String date, String imgUrl) {
        DBChat chat = new DBChat(null,channelId,userId,name,message,date,imgUrl);
        getDBChatDao().insert(chat);
        cursor.requery();
    }

    //add message record
    public void saveMessage(DBMessage message) {
        getDBMessageDao().insert(message);
        cursor.requery();
    }

    //search record
    public List searchDB(int limit,int offset,String table) {
        Query query = null;
        if(table.equals(Constants.DB_CHAT)){
            query = getDBChatDao().queryBuilder()
                    .orderDesc(DBChatDao.Properties.Date)
                    .build();
        }else if(table.equals(Constants.DB_MESSAGE)){
            query = getDBMessageDao().queryBuilder()
                    .orderDesc(DBMessageDao.Properties.Id)
                    .offset(offset)
                    .limit(limit)
                    .build();
        }else if(table.equals(Constants.DB_FRIEND)){
            query = getDBFriendDao().queryBuilder()
                    .orderDesc(DBFriendDao.Properties.Id)
                    .limit(limit)
                    .build();
        }
        List results = query.list();
        return  results;
    }

    //message record
    public List getMessageRecord(int limit,int offset,String myId,String friendId,String channalId) {
        Query query = null;
        if(channalId.equals("0")){
            QueryBuilder qb = getDBMessageDao().queryBuilder();
            qb.where(qb.or(qb.and(DBMessageDao.Properties.FromId.eq(myId), DBMessageDao.Properties.ToId.eq(friendId)),
                    qb.and(DBMessageDao.Properties.FromId.eq(friendId), DBMessageDao.Properties.ToId.eq(myId))))
                    .orderDesc(DBMessageDao.Properties.Id)
                    .offset(offset)
                    .limit(limit);
            query = qb.build();
        }else{
            query = getDBMessageDao().queryBuilder()
                    .orderDesc(DBMessageDao.Properties.Id)
                    .where(DBMessageDao.Properties.ChannleId.eq(channalId))
                    .offset(offset)
                    .limit(limit)
                    .build();
        }
        List results = query.list();
        return  results;
    }

    //delete record
    public void deleteDB(long id,String table){
        if(table.equals(Constants.DB_CHAT)){
            getDBChatDao().deleteByKey(id);
        }else if(table.equals(Constants.DB_MESSAGE)){
            getDBMessageDao().deleteByKey(id);
        }
//        getNoteDao().deleteAll();
        cursor.requery();
    }

    private DBChatDao getDBChatDao() {
        return daoSession.getDBChatDao();
    }

    private DBMessageDao getDBMessageDao() {
        return daoSession.getDBMessageDao();
    }

    private DBFriendDao getDBFriendDao() {
        return daoSession.getDBFriendDao();
    }
}
