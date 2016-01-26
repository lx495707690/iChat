package com.iapps.ichat.model;

public class BeanFriend implements Comparable<BeanFriend>{

    private String id;
    private String name;
    private String avatar;
    private String sortLetters;

    public BeanFriend(String id, String name,String avatar){
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public BeanFriend(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public int compareTo(BeanFriend friend) {
        return sortLetters.compareTo(friend.getSortLetters());
    }

}
