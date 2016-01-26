package me.itangqi.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DBFRIEND".
 */
public class DBFriend implements Comparable<DBFriend>{

    private Long id;
    /** Not-null value. */
    private String my_userId;
    /** Not-null value. */
    private String friend_userId;
    /** Not-null value. */
    private String name;
    private String imgUrl;
    private String phone;
    private String address;

    private String sortLetters;
    public DBFriend() {
    }

    public DBFriend(Long id) {
        this.id = id;
    }

    public DBFriend(Long id, String my_userId, String friend_userId, String name, String imgUrl, String phone, String address) {
        this.id = id;
        this.my_userId = my_userId;
        this.friend_userId = friend_userId;
        this.name = name;
        this.imgUrl = imgUrl;
        this.phone = phone;
        this.address = address;
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
    public String getFriend_userId() {
        return friend_userId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFriend_userId(String friend_userId) {
        this.friend_userId = friend_userId;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public int compareTo(DBFriend friend) {
        return sortLetters.compareTo(friend.getSortLetters());
    }
}
