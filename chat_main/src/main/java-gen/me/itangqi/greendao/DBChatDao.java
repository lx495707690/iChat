package me.itangqi.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import me.itangqi.greendao.DBChat;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DBCHAT".
*/
public class DBChatDao extends AbstractDao<DBChat, Long> {

    public static final String TABLENAME = "DBCHAT";

    /**
     * Properties of entity DBChat.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ChannelId = new Property(1, String.class, "channelId", false, "CHANNEL_ID");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Message = new Property(4, String.class, "message", false, "MESSAGE");
        public final static Property Date = new Property(5, String.class, "date", false, "DATE");
        public final static Property ImgUrl = new Property(6, String.class, "imgUrl", false, "IMG_URL");
    };


    public DBChatDao(DaoConfig config) {
        super(config);
    }
    
    public DBChatDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DBCHAT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CHANNEL_ID\" TEXT NOT NULL ," + // 1: channelId
                "\"USER_ID\" TEXT NOT NULL ," + // 2: userId
                "\"NAME\" TEXT NOT NULL ," + // 3: name
                "\"MESSAGE\" TEXT NOT NULL ," + // 4: message
                "\"DATE\" TEXT NOT NULL ," + // 5: date
                "\"IMG_URL\" TEXT NOT NULL );"); // 6: imgUrl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DBCHAT\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DBChat entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getChannelId());
        stmt.bindString(3, entity.getUserId());
        stmt.bindString(4, entity.getName());
        stmt.bindString(5, entity.getMessage());
        stmt.bindString(6, entity.getDate());
        stmt.bindString(7, entity.getImgUrl());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DBChat readEntity(Cursor cursor, int offset) {
        DBChat entity = new DBChat( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // channelId
            cursor.getString(offset + 2), // userId
            cursor.getString(offset + 3), // name
            cursor.getString(offset + 4), // message
            cursor.getString(offset + 5), // date
            cursor.getString(offset + 6) // imgUrl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DBChat entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setChannelId(cursor.getString(offset + 1));
        entity.setUserId(cursor.getString(offset + 2));
        entity.setName(cursor.getString(offset + 3));
        entity.setMessage(cursor.getString(offset + 4));
        entity.setDate(cursor.getString(offset + 5));
        entity.setImgUrl(cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DBChat entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DBChat entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
