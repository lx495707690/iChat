package com.iapps.ichat.model;

import java.util.ArrayList;

public class BeanContact implements Comparable<BeanContact>{

    private String id;
    private String name;
    private String sortLetters;

    private ArrayList<String> phoneNumbers = new ArrayList<String>();
    private ArrayList<String> emails = new ArrayList<String>();

    public BeanContact(String id, String name){
        this.id = id;
        this.name = name;
    }

    public BeanContact(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void addPhoneNumber(String phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        this.emails.add(email) ;
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
    public int compareTo(BeanContact contact) {
        return sortLetters.compareTo(contact.getSortLetters());
    }
}
