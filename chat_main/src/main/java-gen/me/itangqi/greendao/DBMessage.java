package me.itangqi.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DBMESSAGE".
 */
public class DBMessage {

    private Long id;
    /** Not-null value. */
    private String my_userId;
    /** Not-null value. */
    private String message;
    /** Not-null value. */
    private String date;
    /** Not-null value. */
    private String fromId;
    /** Not-null value. */
    private String toId;
    /** Not-null value. */
    private String channelId;
    /** Not-null value. */
    private String imgUrl;
    /** Not-null value. */
    private String fromName;
    /** Not-null value. */
    private String channalName;
    private boolean fromMe;
    private boolean isSended;

    public DBMessage() {
    }

    public DBMessage(Long id) {
        this.id = id;
    }

    public DBMessage(Long id, String my_userId, String message, String date, String fromId, String toId, String channelId, String imgUrl, String fromName, String channalName, boolean fromMe, boolean isSended) {
        this.id = id;
        this.my_userId = my_userId;
        this.message = message;
        this.date = date;
        this.fromId = fromId;
        this.toId = toId;
        this.channelId = channelId;
        this.imgUrl = imgUrl;
        this.fromName = fromName;
        this.channalName = channalName;
        this.fromMe = fromMe;
        this.isSended = isSended;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getMy_userId() {
        return my_userId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMy_userId(String my_userId) {
        this.my_userId = my_userId;
    }

    /** Not-null value. */
    public String getMessage() {
        return message;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMessage(String message) {
        this.message = message;
    }

    /** Not-null value. */
    public String getDate() {
        return date;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setDate(String date) {
        this.date = date;
    }

    /** Not-null value. */
    public String getFromId() {
        return fromId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    /** Not-null value. */
    public String getToId() {
        return toId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setToId(String toId) {
        this.toId = toId;
    }

    /** Not-null value. */
    public String getChannelId() {
        return channelId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /** Not-null value. */
    public String getImgUrl() {
        return imgUrl;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /** Not-null value. */
    public String getFromName() {
        return fromName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /** Not-null value. */
    public String getChannalName() {
        return channalName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setChannalName(String channalName) {
        this.channalName = channalName;
    }

    public boolean getFromMe() {
        return fromMe;
    }

    public void setFromMe(boolean fromMe) {
        this.fromMe = fromMe;
    }

    public boolean getIsSended() {
        return isSended;
    }

    public void setIsSended(boolean isSended) {
        this.isSended = isSended;
    }

}