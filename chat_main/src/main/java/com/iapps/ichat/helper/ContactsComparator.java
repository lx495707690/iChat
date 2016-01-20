package com.iapps.ichat.helper;



import com.iapps.ichat.model.BeanContact;

import java.util.Comparator;

public class ContactsComparator implements Comparator<BeanContact> {

    public int compare(BeanContact o1, BeanContact o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
