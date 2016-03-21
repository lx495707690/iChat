package com.iapps.ichat.helper;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import me.itangqi.greendao.DBChat;
import me.itangqi.greendao.DBChatDao;
import me.itangqi.greendao.DBFriend;
import me.itangqi.greendao.DBFriendDao;
import me.itangqi.greendao.DBMessage;
import me.itangqi.greendao.DBMessageDao;
import me.itangqi.greendao.DaoMaster;
import me.itangqi.greendao.DaoSession;

public class DBManager {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Cursor cursor;
    private Context ctx;

    public DBManager(Context ctx){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        getDBChatDao();

        String textColumn = DBChatDao.Properties.Message.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(getDBChatDao().getTablename(), getDBChatDao().getAllColumns(), null, null, null, null, orderBy);
        this.ctx = ctx;
    }

    //add chat record
    public long saveChat(DBChat chat) {
//        DBChat chat = new DBChat(null,UserInfoManager.getInstance(ctx).getClientId(),channelId,userId,name,message,date,imgUrl,"0");
        long id = getDBChatDao().insert(chat);
        cursor.requery();
        return id;
    }

    //add friend record
    public long saveFriend(DBFriend friend) {
        long id = getDBFriendDao().insert(friend);
        cursor.requery();
        return id;
    }

    //add message record
    public void saveMessage(DBMessage message) {
        getDBMessageDao().insert(message);
        cursor.requery();
    }

    public List getFriendById(String friendId){
        Query query = getDBFriendDao().queryBuilder()
                        .where(DBFriendDao.Properties.Friend_userId.eq(friendId))
                        .where(DBFriendDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                        .limit(1)
                        .build();
        List results = query.list();
        return  results;
    }

    //search record
    public List searchDB(String table) {
        Query query = null;
        if(table.equals(Constants.DB_CHAT)){
            query = getDBChatDao().queryBuilder()
                    .where(DBChatDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                    .orderDesc(DBChatDao.Properties.Date)
                    .build();
        }else if(table.equals(Constants.DB_FRIEND)){
            query = getDBFriendDao().queryBuilder()
                    .where(DBFriendDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                    .build();
        }

        try {
            List results = query.list();
            return  results;
        }catch (Exception e){

        }
        return  null;
    }

    //get chat
    public List getChat(String channelId,String userId){

        Query query = getDBChatDao().queryBuilder()
                .where(DBChatDao.Properties.ChannalId.eq(channelId))
                .where(DBChatDao.Properties.Friend_userId.eq(userId))
                .where(DBChatDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                .limit(1)
                .build();
        List results = query.list();
        return  results;
    }

    public List getChatByChannalId(String channelId){
        Query query = getDBChatDao().queryBuilder()
                .where(DBChatDao.Properties.ChannalId.eq(channelId))
                .where(DBChatDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                .limit(1)
                .build();
        List results = query.list();
        return  results;
    }

    //update chat
    public void updateChat(Long id, String channelId, String userId, String name, String message, String date, String imgUrl, String unReadNum){
        getDBChatDao().update(new DBChat(id,UserInfoManager.getInstance(ctx).getClientId(),channelId,userId,name,message,date,imgUrl,unReadNum));
    }

    //message record
    public List getMessageRecord(int limit,int offset,String myId,String friendId,String channalId) {
        Query query = null;
        if(channalId.equals(Constants.PRIVATE_CHANNEL_ID)){
            QueryBuilder qb = getDBMessageDao().queryBuilder();
            qb.where(qb.or(qb.and(DBMessageDao.Properties.FromId.eq(myId), DBMessageDao.Properties.ToId.eq(friendId)),
                    qb.and(DBMessageDao.Properties.FromId.eq(friendId), DBMessageDao.Properties.ToId.eq(myId))))
                    .where(DBMessageDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                    .where(DBMessageDao.Properties.ChannelId.eq(Constants.PRIVATE_CHANNEL_ID))
                    .orderDesc(DBMessageDao.Properties.Id)
                    .offset(offset)
                    .limit(limit);
            query = qb.build();
        }else{
            query = getDBMessageDao().queryBuilder()
                    .where(DBMessageDao.Properties.ChannelId.eq(channalId))
                    .where(DBMessageDao.Properties.My_userId.eq(UserInfoManager.getInstance(ctx).getClientId()))
                    .orderDesc(DBMessageDao.Properties.Id)
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
